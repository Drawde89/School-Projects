/************                        N L C N T                              ***********/
/************               Example of dynamic linking                     ***********/
/************               Minas Spetsakis, July 2021                    ***********/
/************   Completed By Edward Shirinian - 214456818, July 2021     ***********/

#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
bool verifyInput(char *s, char array[]);


void show_usage(char *name)
{
  printf("USAGE: %s reading|mapping filename\n",name);
  exit(1);
}

int main(int argc, char **argv)
{
  char *s;
  void *handle;
  int (*my_cnt)(char *);
  bool map, read, opened;

  
  s = argv[1]; 
  opened = false; //did dlopen get called

  char mapping[8] = {'m','a','p','p','i','n','g','\0'};
  char reading[8] = {'r','e','a','d','i','n','g','\0'};
 
  map = verifyInput(s,mapping); 
  read = verifyInput(s,reading);
  

  if(map){
    handle = dlopen("./mapping.so", RTLD_LAZY);
    opened = true;
    if(handle != NULL){
      my_cnt = dlsym(handle,"my_cnt");
      int count = my_cnt(argv[2]);
      
      if(count == -1){
        printf("Error\n");
      }else{
        printf("Mapping Count: %d\n", count);
      }

    }
   
  }else if(read){
    handle = dlopen("./reading.so", RTLD_LAZY);
    opened = true;
    if(handle != NULL){
      my_cnt = dlsym(handle,"my_cnt");
      my_cnt(argv[2]);
      int count = my_cnt(argv[2]);
      
      if(count == -1){
        printf("Error\n");
      }else{
        printf("Reading Count: %d\n", count);
      }

    }
  }else{
    printf("No Match\n");
  }


  
  if (argc!=3)
    show_usage(argv[0]);

  if(opened){
    dlclose(handle);
  }

  exit(0);
}

//verifies if s is a valid input
bool verifyInput(char *s, char array[]){
  int inputSize = strlen(s);
  int i = 0;
  size_t n = sizeof(array)/sizeof(array[0]); //gets the amount of elements in array 

  if(inputSize > n){
    return false;
  }

  if(inputSize < 1){
    return false;
  }
  for(i; i < inputSize; i++){
      if(s[i] != array[i]){
        return false;
      }
  }

  return true;
}