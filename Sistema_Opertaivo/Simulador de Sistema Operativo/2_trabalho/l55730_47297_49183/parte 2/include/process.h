#ifndef PROCESS_H
#define PROCESS_H

struct process {
    int id;
    int frames_starts_at; // Indice do frame inicial do processo em memória
    int size; // Tamanho da tarefa em kb
};

typedef struct process process_t; // t = Type
typedef process_t* Process; 

/**
 * Esta função cria um processo
*/
Process create_process(int id, int size);

/**
 * Esta função remove um processo do sistema
*/
void delete_process(Process task);

#endif // PROCESS_H