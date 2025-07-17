#ifndef FIFO_MEMORY_H
#define FIFO_MEMORY_H


#include "process.h"
#include "base_memory.h"
#include "fifo_frame.h"

/***
 * Esta estrutura representa uma memória que faz uso da política de substituição FIFO
*/
struct clock_mem {
    int tail;
    FIFOFrame buffer_of_frames[MEMORY_FRAMES_NUMBER];
};

typedef struct clock_mem fifo_mem_ds; // ds = Data Structure
typedef fifo_mem_ds* FIFOMemory; 

/**
 * Este função cria uma memória vazia usando alocação dinâmica.
*/
FIFOMemory create_new_fifo_memory();

/**
 * Esta função carrega os frames do processo "process" na memória "memory" usando a política
 * de substituição FIFO.
 * @param memory - referência da memória com política FIFO
 * @param process - referência do processo
*/
int load_fifo(FIFOMemory memory, Process process);

/**
 * Esta função verifica se o processo "process" tem todos os seus frames carregados na memória
 * "memory".
 * @param memory - memoria com política FIFO
 * @param process - processo a verificar
*/
int is_in_fifo(FIFOMemory memory, Process process);

/**
 * Esta função preecnhe no vetor "frames" as posições na memória "memory" que são
 * ocupadas pelos frames do processo "process" e retorna o número de frames identicados.
 * @param memory - memoria com política FIFO
 * @param process - processo a verificar
 * @param frames - vetor a preencher
*/
int get_fifo_frames_pos(FIFOMemory memory, Process process, int frames[]);

/**
 * Esta função permite a remoção/exclusão de uma memória.
*/
void delete_memory_fifo(FIFOMemory memory);

#endif // FIFO_MEMORY_H
