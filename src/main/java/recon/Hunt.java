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
	private static int portStatusCount = 0;
	
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
	
	public void scanPorts(int minPort, int maxPort, int lastOctet) throws IOException {
		int hostsToScan = maxPort - minPort + 1;
		
		for(int port = minPort; port < maxPort + 1; port++) {
			BufferedWriter writer = new BufferedWriter(new FileWriter("output/nethuntResults.txt", true));
			Socket socket = new Socket();
			
			try {
				socket.connect(new InetSocketAddress(this.ipAddress, port));
					
				System.out.printf("\t%s.%d, port %d, status: open\n", this.ipAddress, lastOctet, port);
				possibleServices(port);
					
				writer.write(String.format("\t%s.%d, port %d, status: open\n", this.ipAddress, lastOctet, port));
				writer.write(possibleServices(port) + "\n");
			} catch(ConnectException exc) {
				portStatusCount += 1;
			} catch(SocketException exc) {
				portStatusCount += 1;
			} finally {
				socket.close();
				writer.close();
			}
		}
		
		if(hostsToScan == portStatusCount) {
			System.out.printf("\tport scan done => %d ports scanned, all scanned ports are closed\n", hostsToScan);
		} else {
			System.out.printf("\tport scan done => %d ports scanned, %d ports are closed\n", hostsToScan, portStatusCount);
		}
	}
}

