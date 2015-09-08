package engine.utilities;

public class MemoryCell {

	public String address;
	public String data;
	public boolean end;

	public MemoryCell(String address, String data, boolean end) {
		this.address = address;
		this.data = data;
		this.end = end;
	}
}
