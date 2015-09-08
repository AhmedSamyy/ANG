package engine.utilities;

public class Converter {

	public static String convertToBinary(int value, int length) {

		String result = Integer.toBinaryString(value);
		int diff = length - result.length();

		if (diff < 0) {

			result = result.substring(result.length() - length);

		} else {

			for (int i = 0; i < diff; i++) {
				result = "0" + result;
			}
		}

		return result;
	}

}
