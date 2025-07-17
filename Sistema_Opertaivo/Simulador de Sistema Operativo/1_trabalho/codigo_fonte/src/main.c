#include <stdio.h>
#include <stdlib.h>
#include "system.h"
#include "inputs.h"
#include "messages.h"

int main() {
    FILE* file_to_store_output00 = fopen("output00.out", "w");
    if(file_to_store_output00 == NULL) {
        perror(INVALID_FILE_OPPENING_MESSAGE);
        exit(1);
    }
    run(input00, 3, file_to_store_output00);
    fclose(file_to_store_output00);

    FILE* file_to_store_output01 = fopen("output01.out", "w");
    if(file_to_store_output01 == NULL) {
        perror(INVALID_FILE_OPPENING_MESSAGE);
        exit(1);
    }
    run(input01, 4, file_to_store_output01);
    fclose(file_to_store_output01);

    FILE* file_to_store_output02 = fopen("output02.out", "w");
    if(file_to_store_output02 == NULL) {
        perror(INVALID_FILE_OPPENING_MESSAGE);
        exit(1);
    }
    run(input02, 5, file_to_store_output02);
    fclose(file_to_store_output02);

    FILE* file_to_store_output03 = fopen("output03.out", "w");
    if(file_to_store_output03 == NULL) {
        perror(INVALID_FILE_OPPENING_MESSAGE);
        exit(1);
    }
    run(input03, 7, file_to_store_output03);
    fclose(file_to_store_output03);

    FILE* file_to_store_output04 = fopen("output04.out", "w");
    if(file_to_store_output04 == NULL) {
        perror(INVALID_FILE_OPPENING_MESSAGE);
        exit(1);
    }
    run(input04, 10, file_to_store_output04);
    fclose(file_to_store_output04);

    return 0;
}