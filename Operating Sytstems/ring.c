/* Signal ring */
/* 
Submitted by Edward Shirinian - 214456818
June 11, 2021

When this program is first initailzed it makes the current pid the leader pid. It then calls
forkProcesses() which creates the children processes and suspends the parents in a while loop. 
The while loop ensures that the ring is created cnt times.Once the nth child is created it calls
wakeup().The leader wakes up and calls wakeup() to wake the child up and then is suspended again.
This cycle of calling wakup() to wake the child and suspending is done until cnt == 0. When that 
happends the "wakeup() chain" happens once more from parent to child ensuring that all the processes
terminate.    

*/

#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdint.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <signal.h>
#include <sys/time.h>
#include <errno.h> 
#include <string.h>

void forkProcesses(int n, int leader, int cnt);
void signalHandler(int signal);
void wakeup();
void errorCheck(int i);


struct timeval t1, t2;
float elapsedTime; //time output
int n; //number of processes
int leader;
int cnt; //the current cnt
int cntoutput;//cnt output
pid_t child; //the child of this process

int main(int argc, char *argv[])
{
  //starts the timer and initializes variables
  errorCheck(gettimeofday(&t1, NULL));
  n = atoi(argv[1]);
  leader = atoi(argv[2]);
  cnt = atoi(argv[3]);
  cntoutput = atoi(argv[3]);
  pid_t pid = getpid();
  signal(SIGUSR1,signalHandler);

  //leader is this pid if argv[2] == 0 
  if(leader == 0){
    leader = getpid();        
    forkProcesses(n,leader,cnt);    
  }else{
    forkProcesses(n,leader,cnt);
  }

  return(1);
}


void forkProcesses(int ni, int leader, int cnt)
{ 
  //decrements the next "n" to be passed to exec
  ni = ni - 1;

  //checks to see if enough child processes have been made
  //sleep(1);
  
  if(ni >= 1){
    errorCheck(child=fork());
    if(child < 0){
        printf("Did not fork \n");
    }else if(child==0){
      //child code execution

      //This process will not create another exec but instead wake up the leader with SIGUSR1
      if(ni == 1){
        //printf("n is %d, pid is %d, leader pid is %d\n", ni, getpid(), leader);
        errorCheck(kill(leader, SIGUSR1));

        //keeps looping until cnt = 0. 
        //Another loop is needed here because this child will never get to the parent code execution but must also wait for another signal.
        while(cnt>0){
          errorCheck(pause()); // waits for the next SIGSUR1
          wakeup(); // wakes up next process
        }         
      }else{
        //create the next process
        char nstr[10], leaderstr[10], cntstr[10];    
        sprintf(nstr,"%d", ni);
        sprintf(leaderstr,"%d", leader);
        sprintf(cntstr,"%d",cnt); 
        char *args[] = {"./ring",nstr, leaderstr, cntstr, NULL};
        errorCheck(execvp("./ring", args)); 
      }      
    }else{
      //parent code execution
      //parent is put to sleep after forking another process
      while(cnt > 0){
        errorCheck(pause());
        wakeup();
      }
        
    }
    }
}

void signalHandler(int signal) {}

//when the current process wakes up it wakes up the child process
void wakeup()
{
  if(cnt > 0){
    //sleep(1);
    cnt = cnt -1;
    if(child == 0){
    errorCheck(kill(leader, SIGUSR1));
      
    }else{
     errorCheck(kill(child,SIGUSR1));
    }
  }else{
    //when cnt is 0 the leader exits first but it must call the next child so the next child can call the next child... etc until they all exit.

    if(child == 0){
      errorCheck(kill(leader, SIGUSR1));
    }else{
      errorCheck(kill(child,SIGUSR1));
    }
    
    //leader prints out elapsed time
    if(getpid() == leader){
      errorCheck(gettimeofday(&t2, NULL));
      elapsedTime = (t2.tv_sec - t1.tv_sec) * 1000.0;      
      elapsedTime += (t2.tv_usec - t1.tv_usec) / 1000.0;
      printf("%f ms. with %d cnt\n", elapsedTime, cntoutput) ;
      fflush(stdout);
    }
    exit(1);
  }
}


//exec and kill return -1 if they have an error
void errorCheck(int i){
    if(i == -1){
      strerror(errno);
    }
}

  



  






