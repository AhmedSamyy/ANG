package engine.dataPaths;

import engine.Excuter;
import engine.registers.Register;

public class ALUnit {

	public int ALUResult = 0;
	public boolean zero = false;
	public boolean lessThan = false;

	public void excuteOperation(String ALUcontrol, String shift, Register arg1,
			String arg2) {

		int arg1Value = Integer.parseUnsignedInt(
				Excuter.signExtend(arg1.value), 2);
		int arg2Value = Integer.parseUnsignedInt(Excuter.signExtend(arg2), 2);
		int shiftValue = Integer.parseInt(shift, 2);

		if (ALUcontrol.equals("0010")) {

			ALUResult = add(arg1Value, arg2Value);

		} else if (ALUcontrol.equals("0110")) {

			ALUResult = subtract(arg1Value, arg2Value);
			if (ALUResult == 0)
				zero = true;
			if (ALUResult < 0)
				lessThan = true;

		} else if (ALUcontrol.equals("1000")) {

			ALUResult = shiftLeft(arg1Value, shiftValue);

		} else if (ALUcontrol.equals("1001")) {

			ALUResult = shiftRight(arg1Value, shiftValue);

		} else if (ALUcontrol.equals("1101")) {

			ALUResult = nor(arg1Value, arg2Value);

		} else {

			ALUResult = 0;
		}

	}

	private int nor(int arg1Value, int arg2Value) {
		return ~(arg1Value | arg2Value);
	}

	private int shiftRight(int arg1Value, int shift) {
		return arg1Value / (int) (Math.pow(2, shift));
	}

	private int shiftLeft(int arg1Value, int shift) {
		return arg1Value * (int) (Math.pow(2, shift));
	}

	private int subtract(int arg1Value, int arg2Value) {
		return arg1Value - arg2Value;
	}

	private int add(int arg1Value, int arg2Value) {
		return arg1Value + arg2Value;
	}
	
	public void reset() {
		ALUResult = 0;
		zero = false;
		lessThan = false;
	}
}
