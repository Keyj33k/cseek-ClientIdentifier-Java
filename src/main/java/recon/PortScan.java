package recon;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

abstract class PortScan {
	public String ipAddress;
	private static String possibleService = null;
	private static int openPorts = 0;
	
	public PortScan(String ipAddress) {
	 	this.ipAddress = ipAddress;
	}
	
	private static String possibleServices(int port) {
		if(port == 20 || port == 21) {
			possibleService = " |\t\t * possible service: FTP";
		} else if(port == 22) {
			possibleService = " |\t\t * possible service: SSH";
		} else if(port == 23) {
			possibleService = " |\t\t * possible service: Telnet";
		} else if(port == 25 || port == 456 || port == 587) {
			possibleService = " |\t\t * possible service: SMTP";
		} else if(port == 69) {
			possibleService = " |\t\t * possible service: TFTP";
		} else if(port == 137 || port == 139 || port == 445) {
			possibleService = " |\t\t * possible service: SMB";
		} else if(port == 80 || port == 443 || port == 8080) {
			possibleService = " |\t\t * possible service: HTTP/HTTPS";
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
		
		for(int port = minPort; port <= maxPort; port++) {
			BufferedWriter writer = new BufferedWriter(new FileWriter("output/cseekResults.txt", true));
			String targetAddress = String.format("%s.%d", this.ipAddress, lastOctet);
			Socket socket = new Socket();
			
			try {
				socket.connect(new InetSocketAddress(targetAddress, port));	
				openPorts++;
				
				System.out.printf(" |\t + port: %d, status: open\n", port);
				if(possibleServices(port) != null) System.out.println(possibleServices(port));
				
				writer.write(String.format(" |\t + port: %d, status: open\n", port));
				if(possibleServices(port) != null) writer.write(possibleServices(port) + "\n");
				
				possibleService = null; // reset to avoid invalid notification spamming
			} catch(ConnectException exc) {
			} catch(SocketException exc) {
				exc.getMessage();
			} finally {
				socket.close();
				writer.close();
			}
		}
		
		int closedPorts = portsToScan - openPorts;
		
		if(portsToScan == closedPorts) {
			System.out.printf(" |\n |  port scan done:\n |\t %d ports scanned: all scanned ports are closed\n", portsToScan);
		} else if(openPorts == 1) {
			System.out.printf(
				" |\n |  port scan done:\n |\t %d ports scanned: %d is open, %d ports closed\n", 
				portsToScan, openPorts, closedPorts
			);
		} else {
			System.out.printf(
				" |\n |  port scan done:\n |\t %d ports scanned: %d are open, %d ports closed\n", 
				portsToScan, openPorts, closedPorts
			);
		}
		
		openPorts = 0; // reset to avoid invalid result calculations
	}
}


