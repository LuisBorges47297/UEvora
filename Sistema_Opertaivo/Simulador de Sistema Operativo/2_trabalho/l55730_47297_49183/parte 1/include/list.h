#ifndef LIST_H
#define LIST_H

#include "process.h"

#define LIST_MAX_SIZE 10


struct list {
    int size;
    Process elements[10];
};

typedef struct list list_ds;
typedef list_ds* List;

/**
 * Esta função cria uma lista de processos vazia
*/
List create_empty_list();

/**
 * Esta função insere um elemento do tipo process na memória
*/
int insert(List list, Process value);

/**
 * Esta função apaga uma lista, liberando a memória por ela usada
*/
void delete_list(List list);


#endif // LIST_H