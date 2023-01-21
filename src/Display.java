import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Display extends JPanel{
	private JLabel updateLabel = new JLabel("Click to update your network info");

	private JButton updateButton = new JButton("Update");
	private JLabel hostNameLabel = new JLabel("Host Name . . . . . . . . . : ");
	private JLabel networkAdapterLabel = new JLabel("Network Adapter . . . . : ");
	private JLabel macAddressLabel = new JLabel("MAC Address . . . . . . . : ");
	private JLabel ipv4AddressLabel = new JLabel("IPv4 Address . . . . . . . : ");
	private JLabel subnetMaskLabel = new JLabel("Subnet Mask . . . . . . . : ");
	private JLabel subnetAltLabel = new JLabel("Subnet Alt . . . . . . . . . : ");
	private JLabel ipv6AddressLabel = new JLabel("IPv6 Address . . . . . . : ");
	private JLabel defaultGatewayLabel = new JLabel("Default Gateway . . . : ");
	
	public Display(int width, int height, Color bgColor, Color txtColor) {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(bgColor);
		
		this.add(updateLabel);
		updateLabel.setForeground(txtColor);
		updateLabel.setPreferredSize(new Dimension(width, height / 8));
		updateLabel.setHorizontalAlignment(SwingConstants.CENTER);
				
		this.add(updateButton, BorderLayout.CENTER);		
		
		JPanel things = new JPanel();
		things.setBackground(bgColor);
		things.setLayout(new GridLayout(0, 1));
		
		hostNameLabel.setForeground(txtColor); things.add(hostNameLabel, BorderLayout.WEST);
		
		networkAdapterLabel.setForeground(txtColor); things.add(networkAdapterLabel, BorderLayout.WEST);
		
		macAddressLabel.setForeground(txtColor); things.add(macAddressLabel, BorderLayout.WEST);
		
		ipv4AddressLabel.setForeground(txtColor); things.add(ipv4AddressLabel, BorderLayout.WEST);
		
		subnetMaskLabel.setForeground(txtColor); things.add(subnetMaskLabel, BorderLayout.WEST);
		
		subnetAltLabel.setForeground(txtColor); things.add(subnetAltLabel, BorderLayout.WEST);
		
		ipv6AddressLabel.setForeground(txtColor); things.add(ipv6AddressLabel, BorderLayout.WEST);
		
		defaultGatewayLabel.setForeground(txtColor); things.add(defaultGatewayLabel, BorderLayout.WEST);
		
		things.setPreferredSize(new Dimension(width, height / 2));
		this.add(things);
		
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					hostNameLabel.setText(hostNameLabel.getText() + InetAddress.getLocalHost().getHostName() + "   |   ");

					networkAdapterLabel.setText(networkAdapterLabel.getText() + getNetworkAdapter() + "   |   ");
					
					macAddressLabel.setText(macAddressLabel.getText() + getMac() + "   |   ");
					
					ipv4AddressLabel.setText(ipv4AddressLabel.getText() + getIPv4() + "   |   ");
					
					subnetMaskLabel.setText(subnetMaskLabel.getText() + getSubnetMask() + "   |   ");
					
					subnetAltLabel.setText(subnetAltLabel.getText() + getSubnet() + "   |   ");
					
					ipv6AddressLabel.setText(ipv6AddressLabel.getText() + getIPv6() + "   |   ");
					
					//ipv6AddressLabel.setText(ipv6AddressLabel.getText() + getIPv6Address());
					
				} catch (UnknownHostException x) {
					x.printStackTrace();
				} catch (IOException x) {
					x.printStackTrace();
				}

			}
			
		});
	}
	
	String getNetworkAdapter() throws SocketException, UnknownHostException {
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
		String name = networkInterface.getDisplayName();		
		return name;
	}
	
	String getMac() {
		InetAddress ip;
		String macAddress = null;
		try {
			ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
			}
			System.out.println(sb.toString());
			macAddress = sb.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}
	   return macAddress;
	}
	
	String getIPv4() {
		InetAddress ip;
		String ipv4Address = null;
		try {
			ip = InetAddress.getLocalHost();
			ipv4Address=ip.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	   return ipv4Address;
	}
	
	String getSubnetMask() throws SocketException, UnknownHostException {
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		
		short prflen=Short.valueOf(networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength());
		int shft = 0xffffffff<<(32-prflen);
		int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
		int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
		int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
		int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
		String submask = oct1+"."+oct2+"."+oct3+"."+oct4;
		return submask;
	}
	
	String getSubnet() throws SocketException, UnknownHostException {
		String subnetAlt=null;
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		String mask = String.valueOf(networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength());
		
		subnetAlt=getIPv4()+"/"+mask;
		return subnetAlt;
	}
	
	String getIPv6() throws IOException {
		String ipv6=null;
		InetAddress[] localHost = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		
        boolean status = this.getIPv6Addresses(localHost).isReachable(5000);
		if (status){
            ipv6 = this.getIPv6Addresses(localHost).getHostAddress();
        }
        else{
            System.out.println(this.getIPv6Addresses(localHost).getCanonicalHostName() + " Host Unreachable");
        }		
		return ipv6;
	}
	
	public Inet6Address getIPv6Addresses(InetAddress[] addresses) 
	{
	    for (InetAddress addr : addresses){
	        if (addr instanceof Inet6Address){
	            return (Inet6Address) addr;
	        }
	    }
	    return null;
	}
	
	/* I expected the method below to have the same result as the combined methods above, but for 
	 * some reason it returns the IPv6 Address instead of the Link-local IPv6 Address. 
	 */
	/*String getIPv6Address() throws IOException {
		String thing=null;
		InetAddress[] localHost = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		InetAddress bob = null;
		for (InetAddress addr : localHost){
	        if (addr instanceof Inet6Address){
	            bob=(Inet6Address)addr;
	        }
	    }
		
        boolean status = bob.isReachable(5000);
		if (status){
            System.out.println(" Host Reached\t" + bob.getHostAddress());
            thing = bob.getHostAddress();
        }
        else{
            System.out.println(bob.getCanonicalHostName() + " Host Unreachable");
        }		
		return thing;
	}*/
	
}
