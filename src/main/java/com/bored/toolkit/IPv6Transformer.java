package com.bored.toolkit;

import java.math.BigInteger;

public class IPv6Transformer {

	public static BigInteger IP2BINative(String net) throws UnknownHostException {
		InetAddress ia = InetAddress.getByName(net);
		byte[] bytes = ia.getAddress();
		return new BigInteger(bytes);
	}
	
	public static BigInteger IP2BI(String net) {
		if (net == null) {
			return null;
		}
		boolean embedded = net.contains(".");
		if (net.startsWith("::")) {
			if (net.length() == 2) {
				return BigInteger.valueOf(0l);
			}

			String[] remaining = net.substring(2).split(":");

			byte[] bytes = new byte[embedded ? 2 * remaining.length + 3 : 2 * remaining.length + 1];
			for (int i = 0; i < remaining.length; i++) {
				if (embedded && i == remaining.length - 1) {
					String[] parts = remaining[i].split("\\.");
					bytes[2 * i + 1] = (byte) Integer.parseInt(parts[0]);
					bytes[2 * i + 2] = (byte) Integer.parseInt(parts[1]);
					bytes[2 * i + 3] = (byte) Integer.parseInt(parts[2]);
					bytes[2 * i + 4] = (byte) Integer.parseInt(parts[3]);
					break;
				}
				int part = Integer.parseInt(remaining[i], 16);
				bytes[2 * i + 1] = (byte) (part >> 8);
				bytes[2 * i + 2] = (byte) part;
			}
			return new BigInteger(bytes);
		}

		if (net.endsWith("::")) {
			String[] remaining = net.substring(0, net.length() - 2).split(":");
			if (remaining.length == 0) {
				return null;
			}
			byte[] bytes = new byte[17];
			for (int i = 0; i < remaining.length; i++) {
				int part = Integer.parseInt(remaining[i], 16);
				bytes[2 * i + 1] = (byte) (part >> 8);
				bytes[2 * i + 2] = (byte) part;
			}
			return new BigInteger(bytes);
		}

		String[] remaining = net.split(":");
		byte[] bytes = new byte[17];
		int index = 1;
		for (int i = 0; i < remaining.length; i++) {
			if ("".equals(remaining[i])) {
				index += 2 * (9 - remaining.length) - (embedded ? 2 : 0);
				continue;
			}

			if (embedded && i == remaining.length - 1) {
				String[] parts = remaining[i].split("\\.");
				bytes[index++] = (byte) Integer.parseInt(parts[0]);
				bytes[index++] = (byte) Integer.parseInt(parts[1]);
				bytes[index++] = (byte) Integer.parseInt(parts[2]);
				bytes[index++] = (byte) Integer.parseInt(parts[3]);
				break;
			}

			int part = Integer.parseInt(remaining[i], 16);
			bytes[index++] = (byte) (part >> 8);
			bytes[index++] = (byte) part;
		}
		return new BigInteger(bytes);
	}

}
