# define more variables so it is easier to make changes
# again, study the new varabiles and rules
CC=gcc
CFLAGS=-g -Wall -std=c99
TARGETS=strrender generate-fontfile

all: $(TARGETS)

#Static Pattern Rules
$(TARGETS): % : %.c
	$(CC) $(CFLAGS) -o $@ $<

clean:
	rm -rf *.o *~ *.out $(TARGETS)
	@echo "If you do not need the font file any more, delete it manually."
