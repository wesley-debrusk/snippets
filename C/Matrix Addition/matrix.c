#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <math.h>

#include "matrix.h"

// For the purpose of this assignment
#define     EPSILON     0.0000001

/*
 * Creates and returns a matrix of size rows x cols
 * - rows : (non-negative value) giving the number of rows
 * - cols : (non-negative value) giving the number of columns
 * If the allocation is not successful, the function should return NULL
 * If the allocation is successful, the data field of the matrix should
 * point to an array of pointers (representing the rows) and each pointer
 * in that array should point to an array of TElement representing the values
 * in that row.
 */
TMatrix * newMatrix(unsigned int nrow, unsigned int ncol)
{
    if (! nrow || ! ncol)
        return NULL;

    /* Allocate space for TMatrix */
    TMatrix * newM = malloc(sizeof(TMatrix));

    /* Allocate space for a rows X cols matrix
       allocate pointers to row arrays      */
    TElement ** data = malloc(nrow * sizeof(TElement *));

    /* allocate space for all elements in one call */
    TElement * storage = malloc (nrow*ncol * sizeof(TElement));

    if (newM == NULL || data == NULL || storage == NULL) {
        free(newM);
        free(data);
        free(storage);
        return NULL;
    }

    /* set row array pointers */
    for (size_t i = 0; i < nrow; i++)
            data[i] = storage + i * ncol;
    newM->nrow = nrow;
    newM->ncol = ncol;
    newM->data = data;
    return newM;
}

/*
 * This function is responsible for deallocating the dynamic memory
 * currently used by a matrix. Check the newMatrix() function to see
 * what were allocated.
 */
void freeMatrix(TMatrix * m)
{
    if (m == NULL)          /* remember to check in every function */
        return;
    if (m->data)  {
        free(m->data[0]);   /* free the storage allocated for data */
        free(m->data);      /* free the array of pointers to row pointers */
    }
    free(m);                /* free the matrix itself */
}

TMatrix * fillMatrix(TMatrix * m)
{
    static int first=1;

    if (m == NULL || m->data == NULL)
        return m;

    // again, for the purpose of this assignment.
    if (first) {
        srand(3100);
        first = 0;
    }

    unsigned int i, j;
    for (i = 0; i < m->nrow; i ++)
        for (j = 0; j < m->ncol; j ++)
            m->data[i][j] = (TElement) rand();
    return m;
}

/*
 * The transposeMatrix function takes as input a matrix m and returns a
 * new matrix that holds the transpose of m. Transposition should run in
 * O(nrow x ncol)  (where n is the # of rows and m the # of columns).
 * If memory allocation for the transpose failed or input is NULL,
 * the function returns NULL.
 * Transposition follows the usual mathematical definition of transposition.
 */
TMatrix * transposeMatrix(TMatrix * m)
{
    if (m == NULL)
        return NULL;
    TMatrix * t = newMatrix(m->ncol, m->nrow);
    if (t == NULL)
        return t;
    for (unsigned int i = 0; i < m->nrow; i++)
        for (unsigned int j = 0; j < m->ncol; j++)
            t->data[j][i] = m->data[i][j];
    return t;
}

void printMatrix(TMatrix * m)
{
    if (m == NULL)
        return;
    for (unsigned int i = 0; i < m->nrow; i++) {
        for (unsigned int j = 0; j < m->ncol; j++)
            printf("%12g", m->data[i][j]);
        printf("\n");
    }
}

/* compare two matrices.
 *
 * return 0 if they are the same.
 * return non-zero values otherwise.
 */
int compareMatrix(TMatrix * m, TMatrix *n)
{
    if (m == NULL || n == NULL)
        return -1;
    if (m->nrow != n->nrow || m->ncol != n->ncol)
        return -2;

    unsigned i, j;
    for (i = 0; i < m->nrow; i ++)
        for (j = 0; j < m->ncol; j ++)
            if (fabs(m->data[i][j] - n->data[i][j]) > EPSILON) {
                fprintf(stderr, "element[%u][%u]  %f  %f.\n", i, j, m->data[i][j], n->data[i][j]);
                return 1;
            }
    return 0;
}

/* return the sum of two matrices.
 *
 * Return NULL if anything is wrong.
 */
TMatrix * addMatrix(TMatrix *m, TMatrix *n)
{
    if (    m == NULL || n == NULL
         || m->nrow != n->nrow || m->ncol != n->ncol )
        return NULL;

    TMatrix * t = newMatrix(m->nrow, m->ncol);
    if (t == NULL)
        return t;
    for (unsigned int i = 0; i < m->nrow; i++)
        for (unsigned int j = 0; j < m->ncol; j++)
            t->data[i][j] = m->data[i][j] + n->data[i][j];
    return t;
}


/* return the product of two matrices.
 *
 * Return NULL if anything is wrong.
 */
TMatrix * mulMatrix(TMatrix *m, TMatrix *n)
{
    if (     m == NULL || n == NULL
          || m->ncol != n->nrow   )
        return NULL;

    TMatrix * t = newMatrix(m->nrow, n->ncol);
    if (t == NULL)
        return t;
    for (unsigned int i = 0; i < m->nrow; i++)  {
        for (unsigned int j = 0; j < n->ncol; j++) {
            TElement sum = (TElement)0;
            for (unsigned int k = 0; k < m->ncol; k++)
                sum += m->data[i][k] * n->data[k][j];
            t->data[i][j] = sum;
        }
    }
    return t;
}
