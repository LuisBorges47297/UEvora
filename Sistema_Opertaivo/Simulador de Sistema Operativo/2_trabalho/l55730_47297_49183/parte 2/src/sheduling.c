#include "sheduling.h"
#include <stdlib.h>
#include <stdio.h>
#include "fifo_memory.h"

/**
 * Esta função cria tarefas e passa imediatamente para o estado READY
 * - queue_of_non_created_tasks - é uma fila contento as tarefas que ainda não foram criadas. 
 * Onde as tarefas na fila estão organizadas em ordem do instante de chegada.
 * - ready_queue - é a fila de tarefas prontas (READY).
*/
void create_tasks_as_ready(Queue queue_of_non_created_tasks, Queue ready_queue, int processor_current_timer);

/**
 * Esta função move as tarefas que já esgoraram o tempo de bloqueio do estado INTERRUPTIBLE para o estado READY
 * - interruptible_queue - Fila com as tarefas em bloqueio
 * - ready_queue - Fila com as tarefas prontas
*/
void from_interruptible_to_ready(Queue interruptible_queue, Queue ready_queue);

/**
 * Esta função mve uma tarefa no estado RUNNING para o estado READY.
*/
void from_executing_to_ready(Processor processor, Queue ready_queue);

/**
 * Esta função move as tarefas que já esgoraram o tempo de execução para o estado INTERRUPTIBLE
 * - interruptible_queue - Fila com as tarefas em bloqueio
 * - ready_queue - Fila com as tarefas prontas
*/
void from_executing_to_interruptible(Processor processor, Queue interruptible_queue);

/**
 * Esta função atera o estado de uma tarefa de executing para zombie, se esta tarefa 
 * concluiu o tempo de execução e não tem mais instruções por executar.
*/
void from_executing_to_zombie(Processor processor, Queue zombie_queue);

/**
 * Esta função coloca no processador a tarefa que está no topo da fila de processos prontos
 * se o processador estiver ocioso. 
*/
void from_ready_to_running(Queue ready_queue, FIFOMemory memory, Processor processor);

/**Queue
 * Remove do sistema todas tarefas que já ficaram no estado ZOMBIE por 2 instantes.
*/
void remove_zombie_tasks_in_the_system(Queue zombie_queue);

void decrement_remaining_interruptible_time(Queue interruptible_queue);

void decrement_zombie_remaining_time(Queue zombie_queue);

int belongs_to_same_process(Task task1, Task task2);

/**
 * Esta função altera o estado de uma tarefa de READY normal para SWRDY se o processo
 * da tarefa não estiver completamente em memória. Esta função será efetiva no caso em
 * que o processo em que uma tarefa no estado READY pertence ter sido substituido na memória pelo processo
 * de outra tarefa.
*/
void from_ready_to_sw_rdy(Queue ready_queue, FIFOMemory memory);

/**
 * Esta função altera o estado de uma tarefa de SWRDY normal para READY se o processo
 * da tarefa não estiver completamente em memória. Esta função será efetiva no caso do 
 * processo a que a tarefa no estado SWRDY ter sido colocado em memória através de outra 
 * tarefa do mesmo processo.
*/
void from_sw_rdy_to_ready(Queue ready_queue, FIFOMemory memory);

/**
 * Esta função altera o estado de uma tarefa de READY normal para SWRDY se o processo
 * da tarefa não estiver completamente em memória. Esta função será efetiva no caso em
 * que o processo em que uma tarefa no estado READY pertence ter sido substituido na memória pelo processo
 * de outra tarefa.
*/
void from_interruptible_to_sw_int(Queue interruptible_queue, FIFOMemory memory);

/**
 * Esta função altera o estado de uma tarefa de SWRDY normal para READY se o processo
 * da tarefa não estiver completamente em memória. Esta função será efetiva no caso do 
 * processo a que a tarefa no estado SWRDY ter sido colocado em memória através de outra 
 * tarefa do mesmo processo.
*/
void from_sw_int_to_interruptible(Queue interruptible_queue, FIFOMemory memory);

/**
 * Esta função escreve o cabeçalho da saída, tanto no ficheiro assim como no terminal.
*/
void print_output_header(FILE* output_file, List processes_list);


/**
 * Esta função escreve uma linha do output no ficheiro de saída usando FIFO
*/
void print_output_line_fifo(FIFOMemory memory, FILE* ouput_file, List processes_list, int timer);


void execute_tasks(Queue queue_non_created_tasks, List list_of_all_tasks, FILE *file_to_store_output) {
    processor_t processor = { 
        timer: 1, 
        remaing_waiting_time: 0,
        running_task: NULL, 
        old_running_task: NULL,
        remaining_quantum: DEFAULT_QUANTUM 
    };

    Queue ready_queue = create_new_queue();
    Queue interuptible_queue = create_new_queue();
    Queue zombie_queue = create_new_queue();

    FIFOMemory fifo_memory = create_new_fifo_memory();

    print_output_header(file_to_store_output, list_of_all_tasks);
    
    do {

        from_interruptible_to_ready(interuptible_queue, ready_queue);
        create_tasks_as_ready(queue_non_created_tasks, ready_queue, processor.timer);
        from_executing_to_ready(&processor, ready_queue);
        from_executing_to_zombie(&processor, zombie_queue);
        from_executing_to_interruptible(&processor, interuptible_queue);

        from_ready_to_running(ready_queue, fifo_memory, &processor);

        from_ready_to_sw_rdy(ready_queue, fifo_memory);
        from_interruptible_to_sw_int(interuptible_queue, fifo_memory);
        from_sw_rdy_to_ready(ready_queue, fifo_memory);
        from_sw_int_to_interruptible(interuptible_queue, fifo_memory);

        print_output_line_fifo(fifo_memory, file_to_store_output, list_of_all_tasks, processor.timer);

        processor.timer += 1;
        if(processor.remaing_waiting_time == 0 && 
            processor.running_task != NULL && processor.running_task->status == EXECUTING) {
            processor.remaining_quantum -= 1;
        }
        remove_zombie_tasks_in_the_system(zombie_queue);
    } 
    while( !is_empty(ready_queue) || !is_empty(interuptible_queue) || !is_empty(zombie_queue) || processor.running_task != NULL );

    delete_queue(ready_queue);
    delete_queue(interuptible_queue);
    delete_queue(zombie_queue);
}


void create_tasks_as_ready(Queue queue_of_non_created_tasks, Queue ready_queue, int processor_current_timer) {
    while(!is_empty(queue_of_non_created_tasks)) {
        Task task = peek(queue_of_non_created_tasks);
        int initial_time = task->instructions[task->instrution_pointer]; // Acessa o estado inicial da tarefa
        
        if(initial_time != processor_current_timer) 
            break;
        
        task->instrution_pointer += 1; // Troca a instrução para execução
        task = dequeue(queue_of_non_created_tasks);
        task->status = READY;
        enqueue(ready_queue, task);
    }
}

void from_interruptible_to_ready(Queue interruptible_queue, Queue ready_queue) {
    decrement_remaining_interruptible_time(interruptible_queue);

    while(!is_empty(interruptible_queue)) {
        Task task = peek(interruptible_queue);
        int remaining_interruptible_time = task->instructions[task->instrution_pointer]; // Tempo de bloqueio restante

        if(remaining_interruptible_time != 0)
            break;

        task->instrution_pointer += 1;
        task = dequeue(interruptible_queue);
        task->status = READY;
        enqueue(ready_queue, task);
    }
}

void from_executing_to_ready(Processor processor, Queue ready_queue) {
    Task task = processor->running_task;

    if(task == NULL)
        return;

    int remaining_executing_time = task->instructions[task->instrution_pointer]; // Tempo de execução restante
    if(remaining_executing_time > 0 && processor->remaining_quantum == 0) {
        processor->old_running_task = task;
        processor->running_task = NULL; // Remove do processador o processo no estado EXECUTING
        task->status = READY;
        enqueue(ready_queue, task);
    }
}

void from_executing_to_interruptible(Processor processor, Queue interruptible_queue) {
    Task task = processor->running_task;

    if(task == NULL)
        return;

    int remaining_executing_time = task->instructions[task->instrution_pointer]; // Tempo de execução restante
    int next_interruptible_time = task->instructions[task->instrution_pointer + 1]; // Proximo tempo de bloqueio
    if(remaining_executing_time == 0 && next_interruptible_time > 0) {
        processor->old_running_task = task;
        processor->running_task = NULL; // Remove do processador o processo no estado EXECUTING
        task->instrution_pointer += 1; // Incrementa o apontador de instrução
        task->status = INTERRUPTIBLE;
        enqueue(interruptible_queue, task);   
    }
}

void from_executing_to_zombie(Processor processor, Queue zombie_queue) {
    Task task = processor->running_task;

    if(task == NULL)
        return;

    int remaining_executing_time = task->instructions[task->instrution_pointer]; // Tempo de execução restante
    int next_interruptible_time = task->instructions[task->instrution_pointer + 1];
    if(remaining_executing_time == 0 && next_interruptible_time == 0) {
        processor->old_running_task = task;
        processor->running_task = NULL; // Remove do processador o processo no estado EXECUTING
        task->status = ZOMBIE;
        task->remaining_time_to_leave_the_system = TIME_TO_LEAVE_THE_SYSTEM;
        enqueue(zombie_queue, task);
    }
}

void remove_zombie_tasks_in_the_system(Queue zombie_queue) {
    decrement_zombie_remaining_time(zombie_queue);

    while(!is_empty(zombie_queue)) {
        Task task = peek(zombie_queue);

        if(task->remaining_time_to_leave_the_system != 0)
            break;

        task = dequeue(zombie_queue);
        task->status = NONE;
    }
}

void decrement_remaining_interruptible_time(Queue interruptible_queue) {
    for(int i=interruptible_queue->front; i != interruptible_queue->tail; i=inc_2(i)) {
        Task current_task = interruptible_queue->elements[i];
        // printf("N: %d, %d\n", i, interruptible_queue->tail);
        current_task->instructions[current_task->instrution_pointer] -= 1;
    }
}

void decrement_zombie_remaining_time(Queue zombie_queue) {
    for(int i=zombie_queue->front; i != zombie_queue->tail; i=inc_2(i)) {
        Task current_task = zombie_queue->elements[i];
        current_task->remaining_time_to_leave_the_system -= 1;
    }
}

void from_ready_to_running(Queue ready_queue, FIFOMemory memory, Processor processor) {
    if(processor->remaing_waiting_time > 0) {
        processor->remaing_waiting_time -= 1;
        return;
    }

    if(processor->running_task == NULL) {                   // Atualmente nenhuma tarefa em execução
        processor->remaing_waiting_time = DURATION_3;   // - Configura o dispacher deve correr por 3 instantes
        if(!is_empty(ready_queue)) {                         // Se a fila READY não está vazia
            processor->running_task = dequeue(ready_queue); // - Remove a tarefa em frente da fila
            processor->remaining_quantum = DEFAULT_QUANTUM;
            if(processor->timer == 1) {
                processor->running_task->status = EXECUTING;
                load_fifo(memory, processor->running_task->process);
                processor->remaing_waiting_time = DURATION_1;
                processor->running_task->instructions[processor->running_task->instrution_pointer] -= 1;
            }
            if(belongs_to_same_process(processor->old_running_task, // Se a tarefa em frente da fila de um processo diferente do processp anterior
                processor->running_task)) {
                processor->remaing_waiting_time = DURATION_1; // Configura o dispacher para correr 1 instante apenas
            }   
        }
    } else {
        if(processor->running_task->status != EXECUTING) {
            processor->running_task->status = EXECUTING;
            load_fifo(memory, processor->running_task->process);
        }
        processor->running_task->instructions[processor->running_task->instrution_pointer] -= 1;
    }

    
}

int belongs_to_same_process(Task task1, Task task2) {
    return task1 != NULL && task2 != NULL && 
                    task1->process->id == task2->process->id;
}

void from_ready_to_sw_rdy(Queue ready_queue, FIFOMemory memory) {
    for(int i=ready_queue->front; i != ready_queue->tail; i=inc_2(i)) {
        Task task = ready_queue->elements[i];
        if(!is_in_fifo(memory, task->process) && task->status == READY) { // Se o processo da tarefa não está totalmente em memória e ainda não está ainda no estado SWRDY
            task->status = SWRDY; // Muda o estado
        }
    }
}


void from_sw_rdy_to_ready(Queue ready_queue, FIFOMemory memory) {
    for(int i=ready_queue->front; i != ready_queue->tail; i=inc_2(i)) { // Itera pelos processos na fila READY
        Task task = ready_queue->elements[i];
        if(is_in_fifo(memory, task->process) && task->status == SWRDY) { // Se o processo da tarefa está totalmente em memória e ainda não está ainda no estado READY
            task->status = READY; // Muda o estado
        }
    }
}

void from_interruptible_to_sw_int(Queue interruptible_queue, FIFOMemory memory) {
    for(int i=interruptible_queue->front; i != interruptible_queue->tail; i=inc_2(i)) { // Itera pelos processos na fila INTERRUPTIBLE
        Task task = interruptible_queue->elements[i];
        if(!is_in_fifo(memory, task->process) && task->status == INTERRUPTIBLE) { // Se o processo da tarefa não está totalmente em memória e está marcado como INTERRUPTIBLE
            task->status = SWINT; // Muda o estado
        }
    }
}

void from_sw_int_to_interruptible(Queue interruptible_queue, FIFOMemory memory) {
    for(int i=interruptible_queue->front; i != interruptible_queue->tail; i=inc_2(i)) { // Itera pelos processos na fila INTERRUPTIBLE
        Task task = interruptible_queue->elements[i];
        if(is_in_fifo(memory, task->process) && task->status == SWINT) { // Se o processo da tarefa está totalmente em memória e está marcado como SWINT
            task->status = INTERRUPTIBLE; // Muda o estado
        }
    }
}

/*******************************************************************************************************************
 *                                                FUNÇÕES AUXILIARES
 *******************************************************************************************************************
*/

int cmpfunc (const void * a, const void * b) {
   return ( *(int*)a - *(int*)b );
}

void print_output_header(FILE* output_file, List list_of_tasks) {
    char output_buffer[512];
    int pos = sprintf(output_buffer, "%-10s", "time inst");
    for(int i=0; i < list_of_tasks->size; ++i) {
        pos += sprintf(output_buffer + pos, "| th%-35d", i+1);
    }
    pos += sprintf(output_buffer + pos, "\n");
    fprintf(output_file, output_buffer);
    printf(output_buffer);
}

void print_output_line_fifo(FIFOMemory memory, FILE* ouput_file, List list_of_tasks, int timer) {
    char output_buffer[512];
    int frames_buffer[16];
    char thread_string_buffer[96];

    int pos = sprintf(output_buffer, "%-10d", timer);
    for(int i=0; i < list_of_tasks->size; ++i) {
        int number_of_frames = get_fifo_frames_pos(memory, list_of_tasks->elements[i]->process, frames_buffer);
        int pos_inner = sprintf(thread_string_buffer, 
            "%s (", status_to_string(list_of_tasks->elements[i]->status, thread_string_buffer));
        for(int j=0; j < number_of_frames - 1; ++j) {
            pos_inner += sprintf(thread_string_buffer + pos_inner, "F%d,", frames_buffer[j]);
        }
        if(number_of_frames > 0) {
            if(list_of_tasks->elements[i]->status != NONE) {
                pos_inner += sprintf(thread_string_buffer + pos_inner, "F%d)", frames_buffer[number_of_frames - 1]);
                pos += sprintf(output_buffer + pos, "| %-37s", thread_string_buffer);
            } else {
                pos += sprintf(output_buffer + pos, "| %-37s", " ");
            }
        } else {
            if(list_of_tasks->elements[i]->status != NONE) {
                pos_inner += sprintf(thread_string_buffer + pos_inner, ")");
                pos += sprintf(output_buffer + pos, "| %-37s", thread_string_buffer);
            } else {
                pos += sprintf(output_buffer + pos, "| %-37s", " ");
            }
        }
    }
    pos += sprintf(output_buffer + pos,"\n");
    fprintf(ouput_file, output_buffer);
    printf(output_buffer);
}
