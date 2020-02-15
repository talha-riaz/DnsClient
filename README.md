# DNS Client

<img src="https://github.com/talha-riaz/DnsClient/blob/master/img/img1.png" height="250" width="650"/>

An implementation of a DNS Client in Java using sockets.

## How to use
1. Clone the repository
2. Navigate to `src` directory using the Terminal
3. Run the following command to compile the program: <br/>
`javac *.java`
4. Use the following command to run the client: <br/>
`java DnsClient [-t timeout] [-r max-retries] [-p port] [-mx|-ns] @server name`

Example: <br/>
`java DnsClient -t 3 -r 4 @8.8.8.8 www.github.com`

Required tags: <br/>
`server`: is the IPv4 address of the DNS server, in A.B.C.D format (example 168.200.85.18) <br/>
`name`: is the domain to query for (example www.github.com) <br/>
<br/>

Optional tags: <br/>
`-t` (timeout): gives how long to wait (in seconds) before retransmitting an unanswered query. Default value: 5 <br/>
`-r` (max-retries): the maximum number of times to retransmit an unanswered query before giving up. Default value: 3 <br/>
`-p` (port): is the UDP port number of the DNS server. Default value: 53 <br/>
`-mx` or `-ns` (MX: mail server OR NS: name server): indicate whether to send a MX or NS query. At most one of these can be given, and if neither is given then the client sends a type A (IP address) query by default. 

## Background
Broadly, a Domain Name System (DNS) Client is any computer that issues DNS queries to a Domain Name System (DNS) Server. <br/>
This implementation of the DNS Client can issue requests to the DNS Server that transmit in the form of <a href="https://en.wikipedia.org/wiki/Network_packet">packets</a> through the <a href="https://en.wikipedia.org/wiki/Network_socket">socket</a>. The requests that can be issued have been documented above. 

