ABSTRACT:
This code demonstrates how to find servers on a network. In this demo one or more Arduino servers are 
running software (CommandServer - found in CommandServer Folder) that waits for a basic query command. 
Other commands can be added to the server, the goal here is to introduce a way to find a computer on a 
network. A Java client first determines the subnet range to search, then begins the search for servers 
that are "listening" and they connect to them. If the server is an arduino running the command server, 
then the MAC address is returned to the client.

THE APPLICATIONS:
There are two implementations one that runs on a MacOS and the other runs on WinOS. Both of these use
shell or command line services to find the host machine's IP and subnetmask. On MacOS (Linux) there are
two commands used: ipconfig and ifconfig. This is explained in the source code. On WinOS the ipconfig 
command is used to get both the IP and the subnetmask. These commands are issued to the OS from Java 
and the resulting out put is redirected to a bufferedstream which is parsed for the IP and subnetmask. 

Arduino Source Code: CommandServer.ino

Java Source Code: FindServerMacOS.java - MacOS implementation 
		  FindServerWinOS.java - Windows implemenation
		  TryConnect.java - used by both

NOTE and WARNING: 
DO NOT RUN THIS CODE ON THE CMU NETWORK. IF YOU USE THIS CODE OR ANYTHING LIKE THIS
CODE, CONFINE IT TO WITHIN YOUR PRIVATE ROUTERS. THIS IS A HARD AND FAST CONSTRAINT - NO ACCEPTIONS. 
THIS PROGRAM CAN BE MISINTERPRETED TO BE A DENIAL OF SERVICE ATTACK... IF YOU RUN THIS ON THE CMU 
NETWORK YOU WILL BE VISITED BY CMU'S IT SECURITY TEAM.

