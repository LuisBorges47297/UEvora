#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "process.h"
#include "messages.h"

Process create_process(int id, int size) {
    Process process = (Process) malloc( sizeof(process_t) );

    if(process == NULL) {
        perror(MEMORY_ALLOCATION_ERROR_MESSAGE);
        exit(1);
    }

    process->id = id;
    process->frames_starts_at = -1;
    process->size = size;
    
    return process;
}

void delete_process(Process process) {
    free(process);
}