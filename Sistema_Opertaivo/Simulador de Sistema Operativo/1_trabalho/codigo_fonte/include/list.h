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


List create_empty_list();
int insert(List list, Task value);
int print(List list, char* output_buffer);
void delete_list(List list);


#endif // LIST_H