#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <errno.h>
#include "command.h"

void checkError(int status,int line) {
   if (status < 0) {
      printf("socket error(%d)-%d: [%s]\n",getpid(),line,strerror(errno));
      exit(-1);
   }
}

void doLSCommand(int sid);
void doExitCommand(int sid);
void doGETCommand(int sid);
void doPUTCommand(int sid);
void doLLSCommand();
void doSIZECommand(int sid);
void doLSIZECommand();
void doMGETCommand(int sid);

int main(int argc, char* argv[]) {
    int portno = 8080;
    char *host = malloc(50 * sizeof(char));
    strcpy(host, "localhost");
    
    if (argc == 2) {
        strcpy(host, (char*)argv[1]);
    } else if (argc == 3) {
        strcpy(host, (char*)argv[1]);
        portno = atoi(argv[2]);
    }
    
    // Create a socket
    int sid = socket(PF_INET,SOCK_STREAM,0);
    struct sockaddr_in srv;
    struct hostent *server = gethostbyname(host);
    srv.sin_family = AF_INET;
    srv.sin_port   = htons(portno);
    memcpy(&srv.sin_addr.s_addr,server->h_addr,server->h_length);
    int status = connect(sid,(struct sockaddr*)&srv,sizeof(srv));
    checkError(status,__LINE__);
    int done = 0;
    do {
       char opcode[32];
       scanf("%31s",opcode);
       if (strncmp(opcode,"lsize",5) == 0) {
           doLSIZECommand();
       } else if (strncmp(opcode,"ls",2) == 0) {
          doLSCommand(sid);
       } else if (strncmp(opcode,"get",3)==0) {
          doGETCommand(sid);
       } else if (strncmp(opcode,"put",3)==0) {
          doPUTCommand(sid);
       } else if (strncmp(opcode,"exit",4) == 0) {
          doExitCommand(sid);
          done = 1;
          break;
       } else if (strncmp(opcode,"lls",3) == 0) {
           doLLSCommand();
       } else if (strncmp(opcode, "size", 4) == 0) {
           doSIZECommand(sid);
       } else if (strncmp(opcode, "mget", 4) == 0) {
           doMGETCommand(sid);
       } else if (strncmp(opcode, "help", 4) == 0) {
           printf("Available commands are: help, ls, lls, put, get, mget, size, lsize, and exit.\n");
       } else {
           printf("For a list of available commands type 'help'.\n");
       }
    } while(!done);
     
    free(host);
    return 0;
}

void doLSCommand(int sid) {
   Command c;
   Payload p;
   int status;

   c.code = htonl(CC_LS);
   c.arg[0] = 0;
   status = send(sid,&c,sizeof(c),0);checkError(status,__LINE__);
   status = recv(sid,&p,sizeof(p),0);checkError(status,__LINE__);
   p.code = ntohl(p.code);
   p.length = ntohl(p.length);
   int rec  = 0,rem = p.length;
   char* buf = malloc(sizeof(char)*p.length);
   while (rem != 0) {
      int nbrecv = recv(sid,buf+rec,rem,0);checkError(status,__LINE__);
      rec += nbrecv;
      rem -= nbrecv;
   }
   if(p.code == PL_TXT)
      printf("Got: [\n%s]\n",buf);
   else {
      printf("Unexpected payload: %d\n",p.code);
   }
   free(buf);
}

void doLLSCommand() {
    char* msg = makeFileList(".");
    printf("Got: [\n%s]\n", msg);	
}

void doSIZECommand(int sid) {
    Command c;
    Payload p;
    int status;
    
    c.code = htonl(CC_SIZE);
    c.arg[0] = 0;
    char fName[256];
    fgets(fName, 255, stdin);
    if (fName[1] == '\0') {
        printf("Enter remote filename: ");
        fgets(fName, 255, stdin);
    }
    int x;
    if (fName[0] == ' ') {
        for (x = 0; x < 255; x++) {
            fName[x] = fName[x+1];
        }
    }
    char temp[256];
    sscanf(fName, "%s", temp);
    
    strncpy(c.arg,temp,255);
    c.arg[255] = 0;
    status = send(sid,&c,sizeof(c),0);checkError(status,__LINE__);
    
    status = recv(sid,&p,sizeof(p),0);checkError(status,__LINE__);
    p.code = ntohl(p.code);
    p.length = ntohl(p.length);
    
    int rec  = 0,rem = p.length;
    char* buf = malloc(sizeof(char)*p.length);
    while (rem != 0) {
       int nbrecv = recv(sid,buf+rec,rem,0);checkError(status,__LINE__);
       rec += nbrecv;
       rem -= nbrecv;
    }
    if(p.code == PL_TXT) {
       int sz =(int) strtol(buf, (char **)NULL, 10);
       if (sz >= 0) {
          printf("File size: %s [%s]\n", buf, temp);
       } else {
          printf("Invalid file name.\n");
       }       
    } else {
       printf("Unexpected payload: %d\n",p.code);
    }
    free(buf);    
}

void doLSIZECommand() {
    char fName[256];
    fgets(fName, 255, stdin);
    if (fName[1] == '\0') {
        printf("Enter local filename: ");
        fgets(fName, 255, stdin);
    }
    int x;
    if (fName[0] == ' ') {
        for (x = 0; x < 255; x++) {
            fName[x] = fName[x+1];
        }
    }
    char temp[256];
    sscanf(fName, "%s", temp);
    int size = getFileSize(temp);
    if (size >= 0) {
        printf("File size: %d [%s]\n", size, temp);
    } else {
        printf("Invalid file name.\n");
    }
}

void doGETCommand(int sid) {
   Command c;
   Payload p;
   int status;

   c.code = htonl(CC_GET);
   
   char fName[256];
   fgets(fName, 255, stdin);
   if (fName[1] == '\0') {
       printf("Enter remote filename: ");
       fgets(fName, 255, stdin);
   }
   int x;
   if (fName[0] == ' ') {
       for (x = 0; x < 255; x++) {
           fName[x] = fName[x+1];
       }
   }
   char temp[256];
   sscanf(fName, "%s", temp);
   
   strncpy(c.arg,temp,255);
   c.arg[255] = 0;
   status = send(sid,&c,sizeof(c),0);checkError(status,__LINE__);
   status = recv(sid,&p,sizeof(p),0);checkError(status,__LINE__);
   p.code = ntohl(p.code);
   p.length = ntohl(p.length);
   if (p.code == PL_FILE) {
       receiveFileOverSocket(sid,c.arg,".download",p.length);
       printf("Transfer done [%s]\n", temp);
   } else if (p.code == PL_TXT) {
       int rec  = 0,rem = p.length;
       char* buf = malloc(sizeof(char)*p.length);
       while (rem != 0) {
          int nbrecv = recv(sid,buf+rec,rem,0);checkError(status,__LINE__);
          rec += nbrecv;
          rem -= nbrecv;
       }
       if(p.code == PL_TXT) {
          printf("%s\n", buf);      
       } else {
          printf("Unexpected payload: %d\n",p.code);
       }
       free(buf);    
   }
}

void doGET(int sid, char* name) {
   Command c;
   Payload p;
   int status;

   c.code = htonl(CC_GET);
   strncpy(c.arg,name,255);
   c.arg[255] = 0;
   status = send(sid,&c,sizeof(c),0);checkError(status,__LINE__);
   status = recv(sid,&p,sizeof(p),0);checkError(status,__LINE__);
   p.code = ntohl(p.code);
   p.length = ntohl(p.length);
   if (p.code == PL_FILE) {
       receiveFileOverSocket(sid,c.arg,".download",p.length);
       printf("Transfer done [%s]\n", name);
   } else if (p.code == PL_TXT) {
       int rec  = 0,rem = p.length;
       char* buf = malloc(sizeof(char)*p.length);
       while (rem != 0) {
          int nbrecv = recv(sid,buf+rec,rem,0);checkError(status,__LINE__);
          rec += nbrecv;
          rem -= nbrecv;
       }
       if(p.code == PL_TXT) {
          printf("Invalid file name.\n");      
       } else {
          printf("Unexpected payload: %d\n",p.code);
       }
       free(buf);    
   }
}

void doMGETCommand(int sid) {
   int x, y, z, i, j, count;
   char *filebuf = malloc(255 * sizeof(char) + 1);
   fgets(filebuf, 255, stdin);
   if (filebuf[1] == '\0') {
       printf("Enter remote filenames separated by spaces: ");
       fgets(filebuf, 255, stdin);
   }
   
   if (filebuf[0] == ' ') {
       for (x = 0; x < 255; x++) {
           filebuf[x] = filebuf[x+1];
       }
   }
   
   char **targetList = malloc(20 * sizeof(char*));
   for (y = 0; y < 20; y++) {
       targetList[y] = malloc(255 * sizeof(char) + 1);
   }
   j = 0;
   count = 0;
   for(i = 0; i <= (strlen(filebuf)); i++) {
       if(filebuf[i] == ' ' || filebuf[i] == '\0' || filebuf[i] == '\n') {
           targetList[count][j] = '\0';
           count++;  
           j = 0;    
       } else {
           targetList[count][j] = filebuf[i];
           j++;
       }
   } 
   
   for(z = 0; z < count - 1; z++){
        printf("%s\n", targetList[z]);
        doGET(sid, targetList[z]);
   }
   for (y = 0; y < 20; y++) {
       free(targetList[y]);
   }
   free(targetList);
   free(filebuf);
}

void doPUTCommand(int sid) {
   Command c;
   Payload p;
   int status;

   c.code = htonl(CC_PUT);
   char fName[256];
   fgets(fName, 255, stdin);
   if (fName[1] == '\0') {
       printf("Enter local filename: ");
       fgets(fName, 255, stdin);
   }
   int x;
   if (fName[0] == ' ') {
       for (x = 0; x < 255; x++) {
           fName[x] = fName[x+1];
       }
   }
   char temp[256];
   sscanf(fName, "%s", temp);
   
   FILE* f = fopen(temp,"r");
   if (f != NULL) {
       fclose(f);
       strncpy(c.arg,temp,255);
       c.arg[255] = 0;
       status = send(sid,&c,sizeof(c),0);checkError(status,__LINE__);
       int fs = getFileSize(c.arg);
       p.code   = ntohl(PL_FILE);
       p.length = ntohl(fs);
       status = send(sid,&p,sizeof(p),0);checkError(status,__LINE__);
       status = sendFileOverSocket(c.arg,sid);
       printf("Transfer done [%s]\n", temp);
   } else {
       printf("Invalid file name.\n");
   }
   
}

void doExitCommand(int sid) {
   Command c;
   Payload p;
   int status;

   c.code = htonl(CC_EXIT);
   c.arg[0] = 0;
   status = send(sid,&c,sizeof(c),0);checkError(status,__LINE__);
}


