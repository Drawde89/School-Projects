/***********************************************************************/
/********** Author: Minas Spetsakis                           **********/
/********** Description: A simple example of mmap usage       **********/
/********** for counting newlines in a file                   **********/
/********** July 2021                                         **********/
/********** Completed By Edward Shirinian - 214456818         **********/                                          
/***********************************************************************/
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
int my_cnt(char *f){

    struct stat s;
    int fileDescriptor = open(f, O_RDONLY, S_IRUSR);

    if(fstat(fileDescriptor, &s) == -1){
        perror("could not get file size\n");
        return -1;
    }

    char *file = mmap(NULL, s.st_size, PROT_READ, MAP_PRIVATE, fileDescriptor, 0);

    int count = 0;
    int i = 0;
    for(i; i < s.st_size; i++){
            if(file[i]!= '\n'){
                count ++;
            }
    }
    close(fileDescriptor);
    return count;
}
