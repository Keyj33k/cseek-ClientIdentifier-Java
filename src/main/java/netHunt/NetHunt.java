package netHunt;

import java.util.logging.Logger;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.io.File;
import java.io.IOException;

import exceptions.InvalidOctetException;
import exceptions.InvalidConfigException;
import recon.IPHunt;
import iface.AddressHandling;

public class NetHunt {
	private String ipAddress;
	private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public NetHunt(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	private static final void syntaxInfo() {
		System.out.println(new StringBuilder()
			.append("NetHunt - Version 0.0.5\n")
			.append("------------------------\n\n")
			.append("syntax: \t java -jar NetHunt.jar <ipv4Addr(First Three Octets Only)> <minHost> <maxHost> disable\n")
			.append("\t\t java -jar NetHunt.jar <ipv4Addr(First Three Octets Only)> <minHost> <maxHost> enable <minPort> <maxPort>\n\n")
			.append("examples:\t java -jar NetHunt.jar 192.168.2 1 5 disable\n")
			.append("\t\t\t\tor\n")
			.append("\t\t java -jar NetHunt.jar 192.168.2 1 10 enable 50 60\n")
		);
		
		System.exit(1);
	}
	
	private void outputFileCheck() throws IOException {
		if(new File("output").mkdir() == true) System.out.println("\noutput directory created successfully");
		if(new File("output/nethuntResults.txt").createNewFile() == true) System.out.println("output file /output/nethuntResults.txt created successfully\n");
		
		logger.info("done");
	}
	
	private void octetCheck() {
		try {
			// disassemble the ip address and check the correctness of the individual octets
			AddressHandling extractOctet = (String address, int octet) -> { return address.split("\\.")[octet]; };
			
			int firstOctet = Integer.parseInt(extractOctet.getOctet(this.ipAddress, 0));
			int secondOctet = Integer.parseInt(extractOctet.getOctet(this.ipAddress, 1));
			int thirdOctet = Integer.parseInt(extractOctet.getOctet(this.ipAddress, 2));

            		if(firstOctet <= 0 || firstOctet >= 253) {
               			throw new InvalidOctetException(String.format("octet one (%d) is invalid.", firstOctet));
            		} else if(secondOctet <= 0 || secondOctet >= 253) {
            			throw new InvalidOctetException(String.format("octet two (%d) is invalid.", secondOctet));
            		} else if(thirdOctet <= 0 || thirdOctet >= 253) {
            			throw new InvalidOctetException(String.format("octet three (%d) is invalid.", thirdOctet));
            		}
        	} catch(ArrayIndexOutOfBoundsException exc) {
            		syntaxInfo();
        	} finally {
        		logger.info("done");
        	}
	}
	
	private void portCheck(int minPort, int maxPort) {
	 	if(minPort > maxPort) {
	 		throw new InvalidConfigException();
	 	} else if(minPort >= 65533 || minPort <= 0) {
	 		throw new InvalidConfigException(String.format("port %d is invalid", minPort));
	 	} else if(maxPort >= 65534 || maxPort <= 0) {
	 		throw new InvalidConfigException(String.format("port %d is invalid", maxPort));
	 	}
	}
	
	public static void main(String args[]) throws InterruptedException, IOException {
		int minPort = 0;
		int maxPort = 0;
		
		try {
			NetHunt netHunt = new NetHunt(args[0]);
			IPHunt ipHunt = new IPHunt(args[0]);
			
			if(!args[3].equals("enable") && !args[3].equals("disable")) syntaxInfo();
			if(args[3].equals("enable") && args.length != 6 || args[0].split("\\.").length != 3) syntaxInfo();
			if(args[3].equals("disable") && args.length != 4 || args[0].split("\\.").length != 3) syntaxInfo();
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> { System.out.println("\nnethunt done, exit"); }));
			
			int minHost = Integer.parseInt(args[1]);
			int maxHost = Integer.parseInt(args[2]);
			
			if(minHost > maxHost || minHost <= 0 || minHost >= 253 || maxHost <= 0 || maxHost >= 253) throw new InvalidConfigException();
			
			System.out.println("NetHunt - Version 0.0.5\n");
			System.out.println("////////////////////////// CONFIG CHECK //////////////////////////\n");
			
			netHunt.octetCheck();
			Thread.sleep(500);
			netHunt.outputFileCheck();
			Thread.sleep(500);
			
			System.out.println("\n////////////////////////// SCAN BEGINS ///////////////////////////\n");
			
			if(args[3].equals("enable")) {
				minPort = Integer.parseInt(args[4]);
				maxPort = Integer.parseInt(args[5]);
				
				netHunt.portCheck(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
				logger.info("port scanning enabled, the process may take some time");
				ipHunt.addressReachability(minHost, maxHost, minPort, maxPort, true);
			} else {
				ipHunt.addressReachability(minHost, maxHost, 0, 0, false);
			}
		} catch(NumberFormatException exc) {
			throw new InvalidConfigException();
		} catch(ArrayIndexOutOfBoundsException exc) {
			syntaxInfo();
		}
	}
}
