package recon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class IPHunt extends Hunt {
	public IPHunt(String ipAddress) {
		super(ipAddress);
	}

	private boolean isReachable;
	
	public void addressReachability(int startOctet, int endOctet, int minPort, int maxPort, boolean portScan) throws IOException {
		Date currentDate = new Date();
		long timerStart = System.currentTimeMillis();

        	System.out.printf("\nstart scanning at %s\n\n", currentDate);

        	for(int octet = startOctet; octet < endOctet + 1; octet++) {
        		BufferedWriter outputWriter = null;
        	
            		try {
                		String pingAddr = String.format("%s.%d", ipAddress, octet);
                		outputWriter = new BufferedWriter(new FileWriter("output/nethuntResults.txt", true));
                
                		if(InetAddress.getByName(pingAddr).isReachable(1000)) {
                			System.out.printf("%s, connected successfully\n", pingAddr);
                	
                			outputWriter.write(String.format("%s, connected successfully, %s\n", pingAddr, currentDate));
                	
                			isReachable = true;
                		} else {
                			System.out.printf("%s, connection failed\n", pingAddr);
                	
                			isReachable = false;
                		}
                
                		if(portScan != false) {
                			if(isReachable == true) {
                				scanPorts(minPort, maxPort, octet);
                			}
                		}
            		} catch(UnknownHostException exc) {
                		System.out.printf("Host (%s) not known\n", ipAddress);
            		} finally {
            			outputWriter.close();
            		}
        	}
        
        	long timerEnd = System.currentTimeMillis();
        	long timerFinal = timerEnd - timerStart;
        
        	System.out.printf("\nscan done, needed time: %dms\n", timerFinal);
	}
}

