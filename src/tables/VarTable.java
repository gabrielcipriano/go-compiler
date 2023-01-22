package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import typing.Type;

public final class VarTable {

	// No mundo real isto certamente deveria ser um hash...
	// Implementação da classe não é exatamente Javanesca porque
	// tentei deixar o mais parecido possível com a original em C.
	private List<VarEntry> table = new ArrayList<VarEntry>();


	/**
	 * 
	 * @deprecated Use lookupVar(String s, int scope) because we can have vars with same name
	 */
	@Deprecated
	public int lookupVar(String s) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).name.equals(s)) {
				return i;
			}
		}
		return -1;
	}

	public int lookupVar(String s, int scope) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).scope == scope && table.get(i).name.equals(s)) {
				return i;
			}
		}
		return -1;
	}
	public int addVar(VarEntry varEntry) {
		int idxAdded = table.size();
		table.add(varEntry);
		return idxAdded;
	}

	public VarEntry get(int i) {
		return table.get(i);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("Variables table:\n");
		for (int i = 0; i < table.size(); i++) {
			VarEntry v = get(i);
			String isArray = v.isArray() ? "[" + v.arraySz + "]" : "";
			f.format("Entry %d -- name: %s, line: %d, scope: %d, type: %s%s\n", i,
	                 v.name, v.line, v.scope, isArray, v.type.toString());
		}
		f.close();
		return sb.toString();
	}

	public int size() {
		return table.size();
	}
}
