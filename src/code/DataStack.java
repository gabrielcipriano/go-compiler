package code;

import java.util.Formatter;
import java.util.Stack;

/*
 * Implementação de uma pilha de words, com os métodos
 * que facilitam empilhar ou desempilhar um valor
 * inteiro ou real.
 */
@SuppressWarnings("serial")
public final class DataStack extends Stack<Word> {

	public void push(int value) {
		super.push(Word.fromInt(value));
	}

	public void push(float value) {
		super.push(Word.fromFloat(value));
	}

	public void push(boolean value) {
		super.push(Word.fromBool(value));
	}

	public int popi() {
		return super.pop().toInt();
	}

	public float popf() {
		return super.pop().toFloat();
	}

	public boolean popb() {
		return super.pop().toBool();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("*** STACK: ");
		for (int i = 0; i < this.size(); i++) {
			f.format("%d ", this.get(i).toInt());
		}
		f.format("\n");
		f.close();
		return sb.toString();
	}
	
}
