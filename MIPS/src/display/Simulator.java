package display;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import engine.Excuter;
import exceptions.ZeroRegisterOverrideException;
import utilities.Converter;

public class Simulator {

	Excuter excuter;
	String startAddress;
	ArrayList<String> lines = new ArrayList<String>();
	ArrayList<String> branchLabels = new ArrayList<String>();

	public Simulator() {
		loadFile();
		excuter = new Excuter(startAddress);
		scanForLabels();
		removeLabels();
		storeInstructionsInMemory();
	}

	private void removeLabels() {

		for (int i = 0; i < lines.size(); i++) {
			String firstOfLine = lines.get(i).split(" ")[0];
			if (firstOfLine.charAt(firstOfLine.length() - 1) == ':') {
				if (lines.get(i).split(" ").length > 1) {
					lines.set(
							i,
							lines.get(i).substring(
									lines.get(i).indexOf(':') + 2));

				}
			}
		}
	}

	private void scanForLabels() {

		for (int i = 0; i < lines.size(); i++) {
			String firstOfLine = lines.get(i).split(" ")[0];
			if (firstOfLine.charAt(firstOfLine.length() - 1) == ':') {
				if (lines.get(i).split(" ").length > 1) {
					branchLabels
							.add((i * 4)
									+ Integer.parseInt(startAddress, 2)
									+ "/"
									+ firstOfLine.substring(0,
											firstOfLine.length() - 1));
				} else {
					branchLabels
							.add(((i + 1) * 4)
									+ Integer.parseInt(startAddress, 2)
									+ "/"
									+ firstOfLine.substring(0,
											firstOfLine.length() - 1));
				}
			}
		}
	}

	private void storeInstructionsInMemory() {

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).split(" ")[0].charAt(lines.get(i).split(" ")[0]
					.length() - 1) != ':') {
				String result = "";
				String firstOfLine = lines.get(i).split(" ")[0];
				switch (firstOfLine) {

				case "add":
					result += "000000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[3]);
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += "00000" + "100000";
					break;
				case "sub":
					result += "000000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[3]);
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += "00000" + "100010";
					break;
				case "sll":
					result += "000000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += "00000";
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += encode(lines.get(i).split(" ")[3]);
					result += "000000";
					break;
				case "srl":
					result += "000000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += "00000";
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += encode(lines.get(i).split(" ")[3]);
					result += "000010";
					break;
				case "nor":
					result += "000000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[3]);
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += "00000" + "100111";
					break;
				case "addi":
					result += "001000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += Converter.convertToBinary(
							Integer.parseInt(lines.get(i).split(" ")[3]), 16);
					break;
				case "lw":
					result += "100011";
					result += encode(lines.get(i).split(" ")[2].substring(2,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += "0000000000000000";
					break;
				case "sw":
					result += "101011";
					result += encode(lines.get(i).split(" ")[2].substring(2,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += "0000000000000000";
					break;
				case "beq":
					result += "000100";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += Converter.convertToBinary(
							lookForLabel(lines.get(i).split(" ")[3]), 16);
					break;
				case "bne":
					result += "000101";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += Converter.convertToBinary(
							lookForLabel(lines.get(i).split(" ")[3]), 16);
					break;
				case "j":
					result += "000010";
					result += Converter.convertToBinary(
							lookForLabel(lines.get(i).split(" ")[1]), 26);
					break;
				case "jal":
					result += "000011";
					result += Converter.convertToBinary(
							lookForLabel(lines.get(i).split(" ")[1]), 26);
					break;
				case "jr":
					result += "001100";
					result += Converter.convertToBinary(31, 26);
					break;
				case "slt":
					result += "000000";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[3]);
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += "00000" + "101010";
					break;
				//case "slti" :

				default:
					result += "001010";
					result += encode(lines.get(i).split(" ")[2].substring(0,
							lines.get(i).split(" ")[2].length() - 1));
					result += encode(lines.get(i).split(" ")[1].substring(0,
							lines.get(i).split(" ")[1].length() - 1));
					result += Converter.convertToBinary(
							Integer.parseInt(lines.get(i).split(" ")[3]), 16);
				}

				excuter.memory.storeWord(startAddress, result);
				startAddress = Converter.convertToBinary(
						Integer.parseInt(startAddress, 2) + 4, 8);

			} else {

				excuter.memory.memory.get(excuter.memory
						.locateAddress(startAddress)).end = true;
				startAddress = Converter.convertToBinary(
						Integer.parseInt(startAddress, 2) + 4, 8);
			}
		}
	}

	private int lookForLabel(String label) {

		for (int i = 0; i < branchLabels.size(); i++) {
			if (branchLabels.get(i)
					.substring(branchLabels.get(i).indexOf("/") + 1)
					.equals(label))
				return Integer.parseInt(branchLabels.get(i).substring(0,
						branchLabels.get(i).indexOf("/")));
		}
		return -1;
	}

	private String encode(String register) {

		switch (register) {

		case "$0":
			return Converter.convertToBinary(0, 5);
		case "$1":
			return Converter.convertToBinary(1, 5);
		case "$v0":
			return Converter.convertToBinary(2, 5);
		case "$v1":
			return Converter.convertToBinary(3, 5);
		case "$a0":
			return Converter.convertToBinary(4, 5);
		case "$a1":
			return Converter.convertToBinary(5, 5);
		case "$a2":
			return Converter.convertToBinary(6, 5);
		case "$a3":
			return Converter.convertToBinary(7, 5);
		case "$t0":
			return Converter.convertToBinary(8, 5);
		case "$t1":
			return Converter.convertToBinary(9, 5);
		case "$t2":
			return Converter.convertToBinary(10, 5);
		case "$t3":
			return Converter.convertToBinary(11, 5);
		case "$t4":
			return Converter.convertToBinary(12, 5);
		case "$t5":
			return Converter.convertToBinary(13, 5);
		case "$t6":
			return Converter.convertToBinary(14, 5);
		case "$t7":
			return Converter.convertToBinary(15, 5);
		case "$s0":
			return Converter.convertToBinary(16, 5);
		case "$s1":
			return Converter.convertToBinary(17, 5);
		case "$s2":
			return Converter.convertToBinary(18, 5);
		case "$s3":
			return Converter.convertToBinary(19, 5);
		case "$s4":
			return Converter.convertToBinary(20, 5);
		case "$s5":
			return Converter.convertToBinary(21, 5);
		case "$s6":
			return Converter.convertToBinary(22, 5);
		case "$s7":
			return Converter.convertToBinary(23, 5);
		case "$t8":
			return Converter.convertToBinary(24, 5);
		case "$t9":
			return Converter.convertToBinary(25, 5);
		case "$k0":
			return Converter.convertToBinary(26, 5);
		case "$k1":
			return Converter.convertToBinary(27, 5);
		case "$gp":
			return Converter.convertToBinary(28, 5);
		case "$sp":
			return Converter.convertToBinary(29, 5);
		case "$s8":
			return Converter.convertToBinary(30, 5);
		case "$ra":
			return Converter.convertToBinary(31, 5);
		default:
			return Converter.convertToBinary(Integer.parseInt(register), 5);
		}
	}

	public void loadFile() {
		System.out.println("Enter the file that you want to run");
		try {
			@SuppressWarnings("resource")
			Scanner sc=new Scanner(System.in);
			String x = sc.nextLine();
			FileReader a=new FileReader(x);
			BufferedReader br = new BufferedReader(a);
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i == 0) {
					startAddress = line;
				} else {
					if (line.length() > 1) {
						System.out.println(line);
						lines.add(line);
					}
				}
				i++;
			}
			System.out.println("");
			System.out.println("");
			System.out.println("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws ZeroRegisterOverrideException {
		Simulator s = new Simulator();
		s.excuter.excuteCode();
	}

}
