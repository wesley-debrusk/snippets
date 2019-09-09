#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#define     NUM_CHAR_PER_LINE       8
#define     FONT_WIDTH              8
#define     FONT_HEIGHT             8
#define     FONT_NBYTES_PER_ROW     1
#define     FONT_NBYTES_PER_CHAR    (FONT_NBYTES_PER_ROW * FONT_HEIGHT)
#define     PIXEL_FONT              '*'
#define     PIXEL_BACKGROUND        ' '


// Convert character to reverse binary 
char* to_binary(char c, char* str) {
    //str = malloc(8 * sizeof(char) + 1);
    for (int i = 7; i >= 0; --i)
    {
        if (c & (1 << i)) {
            str[i] = 1;
        } else {
            str[i] = 0;
        }
    }
    return str;    
}

/* buf is the display buffer.
 * s is the string to be displayed on this 'line'.
 * fp points to the opened font file.
 *
 * The function retrieves font information for up to NUM_CHAR_PER_LINE characters
 * in s from fp, and updates the display buffer (buf). 
 *
 * The function returns the number of bytes that have been processed. 
 * Should be a number between 0 and NUM_CHAR_PER_LINE.
 * */
unsigned int str_render(char **buf, char *s, FILE *fp)
{
    unsigned int n = strlen(s);
    
    for (int z = 0; z < n; z++) {
    
        char current = s[z];  
        int ascii = (int)current;     
        fseek(fp, (ascii * 8), SEEK_SET);
        
        for (int i = 0; i < 8; i++) {
            char c = fgetc(fp);
            char* bin = malloc(8 * sizeof(char) + 1);
            bin = to_binary(c, bin);
            for (int j = 0; j < 8; j++) {
                if (bin[j] == 1) {
                    buf[i][j + (z*8)] = PIXEL_FONT;
                }
            }
            free(bin);
        }  
    }
    return n;
}


/* Clear the display buffer. 
 * If print_buffer() is called right after, only PIXEL_BACKGROUND will be displayed.
 * Try to type 'clear' in your bash.
 * */
void clear_buffer(char **buf)
{
    for (int i = 0; i < FONT_HEIGHT; i++) {
        for (int j = 0; j < (FONT_WIDTH * NUM_CHAR_PER_LINE); j++) {
            buf[i][j] = PIXEL_BACKGROUND;
        }
    }    
}

/* shown the display buffer on the screen. */
void print_buffer(char ** buf) 
{    
    for (int i = 0; i < FONT_HEIGHT; i++) {
        for (int j = 0; j < (FONT_WIDTH * NUM_CHAR_PER_LINE); j++) {
            printf("%c", buf[i][j]);   
        }
        printf("\n");
    }    
}


int main(int argc, char **argv)
{

    char * font_filename = "font8x8.dat";

    if (argc != 2) {
	      fprintf(stderr, "Usage: %s <a string>\n", argv[0]);
	      return 1;
    }

    // open the font file
    FILE *fp;
    fp = fopen(font_filename, "r");
    if (fp == NULL) {
	      fprintf(stderr, "Cannot open font file %s :", font_filename);
        perror("");
        fprintf(stderr, "Run ./generate-fontfile to create it. Do not add it to your repo.\n"); 
	      return 2;
    }

    /* There are many ways to do it. 
     * 
     * One strategy can be:
     *
     *      1. Allocate a 'display' buffer, a 2-D array that has FONT_HEIGHT rows.
     *      2. Clear buffer.
     *      3. Call str_render() to render NUM_CHAR_PER_LINE characters into the display buffer.
     *      4. Print the buffer.
     *      5. If there are more characters to display, goto 2.
     *      6. Free buffer.
     * 
     * There are several ways to use buffer. Be consistent in your implementation. 
     */

    char **buffer = (char**)malloc(FONT_HEIGHT * sizeof(char*));
    for (int i = 0; i < FONT_HEIGHT; i++) {
        buffer[i] = (char*)malloc((FONT_HEIGHT * NUM_CHAR_PER_LINE) * sizeof(char));
    }
      
    int pointer = 0;
    
    if (strlen(argv[1]) <= NUM_CHAR_PER_LINE) {
        printf("\n"); // Just for a bit of additional space
        clear_buffer(buffer);
        str_render(buffer, argv[1], fp);
        print_buffer(buffer);
    } else {
    
        while (pointer < strlen(argv[1])){
            char* temp = malloc(NUM_CHAR_PER_LINE * sizeof(char));
            for (int j = 0; j < NUM_CHAR_PER_LINE; j++) {
                temp[j] = argv[1][pointer];
                pointer++;
            }
            printf("\n"); // Just for a bit of additional space
            clear_buffer(buffer);
            str_render(buffer, temp, fp);
            print_buffer(buffer);
            free(temp);
            
        }
    }
    
    
    for (int i = 0; i < FONT_HEIGHT; i++) {
        free(buffer[i]);
    }
    free(buffer);
    fclose(fp);
    return 0;
}
