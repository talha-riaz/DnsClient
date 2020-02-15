# DnsClient

<img src="https://github.com/talha-riaz/DnsClient/blob/master/img/img1.png" height="250" width="600"/>

An implementation of a DNS Client in Java using sockets.

## How to use
1. Clone the repository
2. Navigate to src directory using the Terminal
3. Run the following command to compile the program:
`javac *.java`
4. Use the following command to run the client:
`java DnsClient [-t timeout] [-r max-retries] [-p port] [-mx|-ns] @server name`

Example:
`java DnsClient -t 3 -r 4 @8.8.8.8 www.github.com`

Optional tags:
`-t` (timeout): gives how long to wait (in seconds) before retransmitting an unanswered query. Default value: 5
`-r` (max-retries): the maximum number of times to retransmit an unanswered query before giving up. Default value: 3
`-p` (port): is the UDP port number of the DNS server. Default value: 53
`-mx` or `-ns` (MX: mail server OR NS: name server): indicate whether to send a MX or NS query. At most one of these can be given, and if neither is given then the client sends a type A (IP address) query by default.

Required tags:
`server`: is the IPv4 address of the DNS server, in A.B.C.D format (example 168.260.85.18)
`name`: is the domain to query for (example www.github.com)
