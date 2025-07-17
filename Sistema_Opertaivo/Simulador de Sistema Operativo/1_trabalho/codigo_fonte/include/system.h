#ifndef SYSTEM_H
#define SYSTEM_H
#include <stdio.h>

/**
 * Esta função executa os programas passados como argumento na variável program.
*/
void run(int programs[][11], int numberOfPrograms, FILE *file_to_store_output);

#endif // SYSTEM_H