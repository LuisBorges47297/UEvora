#ifndef TASK_H
#define TASK_H

#include "process.h"

#define MAX_NUMBER_OF_INSTRUTIONS 11

typedef enum status {
    NONE, READY, EXECUTING, INTERRUPTIBLE, ZOMBIE, SWRDY, SWINT
} Status;

struct task {
    int id;
    Process process;
    int instrution_pointer;
    int instructions[10];
    int remaining_time_to_leave_the_system;
    Status status;
};

typedef struct task task_t; // t = Type
typedef task_t* Task; 

Task create_task(int id, int program[], Process process);
void delete_task(Task task);
char *status_to_string(Status status, char* buffer);

#endif // TASK_H