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

	private boolean isAddressReachable;
	
	/**
	 * 
	 * addressReachability builds the target ipv4 address and use the isReachable 
	 * method to check the connectivity of these address. All 
	 * addresses with status success will be written to the output file.
	 * 
	 * @param startOctet		defines the start address using the last octet 
	 * @param endOctet		defines the last address to scan using the last octet
	 * @param minPort		defines the start port
	 * @param maxPort		defines the last port
	 * @param portScan		used to activate or deactivate the port scan
	 * @throws IOException		throws when actions like the connectivity check, the output file writer
	 * 				or the port scanning option fails
	 * 
	 */
	public void addressReachability(int startOctet, int endOctet, int minPort, int maxPort, boolean portScan) throws IOException {
		Date currentDate = new Date();
		long timerStart = System.currentTimeMillis();

        	System.out.printf("\nstart scanning at %s\n\n", currentDate);

        	for(int octet = startOctet; octet < endOctet + 1; octet++) {
        		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("output/nethuntResults.txt", true));
        		String pingAddr = String.format("%s.%d", ipAddress, octet); // build the target ipv4 address
        	
            		try {
                		if(InetAddress.getByName(pingAddr).isReachable(1000)) {
                			System.out.printf("%s, connected successfully\n", pingAddr);
                	
                			outputWriter.write(String.format("%s, connected successfully, %s\n", pingAddr, currentDate));
                	
                			isAddressReachable = true;
                		} else {
                			System.out.printf("%s, connection failed\n", pingAddr);
                	
                			isAddressReachable = false;
                		}
                
               			if(portScan != false && isAddressReachable == true) {
                			scanPorts(minPort, maxPort, octet);
                		}
           		} catch(UnknownHostException exc) {
                		System.out.printf("Host (%s) not known\n", ipAddress);
            		} finally {
            			outputWriter.close();
            		}
        	}
        
        	long timerEnd = System.currentTimeMillis();
        	long timerFinal = timerEnd - timerStart;
        
        	System.out.printf("\nscan finished, needed time: %dms\n", timerFinal);
	}
}

