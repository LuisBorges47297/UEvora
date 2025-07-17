#include <stdlib.h>
#include <stdio.h>
#include "fifo_memory.h"
#include "messages.h"

/**
 * Esta função insere os frames de um processo no final da fila se existir posições vazias
*/
int insert_tail_fifo(FIFOMemory memory, Process process);

/**
 * Esta função substitui frames na memória pelo frame "frame" numa quantidade de number_of_frames
 * páginas da memória.
*/
int replace_frames_fifo(FIFOMemory memory, Process process, int number_of_frames);

/********************************************************************************************************
 * Funções principais
 * *****************************************************************************************************
*/

FIFOMemory create_new_fifo_memory() {
    FIFOMemory memory = malloc( sizeof(fifo_mem_ds) );

    if(memory == NULL) {
        perror(MEMORY_ALLOCATION_ERROR_MESSAGE);
        exit(1);
    }

    memory->tail = 0;
    for(int i=0; i < MEMORY_FRAMES_NUMBER; ++i) {
        memory->buffer_of_frames[i] = NULL;
    }

    return memory;
}

int load_fifo(FIFOMemory memory, Process process) {
    if(memory == NULL) {
        perror(INVALID_QUEUE_MESSAGE);
        return 0;
    }
    if(is_in_fifo(memory, process)) // Se já existem todos frames do processo em algum lugar da memória, encerra
        return 1;
    int remaining_frames = insert_tail_fifo(memory, process);
    if(remaining_frames > 0) { // Se ainda há frames do processo por inserir na memória
        replace_frames_fifo(memory, process, remaining_frames);
    }
    return 1;
}

int is_in_fifo(FIFOMemory memory, Process process) {
    if(process->frames_starts_at < 0)
        return 0;

    int remaining = MEMORY_FRAMES_NUMBER;
    int number_of_frames = (process->size + 1) / BYTES_PER_FRAME;
    for(int i=process->frames_starts_at; remaining > 0 && number_of_frames > 0; i=inc(i), --remaining) {
        if(memory->buffer_of_frames[i] != NULL &&
            memory->buffer_of_frames[i]->data == process->id)
            --number_of_frames;
    }

    return !number_of_frames;
}

int get_fifo_frames_pos(FIFOMemory memory, Process process, int frames[]) {
    if(process->frames_starts_at < 0)
        return 0;

    int remaining = MEMORY_FRAMES_NUMBER;
    int counter = 0;
    for(int i=process->frames_starts_at; remaining > 0; i=inc(i), --remaining) {
        if(memory->buffer_of_frames[i] != NULL && 
            memory->buffer_of_frames[i]->data == process->id) {
            frames[counter] = i;
            ++counter;
        }
    }

    return counter;
}

void delete_memory_fifo(FIFOMemory memory) {
    for(int i=inc(memory->tail); i != memory->tail; i=inc(i)) {
        if(memory->buffer_of_frames[i] != NULL) {
            free(memory->buffer_of_frames[i]);
        }
    }
    free(memory);
}

/**
 * Funções AUXILIARES
*/

int insert_tail_fifo(FIFOMemory memory, Process process) {
    int number_of_frames = (process->size + 1) / BYTES_PER_FRAME; // Número de frames que o processo precisa
    int auxiliar = number_of_frames;
    while(number_of_frames > 0 && memory->buffer_of_frames[memory->tail] == NULL) { // Enquanto houverem frames por inserir e espaço vazio na memória?
        memory->buffer_of_frames[memory->tail] = create_fifo_frame(process->id); // Insere o frame na memória
        if(number_of_frames == auxiliar) { // Se é a primeira inserção
            process->frames_starts_at = memory->tail; // Define o indice de início dos frames
        }
        memory->tail = inc(memory->tail); // Avança para o proximo frame na memória
        --number_of_frames; // Reduz o número de frames por inserir
    }
    return number_of_frames;
}

int replace_frames_fifo(FIFOMemory memory, Process process, int number_of_frames) {
    int auxiliar = (process->size + 1) / BYTES_PER_FRAME; // Número de frames que o processo precisa
    while(number_of_frames > 0) {
        if(memory->buffer_of_frames[memory->tail]->data != process->id) { // Não é frame do processo atual
            free(memory->buffer_of_frames[memory->tail]);
            memory->buffer_of_frames[memory->tail] = create_fifo_frame(process->id); // Substitui o frame
            if(number_of_frames == auxiliar) {
                process->frames_starts_at = memory->tail;
            }
        }
        --number_of_frames;
        memory->tail = inc(memory->tail);
    }
    return 1;
}

