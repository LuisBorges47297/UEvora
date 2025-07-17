#include <stdlib.h>
#include "memory_management.h"

/**
 * Esta função escreve o cabeçalho da saída, tanto no ficheiro assim como no terminal.
*/
void print_output_header(FILE* output_file, List processes_list);


/**
 * Esta função escreve uma linha do output no ficheiro de saída usando FIFO
*/
void print_output_line_fifo(FIFOMemory memory, FILE* ouput_file, List processes_list, int timer);


/**********************************************************************************************************
 *                                           FUNÇÕES PRINCIPAIS
 **********************************************************************************************************/

void execute_using_fifo_memory(List processes_list, int exec_sequence[], int exec_size, FILE* output_file) {
    FIFOMemory fifo_memory =  create_new_fifo_memory();

    print_output_header(output_file, processes_list);

    for(int i=0; i < exec_size; ++i) {
        int process_id = exec_sequence[i];
        load_fifo(fifo_memory, processes_list->elements[process_id - 1]);
        print_output_line_fifo(fifo_memory, output_file, processes_list, i+1);
    }
}

/******************************************************************************************************
 *                                           FUNÇÕES AUXILIARES
 ******************************************************************************************************/

void print_output_header(FILE* output_file, List processes_list) {
    char output_buffer[512];
    int pos = sprintf(output_buffer, "%-10s", "time inst");
    for(int i=0; i < processes_list->size; ++i) {
        pos += sprintf(output_buffer + pos, "| proc%-14d", i+1);
    }
    pos += sprintf(output_buffer + pos, "\n");
    fprintf(output_file, output_buffer);
    printf(output_buffer);
}

void print_output_line_fifo(FIFOMemory memory, FILE* ouput_file, List processes_list, int timer) {
    char output_buffer[512];
    int frames_buffer[16];
    char frames_string_buffer[20];

    int pos = sprintf(output_buffer, "%-10d", timer);
    for(int i=0; i < processes_list->size; ++i) {
        int number_of_frames = get_fifo_frames_pos(memory, processes_list->elements[i], frames_buffer);
        int pos_inner = 0;
        for(int j=0; j < number_of_frames - 1; ++j) {
            pos_inner += sprintf(frames_string_buffer + pos_inner, "F%d,", frames_buffer[j]);
        }
        if(number_of_frames > 0) {
            pos_inner += sprintf(frames_string_buffer + pos_inner, "F%d", frames_buffer[number_of_frames - 1]);
            pos += sprintf(output_buffer + pos, "| %-18s", frames_string_buffer);
        } else {
            pos += sprintf(output_buffer + pos, "| %-18s", "");
        }
    }
    pos += sprintf(output_buffer + pos,"\n");
    fprintf(ouput_file, output_buffer);
    printf(output_buffer);
}
