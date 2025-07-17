#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>

#define MAX_CLIENTS 30
#define BUFFER_SIZE 4096  // Tamanho do buffer aumentado para evitar truncamento
#define INTERVAL 10        // Intervalo de tempo para envio de data e hora (em segundos)

// Função para obter a data e hora formatada
void strdate(char *buffer, int len) {
    time_t now = time(NULL);
    struct tm *ptm = localtime(&now);
    
    if (ptm == NULL) {
        puts("A função localtime() falhou");
        return;
    }

    strftime(buffer, len, "%c", ptm);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Uso: %s <porta_sensor> <porta_atuador>\n", argv[0]);
        return EXIT_FAILURE;
    }

    int sensor_port = atoi(argv[1]);
    int actuator_port = atoi(argv[2]);

    if (sensor_port <= 0 || sensor_port > 65535 || actuator_port <= 0 || actuator_port > 65535) {
        fprintf(stderr, "Porta inválida\n");
        return EXIT_FAILURE;
    }

    int server_fd_sensor, server_fd_actuator, new_socket, max_clients = MAX_CLIENTS;
    int activity, valread, sd;
    int max_sd;
    struct sockaddr_in address_sensor, address_actuator;
    fd_set readfds;
    char buffer[BUFFER_SIZE];
    int clients[MAX_CLIENTS];
    socklen_t addrlen_sensor = sizeof(address_sensor);
    socklen_t addrlen_actuator = sizeof(address_actuator);

    // Inicializa todos os sockets de clientes como 0 (não utilizados)
    for (int i = 0; i < max_clients; i++) {
        clients[i] = 0;
    }

    // Criação do socket servidor para sensores
    if ((server_fd_sensor = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Erro ao criar o socket do sensor");
        exit(EXIT_FAILURE);
    }

    // Configuração do endereço do servidor para sensores
    address_sensor.sin_family = AF_INET;
    address_sensor.sin_addr.s_addr = INADDR_ANY;
    address_sensor.sin_port = htons(sensor_port);

    // Associando o socket ao endereço e à porta para sensores
    if (bind(server_fd_sensor, (struct sockaddr *)&address_sensor, sizeof(address_sensor)) < 0) {
        perror("Erro no bind do sensor");
        exit(EXIT_FAILURE);
    }

    // Define o servidor para ouvir novas conexões de sensores
    if (listen(server_fd_sensor, 3) < 0) {
        perror("Erro no listen do sensor");
        exit(EXIT_FAILURE);
    }

    printf("Servidor de sensores está ouvindo na porta %d...\n", sensor_port);

    // Criação do socket servidor para atuadores
    if ((server_fd_actuator = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Erro ao criar o socket do atuador");
        exit(EXIT_FAILURE);
    }

    // Configuração do endereço do servidor para atuadores
    address_actuator.sin_family = AF_INET;
    address_actuator.sin_addr.s_addr = INADDR_ANY;
    address_actuator.sin_port = htons(actuator_port);

    // Associando o socket ao endereço e à porta para atuadores
    if (bind(server_fd_actuator, (struct sockaddr *)&address_actuator, sizeof(address_actuator)) < 0) {
        perror("Erro no bind do atuador");
        exit(EXIT_FAILURE);
    }

    // Define o servidor para ouvir novas conexões de atuadores
    if (listen(server_fd_actuator, 3) < 0) {
        perror("Erro no listen do atuador");
        exit(EXIT_FAILURE);
    }

    printf("Servidor de atuadores está ouvindo na porta %d...\n", actuator_port);

    while (1) {
        FD_ZERO(&readfds);

        // Adiciona os sockets dos servidores ao conjunto de descritores de leitura
        FD_SET(server_fd_sensor, &readfds);
        FD_SET(server_fd_actuator, &readfds);
        max_sd = (server_fd_sensor > server_fd_actuator) ? server_fd_sensor : server_fd_actuator;

        // Adiciona os sockets dos clientes sensores ao conjunto de descritores de leitura
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

        // Verifica se há nova conexão de cliente sensor
        if (FD_ISSET(server_fd_sensor, &readfds)) {
            if ((new_socket = accept(server_fd_sensor, (struct sockaddr *)&address_sensor, &addrlen_sensor)) < 0) {
                perror("Erro no accept do sensor");
                exit(EXIT_FAILURE);
            }

            // Informa sobre nova conexão de sensor
            printf("Novo sensor conectado, socket FD: %d, IP: %s, Porta: %d\n",
                   new_socket, inet_ntoa(address_sensor.sin_addr), ntohs(address_sensor.sin_port));

            // Adiciona novo socket de sensor ao array de clientes
            for (int i = 0; i < max_clients; i++) {
                if (clients[i] == 0) {
                    clients[i] = new_socket;
                    break;
                }
            }
        }

        // Verifica se há nova conexão de cliente atuador
        if (FD_ISSET(server_fd_actuator, &readfds)) {
            if ((new_socket = accept(server_fd_actuator, (struct sockaddr *)&address_actuator, &addrlen_actuator)) < 0) {
                perror("Erro no accept do atuador");
                exit(EXIT_FAILURE);
            }

            // Informa sobre nova conexão de atuador
            printf("Novo atuador conectado, socket FD: %d, IP: %s, Porta: %d\n",
                   new_socket, inet_ntoa(address_actuator.sin_addr), ntohs(address_actuator.sin_port));

            // Adiciona novo socket de atuador ao array de clientes
            for (int i = 0; i < max_clients; i++) {
                if (clients[i] == 0) {
                    clients[i] = new_socket;
                    break;
                }
            }
        }

        // Processa dados recebidos de clientes sensores
        for (int i = 0; i < max_clients; i++) {
            sd = clients[i];
            
            if (FD_ISSET(sd, &readfds)) {
                // Leitura do cliente sensor
                if ((valread = read(sd, buffer, BUFFER_SIZE - 1)) == 0) {
                    // Cliente sensor desconectado
                    getpeername(sd, (struct sockaddr *)&address_sensor, &addrlen_sensor);
                    printf("Sensor desconectado, IP: %s, Porta: %d\n",
                           inet_ntoa(address_sensor.sin_addr), ntohs(address_sensor.sin_port));

                    // Fecha o socket e marca como 0 no array
                    close(sd);
                    clients[i] = 0;
                } else {
                    buffer[valread] = '\0';
                    printf("Dados recebidos do sensor no socket %d: %s\n", sd, buffer);

                    // Envia dados recebidos para todos os clientes atuadores
                    for (int j = 0; j < max_clients; j++) {
                        int actuator_socket = clients[j];
                        if (actuator_socket > 0 && actuator_socket != sd) {
                            snprintf(buffer, BUFFER_SIZE, "SENSOR %d %s\n", sd, buffer);
                            send(actuator_socket, buffer, strlen(buffer), 0);
                        }
                    }
                }
            }
        }
    }

    return 0;
}
