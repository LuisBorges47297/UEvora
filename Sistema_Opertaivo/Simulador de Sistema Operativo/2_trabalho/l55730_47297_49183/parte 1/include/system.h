#ifndef SYSTEM_H
#define SYSTEM_H

/**
 * Esta função executa os processos que precisão das quantidades de memórias definidas em "proc_mems"
 * e são executados na sequencia definida em "exec_sequence"
*/
void execute_processes(int exec_sequence[], int exec_size, int proc_mems[], int mems_size, int input_id);

#endif