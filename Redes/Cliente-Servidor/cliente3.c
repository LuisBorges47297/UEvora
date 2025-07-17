#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>
#include <errno.h>
#include <sys/select.h>

#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;
    fd_set readfds;
    char buffer[BUFFER_SIZE];

    if (argc != 3) {
        fprintf(stderr,"Uso: %s <endereco> <porta>\n", argv[0]);
        exit(1);
    }

    portno = atoi(argv[2]);

    /* Create a socket point */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("ERROR opening socket");
        exit(1);
    }

    server = gethostbyname(argv[1]);
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

    printf("Conectado ao servidor %s na porta %d...\n", argv[1], portno);

    while (1) {
        FD_ZERO(&readfds);

        // Adiciona o socket ao conjunto de descritores de leitura
        FD_SET(sockfd, &readfds);
        int max_sd = sockfd;

        // Espera pela atividade em um dos descritores
        int activity = select(max_sd + 1, &readfds, NULL, NULL, NULL);
        if ((activity < 0) && (errno != EINTR)) {
            printf("Erro na função select\n");
        }

        // Se houver dados do servidor
        if (FD_ISSET(sockfd, &readfds)) {
            bzero(buffer, BUFFER_SIZE);
            n = read(sockfd, buffer, BUFFER_SIZE - 1);
            if (n > 0) {
                buffer[n] = '\0';
                if (strncmp(buffer, "PING", 4) == 0) {
                    // Construir a mensagem de resposta PONG
                    char pong_response[BUFFER_SIZE];
                    snprintf(pong_response, BUFFER_SIZE, "PONG %s", buffer + 5); // Pular o "PING "
                    send(sockfd, pong_response, strlen(pong_response), 0);
                } else if (strncmp(buffer, "ONLINE", 6) == 0) {
                    // Processar mensagem ONLINE
                    char *user = buffer + 7; // Pular "ONLINE "
                    user[strcspn(user, "\n")] = '\0'; // Remover o newline no final da string
                    printf("%s está online!\n", user);
                } else if (strncmp(buffer, "MSGFROM", 7) == 0) {
                    // Extrair o usuário e a mensagem
                    char user[BUFFER_SIZE];
                    char message[BUFFER_SIZE];
                    sscanf(buffer + 8, "%s %[^\n]", user, message);
                    printf("%s disse %s\n", user, message);
                }
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
