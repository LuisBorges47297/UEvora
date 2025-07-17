#ifndef BASE_MEMORY_H
#define BASE_MEMORY_H

#define MEMORY_MAX_SIZE 20
#define BYTES_PER_FRAME 2
#define MEMORY_FRAMES_NUMBER 10

/**
 * Esta função avança o ponteiro da memória para a posição seguinte
*/
int inc(int i);

/**
 * Esta função retrocede o ponteiro da memória para a posição anterior
*/
int dec(int i);

#endif