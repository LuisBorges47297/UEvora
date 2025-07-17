#include "fifo_frame.h"
#include <stdio.h>
#include <stdlib.h>
#include "messages.h"

FIFOFrame create_fifo_frame(int data) {
    FIFOFrame frame = malloc(sizeof(fifo_frame_dt));
    if(frame == NULL) {
        perror(MEMORY_ALLOCATION_ERROR_MESSAGE);
        exit(1);
    }

    frame->data = data;

    return frame;
}

void delete_fifo_frame(FIFOFrame frame) {
    free(frame);
}