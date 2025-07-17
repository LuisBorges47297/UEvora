#include <stdio.h>
#include "system.h"
#include "list.h"
#include "memory_management.h"

/**
 * Esta função cria um conjunto de processos com base no conjunto de memórias que ela precisa
*/
List create_processes(int proc_mems[], int mems_size);

void execute_processes(int exec_sequence[], int exec_size, int proc_mems[], int mems_size, int input_id) {
    List list_of_processes = create_processes(proc_mems, mems_size);
    
    char fifo_file_name[20];
    sprintf(fifo_file_name, "fifo%02d.out", input_id);
    FILE* fifo_output_file = fopen(fifo_file_name, "w");
    execute_using_fifo_memory(list_of_processes, exec_sequence, exec_size, fifo_output_file);
}

/**
 * Funções AUXILIARES
*/

List create_processes(int proc_mems[], int mems_size) {
    List list_of_processes = create_empty_list();
    for(int i=0; i < mems_size && i < LIST_MAX_SIZE; ++i) {
        Process process = create_process(i+1, proc_mems[i]);
        insert(list_of_processes, process);
    }
    return list_of_processes;
}