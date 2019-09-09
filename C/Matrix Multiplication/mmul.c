#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <assert.h>
#include "matrix.h"

#define     NUM_THREADS     2

// Data to be passed to thread
struct thread_data {
    int thread;
    TMatrix* left;
    TMatrix* right;
    TMatrix* result;
};

void* doMultiplication(void* threadarg) {
    struct thread_data* my_data = (struct thread_data*) threadarg;   
    int lrow = (my_data->left)->nrow;
    int lcol = (my_data->left)->ncol;
    int rcol = (my_data->right)->ncol;
    int middle = lrow / 2;
    
    if (my_data->thread == 0) {
        for (unsigned int i = 0; i < middle; i++)  {
            for (unsigned int j = 0; j < rcol; j++) {
                TElement sum = (TElement)0;
                for (unsigned int k = 0; k < lcol; k++) {
                    sum += my_data->left->data[i][k] * my_data->right->data[k][j];
                }
                my_data->result->data[i][j] = sum;
            }
        }
        return my_data;
                   
    } else if (my_data->thread == 1) {
          for (unsigned int i = middle; i < lrow; i++)  {
            for (unsigned int j = 0; j < rcol; j++) {
                TElement sum = (TElement)0;
                for (unsigned int k = 0; k < lcol; k++) {
                    sum += my_data->left->data[i][k] * my_data->right->data[k][j];
                }
                my_data->result->data[i][j] = sum;
            }
        }
        return my_data; 
    }
    pthread_exit(NULL);
}

/* Return the product of two matrices.
 * Return NULL if anything is wrong.
 *
 * Similar to mulMatrix, but with multi-threading.
 */
TMatrix * mulMatrix_thread(TMatrix *m, TMatrix *n)
{
    int t, rc;  
    pthread_t threads[NUM_THREADS];
    struct thread_data thread_data_array[NUM_THREADS];
    
    TMatrix* final = newMatrix(m->nrow, n->ncol);
 
    for (t = 0; t < NUM_THREADS; t++) {
       thread_data_array[t].thread = t;
       thread_data_array[t].left = m;
       thread_data_array[t].right = n;
       thread_data_array[t].result = final;
 
       printf("Creating thread #%d\n", t);
       rc = pthread_create(&threads[t], NULL, doMultiplication, (void*) &thread_data_array[t]);
       if (rc) {
          printf("ERROR; return code from pthread_create() is %d\n", rc);
          return NULL;
       }
    }
    
    for( t=0; t<NUM_THREADS; t++ ) {
 
       printf("Joining thread #%d\n", t);
       rc = pthread_join( threads[t], NULL );
       if( rc ){
          printf("ERROR; return code from pthread_join() is %d\n", rc);
          exit(-1);
       }
    }
    return final;
}
