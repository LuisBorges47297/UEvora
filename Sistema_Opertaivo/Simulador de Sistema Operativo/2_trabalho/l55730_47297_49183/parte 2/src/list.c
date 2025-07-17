#include <stdlib.h>
#include <stdio.h>
#include "list.h"
#include "messages.h"

List create_empty_list() {
    List list = malloc( sizeof(list_ds) );

    if(list == NULL) {
        perror(MEMORY_ALLOCATION_ERROR_MESSAGE);
        exit(1);
    }
    list->size = 0;
    
    return list;
}

int insert(List list, Task value) {
    if(list == NULL) {
        perror(INVALID_LIST_MESSAGE);
        return 0;
    }

    if(list->size >= LIST_MAX_SIZE) {
        return 0;
    }

    list->elements[list->size] = value;
    list->size += 1;

    return 1;
}

Task get_element(List list, int index) {
    return list->elements[index];
}

void delete_list(List list) {
    for(int i=0; i < list->size; ++i) {
        if(list->elements[i] != NULL) {
            free(list->elements[i]);
        }
    }
    free(list);
}