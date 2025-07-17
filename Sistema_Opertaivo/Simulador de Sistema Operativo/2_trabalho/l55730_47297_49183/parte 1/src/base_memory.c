#include "base_memory.h"


int inc(int i) {
    return (i + 1) % MEMORY_FRAMES_NUMBER;
}

int dec(int i) {
    return (MEMORY_FRAMES_NUMBER + i - 1) % MEMORY_FRAMES_NUMBER;
}