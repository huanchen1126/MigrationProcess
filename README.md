			MigratableProcess Manager
	by Mengwei Ding (mengweid), Huanchen Zhang (huanchez)
	=====================================================

1. System description
======================

This system distributes jobs to slaves and does load balance when necessary. 
The user submit one job to master through master command line, and master 
distributes the job to the slave with lightest work load. The slaves send 
heart beat to master periodically with the list of current running processes
on the slave. When there is an unbalance work load between slaves, the master
 will send a migrate message to heavy loaded slaves, asking them to suspend 
one process and migrate the process to another slave. When the other slave 
receives the suspended process, it will restart it from the point the process
was suspended.

2. Test Instances
======================

Two MigratableProcesses are implemented for testing.

1) Grep
This class implements a similar function as unix command 'grep'. If one lin 
of the input file contains the regex pattern, it will be written to output file.

This class takes three arguments: regex pattern, input file path, output file path.

Example:
==> org.cmu.ds2013s.Grep political input.txt output.txt

2) WebpageCrawler
This class is a basic web crawler.

It takes two arguments: root url, output file path.

It gets all html files under the root url and writes them to the output file.

E.g. if root url is "www.cmu.edu", all pages linked from "www.cmu.edu" directly or 
indirectly with their url containing "cmu.edu" will be downloaded and written to output file.

For testing purpose, only 1000 pages will be downloaded.

Example:
==> org.cmu.ds2013s.WebpageCrawler www.cmu.edu output.txt

3. How to Run
======================

Before run our system, please compile our code first by typing:
	
	"make"

To start a master, use following command:

	"make master PORT=55555"

To start a slave, use following command:
	
	"make slave PORT=55556 MHOST=127.0.0.1 MPORT=55555"

To submit a Grep job, use can type in following command in master command line:

	"org.cmu.ds2013s.Grep $inputfile $outputfile".

To submit a WebpageCrawler job, use can type in following command in master command line:

	"org.cmu.ds2013s.WebpageCrawler $url $outputfile".

To test the load balancing function, user can start one master, one slave, submit several jobs, 
then start some more slaves. User can type in 'ps' command in master command line, to see the 
balance of jobs between slaves.
