#include "system.h"
#include "queue.h"
#include "list.h"
#include "sheduling.h"
#include <stdio.h>

/**
 * Esta função carrega os programas no array program para a lista e fila de tarefas;
 * - queue_of_non_created_tasks - é uma fila com as tarefas organizadas em ordem do instante de chegada.
 * Portanto o elemento no topo da fila é o elemento com o menor tempo de chegada.
 * - list_of_tasks - é uma lista com todas as tarefas. Esta lista é usada para imprimir o 
 * estado de cada tarefa tarefas.
*/
void load_programs(int program[][11], int numberOfPrograms, Queue queue_of_non_created_tasks, List list_of_all_tasks);

void run(int program[][11], int numberOfPrograms, FILE *file_to_store_output) {
    Queue queue_of_non_created_tasks = create_new_queue();
    List list_of_all_tasks = create_empty_list();

   load_programs(program, numberOfPrograms, queue_of_non_created_tasks, list_of_all_tasks);
   execute_tasks(queue_of_non_created_tasks, list_of_all_tasks, file_to_store_output);

   delete_queue(queue_of_non_created_tasks);
   delete_list(list_of_all_tasks);
}


void load_programs(int program[][11], int numberOfPrograms, Queue queue_of_non_created_tasks, List list_of_all_tasks) {
    for(int i=0; i < numberOfPrograms && i < LIST_MAX_SIZE; ++i) {
        Task task = create_task(i+1, program[i]);
        enqueue_priority(queue_of_non_created_tasks, task);
        insert(list_of_all_tasks, task);
    }
}