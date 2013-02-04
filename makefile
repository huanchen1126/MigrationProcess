all:
	cd src; make

master:
	java -cp bin/:lib/commons-logging-1.1.1.jar:lib/commons-logging-1.1.1-javadoc.jar:lib/commons-logging-1.1.1-sources.jar:lib/commons-logging-adapters-1.1.1.jar:lib/commons-logging-api-1.1.1.jar:lib/commons-logging-tests.jar org/cmu/ds2013s/ProcessManager $(PORT)

slave:
	java -cp bin/:lib/commons-logging-1.1.1.jar:lib/commons-logging-1.1.1-javadoc.jar:lib/commons-logging-1.1.1-sources.jar:lib/commons-logging-adapters-1.1.1.jar:lib/commons-logging-api-1.1.1.jar:lib/commons-logging-tests.jar org/cmu/ds2013s/ProcessManager $(PORT) -c $(MHOST):$(MPORT)


clean:
	cd src; make clean

