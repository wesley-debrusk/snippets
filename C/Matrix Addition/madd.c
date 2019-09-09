#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <assert.h>
#include "matrix.h"

#define     NUM_THREADS     2

// Add any needed auxiliary data types and/or functions here

struct thread_data {
    int thread;
    TMatrix* left;
    TMatrix* right;
    TMatrix* result;
};

void* doAddition(void* threadarg) {
    struct thread_data* my_data = (struct thread_data*) threadarg;   
    int row = (my_data->left)->nrow;
    int col = (my_data->left)->ncol;
    int middle = row / 2;
    
    if (my_data->thread == 0) {
        for(int i = 0; i < middle; i++) {
            for (int j = 0; j < col; j++) {  
                (my_data->result->data)[i][j] = (my_data->left)->data[i][j] + (my_data->right)->data[i][j];
            }
        }        
    } else if (my_data->thread == 1) {
        for(int i = middle; i < row; i++) {
            for (int j = 0; j < col; j++) {            
                (my_data->result->data)[i][j] = (my_data->left)->data[i][j] + (my_data->right)->data[i][j];       
            }
        }
    }
    pthread_exit(NULL);
}

/* Return the sum of two matrices.
 * Return NULL if anything is wrong.
 *
 * Similar to addMatrix, but with multi-threading.
 */
TMatrix* addMatrix_thread(TMatrix *m, TMatrix *n) { 
   int t, rc;  
   pthread_t threads[NUM_THREADS];
   struct thread_data thread_data_array[NUM_THREADS];
   
   TMatrix* final = newMatrix(m->nrow, m->ncol);

   for (t = 0; t < NUM_THREADS; t++) {
      thread_data_array[t].thread = t;
      thread_data_array[t].left = m;
      thread_data_array[t].right = n;
      thread_data_array[t].result = final;

      printf("Creating thread #%d\n", t);
      rc = pthread_create(&threads[t], NULL, doAddition, (void*) &thread_data_array[t]);
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
