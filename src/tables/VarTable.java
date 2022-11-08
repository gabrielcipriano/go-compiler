package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import typing.Type;
import typing.SpecialType;

public final class VarTable {

	// No mundo real isto certamente deveria ser um hash...
	// Implementação da classe não é exatamente Javanesca porque
	// tentei deixar o mais parecido possível com a original em C.
	private List<Entry> table = new ArrayList<Entry>();

	public int lookupVar(String s) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).name.equals(s)) {
				return i;
			}
		}
		return -1;
	}

	public int addVar(String s, int line, int scope, Type type) {
		Entry entry = new Entry(s, line, scope, type);
		int idxAdded = table.size();
		table.add(entry);
		return idxAdded;
	}

	public int addVar(String s, int line, int scope, Type type, SpecialType special) {
		Entry entry = new Entry(s, line, scope, type, special);
		int idxAdded = table.size();
		table.add(entry);
		return idxAdded;
	}

	public String getName(int i) {
		return table.get(i).name;
	}

	public int getLine(int i) {
		return table.get(i).line;
	}

	public Type getType(int i) {
		return table.get(i).type;
	}

	public int getScope(int i) {
		return table.get(i).scope;
	}

	public SpecialType getSpecialType(int i) {
		return table.get(i).special;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("Variables table:\n");
		for (int i = 0; i < table.size(); i++) {
			String special = getSpecialType(i) != null 
				? getSpecialType(i).toString() 
				: "-";
			f.format("Entry %d -- name: %s, line: %d, scope: %d, type: %s, special: %s\n", i,
	                 getName(i), getLine(i), getScope(i), getType(i).toString(), special);
		}
		f.close();
		return sb.toString();
	}

	private static final class Entry {
		private final String name;
		private final int line;
		private final Type type;
		private final SpecialType special;
		private final int scope;


		Entry(String name, int line, int scope, Type type, SpecialType special) {
			this.name = name;
			this.scope = scope;
			this.line = line;
			this.type = type;
			this.special = special;
		}

		Entry(String name, int line, int scope, Type type) {
			this.name = name;
			this.line = line;
			this.scope = scope;
			this.type = type;
			this.special = null;
		}
	}
}
