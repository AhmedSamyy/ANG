package dataPaths;

import java.util.ArrayList;

import registers.Register;
import utilities.Converter;
import utilities.HelperMethods;
import engine.Excuter;
import exceptions.ZeroRegisterOverrideException;

public class RegisterData {

	public ArrayList<Register> registers = new ArrayList<Register>();
	public Register readRegister1;
	public Register readRegister2;
	public Register writeRegister;

	String[] reg = { "$0", "$1", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3",
			"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0",
			"$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9",
			"$k0", "$k1", "$gp", "$sp", "$s8", "$ra" };

	public RegisterData() {
		for (int i = 0; i < 32; i++) {
			registers
					.add(new Register(reg[i], Converter.convertToBinary(0, 32)));
		}
	}

	public int getRegisterIndex(String registerName) {
		for (int i = 0; i < registers.size(); i++) {
			if (registers.get(i).name.equalsIgnoreCase(registerName)) {
				return i;
			}
		}
		return -1;
	}

	public void decodeRegisters(String instruction, int regDstControlSignal,
			int regWriteControlSignal, int jumpToRoutineSignal, Register programCounter) {

		int readRegister1Index = Integer.parseInt(
				HelperMethods.insFromXToY(instruction, 21, 25), 2);
		readRegister1 = registers.get(readRegister1Index);

		int readRegister2Index = Integer.parseInt(
				HelperMethods.insFromXToY(instruction, 16, 20), 2);
		readRegister2 = registers.get(readRegister2Index);

		if (regWriteControlSignal == 1) {
			if (regDstControlSignal == 0) {
				writeRegister = readRegister2;
			} else {
				int writeRegisterIndex = Integer.parseInt(
						HelperMethods.insFromXToY(instruction, 11, 15), 2);
				writeRegister = registers.get(writeRegisterIndex);
			}
		}

		if (jumpToRoutineSignal == 1) {
			registers.get(31).value = Excuter.signExtend(programCounter.value);
		}

	}

	public void writeData(String data) throws ZeroRegisterOverrideException {
		if (writeRegister.name.equals("$0"))
			throw new ZeroRegisterOverrideException(
					"Register $0 can't be overriden");
		else
			writeRegister.value = data;
	}

	public void displayRegisters() {
		for (int i = 0; i < registers.size(); i++) {
			System.out.println(registers.get(i).name + " : "
					+ registers.get(i).value);
		}
	}
}
