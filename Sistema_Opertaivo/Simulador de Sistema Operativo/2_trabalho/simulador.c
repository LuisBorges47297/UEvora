#include <stdio.h>
#include <string.h>

#define TOTAL_MEMORY_KB 20
#define PAGE_SIZE_KB 2
#define TOTAL_FRAMES (TOTAL_MEMORY_KB / PAGE_SIZE_KB)
#define MAX_PROCESS_MEMORY_KB 11

typedef struct {
    char page_id[10];  // Aumentei o tamanho do buffer para garantir espaço suficiente
    int is_occupied;
} Frame;

Frame memory[TOTAL_FRAMES];
int total_allocated_memory_kb = 0;

// Inicializa a memória principal
void initialize_memory() {
    for (int i = 0; i < TOTAL_FRAMES; i++) {
        memory[i].is_occupied = 0;
        strcpy(memory[i].page_id, "");
    }
}

// Aloca uma página na memória
int allocate_page(const char* page_id) {
    for (int i = 0; i < TOTAL_FRAMES; i++) {
        if (!memory[i].is_occupied) {
            memory[i].is_occupied = 1;
            strncpy(memory[i].page_id, page_id, sizeof(memory[i].page_id) - 1);
            memory[i].page_id[sizeof(memory[i].page_id) - 1] = '\0';  // Garantir terminação nula
            return i;
        }
    }
    return -1;  // Indica falha na alocação
}

// Desaloca uma página da memória
void deallocate_page(const char* page_id) {
    for (int i = 0; i < TOTAL_FRAMES; i++) {
        if (memory[i].is_occupied && strcmp(memory[i].page_id, page_id) == 0) {
            memory[i].is_occupied = 0;
            strcpy(memory[i].page_id, "");
            return;
        }
    }
}

// Aloca um processo na memória
int allocate_process(int process_id, int process_size_kb) {
    int pages_needed = (process_size_kb + PAGE_SIZE_KB - 1) / PAGE_SIZE_KB;
    char page_id[10];
    snprintf(page_id, sizeof(page_id), "P%d_", process_id);

    if (total_allocated_memory_kb + process_size_kb > TOTAL_MEMORY_KB) {
        printf("Erro: Memória insuficiente para alocar o processo %d.\n", process_id);
        return 0;  // Indica falha na alocação
    }

    for (int i = 0; i < pages_needed; i++) {
        char full_page_id[10];
        snprintf(full_page_id, sizeof(full_page_id), "%s%d", page_id, i + 1);
        if (allocate_page(full_page_id) == -1) {
            printf("Erro: Memória cheia. Não foi possível alocar o processo %d.\n", process_id);
            return 0;  // Indica falha na alocação
        }
    }

    total_allocated_memory_kb += process_size_kb;
    printf("Processo %d alocado com sucesso. Memória alocada: %d KB.\n", process_id, total_allocated_memory_kb);
    return 1;  // Indica sucesso na alocação
}

// Desaloca um processo da memória
void deallocate_process(int process_id, int process_size_kb) {
    char page_id[10];
    snprintf(page_id, sizeof(page_id), "P%d_", process_id);

    for (int i = 0; i < TOTAL_FRAMES; i++) {
        if (memory[i].is_occupied && strncmp(memory[i].page_id, page_id, strlen(page_id)) == 0) {
            deallocate_page(memory[i].page_id);
        }
    }

    total_allocated_memory_kb -= process_size_kb;
    printf("Processo %d desalocado com sucesso. Memória alocada: %d KB.\n", process_id, total_allocated_memory_kb);
}

// Exibe o estado atual da memória
void display_memory() {
    printf("Estado atual da memória:\n");
    for (int i = 0; i < TOTAL_FRAMES; i++) {
        if (memory[i].is_occupied) {
            printf("Frame %d: %s\n", i, memory[i].page_id);
        } else {
            printf("Frame %d: Vazio\n", i);
        }
    }
    printf("Memória total alocada: %d KB\n", total_allocated_memory_kb);
}

int main() {
    initialize_memory();

    // Tamanhos dos processos
    int process_sizes[] = {5, 6, 3, 6, 4};
    int num_processes = sizeof(process_sizes) / sizeof(process_sizes[0]);

    // Aloca os processos
    for (int i = 0; i < num_processes; i++) {
        if (!allocate_process(i + 1, process_sizes[i])) {
            printf("Erro: Não foi possível alocar todos os processos. Memória insuficiente.\n");
            break;
        }
    }

    // Exibe o estado atual da memória
    display_memory();

    return 0;
}
