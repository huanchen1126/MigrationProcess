			MigratableProcess Manager
	by Mengwei Ding (mengweid), Huanchen Zhang (huanchez)
	=====================================================

This system distributes jobs to slaves and does load balance when necessary.

Two MigratableProcesses are implemented for testing.
1) Grep
This class implements a similar function as unix command 'grep'. If one line of the input file contains the regex pattern, it will be written to output file.
This class takes three arguments: regex pattern, input file path, output file path.
2) WebpageCrawler
This class is a basic web crawler.
It takes two arguments: root url, output file path.
It gets all html files under the root url and writes them to the output file.
E.g. if root url is "www.cmu.edu", all pages linked from "www.cmu.edu" directly or indirectly with their url containing "cmu.edu" will be downloaded and written to output file.
For testing purpose, only 1000 pages will be downloaded.

How to test
By running Make file, a runnable jar MigratableProcessManager.jar will be created.
To start a master, use following command "java -jar MigratableProcessManager.jar $port".
To start a slave, use following command "java -jar MigratableProcessManager.jar $port -c $masterip:$masterport"
To submit a Grep job, use can type in following command in master command line "org.cmu.ds2013s.Grep $inputfile $outputfile".
To submit a WebpageCrawler job, use can type in following command in master command line "org.cmu.ds2013s.WebpageCrawler $url $outputfile".

To test the load balancing function, user can start one master, one slave, submit several jobs, then start some more slaves. User can type in 'ps' command in master command line, tosee the balance of jobs between slaves.
