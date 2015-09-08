package engine.dataPaths;

public class Controler {

	public int regWrite = 0;
	public int memToRegWrite = 0;
	public int memRead = 0;
	public int memWrite = 0;
	public int branch = 0;
	public int branchLessThan = 0;
	public int bne = 0;
	public int jump = 0;
	public int jumpToReturnAddress = 0;
	public int jumpToRoutine = 0;
	public int regDst = 0;
	public int ALUsrc = 0;
	public String ALUop = "xx";

	public void decodeOpcode(String opCode) {

		if (opCode.equals("000000")) { // R-format
			regDst = 1;
			ALUsrc = 0;
			regWrite = 1;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "10";

		} else if (opCode.equals("001000")) { // addi
			regDst = 0;
			ALUsrc = 1;
			regWrite = 1;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "00";

		} else if (opCode.equals("100011")) { // lw
			regDst = 0;
			ALUsrc = 1;
			regWrite = 1;
			memToRegWrite = 1;
			memWrite = 0;
			memRead = 1;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "00";

		} else if (opCode.equals("101011")) { // sw
			regDst = 1;
			ALUsrc = 1;
			regWrite = 0;
			memToRegWrite = 0;
			memWrite = 1;
			memRead = 0;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "00";

		} else if (opCode.equals("000100")) { // beq
			regDst = 1;
			ALUsrc = 0;
			regWrite = 0;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 1;
			branchLessThan = 0;
			bne = 0;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "01";

		} else if (opCode.equals("000101")) { // bne
			regDst = 1;
			ALUsrc = 0;
			regWrite = 0;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 1;
			branchLessThan = 0;
			bne = 1;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "01";

		} else if (opCode.equals("000010")) { // j
			regDst = 0;
			ALUsrc = 0;
			regWrite = 0;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 1;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "11";

		} else if (opCode.equals("000011")) { // jal
			regDst = 0;
			ALUsrc = 0;
			regWrite = 0;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 1;
			jumpToReturnAddress = 0;
			jumpToRoutine = 1;
			ALUop = "11";

		} else if (opCode.equals("001100")) { // jr
			regDst = 0;
			ALUsrc = 0;
			regWrite = 0;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 0;
			branchLessThan = 0;
			bne = 0;
			jump = 1;
			jumpToReturnAddress = 1;
			jumpToRoutine = 0;
			ALUop = "11";

		} else if (opCode.equals("001010")) { // slti
			regDst = 0;
			ALUsrc = 1;
			regWrite = 1;
			memToRegWrite = 0;
			memWrite = 0;
			memRead = 0;
			branch = 0;
			branchLessThan = 1;
			bne = 0;
			jump = 0;
			jumpToReturnAddress = 0;
			jumpToRoutine = 0;
			ALUop = "01";

		}
	}

	public void printControlers() {

		System.out.println("Control signals are :");
		System.out.println("regWrite = " + regWrite);
		System.out.println("memToRegWrite = " + memToRegWrite);
		System.out.println("memRead = " + memRead);
		System.out.println("memWrite = " + memWrite);
		System.out.println("branch = " + branch);
		System.out.println("branchLessThan = " + branchLessThan);
		System.out.println("regDst = " + regDst);
		System.out.println("ALUsrc = " + ALUsrc);
		System.out.println("ALUop = " + ALUop);

	}

	public void reset() {

		regWrite = 0;
		memToRegWrite = 0;
		memRead = 0;
		memWrite = 0;
		branch = 0;
		branchLessThan = 0;
		bne = 0;
		jump = 0;
		jumpToReturnAddress = 0;
		jumpToRoutine = 0;
		regDst = 0;
		ALUsrc = 0;
		ALUop = "xx";

	}

}
