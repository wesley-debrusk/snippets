#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#define     TElement    int

typedef struct Matrix {
    unsigned int nrow;
    unsigned int ncol;
    TElement **data;
} TMatrix;


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
TMatrix * newMatrix(unsigned int row, unsigned int col) 
{
    TMatrix *mp = malloc(1 * sizeof(TMatrix));
    mp->nrow = row;
    mp->ncol = col;
    
    int **matrix = (int**)malloc(sizeof(int*) * row);
    for (int i = 0; i < row; i++) {
        matrix[i] = (int*)malloc(sizeof(int) * col);
    }
    
    mp->data = matrix;
    return mp;
}

/*
 * This function is responsible for deallocating the dynamic memory
 * currently used by a matrix. Check the newMatrix() function to see
 * what were allocated.
 */
void freeMatrix(TMatrix * m) 
{
    for (int i = 0; i < m->nrow; i++) {
        
        free(m->data[i]);
        
    }
    free(m->data);
    free(m);
}

/* This function takes as input a filename, and read a matrix from the file.
 * The dimensions of the matrix and data are in the file. See the source code of 
 * the program that generates the matrix file for details.
 * Failure of file operatoins or memory allocations is a fatal error.
 * The function returns a matrix.
 */
TMatrix * readMatrix(char *filename)
{
    FILE *fp;
    fp = fopen(filename, "r");
    if (fp == NULL) {
	      fprintf(stderr, "Cannot open font file %s :", filename);
        perror("");
        fprintf(stderr, "Run ./mf-generate to create it.\n"); 
	      return NULL;
    }
    
    unsigned int m, n;
    
    fread(&m, sizeof(m), 1, fp);
    fread(&n, sizeof(n), 1, fp);
    
    TMatrix *mp = newMatrix(m, n);
    
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            unsigned int value;
            fread(&value, sizeof(unsigned int), 1, fp);
            mp->data[i][j] = value;
        }
    }
    fclose(fp);
    return mp;
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

    TMatrix *new = newMatrix(m->ncol, m->nrow);
    
    for (int i = 0; i < m->nrow; i++) {
        for (int j = 0; j < m->ncol; j++) {
            
            new->data[j][i] = m->data[i][j];
            
        }
    }
    
    return new;
}

/* Write the matrix to a file named filename.
 * If m is NULL, do nothing.
 * Failure on file operations is fatal.
 */ 
void writeMatrix(TMatrix *m, char *filename)
{
    if (m == NULL)
        return;

    FILE *fp;

    fp = fopen(filename, "wb");
    if (fp == NULL) {
        fprintf(stderr, "Cannot open file %s for write: ", filename);
        perror("");
        return NULL;
    }
    
    fwrite(&(m->nrow), sizeof(m->nrow), 1, fp);
    fwrite(&(m->ncol), sizeof(m->ncol), 1, fp);
    
    unsigned int i, j;
    int buf[m->ncol]; // buffer for each row
    for (i = 0; i < m->nrow; i ++) {
        for (j = 0; j < m->ncol; j ++)
        buf[j] = m->data[i][j];
        fwrite(buf, sizeof(buf[0]), m->ncol, fp);
    }
    fclose(fp);
    
    
} 

/************************************************************/
/* Do not change the code below                             */
/************************************************************/

/*
 * May be helpful if you want to see small matrices on screen.
 * Remember to undo the changes.
 *
 * The printMatrix function takes a matrix as input and produces, on the
 * standard output, a representation of the matrix in row-major format. For
 * instance, the 3x3 identity matrix should print on 3 lines as:
 * 1 0 0 
 * 0 1 0
 * 0 0 1
 */
void printMatrix(TMatrix * m) 
{
    if (m == NULL || m->data == NULL)
        return;
    for (unsigned int i = 0; i < m->nrow; i++) {
        for (unsigned int j = 0; j < m->ncol; j++) 
            printf("%10d", m->data[i][j]);
        printf("\n");
    }
}

#define     FN_LEN      128

int main(int argc, char ** argv) 
{
    char * filename = "matrix.out";
    char output_filename[FN_LEN];

    // Use the file specified if it is present
    if (argc >= 2) {
	      filename = argv[1];
    }

    // Generate output file name 
    // Using 10 here is actually not a good practice
    if (strlen(filename) > FN_LEN - 10) {
        fprintf(stderr, "File name is too long:%s\n", filename);
        return 1;
    }

    strcpy(output_filename, filename);
    strcat(output_filename, ".out");

    TMatrix *a = readMatrix(filename);
    assert(a != NULL);
    

    TMatrix *t = transposeMatrix(a);

    writeMatrix(t, output_filename);

    freeMatrix(a);
    freeMatrix(t);

    return 0;
}
