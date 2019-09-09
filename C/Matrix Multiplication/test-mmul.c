#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include    "matrix.h"
#include    "unixtimer.h"

/************************************************************/
/* Do not change the code below                             */
/************************************************************/

#define     DEFAULT_NROW    6
#define     DEFAULT_NCOL    6

int main(int argc, char ** argv)
{
    int     nrow = DEFAULT_NROW;
    int     ncol = DEFAULT_NCOL;
    int     ncol2 = DEFAULT_NCOL;

    if (argc >= 4) {
        nrow = atoi(argv[1]);
        ncol = atoi(argv[2]);
        ncol2 = atoi(argv[3]);
    }
    else {
        fprintf(stderr, "Usage: %s nrows ncols ncols_2\n", argv[0]);
        fprintf(stderr, "Currently using the default values for the number of rows and columns.\n");
    }

    assert(nrow > 0 && ncol > 0);

    TMatrix *m, *n;

    m = newMatrix(nrow, ncol);
    assert( m != NULL);
    n = newMatrix(ncol, ncol2);
    assert( n != NULL);

    fillMatrix(m);
    fillMatrix(n);

    start_timer();
    TMatrix * t1 = mulMatrix(m, n);
    double time1 = cpu_seconds();
    assert(t1 != NULL);

    start_timer();
    TMatrix * t2 = mulMatrix_thread(m, n);
    double time2 = cpu_seconds();
    assert(t2 != NULL);

    int     r = compareMatrix(t1, t2);

    freeMatrix(m);
    freeMatrix(n);
    freeMatrix(t1);
    freeMatrix(t2);

    printf("%d %s\n", r, r ? "Do not match." : "Good work!");
    printf("Time 1: %f\n", time1);
    printf("Time 2: %f\n", time2);
    return 0;
}
