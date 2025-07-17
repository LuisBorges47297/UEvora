#ifndef FIFO_FRAME
#define FIFO_FRAME

struct fifo_frame { // Tipo correspondente a uma página/frame numa memória com política FIFO
    int data; // ID do processo a que o frame pertence
};

typedef struct fifo_frame fifo_frame_dt;
typedef fifo_frame_dt* FIFOFrame; // Ponteiro para o tipo correspondente a uma página/frame numa memória com política FIFO

/**
 * Função para criar uma página/frame de uma memória com política FIFO
*/
FIFOFrame create_fifo_frame(int data);

/**
 * Função para destruir uma página/frame de uma memória com política FIFO
*/
void delete_fifo_frame(FIFOFrame frame);


#endif // FIFO_FRAME