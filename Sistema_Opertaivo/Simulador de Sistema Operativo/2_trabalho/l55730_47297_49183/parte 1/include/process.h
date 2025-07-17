#ifndef TASK_H
#define TASK_H

struct process {
    int id;
    int frames_starts_at; // Indice do primeiro frame do processo na memória
    int size; // Tamanho do processo em kb
};

typedef struct process process_t; // t = Type
typedef process_t* Process; // Ponteiro para a estrutura de ponteiro

/**
 * Esta função cria um processo
*/
Process create_process(int id, int size);

/**
 * Esta função apaga um processo, liberando a memória por ela usada
*/
void delete_process(Process process);

#endif // TASK_H