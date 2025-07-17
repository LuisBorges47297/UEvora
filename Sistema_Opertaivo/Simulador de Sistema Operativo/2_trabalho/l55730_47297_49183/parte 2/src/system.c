#include "system.h"
#include "queue.h"
#include "list.h"
#include "sheduling.h"
#include "messages.h"
#include <stdio.h>
#include <stdlib.h>

/**
 * Esta função carrega os programas no array program para a lista e fila de tarefas;
 * - queue_of_non_created_tasks - é uma fila com as tarefas organizadas em ordem do instante de chegada.
 * Portanto o elemento no topo da fila é o elemento com o menor tempo de chegada.
 * - list_of_tasks - é uma lista com todas as tarefas. Esta lista é usada para imprimir o 
 * estado de cada tarefa tarefas.
*/
void load_programs(int program[][11], int mem_data[], int numberOfPrograms,
        Queue queue_of_non_created_tasks, List list_of_all_tasks);

void run(int program[][11], int mem_data[], int numberOfPrograms, int input_id) {
    Queue queue_of_non_created_tasks = create_new_queue();
    List list_of_all_tasks = create_empty_list();
    
    char file_name[20];
    sprintf(file_name, "simulador%02d.out", input_id);
    FILE *file_stream = fopen(file_name, "w");
    if(file_stream == NULL) {
        perror(INVALID_FILE_OPPENING_MESSAGE);
        exit(1);
    }

    load_programs(program, mem_data, numberOfPrograms, queue_of_non_created_tasks, list_of_all_tasks);
    execute_tasks(queue_of_non_created_tasks, list_of_all_tasks, file_stream);

    fclose(file_stream);
    delete_queue(queue_of_non_created_tasks);
    delete_list(list_of_all_tasks);
}

void load_programs(int program[][11], int mem_data[], int numberOfPrograms, Queue queue_of_non_created_tasks, List list_of_all_tasks) {
    Process processes[numberOfPrograms];
    for(int i=0; i < numberOfPrograms; ++i) { processes[i] = NULL; } // Inicializa os arrays com nulos
    for(int i=0; i < numberOfPrograms && i < LIST_MAX_SIZE; ++i) {
        int process_id = program[i][0];
        if(processes[process_id - 1] == NULL) {
            processes[process_id - 1] = create_process(process_id, mem_data[process_id - 1]);
        }
        Process process = processes[process_id - 1];
        Task task = create_task(i+1, program[i], process);
        printf("%d\n", processes[process_id - 1]->id);
        enqueue_priority(queue_of_non_created_tasks, task);
        insert(list_of_all_tasks, task);
    }
}