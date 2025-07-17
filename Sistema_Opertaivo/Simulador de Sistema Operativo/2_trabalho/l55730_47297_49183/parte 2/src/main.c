#include <stdio.h>
#include <stdlib.h>
#include "system.h"
#include "inputs.h"
#include "messages.h"

int main() {
    
    run(inputP2Exec00, inputP2Mem00, 3, 0); 
    run(inputP2Exec01, inputP2Mem01, 4, 1); 
    run(inputP2Exec02, inputP2Mem02, 5, 2); 
    run(inputP2Exec03, inputP2Mem03, 7, 3); 
    run(inputP2Exec04, inputP2Mem04, 10, 4); 
    
    return 0;
}