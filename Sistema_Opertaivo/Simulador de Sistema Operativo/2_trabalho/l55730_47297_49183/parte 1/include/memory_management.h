#ifndef MEMORY_MANAGEMENT
#define MEMORY_MANAGEMENT

#include "list.h"
#include "fifo_memory.h"
#include <stdio.h>

/**
 * Esta função executa um conjunto de processos defindos na lista "list_processes", seguindo a sequencia
 * definida no vetor "exec_sequence". Durante a execução esta função utiliza o algoritmo FIFO para gestão
 * de memória
*/
void execute_using_fifo_memory(List list_processes, int exec_sequence[], int exec_size, FILE* output_file);

#endif // MEMORY_MANAGEMENT