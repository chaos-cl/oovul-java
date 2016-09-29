package com.oo.vul.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class IPUtil {

	private static Log log = LogFactory.getLog(IPUtil.class);
	/**
	 * 简单的IP正则匹配
	 */
	private static final String IP_REGEX = "(\\d+\\.){3}\\d+";
	/**
	 * 简单的子网掩码正则匹配
	 */
	private static final String SUBNETMASK_REGEX = "(\\d+\\.){3}\\d+";

	/**
	 * 
	 * @category param ip 因为如果计算的结果中IP太多 导致计算时间过长,所以咱不适用该方法
	 * @param netmask
	 * @return
	 * @author
	 * @createDate 2014-11-25
	 */
	@Deprecated
	public static List<String> calculate(String ip, String netmask) {
		// log.info("要计算的IP=" + ip + ",子网掩码是" + netmask);
		if (!ip.matches(IP_REGEX) || !netmask.matches(SUBNETMASK_REGEX)) {
			throw new RuntimeException("IP或者子网掩码格式不正确!");
		}
		netmask = reverse(netmask);
		String networkAddress = calculateNetworkAddress(ip, netmask);
		String broadcastAddress = calculateBroadcastAddress(ip, netmask);
		// log.info("结果是->网络地址是" + networkAddress + ",广播地址是" +
		// broadcastAddress);
		// 计算出所有主机数
		List<String> resultList = new ArrayList<String>();
		if (lessThan(networkAddress, broadcastAddress)) {
			resultList.add(networkAddress);
		} else {
			return resultList;
		}
		while (true) {
			if (resultList.size() > 1000) {
				throw new RuntimeException("计算出结果的IP太多,超过1000个......");
			}
			String addIp = add(resultList.get(resultList.size() - 1));
			if (addIp != null && lessThan(addIp, broadcastAddress)) {
				resultList.add(addIp);
			} else {
				break;
			}
		}
		return resultList;
	}

	/**
	 * 
	 * @category 只计算出网络地址和和广播地址
	 * @param netmask
	 * @return
	 * @author
	 * @createDate 2014-11-25
	 */
	public static String calculateResult(String ip, String netmask) {
		try{
			// log.info("要计算的IP=" + ip + ",子网掩码是" + netmask);
			if (!ip.matches(IP_REGEX) || !netmask.matches(SUBNETMASK_REGEX)) {
				throw new RuntimeException("IP或者子网掩码格式不正确!");
			}
			String subnetmask = reverse(netmask);
			String networkAddress = calculateNetworkAddress(ip, subnetmask);
			String broadcastAddress = calculateBroadcastAddress(ip, subnetmask);
			// log.info("结果是->网络地址是" + networkAddress + ",广播地址是" +
			// broadcastAddress);
			return networkAddress + "/" + broadcastAddress;
		}catch(Exception e){
			//e.printStackTrace();
			log.error("计算IP地址"+ip+"吗"+netmask+"出错......");
		}
		return null;
	}

	/**
	 * 
	 * @category 是否是反码 如果是反码 进行反转
	 * @param netmask
	 * @return
	 * @author
	 * @createDate 2014-11-27
	 */
	private static String reverse(String netmask) {
		String result = null;
		String binary = toBinary(netmask);
		if (binary.replaceAll("\\.", "").matches("1+0*")) {
			result = netmask;
		} else {
			//log.info("要计算是否需要反转的掩码是" + netmask);
			binary = binary.replaceAll("0", "2");
			binary = binary.replaceAll("1", "0");
			binary = binary.replaceAll("2", "1");
			if (binary.replaceAll("\\.", "").matches("1+0*")) {
				result = toDecimal(binary);
			}
			//log.info("计算完成，得到的子网掩码是" + result);
		}
		return result;
	}

	private static String add(String ip) {
		String ipArray[] = ip.split("\\.");
		int x1 = Integer.parseInt(ipArray[0]);
		int x2 = Integer.parseInt(ipArray[1]);
		int x3 = Integer.parseInt(ipArray[2]);
		int x4 = Integer.parseInt(ipArray[3]);
		if (x4 < 255) {
			return x1 + "." + x2 + "." + x3 + "." + (x4 + 1);
		}
		if (x3 < 255) {
			return x1 + "." + x2 + "." + (x3 + 1) + ".0";
		}
		if (x2 < 255) {
			return x1 + "." + (x2 + 1) + ".0.0";
		}
		if (x1 < 255) {
			return (x1 + 1) + ".0.0.0";
		}
		return null;
	}

	private static boolean lessThan(String source, String target) {
		String[] start = source.split("\\.");
		String[] end = target.split("\\.");
		int x1 = Integer.parseInt(start[0]);
		int y1 = Integer.parseInt(end[0]);
		int x2 = Integer.parseInt(start[1]);
		int y2 = Integer.parseInt(end[1]);
		int x3 = Integer.parseInt(start[2]);
		int y3 = Integer.parseInt(end[2]);
		int x4 = Integer.parseInt(start[3]);
		int y4 = Integer.parseInt(end[3]);
		if (x1 < y1 || (x1 == y1 && x2 < y2)
				|| (x1 == y1 && x2 == y2 && x3 <y3)
				|| (x1 == y1 && x2 == y2 && x3 == y3 && x4 <=y4)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @category 由ip地址和子网掩码计算出网络地址（IP地址和子网掩码地址都必须是十进制格式的数据）
	 * @param ip
	 * @param netmask
	 * @author
	 * @createDate 2014-11-25
	 */
	private static String calculateNetworkAddress(String ip, String netmask) {
		String ipArray[] = ip.split("\\.");
		String netmaskArray[] = netmask.split("\\.");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ipArray.length; i++) {
			int x = Integer.parseInt(ipArray[i]);
			int y = Integer.parseInt(netmaskArray[i]);
			sb.append(x & y);
			if (i < ipArray.length - 1) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @category 由ip地址和子网掩码计算出广播地址（IP地址和子网掩码地址都必须是十进制格式的数据）
	 * @param ip
	 * @param netmask
	 * @author
	 * @createDate 2014-11-25
	 */
	private static String calculateBroadcastAddress(String ip, String netmask) {
		String binary = toBinary(netmask);
		int x = binary.replaceAll("1", "").length() - 3;// 计算出主机地址的位数
		String networkAddress = calculateNetworkAddress(ip, netmask);// 计算出广播地址
		String binaryNetworkAddress = toBinary(networkAddress).replaceAll(
				"\\.", "");// 转换成二进制 并把分隔符去掉
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= x; i++) {
			sb.append("1");
		}
		binaryNetworkAddress = binaryNetworkAddress.substring(0, 32 - x)
				+ sb.toString();
		StringBuffer result = new StringBuffer();
		for (int i = 1; i <= 4; i++) {
			result.append(toDecimal(binaryNetworkAddress.substring((i - 1) * 8,
					i * 8)));
			if (i < 4) {
				result.append(".");
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @category 计算格式如参数一 为192.168.1.160 参数二为24 类型的范围
	 * @param ip
	 * @param num
	 * @author
	 * @createDate 2014-11-25
	 */
	@Deprecated
	public static List<String> calculate(String ip, int num) {
		if (!ip.matches(IP_REGEX)) {
			throw new RuntimeException("IP格式不正确!" + ip);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= 32; i++) {
			if (i <= num) {
				sb.append("1");
			} else {
				sb.append("0");
			}
			if (i % 8 == 0 && i < 32) {
				sb.append(".");
			}
		}

		return calculate(ip, toDecimal(sb.toString()));
	}

	public static String calculateResult(String ip, int num) {
		if (!ip.matches(IP_REGEX)) {
			throw new RuntimeException("IP格式不正确!" + ip);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= 32; i++) {
			if (i <= num) {
				sb.append("1");
			} else {
				sb.append("0");
			}
			if (i % 8 == 0 && i < 32) {
				sb.append(".");
			}
		}

		return calculateResult(ip, toDecimal(sb.toString()));
	}

	/**
	 * 
	 * @category 计算格式如 192.168.1.160/24 类型的范围
	 * @param address
	 * @author
	 * @createDate 2014-11-25
	 */
	@Deprecated
	public static List<String> calculate(String address) {
		String ip = address.substring(0, address.lastIndexOf("/"));
		int num = Integer.parseInt(address.substring(
				address.lastIndexOf("/") + 1, address.length()));
		return calculate(ip, num);
	}

	/**
	 * 
	 * @category param address ip/num
	 * @author
	 * @createDate 2014-11-25
	 */
	public static String calculateResult(String address) {
		String ip = address.substring(0, address.lastIndexOf("/"));
		int num = Integer.parseInt(address.substring(
				address.lastIndexOf("/") + 1, address.length()));
		return calculateResult(ip, num);
	}

	/**
	 * 
	 * @category param address ip/subnetmask
	 * @author
	 * @createDate 2014-11-25
	 */
	public static String calculateResult2(String address) {
		String ip = address.substring(0, address.lastIndexOf("/"));
		String subnetmask = address.substring(address.lastIndexOf("/") + 1,
				address.length());
		return calculateResult(ip, subnetmask);
	}

	/**
	 * 
	 * @category 将IP由十进制转换成2进制
	 * @param ip
	 * @return
	 * @author
	 * @createDate 2014-11-25
	 */
	public static String toBinary(String ip) {
		String[] ips = ip.split("\\.");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ips.length; i++) {
			String binary = Integer.toBinaryString(Integer.parseInt(ips[i]));
			int len = binary.length();
			for (int j = 1; j < 8 - len + 1; j++) {
				binary = "0" + binary;
			}
			sb.append(binary);
			if (i < ips.length - 1) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @category 讲IP由二进制转换成十进制
	 * @param ip
	 * @return
	 * @author
	 * @createDate 2014-11-25
	 */
	public static String toDecimal(String ip) {
		String[] ips = ip.split("\\.");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ips.length; i++) {
			int x = Integer.valueOf(ips[i], 2);
			sb.append(x);
			if (i < ips.length - 1) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(IPUtil.calculateResult("10.0.0.0/8"));
		System.out.println(IPUtil.calculateResult("172.16.0.0/12"));
		System.out.println(IPUtil.calculateResult("192.168.0.0/16"));
	}
	public static boolean lessOrEqualThan(String source, String target) {
		String[] start = source.split("\\.");
		String[] end = target.split("\\.");
		int x1 = Integer.parseInt(start[0]);
		int y1 = Integer.parseInt(end[0]);
		int x2 = Integer.parseInt(start[1]);
		int y2 = Integer.parseInt(end[1]);
		int x3 = Integer.parseInt(start[2]);
		int y3 = Integer.parseInt(end[2]);
		int x4 = Integer.parseInt(start[3]);
		int y4 = Integer.parseInt(end[3]);
		if (x1 < y1 || (x1 == y1 && x2 < y2)
				|| (x1 == y1 && x2 == y2 && x3 < y3)
				|| (x1 == y1 && x2 == y2 && x3 == y3 && x4 <= y4)) {
			return true;
		}
		return false;
	}
	public static boolean filter(String ip, List<String> domainList) {
			if (ip == null || "any".equals(ip)) {
				return true;
			}
			if (ip.matches("(\\d+\\.){3}\\d+(\\/(\\d+\\.){3}\\d+)?")) {
				for (String domain : domainList) {
					if (domain.indexOf("/") != -1) {
						domain = IPUtil.calculateResult(domain);
					}
					if (ip.indexOf("/") != -1) {
						String ip1 = ip.substring(0, ip.indexOf("/"));
						String ip2 = ip.substring(ip.indexOf("/") + 1,
								ip.length());
						if (domain.indexOf("/") != -1) {
							String domain1 = domain.substring(0,
									domain.indexOf("/"));
							String domain2 = domain.substring(
									domain.indexOf("/") + 1, domain.length());
							if ((lessOrEqualThan(ip2, domain2) && lessOrEqualThan(
									domain1, ip2))
									|| (lessOrEqualThan(ip1, domain2) && lessOrEqualThan(
											domain1, ip1))) {
								return true;
							}
						} else if (lessOrEqualThan(ip1, domain)
								&& lessOrEqualThan(domain, ip2)) {
							return true;
						}
					} else {
						if (domain.indexOf("/") != -1) {
							String domain1 = domain.substring(0,
									domain.indexOf("/"));
							String domain2 = domain.substring(
									domain.indexOf("/") + 1, domain.length());
							if (lessOrEqualThan(domain1, ip)
									&& lessOrEqualThan(ip, domain2)) {
								return true;
							}
						} else {
							if (ip.equals(domain)) {
								return true;
							}
						}
					}
				}
			}
		return false;
	}
}
