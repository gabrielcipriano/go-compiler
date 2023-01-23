package code;

import java.util.Formatter;
import java.util.Vector;

import tables.VarTable;

/*
 * Implementação de uma memória (vetor) de words,
 * com os métodos que facilitam acessar um valor
 * inteiro ou real.
 */
@SuppressWarnings("serial")
public class Memory extends Vector<Word> {
	private final VarTable vt;

	// Cria a memória do tamanho das tabela de variáveis.
	// O índice na tabela é o "endereço" na memória.
	public Memory(VarTable vt) {
		this.vt = vt;

		for (int i = 0; i < vt.size(); i++) {
			this.add(Word.fromInt(0));
		}
	}

	// No mundo real esses métodos precisam de verificações de erros.
	
	public void storei(int addr, int value) {
		this.set(addr, Word.fromInt(value));
	}
	
	public int loadi(int addr) {
		return this.get(addr).toInt();
	}
	
	public void storef(int addr, float value) {
		this.set(addr, Word.fromFloat(value));
	}
	
	public float loadf(int addr) {
		return this.get(addr).toFloat();
	}

	public void storeb(int addr, boolean value) {
		this.set(addr, Word.fromBool(value));
	}
	
	public boolean loadb(int addr) {
		return this.get(addr).toBool();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("*** Memory: \n");
		f.format("%4s %10s %5s\n", "line", "name", "value");
		for (int i = 0; i < this.size(); i++) {
			boolean isVar = i < this.vt.size();
			f.format("%4d %10s %5d\n", isVar ? this.vt.get(i).line : 0, isVar ? this.vt.get(i).name : "", this.get(i).toInt());
		}
		f.format("\n");
		f.close();
		return sb.toString();
	}
	
}
