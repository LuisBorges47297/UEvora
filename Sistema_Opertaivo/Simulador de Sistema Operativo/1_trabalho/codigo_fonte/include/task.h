#ifndef TASK_H
#define TASK_H

#define MAX_NUMBER_OF_INSTRUTIONS 11

typedef enum status {
    NONE, READY, EXECUTING, INTERRUPTIBLE, ZOMBIE
} Status;

struct task {
    int id;
    int process_id;
    int instrution_pointer;     //o ponteiro de instrução ou o índice da próxima instrução a ser executada no array de instruções
    int instructions[10];
    int remaining_time_to_leave_the_system;
    Status status;
};

typedef struct task task_t; // t = Type
typedef task_t* Task; 

Task create_task(int id, int program[]);
void delete_task(Task task);
char *status_to_string(Status status, char* buffer);

#endif // TASK_H