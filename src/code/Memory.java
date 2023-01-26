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

	// Cria a memória do tamanho das tabela de variáveis.
	// O índice na tabela é o "endereço" na memória.
	public Memory() {
	}

	public void add(int value) {
		this.add(Word.fromInt(0));
	}

	public void store(int addr, Word word) {
		this.set(addr, word);
	}
	
	public void store(int addr, boolean value) {
		this.set(addr, Word.fromBool(value));
	}

	public void store(int addr, int value) {
		this.set(addr, Word.fromInt(value));
	}

	public void store(int addr, float value) {
		this.set(addr, Word.fromFloat(value));
	}
	
	public int loadi(int addr) {
		return this.get(addr).toInt();
	}
	
	public float loadf(int addr) {
		return this.get(addr).toFloat();
	}
	
	public boolean loadb(int addr) {
		return this.get(addr).toBool();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("*** Memory: \n");
		f.format("%4s %5s\n", "idx", "value");
		for (int i = 0; i < this.size(); i++) {
			f.format("%4d %5d\n",  i , this.get(i).toInt());
		}
		f.format("\n");
		f.close();
		return sb.toString();
	}
	
}
