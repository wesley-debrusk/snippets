#include <stdlib.h>
#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[])
{
    int n, i;
    double PI25 = 3.141592653589793238462643;
    double my_pi, sum;

    if (argc != 2) {
        printf("Usage: %s n\n", argv[0] );
        exit(0);
    }
    n = atoi(argv[1]);

    sum = 0.0;

    for(i=0; i<=n; i++) {
        if(i%2==0) sum += 1.0/(2*i+1);
        else sum -= 1.0/(2*i+1);
    }

    my_pi = 4 * sum;
    printf("pi approximation: %.16f Error: %.16f\n", my_pi, fabs(my_pi - PI25));
    return 0;
}
