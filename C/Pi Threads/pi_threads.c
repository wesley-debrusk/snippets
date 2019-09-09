#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <pthread.h>
#include <assert.h>

typedef struct {
    int n;          // number of terms to be added
    int n_threads;  // number of threads, to tell how much work each thread must do
    int id;         // between 0 and n_threads - 1
    pthread_t tid;  // thread ID for joining
    double sum;     // used to return thread sum
} thread_struct;

// the main funciton of the threads
void *adder(void* args) {
    thread_struct* data = (thread_struct*) args;
    
    int to_compute = data->n / data->n_threads;
    int start = data->id * to_compute;
    int end = start + to_compute;
    
    int i;
    for(i = start; i <= end; i++) {
        if(i%2==0) data->sum += 1.0/(2*i+1);
        else data->sum -= 1.0/(2*i+1);
    }
    return data;
}

int main(int argc, char *argv[]) 
{
    int n, n_threads;
    double PI25 = 3.141592653589793238462643;
    double my_pi, sum = 0.0;
    int t;
    int rc;

    if (argc != 3) {
        printf("Usage: %s n t\n", argv[0] );
        exit(0);
    }

    n = atoi(argv[1]);
    n_threads = atoi(argv[2]);
    assert(n > 0);
    assert(n_threads > 0);
    
    pthread_t threads[n_threads];
    thread_struct thread_data_array[n_threads];
    
    for (t = 0; t < n_threads; t++) {
        thread_data_array[t].n = n;
        thread_data_array[t].n_threads = n_threads;
        thread_data_array[t].id = t;
        thread_data_array[t].tid = t;
        thread_data_array[t].sum = 0.0;
        
        printf("Creating thread #%d\n", t);
        rc = pthread_create(&threads[t], NULL, adder, (void*) &thread_data_array[t]);
        if (rc) {
           printf("ERROR; return code from pthread_create() is %d\n", rc);
           return NULL;
        }
    }
    
    for(t = 0; t < n_threads; t++) {
 
       printf("Joining thread #%d\n", t);
       rc = pthread_join(threads[t], NULL);
       sum += thread_data_array[t].sum;
       if( rc ){
          printf("ERROR; return code from pthread_join() is %d\n", rc);
          exit(-1);
       }
    }
   
    my_pi = 4 * sum;
    printf("pi approximation: %.16f Error: %.16f\n", my_pi, fabs(my_pi - PI25));

    return 0;
}
