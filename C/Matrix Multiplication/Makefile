TARGET=test-mmul
OBJS=test-mmul.o matrix.o mmul.o unixtimer.o
CC=gcc
CFLAGS= -Wall -std=c99 -pthread

all : $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

clean: 
	@rm -f $(OBJS) $(TARGET)

mmul.o: $(@:.o=.c) matrix.h  

matrix.o: $(@:.o=.c) matrix.h  

test-mmul.o: $(@:.o=.c) matrix.h unixtimer.h

unixtimer.o: $(@:.o=.c) unixtimer.h
