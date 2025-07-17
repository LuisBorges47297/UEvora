#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 9999
#define MAX_CLIENTS 30
#define BUFFER_SIZE 1024
#define TIMEOUT_SECONDS 10

#define MAX_QUESTIONS 100
#define MAX_ANSWERS_PER_QUESTION 10
#define MAX_QUESTION_TEXT_SIZE 1024
#define MAX_ANSWER_TEXT_SIZE 1024

// Estrutura para armazenar informações de cada cliente
struct client_info {
    int sockfd;
    char username[100];
};

struct answer {
    char responder_name[100];
    char answer_text[MAX_ANSWER_TEXT_SIZE];
    int answer_id;
};

struct question {
    char question_text[MAX_QUESTION_TEXT_SIZE];
    int question_id;
    struct answer answers[MAX_ANSWERS_PER_QUESTION];
    int answer_count;
};

struct question_data {
    struct question questions[MAX_QUESTIONS];
    int question_count;
};

struct answer_info {
    char responder_name[100];
    char answer_text[MAX_ANSWER_TEXT_SIZE];
};

int main(int argc, char const *argv[]) {
    int server_fd, new_socket, max_clients = MAX_CLIENTS;
    int activity, valread, sd;
    int max_sd;
    struct sockaddr_in address;
    fd_set readfds;
    char buffer[BUFFER_SIZE];
    struct client_info clients[MAX_CLIENTS];
    int client_count = 0;
    socklen_t addrlen;
    struct question_data server_questions;

    // Inicializa estrutura de dados para perguntas e respostas
    server_questions.question_count = 0;
    for (int i = 0; i < MAX_QUESTIONS; i++) {
        server_questions.questions[i].question_id = -1;  // Indicando que a posição está vazia
        server_questions.questions[i].answer_count = 0;
    }

    // Inicializa todos os sockets de clientes como 0 (não utilizados)
    for (int i = 0; i < max_clients; i++) {
        clients[i].sockfd = 0;
        strcpy(clients[i].username, "");
    }

    // Criação do socket servidor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Erro ao criar o socket");
        exit(EXIT_FAILURE);
    }

    // Configuração do endereço do servidor
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

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

    printf("Servidor está ouvindo na porta %d...\n", PORT);

    while (1) {
        FD_ZERO(&readfds);

        // Adiciona o socket do servidor ao conjunto de descritores de leitura
        FD_SET(server_fd, &readfds);
        max_sd = server_fd;

        // Adiciona os sockets dos clientes ao conjunto de descritores de leitura
        for (int i = 0; i < max_clients; i++) {
            sd = clients[i].sockfd;
            if (sd > 0) {
                FD_SET(sd, &readfds);
            }
            if (sd > max_sd) {
                max_sd = sd;
            }
        }

        // Configuração do timeout para o select
        struct timeval timeout;
        timeout.tv_sec = TIMEOUT_SECONDS;
        timeout.tv_usec = 0;

        // Espera por atividade em algum dos sockets
        activity = select(max_sd + 1, &readfds, NULL, NULL, &timeout);

        if ((activity < 0) && (errno != EINTR)) {
            printf("Erro na função select\n");
        }

        // Verifica se há nova conexão de cliente
        if (FD_ISSET(server_fd, &readfds)) {
            addrlen = sizeof(address);
            if ((new_socket = accept(server_fd, (struct sockaddr *)&address, &addrlen)) < 0) {
                perror("Erro no accept");
                exit(EXIT_FAILURE);
            }

            // Informa sobre nova conexão
            printf("Novo cliente conectado, socket FD: %d, IP: %s, Porta: %d\n",
                   new_socket, inet_ntoa(address.sin_addr), ntohs(address.sin_port));

            // Adiciona novo socket ao array de clientes
            for (int i = 0; i < max_clients; i++) {
                if (clients[i].sockfd == 0) {
                    clients[i].sockfd = new_socket;
                    break;
                }
            }
        }

        // Processa dados recebidos de clientes
        for (int i = 0; i < max_clients; i++) {
            sd = clients[i].sockfd;

            if (FD_ISSET(sd, &readfds)) {
                // Leitura do cliente
                if ((valread = read(sd, buffer, BUFFER_SIZE - 1)) == 0) {
                    // Cliente desconectado
                    getpeername(sd, (struct sockaddr *)&address, &addrlen);
                    printf("Cliente desconectado, IP: %s, Porta: %d\n",
                           inet_ntoa(address.sin_addr), ntohs(address.sin_port));

                    // Fecha o socket e marca como 0 no array
                    close(sd);
                    clients[i].sockfd = 0;
                    strcpy(clients[i].username, "");
                } else {
                    buffer[valread] = '\0';

                    // Verifica se é uma mensagem de login
                    if (strncmp(buffer, "IAM ", 4) == 0) {
                        // Extrai o nome de usuário
                        char *username = buffer + 4;
                        strcpy(clients[i].username, username);

                        printf("Novo usuário registrado: %s no socket %d\n", clients[i].username, sd);
                    } else if (strncmp(buffer, "MSG ", 4) == 0) {
                        // Envia a mensagem recebida para todos os clientes
                        char msg_to_send[BUFFER_SIZE];
                        snprintf(msg_to_send, BUFFER_SIZE, "<%s> %s", clients[i].username, buffer + 4);

                        // Exibe no servidor
                        printf("Recebida mensagem do usuário %s no socket com FD %d:\n%s\n", clients[i].username, sd, buffer + 4);

                        // Envia para todos os clientes conectados
                        for (int j = 0; j < max_clients; j++) {
                            if (clients[j].sockfd > 0) {
                                send(clients[j].sockfd, msg_to_send, strlen(msg_to_send), 0);
                            }
                        }
                    } else if (strncmp(buffer, "ASK ", 4) == 0) {
                        // Mensagem de fazer uma pergunta
                        char *question_text = buffer + 4;

                        // Adiciona a pergunta à estrutura de dados
                        if (server_questions.question_count < MAX_QUESTIONS) {
                            int question_id = server_questions.question_count + 1;
                            strcpy(server_questions.questions[server_questions.question_count].question_text, question_text);
                            server_questions.questions[server_questions.question_count].question_id = question_id;
                            server_questions.questions[server_questions.question_count].answer_count = 0;

                            printf("QUESTION %d: %s\n", question_id, question_text);

                            server_questions.question_count++;
                        } else {
                            printf("Limite máximo de perguntas alcançado. Não foi possível adicionar nova pergunta.\n");
                        }
                    } else if (strncmp(buffer, "ANSWER ", 7) == 0) {
                        // Mensagem de responder a uma pergunta
                        int question_id;
                        char *answer_text = buffer + 7;

                        if (sscanf(buffer + 7, "%d ", &question_id) == 1) {
                            // Procura a pergunta correspondente pelo ID
                            for (int q = 0; q < server_questions.question_count; q++) {
                                if (server_questions.questions[q].question_id == question_id) {
                                    // Adiciona a resposta à pergunta
                                    struct question *q_ptr = &server_questions.questions[q];
                                    struct answer *answers = q_ptr->answers;
                                    int answer_count = q_ptr->answer_count;

                                    if (answer_count < MAX_ANSWERS_PER_QUESTION) {
                                        strcpy(answers[answer_count].responder_name, clients[i].username);
                                        strcpy(answers[answer_count].answer_text, answer_text);
                                        answers[answer_count].answer_id = answer_count + 1;
                                        q_ptr->answer_count++;

                                        printf("REGISTERED %d\n", answer_count + 1);
                                    } else {
                                        printf("Limite máximo de respostas para esta pergunta alcançado.\n");
                                    }
                                    break;
                                }
                            }
                        }
                    } else if (strncmp(buffer, "LISTQUESTIONS", 13) == 0) {
                        // Envia a lista de perguntas para o cliente
                        char list_response[BUFFER_SIZE];
                        snprintf(list_response, BUFFER_SIZE, "LISTQUESTIONS\n");

                        // Constrói a lista de perguntas
                        for (int q = 0; q < server_questions.question_count; q++) {
                            struct question *q_ptr = &server_questions.questions[q];
                            snprintf(list_response + strlen(list_response), BUFFER_SIZE - strlen(list_response),
                                    "QUESTION %d: %s\n", q_ptr->question_id, q_ptr->question_text);

                            // Adiciona as respostas à lista
                            for (int a = 0; a < q_ptr->answer_count; a++) {
                                struct answer *answer_ptr = &q_ptr->answers[a];
                                snprintf(list_response + strlen(list_response), BUFFER_SIZE - strlen(list_response),
                                        "  ANSWER %d by %s: %s\n", answer_ptr->answer_id, answer_ptr->responder_name, answer_ptr->answer_text);
                            }
                        }

                        // Exibe no servidor
                        printf("Enviando lista de perguntas e respostas para o usuário %s no socket com FD %d:\n%s\n", clients[i].username, sd, list_response);

                        // Envia para o cliente conectado
                        send(sd, list_response, strlen(list_response), 0);
                    }
                }
            }
        }
    }
}