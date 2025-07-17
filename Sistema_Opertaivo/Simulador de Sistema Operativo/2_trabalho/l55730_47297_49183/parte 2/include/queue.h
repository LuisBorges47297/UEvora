#ifndef QUEUE_H
#define QUEUE_H

#define QUEUE_MAX_SIZE 11

#include "task.h"

/***
 * Esta estrutura define uma fila (FIFO) circular. Vale obersar que a fila suporta um número máximo de 10 elementos do tipo Task,
 * Ou seja, esta fila armazena tarefas apenas.
*/
struct queue {
    int front;
    int tail;
    Task elements[11];
};

typedef struct queue queue_ds; // ds = Data Structure
typedef queue_ds* Queue; 

/**
 * Este função cria uma nova fila vazia.
*/
Queue create_new_queue();

/**
 * Esta função permite a inserção de uma tarefa na fila.
*/
int enqueue(Queue queue, Task value);

/**
 * Esta função permite a inserção de uma tarefa na fila com prioridade.
 * - A variável que define a prioridade de uma tarefa é definida pela instrução 
 * instructions[insttuction_pointer]
*/
int enqueue_priority(Queue queue, Task value);

/**
 * Esta função permite a remoção de um elemento na fila.
*/
Task dequeue(Queue queue);

/**
 * Esta função permite o acesso do elemento em frente da fila sem removê-lo.
*/
Task peek(Queue queue);

/**
 * Esta função determina o número de elementos inseridos na fila
*/
int size(Queue queue);

/**
 * Esta função verifica se a fila está vazia
*/
int is_empty(Queue queue);

/**
 * Esta função verifica se a fila está cheia
*/
int is_full(Queue queue);

/**
 * Esta função permite a remoção/exclusão de uma fila.
*/
void delete_queue(Queue queue);

int inc_2(int i);
int dec_2(int i);

#endif // QUEUE_H
