package dataPaths;

import java.util.ArrayList;

import utilities.Converter;
import utilities.HelperMethods;
import utilities.MemoryCell;

public class Memory {

	public ArrayList<MemoryCell> memory = new ArrayList<MemoryCell>();

	public Memory() {

		for (int i = 0; i < 200; i++) {
			memory.add(new MemoryCell(Converter.convertToBinary(i, 8),
					Converter.convertToBinary(0, 8), false));
		}
	}

	public int locateAddress(String address) {
		for (int i = 0; i < memory.size(); i++) {
			if (memory.get(i).address.equals(address)) {
				return i;
			}
		}
		return -1;
	}

	public boolean programEnd(String adress) {
		return memory.get(locateAddress(adress)).end;
	}

	public String loadWord(String address) {
		int memoryIndex = locateAddress(address);
		String byteOfData = memory.get(memoryIndex).data;
		String resultedWord = byteOfData;

		for (int i = 0; i < 3; i++) {

			memoryIndex++;
			byteOfData = memory.get(memoryIndex).data;
			resultedWord += byteOfData;

		}
		return resultedWord;
	}

	public void storeWord(String address, String memWriteData) {
		int memoryIndex = locateAddress(address);
		memory.get(memoryIndex).data = HelperMethods.insFromXToY(memWriteData,
				24, 31);

		memoryIndex++;
		memory.get(memoryIndex).data = HelperMethods.insFromXToY(memWriteData,
				16, 23);

		memoryIndex++;
		memory.get(memoryIndex).data = HelperMethods.insFromXToY(memWriteData,
				8, 15);

		memoryIndex++;
		memory.get(memoryIndex).data = HelperMethods.insFromXToY(memWriteData,
				0, 7);
	}

	public void displayMemory() {

		System.out.println("Memory content :");

		for (int i = 0; i < 200; i++) {
			System.out.println("Address " + memory.get(i).address + " : "
					+ memory.get(i).data);
		}
	}

}
