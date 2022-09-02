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
	
	public Hunt(String ipAddress) {
		this.ipAddress = ipAddress;
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
					
					writer.write(String.format("\t%s.%d, port %d, status: open\n", this.ipAddress, lastOctet, port));
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

