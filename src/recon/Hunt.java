package recon;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

public class Hunt {
	protected String ipAddress;
	private static String possibleService;
	
	public Hunt(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public static String possibleServices(int port) {
		if(port == 20 || port == 21) {
			possibleService = String.format("\t\tport %d, possible FTP service detected!\n", port);
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
	
	public void scanPorts(int minPort, int maxPort, int lastOctet) {
		try {
			for(int port = minPort; port < maxPort + 1; port++) {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter("output/nethuntResults.txt", true));
					Socket socket = new Socket();
				
					socket.connect(new InetSocketAddress(this.ipAddress, port));
					socket.close();
					
					System.out.printf("\t%s.%d, port %d, status: open\n", this.ipAddress, lastOctet, port);
					System.out.println(possibleServices(port));
					
					writer.write(String.format("\t%s.%d, port %d, status: open\n", this.ipAddress, lastOctet, port));
					writer.write(possibleServices(port));
					writer.close();
				} catch(ConnectException exc) {
					System.out.printf("\t%s.%d, port %d, status: closed\n", this.ipAddress, lastOctet, port);
				} catch(SocketException exc) {
					System.out.printf("\t%s.%d, port %d, status: closed\n", this.ipAddress, lastOctet, port);
				}
			}
		} catch(IOException exc) {
			exc.printStackTrace();
		}
	}
}
