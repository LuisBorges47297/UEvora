#ifndef SYSTEM_H
#define SYSTEM_H
#include <stdio.h>

/**
 * Esta função executa os programas passados como argumento na variável program.
 * @param program - vetor de programas
 * @param mem_data - vetor com as memórias que cada processo precisa para poder executar
 * @param numberOfPrograms - número de programas no vetor de programas
 * @param input_id - ID do input/teste
*/
void run(int program[][11], int mem_data[], int numberOfPrograms, int input_id);

#endif // SYSTEM_H