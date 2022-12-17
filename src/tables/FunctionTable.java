package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import typing.Type;

public final class FunctionTable {

	// No mundo real isto certamente deveria ser um hash...
	// Implementação da classe não é exatamente Javanesca porque
	// tentei deixar o mais parecido possível com a original em C.
	private List<FunctionEntry> table = new ArrayList<FunctionEntry>();

	public int lookupVar(String s) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).name.equals(s)) {
				return i;
			}
		}
		return -1;
	}

	public int addFunction(String name, int line, List<Type> params, List<Type> returns) {
		FunctionEntry entry = new FunctionEntry(name, line, params, returns);
		int idxAdded = table.size();
		table.add(entry);
		return idxAdded;
	}

	public FunctionEntry get(int i) {
		return table.get(i);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("Functions table:\n");
		for (int i = 0; i < table.size(); i++) {
			FunctionEntry func = get(i);
			f.format("Entry %d -- name: %s, line: %d, params types: %d, returns types: %s\n", i,
	                 func.name, func.line, Type.listToString(func.params), Type.listToString(func.returns));
		}
		f.close();
		return sb.toString();
	}
}
