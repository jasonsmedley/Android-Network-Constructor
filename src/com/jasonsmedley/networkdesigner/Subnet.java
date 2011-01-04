package com.jasonsmedley.networkdesigner;
/*******************************************************************************
 * Copyright (c) 2010 Hippos Development Team
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * Contributors:
 *
 *    Hippos Development Team - Java Subnetting Calculator
 *
 ******************************************************************************/


import util.ANDing;
import util.Conversion;

import java.util.HashMap;

/**
 * API to deal with classful subnetting.
 */
public class Subnet {

	/** array filled with IP numbers. */
	private String[] ip_blocks;

	/** stores network class. */
	private char net_class;

	/** stores base IP address. */
	private String ip_addr;

	/** stores the subnet address. */
	private String subnet_addr;

	/** stores the broadcast address. */
	private String broadcast_addr;

	/** stores the Host bits. */
	private int host_bits;

	/** stores the Subnet Mask. */
	private String sub_mask;

	/** stores the subnet bits. */
	private int sub_bits;

	/** stores the total subnets. */
	private int total_subnets;

	/** stores the masked bits. */
	private int masked_bits;

	/** stores total hosts. */
	private int total_hosts;

	/** stores Minimum Host Address Range. */
	private String min_host_range;

	/** stores Maximum Host Address Range. */
	private String max_host_range;

	/** Hashmap of associated subnet bits for network classes. */
	private HashMap<Character, Integer> class_subnets = new HashMap<Character, Integer>();

	/**
	 * Sets the subnet mask, which in turn calculates everything else.
	 * 
	 * @param ip the subnet mask
	 */
	public void setSubnetMask(String ip) {
		/*
		 * Going to calculate the subnet mask and the broadcast address number
		 * of usable host, usable subnets, number of host bits, assignable
		 * address range, and increment
		 */
		sub_mask = ip;

		subnet_addr = ANDing.and(ip_addr, sub_mask);
		broadcast_addr = ANDing.broadcast(subnet_addr, sub_mask);

		calculateBitInformation(sub_mask, getNetworkClass());

		// minimum host address is subnet address plus one.
		String[] minimumHostAddress = subnet_addr.split("[.]");
		minimumHostAddress[3] = Integer.toString(Integer
				.parseInt(minimumHostAddress[3]) + 1);
		min_host_range = minimumHostAddress[0] + "." + minimumHostAddress[1]
				+ "." + minimumHostAddress[2] + "." + minimumHostAddress[3];

		// maximum host address, is the broadcast address minus one.
		String[] maximumHostAddress = broadcast_addr.split("[.]");
		maximumHostAddress[3] = Integer.toString(Integer
				.parseInt(maximumHostAddress[3]) - 1);
		max_host_range = maximumHostAddress[0] + "." + maximumHostAddress[1]
				+ "." + maximumHostAddress[2] + "." + maximumHostAddress[3];
	}

	/**
	 * Takes the subnet mask, and calculates the bit information. Sets the bit
	 * variables so they can be accessed.
	 *
	 * @param sub_mask the sub_mask
	 * @param network_class the classful type of IP, a, b, c, or even d
	 */
	public void calculateBitInformation(String sub_mask, char network_class) {
		char[][] bin_sub_mask = Conversion.ipToBin(sub_mask);
		int num_bits = 0;

		// For every bit in the IP, if it equals '1' add it to bin_sub_mask
		for (int i = 0; i < 4; i++) {
			for (int n = 0; n < 8; n++) {
				if (bin_sub_mask[i][n] == '1') {
					num_bits++;
				}
			}
		}

		// The subnet bits are the bits, minus the default bits of that class
		int subnet_bits = num_bits - class_subnets.get(network_class);
		this.sub_bits = subnet_bits;

		// The Mask Bits are the total number of bits
		this.masked_bits = num_bits;

		// The total subnets are 2^the subnet bits
		this.total_subnets = (int) Math.pow(2, subnet_bits);

		// the host bits are equal to 32 - total bits
		int host_bits = 32 - num_bits;
		this.host_bits = host_bits;

		// The Hosts per subnet are 2^the host bits
		this.total_hosts = (int) Math.pow(2, host_bits);

	}

	/**
	 * Sets the subnet bits, which then resolves the subnet mask and calculates all 
	 * of the other information.
	 *
	 * @param subnetBits the subnet bits
	 */
	public void setSubnetBits(int subnetBits) {
		sub_bits = subnetBits;

		int bits = subnetBits + class_subnets.get(net_class);
		int re = 32 - bits;
		int mb = 32;
		String strt = "00000000000000000000000000000000";
		char[] b = strt.toCharArray();

		for (int n = 0; n <= re; n++) {
			mb--;
		}

		for (int i = 0; i <= mb; i++) {
			b[i] = '1';
		}

		String s = new String(b);
		char[][] ip = { s.substring(0, 8).toCharArray(),
				s.substring(8, 16).toCharArray(),
				s.substring(16, 24).toCharArray(),
				s.substring(24, 32).toCharArray() };

		String mask = Integer.toString(Integer.parseInt(new String(ip[0]), 2))
				+ "."
				+ Integer.toString(Integer.parseInt(new String(ip[1]), 2))
				+ "."
				+ Integer.toString(Integer.parseInt(new String(ip[2]), 2))
				+ "."
				+ Integer.toString(Integer.parseInt(new String(ip[3]), 2));

		setSubnetMask(mask);
	}
	
	/**
	 * Sets the IP address, which is essencial for any information to be recieved.
	 *
	 * @param ip the new IP address
	 */
	public void setIPAddress(String ip) {
		ip_addr = ip;
		ip_blocks = ip.split("[.]");

		int f = Integer.parseInt(ip_blocks[0]);
		if (f > 255) {
			System.err.print("Not a binary octet");
		} else {
			if (f <= 127) {
				net_class = 'a';
			}
			if (f <= 191 && f >= 128) {
				net_class = 'b';
			}
			if (f <= 223 && f >= 192) {
				net_class = 'c';
			}
			if (f <= 239 && f >= 224) {
				net_class = 'd';
			}
			if (f <= 255 && f >= 240) {
				net_class = 'e';
			}
		}

		class_subnets.put('a', 8); // 255.0.0.0
		class_subnets.put('b', 16); // 255.255.0.0
		class_subnets.put('c', 24); // 255.255.255.0
		class_subnets.put('d', 3); // 224.0.0.0
		class_subnets.put('e', 4); // 240.0.0.0
	}

	/**
	 * Sets the total subnets required, which based on this calculates the subnet mask
	 * and figures out all of the other information.
	 *
	 * @param totalSubnets the total required subnets
	 */
	public void setTotalSubnets(int totalSubnets) {
		total_subnets = totalSubnets;
		int subnetBits = (int)(Math.log(totalSubnets) / Math.log(2)); // 2^x = totalSubnets; x = log2(totalSubnets)
		setSubnetBits(subnetBits);
	}

	/**
	 * Sets the total required hosts, which based on this calculates the subnet mask
	 * and figures out all of the other information.
	 *
	 * @param totalHosts the total required hosts
	 */
	public void setTotalHosts(int totalHosts) {
		total_hosts = totalHosts;
		int hostBits = (int)(Math.log(totalHosts) / Math.log(2));
		int subnetBits = 32 - (hostBits + class_subnets.get(net_class));
		setSubnetBits(subnetBits);
	}

	/**
	 * Sets the total masked bits, which figures out the subnet bits, calls setSubnetBits() which 
	 * figures out the subnet mask and calculates the rest of the data.
	 *
	 * @param maskedBits the new masked bits
	 */
	public void setMaskedBits(int maskedBits) {
		masked_bits = maskedBits;
		int subnetBits = masked_bits - class_subnets.get(net_class);
		setSubnetBits(subnetBits);
	}

	/**
	 * Gets the subnet address.
	 * 
	 * @return the subnet address
	 */
	public String getSubnetAddress() {
		return subnet_addr;
	}

	/**
	 * Gets the network class: a, b, c, d, e
	 * 
	 * @return the network class
	 */
	public char getNetworkClass() {
		return net_class;
	}

	/**
	 * Gets the broadcast address.
	 * 
	 * @return the subnet address
	 */
	public String getBroadcastAddress() {
		return broadcast_addr;
	}

	/**
	 * Gets the subnet bits.
	 *
	 * @return the subnet bits
	 */
	public int getSubnetBits() {
		return sub_bits;
	}

	/**
	 * Gets the total subnets.
	 *
	 * @return the total subnets
	 */
	public int getTotalSubnets() {
		return total_subnets;
	}

	/**
	 * Gets the usable subnets.
	 *
	 * @return the usable subnets
	 */
	public int getUsableSubnets() {
		return total_subnets - 2;
	}

	/**
	 * Gets the masked bits.
	 *
	 * @return the masked bits
	 */
	public int getMaskedBits() {
		return masked_bits;
	}

	/**
	 * Gets the total hosts.
	 *
	 * @return the total hosts
	 */
	public int getTotalHosts() {
		return total_hosts;
	}

	/**
	 * Gets the usable hosts.
	 *
	 * @return the usable hosts
	 */
	public int getUsableHosts() {
		return total_hosts - 2;
	}

	/**
	 * Gets the minimum host address range.
	 *
	 * @return the minimum host address range
	 */
	public String getMinimumHostAddressRange() {
		return min_host_range;
	}

	/**
	 * Gets the maximum host address range.
	 *
	 * @return the maximum host address range
	 */
	public String getMaximumHostAddressRange() {
		return max_host_range;
	}

	/**
	 * Gets the subnet mask.
	 *
	 * @return the subnet mask
	 */
	public String getSubnetMask() {
		return sub_mask;
	}

	/**
	 * Gets the host bits.
	 *
	 * @return the host bits
	 */
	public int getHostBits() {
		return host_bits;
	}
	
	/**
	 * Gets the IP address.
	 *
	 * @return the IP address
	 */
	public String getIPAddress() {
		return ip_addr;
	}
}
