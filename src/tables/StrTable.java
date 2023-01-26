package tables;

import java.util.ArrayList;
import java.util.Formatter;

@SuppressWarnings("serial")
public final class StrTable extends ArrayList<String>{
    // @Override
	public int addString(String s) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).equals(s)) {
				return i;
			}
		}
		super.add(s);
		return this.size() - 1; // index da ultima inserção
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("Strings table:\n");
		for (int i = 0; i < this.size(); i++) {
			f.format("Entry %d -- %s\n", i, this.get(i));
		}
		f.close();
		return sb.toString();
	}

}
