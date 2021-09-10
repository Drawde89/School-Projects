MAIN_FLAGS    = -ldl -O2
PIC_FLAGS     = -fPIC -c -O2
SHARED_FLAGS  = -ldl -shared -lc

all: nlcnt reading.so mapping.so

nlcnt: nlcnt.c
	gcc ${MAIN_FLAGS} nlcnt.c -o nlcnt 

reading.so: reading.c
	gcc ${PIC_FLAGS} reading.c -o reading.o
	ld ${SHARED_FLAGS} reading.o -o reading.so

mapping.so: mapping.c
	gcc ${PIC_FLAGS} mapping.c -o mapping.o
	ld ${SHARED_FLAGS} mapping.o -o mapping.so

clean:
	rm *.so *.o nlcnt

tar:
	tar cf nlcnt.tar nlcnt.c reading.c mapping.c Makefile

touch:
	touch *.c
