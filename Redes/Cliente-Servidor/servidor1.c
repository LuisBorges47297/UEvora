#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>  // Para a função gethostbyname

#define MAX_CLIENTS 30
#define BUFFER_SIZE 4096  // Aumentando o tamanho do buffer para evitar truncamentos

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Uso: %s <porta>\n", argv[0]);
        return EXIT_FAILURE;
    }

    int port = atoi(argv[1]);
    if (port <= 0 || port > 65535) {
        fprintf(stderr, "Porta inválida: %s\n", argv[1]);
        return EXIT_FAILURE;
    }

    int server_fd, new_socket, max_clients = MAX_CLIENTS;
    int activity, valread, sd;
    int max_sd;
    struct sockaddr_in address;
    fd_set readfds;
    char buffer[BUFFER_SIZE];
    int clients[MAX_CLIENTS];
    socklen_t addrlen = sizeof(address);

    // Inicializa todos os sockets de clientes como 0 (não utilizados)
    for (int i = 0; i < max_clients; i++) {
        clients[i] = 0;
    }

    // Criação do socket servidor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Erro ao criar o socket");
        exit(EXIT_FAILURE);
    }

    // Configuração do endereço do servidor
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(port);

    // Associando o socket ao endereço e à porta
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("Erro no bind");
        exit(EXIT_FAILURE);
    }

    // Define o servidor para ouvir novas conexões
    if (listen(server_fd, 3) < 0) {
        perror("Erro no listen");
        exit(EXIT_FAILURE);
    }

    printf("Servidor está ouvindo na porta %d...\n", port);

    while (1) {
        FD_ZERO(&readfds);

        // Adiciona o socket do servidor ao conjunto de descritores de leitura
        FD_SET(server_fd, &readfds);
        max_sd = server_fd;

        // Adiciona os sockets dos clientes ao conjunto de descritores de leitura
        for (int i = 0; i < max_clients; i++) {
            sd = clients[i];
            if (sd > 0) {
                FD_SET(sd, &readfds);
            }
            if (sd > max_sd) {
                max_sd = sd;
            }
        }

        // Espera por atividade em algum dos sockets
        activity = select(max_sd + 1, &readfds, NULL, NULL, NULL);

        if ((activity < 0) && (errno != EINTR)) {
            printf("Erro na função select\n");
        }

        // Verifica se há nova conexão de cliente
        if (FD_ISSET(server_fd, &readfds)) {
            if ((new_socket = accept(server_fd, (struct sockaddr *)&address, &addrlen)) < 0) {
                perror("Erro no accept");
                exit(EXIT_FAILURE);
            }

            // Informa sobre nova conexão
            printf("HOST %s %d\n",
                    inet_ntoa(address.sin_addr), ntohs(address.sin_port));

            // Adiciona novo socket ao array de clientes
            for (int i = 0; i < max_clients; i++) {
                if (clients[i] == 0) {
                    clients[i] = new_socket;
                    break;
                }
            }
        }

        // Processa dados recebidos de clientes
        for (int i = 0; i < max_clients; i++) {
            sd = clients[i];
            
            if (FD_ISSET(sd, &readfds)) {
                // Leitura do cliente
                if ((valread = read(sd, buffer, BUFFER_SIZE - 1)) == 0) {
                    // Cliente desconectado
                    getpeername(sd, (struct sockaddr *)&address, &addrlen);
                    printf("Cliente desconectado, IP: %s, Porta: %d\n",
                           inet_ntoa(address.sin_addr), ntohs(address.sin_port));

                    // Fecha o socket e marca como 0 no array
                    close(sd);
                    clients[i] = 0;
                } else {
                    buffer[valread] = '\0';
                    printf("Recebido do cliente no socket %d: %s\n", sd, buffer);

                    // Processamento da requisição DNS
                    char *hostname = strtok(buffer, " ");
                    if (hostname != NULL && strcmp(hostname, "IP") == 0) {
                        hostname = strtok(NULL, " ");
                        if (hostname != NULL) {
                            struct hostent *host_entry;
                            host_entry = gethostbyname(hostname);

                            if (host_entry == NULL) {
                                snprintf(buffer, BUFFER_SIZE, "HOST %s UNKNOWN\n", hostname);
                            } else {
                                struct in_addr **addr_list = (struct in_addr **)host_entry->h_addr_list;
                                if (addr_list[0] != NULL) {
                                    snprintf(buffer, BUFFER_SIZE, "HOST %s %s\n", hostname, inet_ntoa(*addr_list[0]));
                                } else {
                                    snprintf(buffer, BUFFER_SIZE, "HOST %s UNKNOWN\n", hostname);
                                }
                            }
                            
                            // Envia resposta ao cliente
                            send(sd, buffer, strlen(buffer), 0);
                        }
                    }
                }
            }
        }
    }

    return 0;
}
