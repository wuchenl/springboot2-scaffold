package com.letters7.wuchen.springboot2.utils.encode;

/**
 * 
 * 字节工具类
 * 
 * @author zoubin02
 *
 */
public class UtilByte {

	/**
	 * 用于建立十六进制字符的输出
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * 用于建立十六进制字符的输出
	 */
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public static String toHumanSize(long size) {
		// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
		if (size < 1024) {
			return String.valueOf(size) + "B";
		} else {
			size = size / 1024;
		}
		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
		// 因为还没有到达要使用另一个单位的时候
		// 接下去以此类推
		if (size < 1024) {
			return String.valueOf(size) + "KB";
		} else {
			size = size / 1024;
		}
		if (size < 1024) {
			// 因为如果以MB为单位的话，要保留最后1位小数，
			// 因此，把此数乘以100之后再取余
			size = size * 100;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
		} else {
			// 否则如果要以GB为单位的，先除于1024再作同样的处理
			size = size * 100 / 1024;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
		}
	}

	/**
	 * 将字节数组转换为十六进制字符数组。
	 * 
	 * 因为使用两个字符表示一个字节，所以返回的char[]长度将是参数byte[]长度的两倍。
	 * 
	 * @param data
	 *            用于转换为十六进制字符的byte[]
	 * @return 包含十六进制字符的char[]
	 */
	public static char[] encode2Hex(final byte[] data) {
		return encode2Hex(data, true);
	}

	/**
	 * 将字节数组转换为十六进制字符数组。
	 * 
	 * 因为使用两个字符表示一个字节，所以返回的char[]长度将是参数byte[]长度的两倍。
	 * 
	 * @param data
	 *            用于转换为十六进制字符的byte[]
	 * @param toLowerCase
	 *            <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 包含十六进制字符的char[]
	 */
	public static char[] encode2Hex(final byte[] data, final boolean toLowerCase) {
		return encode2Hex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * 将字节数组转换为十六进制字符数组。
	 * 
	 * 因为使用两个字符表示一个字节，所以返回的char[]长度将是参数byte[]长度的两倍。
	 * 
	 * @param data
	 *            用于转换为十六进制字符的byte[]
	 * @param toDigits
	 *            用于控制输出的字母表
	 * @return 包含十六进制字符的char[]
	 */
	protected static char[] encode2Hex(final byte[] data, final char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex paramName.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * 将字节数组转换为十六进制字符串。
	 * 
	 * 因为使用两个字符表示一个字节，所以返回的的字符串长度将是参数byte[]长度的两倍。
	 * 
	 * @param data
	 *            用于转换为十六进制字符的byte[]
	 * @return 十六进制字符串
	 */
	public static String encode2HexStr(final byte[] data) {
		return encode2HexStr(data, false);
	}

	/**
	 * 将字节数组转换为十六进制字符串。
	 * 
	 * 因为使用两个字符表示一个字节，所以返回的的字符串长度将是参数byte[]长度的两倍。
	 * 
	 * @param data
	 *            用于转换为十六进制字符的byte[]
	 * @param toLowerCase
	 *            <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制字符串
	 */
	public static String encode2HexStr(byte[] data, boolean toLowerCase) {
		return encode2HexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * 将字节数组转换为十六进制字符串。
	 * 
	 * 因为使用两个字符表示一个字节，所以返回的的字符串长度将是参数byte[]长度的两倍。
	 * 
	 * @param data
	 *            用于转换为十六进制字符的byte[]
	 * @param toDigits
	 *            用于控制输出的字母表
	 * @return 十六进制字符串
	 */
	protected static String encode2HexStr(byte[] data, char[] toDigits) {
		return new String(encode2Hex(data, toDigits));
	}

	/**
	 * 将十六进制字符数组转换为字节数组
	 * 
	 * @param data
	 *            十六进制char[]
	 * @return byte[]
	 * @throws RuntimeException
	 *             如果源十六进制字符数组的长度是奇数，将抛出运行时异常
	 */
	public static byte[] decode2Hex(char[] data) {
		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}

		// 一个byte对应两个十六进制字符，则将byte[]大小设置为char[]大小的一半
		byte[] out = new byte[len >> 1];

		// two characters form the hex paramName.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	// 将十六进制字符转换成一个整数
	private static int toDigit(final char ch, final int index) {
		final int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

}
