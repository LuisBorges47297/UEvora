#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>
#include <errno.h>
#include <sys/select.h>

#define PORT 9999
#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;
    fd_set readfds;
    char buffer[BUFFER_SIZE];

    portno = PORT;

    /* Create a socket point */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("ERROR opening socket");
        exit(1);
    }

    server = gethostbyname("localhost");
    if (server == NULL) {
        fprintf(stderr, "ERROR, no such host\n");
        exit(1);
    }

    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, (char *)&serv_addr.sin_addr.s_addr, server->h_length);
    serv_addr.sin_port = htons(portno);

    /* Now connect to the server */
    if (connect(sockfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
        perror("ERROR connecting");
        exit(2);
    }

    printf("Conectado ao servidor na porta %d...\n", portno);

    while (1) {
        FD_ZERO(&readfds);

        // Adiciona STDIN ao conjunto de descritores de leitura
        FD_SET(STDIN_FILENO, &readfds);
        // Adiciona o socket ao conjunto de descritores de leitura
        FD_SET(sockfd, &readfds);
        int max_sd = sockfd;

        // Espera pela atividade em um dos descritores
        int activity = select(max_sd + 1, &readfds, NULL, NULL, NULL);
        if ((activity < 0) && (errno != EINTR)) {
            printf("Erro na função select\n");
        }

        // Se houver entrada do usuário
        if (FD_ISSET(STDIN_FILENO, &readfds)) {
            char input[BUFFER_SIZE];
            if (fgets(input, sizeof(input), stdin) != NULL) {
                send(sockfd, input, strlen(input), 0);
            } else {
                printf("Erro ao ler entrada\n");
            }
        }

        // Se houver dados do servidor
        if (FD_ISSET(sockfd, &readfds)) {
            bzero(buffer, BUFFER_SIZE);
            n = read(sockfd, buffer, BUFFER_SIZE - 1);
            if (n > 0) {
                buffer[n] = '\0';
                printf("Servidor: %s", buffer);
            } else if (n == 0) {
                printf("Servidor desconectado\n");
                close(sockfd);
                exit(0);
            } else {
                perror("ERROR reading from socket");
                close(sockfd);
                exit(3);
            }
        }
    }

    close(sockfd);
    return 0;
}
