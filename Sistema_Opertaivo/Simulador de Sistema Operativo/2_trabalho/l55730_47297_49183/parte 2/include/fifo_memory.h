#ifndef FIFO_MEMORY_H
#define FIFO_MEMORY_H


#include "process.h"
#include "base_memory.h"
#include "fifo_frame.h"

/***
 * Esta estrutura define uma fila (FIFO) circular. Vale obersar que a fila suporta um número máximo de 10 elementos do tipo Process,
 * Ou seja, esta fila armazena tarefas apenas.
*/
struct clock_mem {
    int tail;
    FIFOFrame buffer_of_frames[MEMORY_FRAMES_NUMBER];
};

typedef struct clock_mem fifo_mem_ds; // ds = Data Structure
typedef fifo_mem_ds* FIFOMemory; 

/**
 * Este função cria uma memória vazia.
*/
FIFOMemory create_new_fifo_memory();

/**
 * Esta função permite carregar um processo/tarefa na memória usando política de substituição FIFO.
*/
int load_fifo(FIFOMemory memory, Process process);

/**
 * Esta função verifica se o processo/task process está carregado na memoria.
*/
int is_in_fifo(FIFOMemory memory, Process process);

/**
 * Esta função determina a lista de posições da memória "memory" que são
 * ocupadas pelos frames do processo "process".
*/
int get_fifo_frames_pos(FIFOMemory memory, Process process, int frames[]);

/**
 * Esta função permite a remoção/exclusão de uma memória.
*/
void delete_memory_fifo(FIFOMemory memory);

#endif // FIFO_MEMORY_H
