/***********************************************************************/
/********** Author: Minas Spetsakis                           **********/
/********** Description: A character count using read         **********/
/********** July 2021                                         **********/
/********** Completed By Edward Shirinian - 214456818         **********/                                          
/***********************************************************************/
#include <sys/mman.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

int my_cnt(char *f){

  
  struct stat s;
  int pageSize = sysconf(_SC_PAGESIZE);
  char * buffer = (char *)malloc(pageSize);
  int fileDescriptor = open(f, O_RDONLY, S_IRUSR);

  if(fstat(fileDescriptor, &s) == -1){
        perror("");
        return -1;
  }


  int fileSize = s.st_size;
  int count = 0;
   
  while (fileSize > 0){
    int chunkSize; 
    
    //Determining pagsize chunk
    if(fileSize > pageSize){
      chunkSize = pageSize;
    }else{
      chunkSize = fileSize;
    }

    int bytesRead = read(fileDescriptor, buffer, chunkSize);
    fileSize -= bytesRead;        
        
    if (bytesRead == -1){
      perror("Error Reading File");
      return -1;
    } 

    //counting characters
    int i = 0;
    for(i; i < bytesRead; i++){
      if(buffer[i] != '\n'){
        count++;
      }     
    }       
  }

  return count;
   
  close(fileDescriptor);

}

