#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "task.h"
#include "messages.h"

Task create_task(int id, int program[], Process process) {
    Task task = malloc( sizeof(task_t) );

    if(task == NULL) {
        perror(MEMORY_ALLOCATION_ERROR_MESSAGE);
        exit(1);
    }
        
    task->id = id;
    task->process = process; 
    task->instrution_pointer = 0;
    
    for(int i=0; i < 10; ++i) {
        task->instructions[i] = program[i+1];
    }

    task->status = NONE;
    task->remaining_time_to_leave_the_system = 0;

    return task;
}

char *status_to_string(Status status, char* buffer) {
    switch(status) {
        case READY:
            strcpy(buffer, "READY");
            return buffer;
        case EXECUTING:
            strcpy(buffer, "EXECUTING");
            return buffer;
        case INTERRUPTIBLE:
            strcpy(buffer, "INTERRUPTIBLE");
            return buffer;
        case ZOMBIE:
            strcpy(buffer, "ZOMBIE");
            return buffer;
        case SWINT:
            strcpy(buffer, "SWINT");
            return buffer;
        case SWRDY:
            strcpy(buffer, "SWRDY");
            return buffer;
        case NONE:
        default:
            strcpy(buffer, "");
            return buffer;
    }

    return NULL;
}

void delete_task(Task task) {
    free(task);
}