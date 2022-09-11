package recon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Date;

public class IPHunt extends Hunt {
	public IPHunt(String ipAddress) {
		super(ipAddress);
	}
	private boolean isAddressReachable;
	private int hostsCount;
	
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
        		String pingAddr = String.format("%s.%d", ipAddress, octet);
        		InetAddress inetAddress = InetAddress.getByName(pingAddr);
        	
            		try {
            			if(inetAddress.isReachable(1000)) {
                			System.out.printf(
                				"[+] %s ( %s ):\n |••• connected successfully, cTime=%s\n", pingAddr, 
                				inetAddress.getCanonicalHostName(), LocalTime.now().toString()
                			);
                			outputWriter.write(String.format(
                				"\n%s ( %s ):\n |••• connected successfully, cTime=%s\n", pingAddr, 
                				inetAddress.getCanonicalHostName(), currentDate
                			));
                			
                			hostsCount++;
                			isAddressReachable = true;
                		} else {
                			System.out.printf("%s: connection failed, cTime=%s\n", pingAddr, LocalTime.now().toString());
                			isAddressReachable = false;
                		}
           		} catch(UnknownHostException exc) {
                		System.out.printf("Host (%s) not known\n", ipAddress);
            		} finally {
            			outputWriter.close();
            		}
            		
            		if(portScan != false && isAddressReachable == true) scanPorts(minPort, maxPort, octet);
        	}
        	
        	// build statistics section
        	int hostsToScan = endOctet - startOctet;
        	long timerEnd = System.currentTimeMillis();
        	long timerFinal = timerEnd - timerStart;
        	int inactiveHosts = hostsToScan - hostsCount;
        	int activeHosts = hostsToScan - inactiveHosts;
        	
        	if(activeHosts < 0) activeHosts = 0;
        	
        	String minHost = String.format("%s.%d", ipAddress, startOctet);
        	String maxHost = String.format("%s.%d", ipAddress, endOctet);
        	
        	System.out.println("\n\n******************* statistics *******************");
        	System.out.printf(
        		"total=%d, active=%d, inactive=%d, minHost=%s,\nmaxHost=%s, neededTime=%dms\n", 
        		hostsToScan, activeHosts, inactiveHosts, minHost, maxHost, timerFinal
        	);
	}
}
