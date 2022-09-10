package recon;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

abstract class Hunt {
	public String ipAddress;
	private static String possibleService;
	private static int portStatusCount;
	
	public Hunt(String ipAddress) {
	 	this.ipAddress = ipAddress;
	}
	
	private static String possibleServices(int port) {
		if(port == 20 || port == 21) {
			possibleService = String.format("\t\tport %d, possible FTP service detected!", port);
		} else if(port == 22) {
			possibleService = "\t\tport 22, possible SSH service detected!";
		} else if(port == 23) {
			possibleService = "\t\tport 23, possible Telnet service detected!";
		} else if(port == 25 || port == 456 || port == 587) {
			possibleService = String.format("\t\tport %d, possible SMTP service detected!", port);
		} else if(port == 69) {
			possibleService = "\t\tport 69, possible TFTP service detected!";
		} else if(port == 137 || port == 139 || port == 445) {
			possibleService = String.format("\t\tport %d, possible SMB service detected!", port);
		} else if(port == 80 || port == 443 || port == 8080) {
			possibleService = String.format("\t\tport %d, possible HTTP/HTTPS service detected!", port);
		}
		
		return possibleService;
	}
	
	/**
	 * 
	 * scanPorts scans (if enabled) the current target address for 
	 * open ports using the socket class and provides information
	 * when a specific, most vulnerable port accepts the request 
	 * (using the possibleServices method). The results will 
	 * be saved in the output file.
	 * 
	 * @param minPort		defines where the port scanner need to start
	 * @param maxPort		defines the maximum port where the scan will end 
	 * @param lastOctet		extend the 24 bit value to a valid ipv4 address 
	 * @throws IOException		throws when processes like saving the output 
	 * 				or the socket creation fails
	 * 
	 */
	public void scanPorts(int minPort, int maxPort, int lastOctet) throws IOException {
		int portsToScan = maxPort - minPort;
		
		for(int port = minPort; port < maxPort; port++) {
			BufferedWriter writer = new BufferedWriter(new FileWriter("output/nethuntResults.txt", true));
			String targetAddress = String.format("%s.%d", this.ipAddress, lastOctet);
			Socket socket = new Socket();
			
			try {
				socket.connect(new InetSocketAddress(targetAddress, port));	
				System.out.printf("\t%s, port %d, status: open\n", targetAddress, port);
				possibleServices(port);	
				writer.write(String.format("\t%s, port %d, status: open\n", targetAddress, port));
				
				if(possibleServices(port) != null) writer.write(possibleServices(port) + "\n");
			} catch(ConnectException exc) {
				portStatusCount++;
			} catch(SocketException exc) {
				portStatusCount++;
			} finally {
				socket.close();
				writer.close();
			}
		}
		
		if(portsToScan == portStatusCount) {
			System.out.printf("\tport scan done => %d ports scanned, all scanned ports are closed\n", portsToScan);
		} else {
			System.out.printf("\tport scan done => %d ports scanned, %d ports are closed\n", portsToScan, portStatusCount);
		}
	}
}



