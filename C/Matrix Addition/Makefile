TARGET=test-madd
OBJS=test-madd.o matrix.o madd.o
CC=gcc
CFLAGS= -Wall -std=c99 -pthread

all : $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

clean: 
	@rm -f $(OBJS) $(TARGET)

%.o : %.c matrix.h
	$(CC) $(CFLAGS) -c $<
