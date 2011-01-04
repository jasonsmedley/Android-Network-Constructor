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
package util;

/**
 * Tools for ANDing IP addresses
 */
public class ANDing {
	
	/**
	 * Takes two IP address and ANDs them
	 *
	 * @param ip1 the IP address
	 * @param ip2 the Subnet Mask
	 * @return the subnet address
	 */
	public static String and(String ip1, String ip2) {
		char[][] ip_bin1 = Conversion.ipToBin(ip1);
		char[][] ip_bin2 = Conversion.ipToBin(ip2);
		
		char[][] ip_result = new char[4][8];
		
		for (int i = 0; i < 8; i++) {
			if (ip_bin1[0][i] == '0' || ip_bin2[0][i] == '0') {
				ip_result[0][i] = '0';
			} else {
				ip_result[0][i] = '1';
			}
			
			if (ip_bin1[1][i] == '0' || ip_bin2[1][i] == '0') {
				ip_result[1][i] = '0';
			} else {
				ip_result[1][i] = '1';
			}
			
			if (ip_bin1[2][i] == '0' || ip_bin2[2][i] == '0') {
				ip_result[2][i] = '0';
			} else {
				ip_result[2][i] = '1';
			}
			
			if (ip_bin1[3][i] == '0' || ip_bin2[3][i] == '0') {
				ip_result[3][i] = '0';
			} else {
				ip_result[3][i] = '1';
			}
		}

		return Conversion.toDecimal(new String(ip_result[0])) + "."
				+ Conversion.toDecimal(new String(ip_result[1])) + "."
				+ Conversion.toDecimal(new String(ip_result[2])) + "."
				+ Conversion.toDecimal(new String(ip_result[3]));
	}
	
	/**
	 * Takes two IP address and returns the AND'ed broadcast address
	 *
	 * @param ip1 the subnet address
	 * @param ip2 the Subnet Mask
	 * @return the broadcast address
	 */
	public static String broadcast(String ip1, String ip2) {
		char[][] ip_bin1 = Conversion.ipToBin(ip1);
		char[][] ip_bin2 = Conversion.ipToBin(ip2);
		
		char[][] ip_result = new char[4][8];
		
		for (int i = 0; i < 8; i++) {
			if( ip_bin2[0][i] == '0' ) {
				ip_result[0][i] = '1';
			} else {
				ip_result[0][i] = ip_bin1[0][i];
			}
			
			if( ip_bin2[1][i] == '0' ) {
				ip_result[1][i] = '1';
			} else {
				ip_result[1][i] = ip_bin1[1][i];
			}
			
			if( ip_bin2[2][i] == '0' ) {
				ip_result[2][i] = '1';
			} else {
				ip_result[2][i] = ip_bin1[2][i];
			}
			
			if( ip_bin2[3][i] == '0' ) {
				ip_result[3][i] = '1';
			} else {
				ip_result[3][i] = ip_bin1[3][i];
			}
		}
		
		return Conversion.toDecimal(new String(ip_result[0])) + "."
		+ Conversion.toDecimal(new String(ip_result[1])) + "."
		+ Conversion.toDecimal(new String(ip_result[2])) + "."
		+ Conversion.toDecimal(new String(ip_result[3]));
	}
}
