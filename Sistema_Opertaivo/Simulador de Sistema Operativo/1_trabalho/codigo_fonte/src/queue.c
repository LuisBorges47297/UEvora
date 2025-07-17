#include <stdlib.h>
#include <stdio.h>
#include "queue.h"
#include "messages.h"

Queue create_new_queue() {
    Queue queue = malloc( sizeof(queue_ds) );

    if(queue == NULL) {
        perror(MEMORY_ALLOCATION_ERROR_MESSAGE);
        exit(1);
    }

    queue->front = 0;
    queue->tail = 0;
    for(int i=0; i < QUEUE_MAX_SIZE; ++i) {
        queue->elements[i] = NULL;
    }

    return queue;
}

int enqueue(Queue queue, Task value) {
    if(queue == NULL) {
        perror(INVALID_QUEUE_MESSAGE);
        return 0;
    }

    if( is_full(queue) )
        return 0;

    queue->elements[ queue->tail ] = value;
    queue->tail = inc(queue->tail);

    return 1;
}

int enqueue_priority(Queue queue, Task value) {
    if(queue == NULL) {
        perror(INVALID_QUEUE_MESSAGE);
        return 0;
    }

    if( is_full(queue) ) {
        return 0;
    }

    int finded_position = -1;                
    for(int i=queue->front; i != queue->tail; i=inc(i)) {
        Task current_value = queue->elements[i];
        if( value->instructions[value->instrution_pointer] < //atual tempo do bloqueio dessa tarefa 
            current_value->instructions[current_value->instrution_pointer] ) {
            finded_position = i;
            break;
        }
    }

    if(finded_position >= 0) {
        for(int i=queue->tail; i != finded_position; i=dec(i)) {
            queue->elements[i] = queue->elements[dec(i)];
        }
        queue->elements[finded_position] = value;
        queue->tail = inc(queue->tail);
    } else {
        enqueue(queue, value);
    }

    return 1;
}

Task dequeue(Queue queue) {
    if( is_empty(queue) )
        return NULL;

    Task x = queue->elements[queue->front];
    queue->front = inc(queue->front);

    return x;
}

Task peek(Queue queue) {   //espreita um elemento q está frente da fila
    if( is_empty(queue) )
        return NULL;

    return queue->elements[queue->front];
}

int size(Queue queue) {
    return (QUEUE_MAX_SIZE - queue->front + queue->tail) % QUEUE_MAX_SIZE;
}

int is_empty(Queue queue) {
    return queue->front == queue->tail;
}

int is_full(Queue queue) {
    return size(queue) == QUEUE_MAX_SIZE - 1;
}

void delete_queue(Queue queue) {
    for(int i=queue->front; i != queue->tail; i=inc(i)) {
        if(queue->elements[i] != NULL) {
            free(queue->elements[i]);
        }
    }
    free(queue);
}

/**
 * Função auxiliar para avançar o cursor da fila para a posição seguinte
*/
int inc(int i) {    //de forma crescente
    return (i + 1) % QUEUE_MAX_SIZE;             
}

/**
 * Função auxiliar para voltar o cursor a posição anterior da fila
*/
int dec(int i) {
    return (QUEUE_MAX_SIZE + i - 1) % QUEUE_MAX_SIZE;
}