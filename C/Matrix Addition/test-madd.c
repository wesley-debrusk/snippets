#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include    "matrix.h"

/************************************************************/
/* Do not change the code below                             */
/************************************************************/

#define     DEFAULT_NROW    6
#define     DEFAULT_NCOL    6

int main(int argc, char ** argv)
{
    int     nrow = DEFAULT_NROW;
    int     ncol = DEFAULT_NCOL;

    if (argc >= 3) {
        nrow = atoi(argv[1]);
        ncol = atoi(argv[2]);
    }
    else {
        fprintf(stderr, "Usage: %s nrows ncols\n", argv[0]);
        fprintf(stderr, "Currently using the default values for the number of rows and columns.\n");
    }

    assert(nrow > 0 && ncol > 0);

    TMatrix *m, *n;

    m = newMatrix(nrow, ncol);
    assert( m != NULL);
    n = newMatrix(nrow, ncol);
    assert( n != NULL);

    fillMatrix(m);
    fillMatrix(n);

    TMatrix * t1 = addMatrix(m, n);
    assert(t1 != NULL);
    TMatrix * t2 = addMatrix_thread(m, n);
    assert(t2 != NULL);

    int     r = compareMatrix(t1, t2);

    freeMatrix(m);
    freeMatrix(n);
    freeMatrix(t1);
    freeMatrix(t2);

    printf("%d %s\n", r, r ? "Do not match." : "Good work!");
    return 0;
}
