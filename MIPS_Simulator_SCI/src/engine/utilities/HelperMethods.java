package engine.utilities;

public class HelperMethods {

	public static String insFromXToY(String bits, int x, int y) {

		return bits.substring(32 - y - 1, 32 - y + (y - x));

	}

}
