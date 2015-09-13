package dataPaths;

public class ALUControler {
	
	public String ALUControlerOutput(String ALUOp, String funcCode, Controler controler) {
		
		if (ALUOp.equals("00")) {
			
			return "0010";
			
		} else if (ALUOp.equals("01")) {
			
			return "0110";
			
		} else if (ALUOp.equals("11")) {
			
			return "";
			
		} else if (ALUOp.equals("10") && funcCode.equals("100000")) {
			
			return "0010";
			
		} else if (ALUOp.equals("10") && funcCode.equals("100010")) {
			
			return "0110";
			
		} else if (ALUOp.equals("10") && funcCode.equals("000000")) {
			
			return "1000";
			
		} else if (ALUOp.equals("10") && funcCode.equals("000010")) {
			
			return "1001";
			
		} else if (ALUOp.equals("10") && funcCode.equals("100111")) {
			
			return "1101";
			
		} else if (ALUOp.equals("10") && funcCode.equals("001000")) {
			
			return "1111";
			
		} else if (ALUOp.equals("10") && funcCode.equals("101010")) {
			
			controler.branchLessThan = 1;
			return "0110";
			
		} else {
			
			return "";
			
		}
		
	}

}
