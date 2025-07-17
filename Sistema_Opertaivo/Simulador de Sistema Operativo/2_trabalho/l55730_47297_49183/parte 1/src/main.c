#include <stdio.h>
#include <stdlib.h>
#include "inputs.h"
#include "system.h"

int main() {
    execute_processes(inputP1Exec00, 12, inputP1Mem00, 5, 00);
    execute_processes(inputP1Exec01, 9, inputP1Mem01, 4, 01);
    execute_processes(inputP1Exec02, 9, inputP1Mem02, 4, 02);
    execute_processes(inputP1Exec03, 18, inputP1Mem03, 7, 03);
    execute_processes(inputP1Exec04, 10, inputP1Mem04, 3, 04);
    return 0;
}