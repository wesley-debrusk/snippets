#include    <stdio.h>
#include    <stdlib.h>

#define		MAX_DIMENSION	65535
#define		MAX_SIZE	(1 << 22)

int main(int argc, char **argv)
{
    char * filename = "matrix.out";
    unsigned int m, n;

    if (argc == 3) {
        m = atol(argv[1]);
        n = atol(argv[2]);
    }
    else {
        printf("Usage: %s Num-of-rows Num-of-columns\n", argv[0]);
        return -1;
    }

    if (m < 1 || n < 1 || m > MAX_DIMENSION || n > MAX_DIMENSION || m * n > MAX_SIZE) {
        printf("Both integers must be >= 1 and <= %d and their product must be <= %d.\n", 
                MAX_DIMENSION, MAX_SIZE);
        return -2;
    }

    FILE *fp;

    fp = fopen(filename, "wb");
    if (fp == NULL) {
        fprintf(stderr, "Cannot open file %s for write: ", filename);
        perror("");
        return 2;
    }

    fwrite(&m, sizeof(m), 1, fp);
    fwrite(&n, sizeof(n), 1, fp);

    srand(3100);
    unsigned int i, j;
    int buf[n]; // buffer for each row
    for (i = 0; i < m; i ++) {
        for (j = 0; j < n; j ++)
            buf[j] = rand();
        fwrite(buf, sizeof(buf[0]), n, fp);
    }
    fclose(fp);
    return 0;
}

