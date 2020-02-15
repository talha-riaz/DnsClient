import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.*;


//added
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class DnsController {
	
	private int timeout = 5000; //deafault 5
	private int maxRetries = 3;// default 3
	private int port = 53; //default 53
	private String queryType = "A"; //default type A
	private byte[] server = new byte[4];
	String address;
	private String domainName;
	private DatagramSocket socket;
	private InetAddress inetAddr;
	private DnsResponse response;
	
	public DnsController(String args[]) throws Exception {
		try {
			parseArguments(args);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		if(server == null || domainName == null) {
			throw new Exception("Inavlid Usage/Inputs");
		}
	}
	
	private void parseArguments(String[] args) throws Exception {
		List<String> argList = new ArrayList<String>();
		argList = Arrays.asList(args);
		Iterator<String> argsIterator = argList.iterator();
		
		while(argsIterator.hasNext()) {
			String arg = argsIterator.next();
			if(arg.equals("-t")) {
				this.timeout = Integer.parseInt(argsIterator.next()) * 1000;;
			}else if(arg.equals("-r")) {
				this.maxRetries = Integer.parseInt(argsIterator.next());
			}else if(arg.equals("-p")) {
				this.port = Integer.parseInt(argsIterator.next());
			}else if(arg.equals("-mx") || arg.equals("-ns")) {
				this.queryType = arg;
			}else {
				address = arg.substring(1);
				String ipAddress[] = address.split("\\.");
				for(int i = 0 ; i < ipAddress.length; i++) {
					int address = Integer.parseInt(ipAddress[i]);
					if(address < 0 || address > 255) {
						throw new Exception("Error\tInvalid server address, each byte has to between 0 and 255");
					}
					server[i] = (byte) address;
				}
				this.domainName = argsIterator.next();
			}
		}
	}
	public void request(){
		System.out.println("DnsClient sending request for " + domainName);
        System.out.println("Server: " + address);
        System.out.println("Request type: " + queryType + "\n");
        makeRequest(0);
	}
	
	private void makeRequest(int trialNumber) {
		if(trialNumber > maxRetries) {
			throw new RuntimeException("ERROR\tMaximum number of retries " + maxRetries+ " exceeded");
		}
		try {	
			socket = new DatagramSocket();
			socket.setSoTimeout(this.timeout);
			inetAddr = InetAddress.getByAddress(server);
		} catch (SocketException e) {
			System.out.println("ERROR\tCould not create socket");
			return;
		} catch (UnknownHostException e) {
			System.out.println("ERROR\tUnknown host");
			return;
		}
		
		//create packet
		DnsPacket dnsQuery = new DnsPacket(this.domainName, this.queryType);
		byte[] dnsReq = dnsQuery.createRequestPacket();
		DatagramPacket sendPacket = new DatagramPacket(dnsReq, dnsReq.length, inetAddr, this.port);
		
		//buffer used for datagram packet received
		byte[] buf = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		
		
	
		try {
			long sentTime = System.currentTimeMillis();
			socket.send(sendPacket);
			socket.receive(receivePacket);
			
			long receiveTime = System.currentTimeMillis();
			
			double totalTime = (receiveTime-sentTime)/1000.0;
			
		
			response = new DnsResponse(buf, sendPacket.getLength());
			
			if(response.getRcode() != 0) {
				makeRequest(++trialNumber);
			}
			
			response.parseResponse();
			System.out.println("Responce received after time: " + totalTime + " seconds ("+ trialNumber + " retries)" );
	        response.DisplayResponse();
		} catch (SocketTimeoutException e) {	
			System.out.println("ERROR\tSocket Timeout");
			if(trialNumber < maxRetries) {
				System.out.println("Retrying. . .");
			}
			makeRequest(++trialNumber);
		} catch (IOException e) {
			System.out.println("ERROR\t"+e.getMessage());
		}
	
		
	}
}
