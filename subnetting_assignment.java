
import java.util.*;
import java.io.*;
import java.lang.*; 


public class subnetting_assignment {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter IP Address in Dotted Notation");
        String str = sc.nextLine();
        String ipClass = determineClass(str);
        System.out.println("1.Class of given IP address is " + ipClass);
        System.out.println("_____________________________________________________________________");
        String defaultSubnetMask = getDefaultSubnetMask(ipClass);
        System.out.println("2.Default Subnet Mask: " + defaultSubnetMask);
        System.out.println("_____________________________________________________________________");
        int[] subnetInfo = calculateSubnetInfo(ipClass);
        int numSubnets = subnetInfo[0];
        int numHostsPerSubnet = subnetInfo[1];
        System.out.println("3.Number of Subnets: " + numSubnets);
        System.out.println("_____________________________________________________________________");
        System.out.println("4.Number of Hosts per Subnet: " + numHostsPerSubnet);
        separate(str,ipClass);
        String broadcastAddress = calculateBroadcastAddress(str, defaultSubnetMask);
        System.out.println("_____________________________________________________________________");
    System.out.println("7.Broadcast Address: " + broadcastAddress);
       
    String[] validHostRange = calculateValidHostRange(str, defaultSubnetMask);
    System.out.println("_____________________________________________________________________");
    System.out.println("8.Valid Host Range: " + validHostRange[0] + " - " + validHostRange[1]);
    

      
       
    }

    // Method to determine the class of an IP address
    static String determineClass(String str) {
        int index = str.indexOf('.'); // will find . in the given IP address
        String octet = str.substring(0, index);
        int ip = Integer.parseInt(octet);
        // Class A
        if (ip >= 1 && ip <= 126) {
            return "A";
        }
        // Class B
        else if (ip >= 128 && ip <= 191) {
            return "B";
        }
        // Class C
        else if (ip >= 192 && ip <= 223) {
            return "C";
        } else {
            return "Invalid";
        }
    }

    // Method to get the default subnet mask based on IP class
    static String getDefaultSubnetMask(String ipClass) {
        if (ipClass.equals("A")) {
            return "255.0.0.0";
        } else if (ipClass.equals("B")) {
            return "255.255.0.0";
        } else if (ipClass.equals("C")) {
            return "255.255.255.0";
        } else {
            return "Invalid";
        }
    }
     // Method to calculate the number of subnets and hosts per subnet
     static int[] calculateSubnetInfo(String ipClass) {
      int numSubnets = 0;
      int numHostsPerSubnet = 0;

      switch (ipClass) {
          case "A":
              numSubnets = 128; // Class A allows for 128 subnets
              numHostsPerSubnet = 16777214; // 2^24 - 2
              break;
          case "B":
              numSubnets = 16384; // Class B allows for 16384 subnets
              numHostsPerSubnet = 16382; // 2^14 - 2
              break;
          case "C":
              numSubnets = 2097152; // Class C allows for 2097152 subnets
              numHostsPerSubnet = 254; // 2^8 - 2
              break;
      }

      return new int[]{numSubnets, numHostsPerSubnet};
  }

  // Now method to print Network Id and broadcast Address of entered ip address
  static void separate(String str, String ipClass){
    // Initializing network and host empty
    String network = "", host = "";

    if(ipClass == "A"){
        int index = str.indexOf('.');
        network = str.substring(0,index);
        host = str.substring(index+1,str.length());
    }else if(ipClass == "B"){
        //Position of breaking network and HOST id
        int index = -1;
        int dot = 2;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='.'){
                dot -=1;
                if(dot==0){
                    index = i;
                    break;
                }
            }
        }
        network = str.substring(0,index);
        host = str.substring(index+1,str.length());
    }else if(ipClass == "C"){
        //Position of breaking network and HOST id
        int index = -1;
        int dot = 3;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='.'){
                dot -=1;
                if(dot==0){
                    index = i;
                    break;                    
                }
            }
        }
        network = str.substring(0,index);
        host = str.substring(index+1,str.length());
    
    }
    System.out.println("_____________________________________________________________________");
    System.out.println("5.Network ID is "+network);
    System.out.println("_____________________________________________________________________");
    System.out.println("6.Host ID is "+host);
}


// Method to calculate broadcast address for the entered IP address
static String calculateBroadcastAddress(String str, String subnetMask) {
  String broadcast = "";

  String[] ipOctets = str.split("\\.");
  String[] maskOctets = subnetMask.split("\\.");

  int[] broadcastAddressOctets = new int[4];

  for (int j = 0; j < 4; j++) {
      broadcastAddressOctets[j] = Integer.parseInt(ipOctets[j]) | (255 - Integer.parseInt(maskOctets[j]));
  }

  broadcast = broadcastAddressOctets[0] + "." + broadcastAddressOctets[1] + "." + broadcastAddressOctets[2] + "." + broadcastAddressOctets[3];

  return broadcast;
}
 
// Method to find the range of valid host addresses for a given IP and subnet mask
static String[] calculateValidHostRange(String str, String subnetMask) {
  String[] validHostRange = new String[2];

  String[] ipOctets = str.split("\\.");
  String[] maskOctets = subnetMask.split("\\.");

  int[] networkAddressOctets = new int[4];
  int[] broadcastAddressOctets = new int[4];

  for (int j = 0; j < 4; j++) {
      networkAddressOctets[j] = Integer.parseInt(ipOctets[j]) & Integer.parseInt(maskOctets[j]);
      broadcastAddressOctets[j] = networkAddressOctets[j] | (255 - Integer.parseInt(maskOctets[j]));
  }

  // Calculate the first usable host address (network address + 1)
  int[] firstUsableHostOctets = Arrays.copyOf(networkAddressOctets, 4);
  firstUsableHostOctets[3]++; // Increment the last octet

  // Calculate the last usable host address (broadcast address - 1)
  int[] lastUsableHostOctets = Arrays.copyOf(broadcastAddressOctets, 4);
  lastUsableHostOctets[3]--; // Decrement the last octet

  validHostRange[0] = firstUsableHostOctets[0] + "." + firstUsableHostOctets[1] + "." + firstUsableHostOctets[2] + "." + firstUsableHostOctets[3];
  validHostRange[1] = lastUsableHostOctets[0] + "." + lastUsableHostOctets[1] + "." + lastUsableHostOctets[2] + "." + lastUsableHostOctets[3];

  return validHostRange;
}


}



