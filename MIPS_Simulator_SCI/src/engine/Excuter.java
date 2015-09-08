package engine;

import engine.dataPaths.ALUControler;
import engine.dataPaths.ALUnit;
import engine.dataPaths.Controler;
import engine.dataPaths.Memory;
import engine.dataPaths.RegisterData;
import engine.exceptions.ZeroRegisterOverrideException;
import engine.registers.Register;
import engine.utilities.Converter;
import engine.utilities.HelperMethods;

public class Excuter {

	public ALUnit ALU;
	public Memory memory;
	public RegisterData registers;
	public Controler controler;
	public ALUControler ALUcontroler;
	public Register programCounter;
	public String instruction;
	public String dataReadFromMemory;
	public boolean programEnded;
	public int cyclesCounter;

	public Excuter(String address) {
		ALU = new ALUnit();
		memory = new Memory();
		registers = new RegisterData();
		controler = new Controler();
		ALUcontroler = new ALUControler();
		programCounter = new Register("PC", address);
		instruction = "";
		dataReadFromMemory = "";
		programEnded = false;
		cyclesCounter = 0;
	}

	public void excuteCode() throws ZeroRegisterOverrideException {

		while (!memory.programEnd(programCounter.value)) {
			excuteFirstCycle();
			excuteSecondCycle();
			excuteThirdCycle();
			excuteForthCycle();
			excuteFifthCycle();
		}

	}

	private void excuteFifthCycle() throws ZeroRegisterOverrideException {

		cyclesCounter++;
		System.out.println("Cycle number " + cyclesCounter + " :");

		writeBack();

		System.out.println("PC : " + programCounter.value);
		controler.printControlers();

		System.out.println("Total number of cycles excuted : " + cyclesCounter);

		System.out.println("");
		System.out.println("");
		System.out.println("");

		registers.displayRegisters();

		System.out.println("");
		System.out.println("");
		System.out.println("");

		memory.displayMemory();

		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		ALU.reset();
		controler.reset();
	}

	private void excuteForthCycle() throws NumberFormatException {

		cyclesCounter++;
		System.out.println("Cycle number " + cyclesCounter + " :");

		memoryRead();

		System.out.println("PC : " + programCounter.value);
		controler.printControlers();

		System.out.println("");
		System.out.println("");
		System.out.println("");

	}

	private void excuteThirdCycle() {

		cyclesCounter++;
		System.out.println("Cycle number " + cyclesCounter + " :");

		excute();

		System.out.println("PC : " + programCounter.value);
		controler.printControlers();

		System.out.println("");
		System.out.println("");
		System.out.println("");

	}

	private void excuteSecondCycle() {

		cyclesCounter++;
		System.out.println("Cycle number " + cyclesCounter + " :");

		decode();

		System.out.println("PC : " + programCounter.value);
		controler.printControlers();

		System.out.println("");
		System.out.println("");
		System.out.println("");

	}

	private void excuteFirstCycle() throws NumberFormatException {

		cyclesCounter++;
		System.out.println("Cycle number " + cyclesCounter + " :");

		instruction = fetch(programCounter.value);
		incrementPc();

		System.out.println("the fetched instruction is : " + instruction);
		System.out.println("new PC : " + programCounter.value);
		controler.printControlers();

		System.out.println("");
		System.out.println("");
		System.out.println("");

	}

	private String fetch(String instructionAddress) {

		return memory.loadWord(instructionAddress);

	}

	private void decode() {

		controler.decodeOpcode(HelperMethods.insFromXToY(instruction, 26, 31));

		if (controler.jumpToReturnAddress == 1) {
			programCounter.value = registers.registers.get(31).value.substring(24);
		}

		registers.decodeRegisters(instruction, controler.regDst,
				controler.regWrite, controler.jumpToRoutine, programCounter);

	}

	private void excute() {

		if (controler.ALUsrc == 0) {
			ALU.excuteOperation(ALUcontroler.ALUControlerOutput(
					controler.ALUop,
					HelperMethods.insFromXToY(instruction, 0, 5), controler),
					HelperMethods.insFromXToY(instruction, 6, 10),
					registers.readRegister1, registers.readRegister2.value);

		} else {
			ALU.excuteOperation(ALUcontroler.ALUControlerOutput(
					controler.ALUop,
					HelperMethods.insFromXToY(instruction, 0, 5), controler),
					HelperMethods.insFromXToY(instruction, 6, 10),
					registers.readRegister1, signExtend(HelperMethods
							.insFromXToY(instruction, 0, 15)));
		}

	}

	private void memoryRead() {

		if (controler.branch == 1) {
			if (controler.bne == 1 && !ALU.zero) {
				programCounter.value = Converter
						.convertToBinary(
								Integer.parseInt(programCounter.value, 2)
										- (Integer.parseInt(
												programCounter.value, 2) - Integer
												.parseInt(
														signExtend(HelperMethods
																.insFromXToY(
																		instruction,
																		0, 15)),
														2)), 8);
			} else if (ALU.zero && controler.bne != 1) {
				programCounter.value = Converter
						.convertToBinary(
								Integer.parseInt(programCounter.value, 2)
										- (Integer.parseInt(
												programCounter.value, 2) - Integer
												.parseInt(
														signExtend(HelperMethods
																.insFromXToY(
																		instruction,
																		0, 15)),
														2)), 8);
			}
		}

		if (controler.branchLessThan == 1) {
			if (ALU.lessThan) {
				ALU.ALUResult = 1;
			} else {
				ALU.ALUResult = 0;
			}
		}

		if (controler.jump == 1) {
			if (controler.jumpToReturnAddress != 1) {
				programCounter.value = Converter
						.convertToBinary(
								Integer.parseInt(programCounter.value, 2)
										- (Integer.parseInt(
												programCounter.value, 2) - Integer
												.parseInt(HelperMethods
														.insFromXToY(
																instruction, 0,
																25), 2)), 8);
			}
		}

		if (controler.memRead == 1) {
			dataReadFromMemory = memory.loadWord(Converter.convertToBinary(
					ALU.ALUResult, 8));
		}

		if (controler.memWrite == 1) {
			memory.storeWord(Converter.convertToBinary(ALU.ALUResult, 8),
					registers.readRegister2.value);
		}
	}

	private void writeBack() throws ZeroRegisterOverrideException {
		if (controler.regWrite == 1) {
			if (controler.memToRegWrite == 0) {
				registers.writeData(Converter
						.convertToBinary(ALU.ALUResult, 32));
			} else {
				registers.writeData(dataReadFromMemory);
			}
		}
	}

	private void incrementPc() throws NumberFormatException {
		programCounter.value = Converter.convertToBinary(
				Integer.parseInt(programCounter.value, 2) + 4, 8);

	}

	public static String signExtend(String extendingValue) {
		char firstBit = extendingValue.charAt(0);
		String result = "";

		for (int i = 0; i < 32 - extendingValue.length(); i++) {
			result += firstBit;
		}

		return result += extendingValue;
	}
}
