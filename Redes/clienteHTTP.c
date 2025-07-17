#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>

int main(int argc, char *argv[]) {
   int sockfd, portno, n;
   struct sockaddr_in serv_addr;
   struct hostent *server;
   char* pedidos =  "GET /~pp/redes/grande.txt HTTP/1.0\n\n";

   char input[100];


   char buffer[1024];
   portno = 80;

   /* Create a socket point */
   sockfd = socket(AF_INET, SOCK_STREAM, 0);
   if (sockfd < 0) {
      perror("ERROR opening socket");
      exit(1);
   }
   
   server = gethostbyname("www.di.uevora.pt");
   if (server == NULL) {
     fprintf(stderr,"ERROR, no such host\n");
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

    // Envia a solicitação GET
    if (send(sockfd, pedidos, strlen(pedidos), 0) < 0) {
        perror("Erro ao enviar no socket");
        close(sockfd);
        exit(1);
    }
                              
   /* Now read server response */
   bzero(buffer,1024);
   
   // Lê a resposta do servidor
    while ((n = read(sockfd, buffer, 1024 - 1)) > 0) {
        buffer[n] = '\0'; // Assegura que o buffer é uma string NUL-terminated
        printf("%s", buffer);
    }
   
   if (n < 0) {
     perror("ERROR reading from socket");
     exit(3);
   }

   printf("%s\n",buffer);

   close(sockfd);

   return 0;
}