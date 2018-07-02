package pfc.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SHA_256 {
	String msg = null;
	String msgBinary = new String();// 消息用二进制表示
	String msgFillString = null;
	String H0 = "6a09e667";// 初始值
	String H1 = "bb67ae85";
	String H2 = "3c6ef372";
	String H3 = "a54ff53a";
	String H4 = "510e527f";
	String H5 = "9b05688c";
	String H6 = "1f83d9ab";
	String H7 = "5be0cd19";
	String A, B, C, D, E, F, G, H;
	long begin;
	long end;
	String[] K = { "428a2f98", "71374491", "b5c0fbcf", "e9b5dba5", "3956c25b",
			"59f111f1", "923f82a4", "ab1c5ed5", "d807aa98", "12835b01",
			"243185be", "550c7dc3", "72be5d74", "80deb1fe", "9bdc06a7",
			"c19bf174", "e49b69c1", "efbe4786", "0fc19dc6", "240ca1cc",
			"2de92c6f", "4a7484aa", "5cb0a9dc", "76f988da", "983e5152",
			"a831c66d", "b00327c8", "bf597fc7", "c6e00bf3", "d5a79147",
			"06ca6351", "14292967", "27b70a85", "2e1b2138", "4d2c6dfc",
			"53380d13", "650a7354", "766a0abb", "81c2c92e", "92722c85",
			"a2bfe8a1", "a81a664b", "c24b8b70", "c76c51a3", "d192e819",
			"d6990624", "f40e3585", "106aa070", "19a4c116", "1e376c08",
			"2748774c", "34b0bcb5", "391c0cb3", "4ed8aa4a", "5b9cca4f",
			"682e6ff3", "748f82ee", "78a5636f", "84c87814", "8cc70208",
			"90befffa", "a4506ceb", "bef9a3f7", "c67178f2" };
	String[] w = new String[64];
	int group_num = 1;// 组数

	public static String SHA_256(String toEn) {
		SHA_256 sha256 = new SHA_256();
		return sha256.calculate(toEn);
		
	}
	
	public static void main(String[] args) throws IOException {
		//sha256.input();
		System.out.println(SHA_256("abcdeabcdeabcdeabcde").length());
	}
	
	public void input() throws IOException {
		String choice;
		String fileName;
		BufferedReader stdin;
		//String msg;
		File file;
		loop: while (true) {
			stdin = new BufferedReader(new InputStreamReader(System.in));
			//System.out.print("请选择从屏幕读(1)，从文件中读(2):");
			choice = stdin.readLine();

			if (choice.equals("1")) {
				//System.out.println("请输入消息");
				msg = stdin.readLine();
				break;
			}
			if (choice.equals("2")) {
				//System.out.println("请输入文件名");
				fileName = stdin.readLine();
				try {
					file = new File(fileName);
					stdin = new BufferedReader(new FileReader(file));
					msg = stdin.readLine();

				} catch (FileNotFoundException fe) {
					System.out.println("the file don't exist");

				} finally {
					stdin.close();
					break;
				}
			}
			if (!(choice.equals("1") || choice.equals("2"))) {
				continue loop;
			}
		}
	}

	public String calculate(String message)  {
		String result = "";
		
		msgBinary = stringToBinary(message);
		massage_group_and_fill(msgBinary);
		for (int i = 0; i < 64; i++) {
			K[i] = hexToBinary(K[i]);
		}
		
		// 求w[64]
		for (int n = 0; n < group_num; n++) {
			//System.out.println("第" + (n + 1) + "组:");
			String string = new String();// 存放每一个分组的512位
			string = msgFillString.substring(n * 512, (n + 1) * 512);// 取每一组的512为
			for (int i = 0; i < 16; i++) {
				w[i] = string.substring(i * 32, (i + 1) * 32);
				//System.out.println("w[" + i + "]  " + binaryToHex(w[i]));
			}
			for (int i = 16; i < 64; i++) {
				String string1 = binaryplus(small_sigma_one(w[i - 2]), w[i - 7]);
				String string2 = binaryplus(small_sigma_zero(w[i - 15]),w[i - 16]);
				w[i] = binaryplus(string1, string2);
				//System.out.println("w[" + i + "]  " + binaryToHex(w[i]));
			}

			A = new String(hexToBinary(H0));
			B = new String(hexToBinary(H1));
			C = new String(hexToBinary(H2));
			D = new String(hexToBinary(H3));
			E = new String(hexToBinary(H4));
			F = new String(hexToBinary(H5));
			G = new String(hexToBinary(H6));
			H = new String(hexToBinary(H7));

			result += calculate_sha_256(A, B, C, D, E, F, G, H);
		}
		return result;
	}

	public void massage_group_and_fill(String massage) {
		final int SIZE = massage.length();// 消息变成二进制后的长度
		int mod = SIZE % 512;

		// 求消息的组数
		if (SIZE < 448)
			group_num = 1;
		else if (SIZE >= 448 && SIZE <= 512)
			group_num = 2;
		else {
			if (mod < 448)
				group_num = SIZE / 512 + 1;
			else
				group_num = SIZE / 512 + 2;
		}

		char[] cw = new char[512 * group_num];// 存消息的二进制数

		for (int i = 0; i < SIZE; i++) {
			cw[i] = msgBinary.charAt(i);
		}
		// 填充
		String msgSizeBinary = new String(Integer.toBinaryString(SIZE));
		final int MSG_SIZE = msgSizeBinary.length();
		int fillZeroSize = 512 * group_num - MSG_SIZE;// 消息的二进制长度的二进制长度
		int j = 0;

		if (SIZE < 448) {
			cw[SIZE] = '1';
			Arrays.fill(cw, SIZE + 1, fillZeroSize, '0');
			for (int i = fillZeroSize; i < 512 * group_num; i++) {
				cw[i] = msgSizeBinary.charAt(j);
				j++;
			}

		}

		if (SIZE >= 448 && SIZE <= 512) {
			cw[SIZE] = '1';
			Arrays.fill(cw, SIZE + 1, fillZeroSize, '0');
			for (int i = fillZeroSize; i < 512 * group_num; i++) {
				cw[i] = msgSizeBinary.charAt(j);
				j++;
			}
		}

		if (SIZE > 512) {
			cw[SIZE] = '1';
			Arrays.fill(cw, SIZE + 1, fillZeroSize, '0');
			for (int i = fillZeroSize; i < 512 * group_num; i++) {
				cw[i] = msgSizeBinary.charAt(j);
				j++;
			}
		}
		msgFillString = new String(cw);
		//System.out.println("消息填充后的十六进制输出");
		//System.out.println(binaryToHex(msgFillString));
	}

	/*// 计算A,B,C,D,E,F,G,H函数
	public void calculate_sha_256(String A, String B, String C, String D,
			String E, String F, String G, String H) {
		String string1 = new String();
		String string2 = new String();
		for (int i = 0; i < 64; i++) {
			System.out.print("i=" + i + "  ");
			string1 = T1(H, E, ch(E, F, G), w[i], K[i]);
			string2 = binaryplus(string1, T2(A, maj(A, B, C)));
			H = G;
			G = F;
			F = E;
			E = binaryplus(D, string1);
			D = C;
			C = B;
			B = A;
			A = string2;
			display(A, B, C, D, E, F, G, H);
		}
		H0 = binaryToHex(binaryplus(A, hexToBinary(H0)));
		H1 = binaryToHex(binaryplus(B, hexToBinary(H1)));
		H2 = binaryToHex(binaryplus(C, hexToBinary(H2)));
		H3 = binaryToHex(binaryplus(D, hexToBinary(H3)));
		H4 = binaryToHex(binaryplus(E, hexToBinary(H4)));
		H5 = binaryToHex(binaryplus(F, hexToBinary(H5)));
		H6 = binaryToHex(binaryplus(G, hexToBinary(H6)));
		H7 = binaryToHex(binaryplus(H, hexToBinary(H7)));
		System.out.println(H0 + " " + H1 + " " + H2 + " " + H3 + " " + H4 + " "
				+ H5 + " " + H6 + " " + H7);
	}*/
	
	// 计算A,B,C,D,E,F,G,H函数
	public String calculate_sha_256(String A, String B, String C, String D,
			String E, String F, String G, String H) {
		String string1 = new String();
		String string2 = new String();
		for (int i = 0; i < 64; i++) {
			string1 = T1(H, E, ch(E, F, G), w[i], K[i]);
			string2 = binaryplus(string1, T2(A, maj(A, B, C)));
			H = G;
			G = F;
			F = E;
			E = binaryplus(D, string1);
			D = C;
			C = B;
			B = A;
			A = string2;
			display(A, B, C, D, E, F, G, H);
		}
		H0 = binaryToHex(binaryplus(A, hexToBinary(H0)));
		H1 = binaryToHex(binaryplus(B, hexToBinary(H1)));
		H2 = binaryToHex(binaryplus(C, hexToBinary(H2)));
		H3 = binaryToHex(binaryplus(D, hexToBinary(H3)));
		H4 = binaryToHex(binaryplus(E, hexToBinary(H4)));
		H5 = binaryToHex(binaryplus(F, hexToBinary(H5)));
		H6 = binaryToHex(binaryplus(G, hexToBinary(H6)));
		H7 = binaryToHex(binaryplus(H, hexToBinary(H7)));
		return H0 + H1+ H2 + H3 + H4 + H5 + H6 + H7;
	}


	// 填充函数
	public String fill_zero(String str, int n) {
		String string = new String();
		StringBuffer stringBuffer = new StringBuffer();

		if (str.length() < n)
			for (int i = 0; i < n - str.length(); i++) {
				string = stringBuffer.append('0').toString();
			}
		return string + str;
	}

	// 从字符串到二进制
	public String stringToBinary(String str) {
		StringBuffer stringBuffer = new StringBuffer();
		int temp = 0;
		for (int i = 0; i < str.length(); i++) {
			temp = Integer.valueOf(str.charAt(i));

			stringBuffer = stringBuffer.append(fill_zero(Integer
					.toBinaryString(temp), 8));
		}
		return stringBuffer.toString();
	}

	// 异或
	public String xor(String str1, String str2) {
		String string = new String();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < str1.length(); i++) {
			if (str1.charAt(i) == str2.charAt(i))
				string = stringBuffer.append('0').toString();
			else
				string = stringBuffer.append('1').toString();
		}
		return string;
	}

	public String xor(String str1, String str2, String str3) {
		String string1 = xor(str1, str2);
		return xor(string1, str3);
	}

	// 与
	public String and(String str1, String str2) {
		String string = new String();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < str1.length(); i++) {
			if (str1.charAt(i) == '0' || str2.charAt(i) == '0')
				string = stringBuffer.append('0').toString();
			else
				string = stringBuffer.append('1').toString();
		}
		return string;
	}

	// 非
	public String non(String str1) {
		String str = new String();
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < str1.length(); i++) {
			if (str1.charAt(i) == '0')
				str = s.append('1').toString();
			else
				str = s.append('0').toString();
		}
		return str;
	}

	// 循环右移n位
	public String rotr(String str, int n) {
		String str1 = str.substring(str.length() - n);
		String str2 = str.substring(0, str.length() - n);
		return str1 + str2;
	}

	// 右移n位
	public String shr(String str, int n) {
		char[] fillZero = new char[n];
		Arrays.fill(fillZero, '0');
		String str1 = str.substring(0, str.length() - n);
		return new String(fillZero) + str1;
	}

	public String ch(String x, String y, String z) {
		String string1 = and(non(x), z);
		String string2 = and(x, y);
		return xor(string1, string2);
	}

	public String maj(String x, String y, String z) {
		return xor(and(x, y), and(x, z), and(y, z));
	}

	public String big_sigma_zero(String x) {
		return xor(rotr(x, 2), rotr(x, 13), rotr(x, 22));
	}

	public String big_sigma_one(String x) {
		return xor(rotr(x, 6), rotr(x, 11), rotr(x, 25));
	}

	public String small_sigma_zero(String x) {
		return xor(rotr(x, 7), rotr(x, 18), shr(x, 3));
	}

	public String small_sigma_one(String x) {
		return xor(rotr(x, 17), rotr(x, 19), shr(x, 10));
	}

	// 相加
	public String binaryplus(String str1, String str2) {
		char[] cstr = new char[32];
		int flag = 0;
		int temp = 0;
		for (int i = str1.length() - 1; i >= 0; i--) {
			temp = ((str1.charAt(i) - '0') + ((str2.charAt(i) - '0')) + flag);
			cstr[i] = (char) (temp % 2 + '0');
			if (temp >= 2)
				flag = 1;
			else
				flag = 0;
		}
		return new String(cstr);
	}

	// 二进制到十六进制;参数str为一串二进制的数
	public String binaryToHex(String str) {
		int temp = 0;
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < str.length() / 4; i++) {
			temp = Integer.valueOf(str.substring(i * 4, (i + 1) * 4), 2);
			stringBuffer = stringBuffer.append(Integer.toHexString(temp));
		}
		return stringBuffer.toString();
	}

	// 从十六进制到二进制
	public String hexToBinary(String str) {
		StringBuffer stringBuffer = new StringBuffer();
		String string = new String();
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) {
			case '0':string = "0000";break;
			case '1':string = "0001";break;
			case '2':string = "0010";break;
			case '3':string = "0011";break;
			case '4':string = "0100";break;
			case '5':string = "0101";break;
			case '6':string = "0110";break;
			case '7':string = "0111";break;
			case '8':string = "1000";break;
			case '9':string = "1001";break;
			case 'a':string = "1010";break;
			case 'b':string = "1011";break;
			case 'c':string = "1100";break;
			case 'd':string = "1101";break;
			case 'e':string = "1110";break;
			case 'f':string = "1111";break;	
			default:break;	

			}
			stringBuffer = stringBuffer.append(string);
		}
		return stringBuffer.toString();
	}

	// 计算T1//liulang
	public String T1(String str_h, String str_e, String str_ch, String str_w,
			String str_k) {
		String string1 = binaryplus(str_h, big_sigma_one(str_e));
		String string2 = binaryplus(str_ch, str_w);
		String string3 = binaryplus(string1, string2);
		return binaryplus(string3, str_k);
	}

	// 计算T2
	public String T2(String str_a, String str_maj) {
		return binaryplus(big_sigma_zero(str_a), str_maj);
	}

	// 打印出A,B,C,D,E,F,G,H
	public void display(String A, String B, String C, String D, String E,
			String F, String G, String H) {
//		System.out.print("A=" + binaryToHex(A) + "  ");
//		System.out.print("B=" + binaryToHex(B) + "  ");
//		System.out.print("C=" + binaryToHex(C) + "  ");
//		System.out.print("D=" + binaryToHex(D) + "  ");
//		System.out.print("E=" + binaryToHex(E) + "  ");
//		System.out.print("F=" + binaryToHex(F) + "  ");
//		System.out.print("G=" + binaryToHex(G) + "  ");
//		System.out.print("H=" + binaryToHex(H) + "  ");
		//System.out.println();
	}
}
