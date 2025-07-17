#ifndef SHEDULING_H
#define SHEDULING_H

#include <stdio.h>
#include "task.h"
#include "list.h"
#include "queue.h"

#define DEFAULT_QUANTUM 3
#define DURATION_1 0
#define DURATION_3 2
#define TIME_TO_LEAVE_THE_SYSTEM 2

struct processor {
    int timer; // Contador ou relógio do sistema
    int remaing_waiting_time; // Tempo de duração restante para mudança de tarefas ou de execução do dispacher
    Task running_task; // Tarefa atualmente em execução
    Task old_running_task; // Última tarefa executada
    int remaining_quantum; // Quantum restante para a tarefa ser removida do processador
};

typedef struct processor processor_t;
typedef processor_t* Processor;

/**
 * Esta função executa as tarefas carregadas para a fila com prioridade queue_non_created_of_tasks.
 * - Os elementos na fila são organizados de acordo com o instante inicial do programa.
*/
void execute_tasks(Queue queue_non_created_tasks, List list_of_all_tasks, FILE *file_to_store_output);


#endif // SHEDULING_H