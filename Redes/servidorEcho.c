
// Server side C program to demonstrate Socket
// programming
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/socket.h>
#include <unistd.h>

#define PORT 9999

int main(int argc, char const* argv[])
{
    int server_fd, new_socket;
    ssize_t valread;
    struct sockaddr_in address;
    int opt = 1;
    socklen_t addrlen = sizeof(address);
    time_t rawtime;
    struct tm *timeinfo;
    char buffer[1024] = { 0 };

    //char* hello = "Hello from server";
 
    // Creating socket file descriptor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }
 
    // Forcefully attaching socket to the port 9999

    if (setsockopt(server_fd, SOL_SOCKET,
                   SO_REUSEADDR | SO_REUSEPORT, &opt,
                   sizeof(opt))) {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);
 
    // Forcefully attaching socket to the port 9999

    if (bind(server_fd, (struct sockaddr*)&address,
             sizeof(address))
        < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_fd, 3) < 0) {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    

    while (1) {

        if ((new_socket = accept(server_fd, (struct sockaddr*)&address, &addrlen)) < 0) {
        perror("accept");
        exit(EXIT_FAILURE);
        }

        valread = read(new_socket, buffer, 1024 - 1);
        if (valread == -1) {
            perror("read");
            exit(EXIT_FAILURE);
        } else if (valread == 0) {
            printf("Client disconnected\n");
            break;
        }

        printf("%s\n", buffer);
        
        int fim = strlen(buffer)-1;

        //printf("%d\n", fim);
        
        buffer[fim] = '\0'; 

            if (strcmp(buffer, "quit") == 0) {
                printf("Comando 'quit' recebido. Fechar a ligacao.\n");
                break;
            }
        // Escrever a hora atual para o cliente
        write(new_socket, buffer, strlen(buffer));
    }
    //printf("Hello message sent\n");
 
    // closing the connected socket
    close(new_socket);
    // closing the listening socket
    close(server_fd);
    return 0;   
}