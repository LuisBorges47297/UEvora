#ifndef LIST_H
#define LIST_H

#include "task.h"

#define LIST_MAX_SIZE 10


struct list {
    int size;
    Task elements[10];
};

typedef struct list list_ds;
typedef list_ds* List;

/**
 * Esta função cria uma lista vazia. A lista criada composta um conjunto de tarefas
*/
List create_empty_list();

/**
 * Esta função insere um tarefa na lista
*/
int insert(List list, Task value);

/**
 * Esta função remove um elemento na lista
*/
void delete_list(List list);


#endif // LIST_H