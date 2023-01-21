package code;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Scanner;

import tables.StrTable;

public class Io {
  private final DataStack stack;
  private final Memory memory;
	private final StrTable st;
  private final Scanner in; // Para leitura de stdin

	public Io(DataStack stack, Memory mem, StrTable st) {
		this.stack = stack;
    this.memory = mem;
		this.st = st;		
    this.in = new Scanner(System.in);
	}

  public Void close() {
		in.close(); // fecha leitura de stdin.
    return null;
  }

  // *** Write ***

	protected Void writeInt() {
		System.out.println(stack.popi());
		return null;
	}

	protected Void writeReal() {
		System.out.println(stack.popf());
		return null;
	}

	protected Void writeBool() {
		System.out.println(stack.popb());
		return null;
	}

	protected Void writeStr() {
		int strIdx = stack.popi(); // String pointer
		String originalStr = st.get(strIdx);
		String unescapedStr = unescapeStr(originalStr);
		System.out.print(unescapedStr);
		return null;
	}


	// *** Read ***

	protected Void readInt(int varIdx) {
		System.out.printf("read (int): ");
		int value = in.nextInt();
		memory.storei(varIdx, value);
		return null; // Java exige um valor de retorno mesmo para Void... :/
	}

	protected Void readReal(int varIdx) {
		System.out.printf("read (real): ");
		float value = in.nextFloat();
		memory.storef(varIdx, value);
		return null; // Java exige um valor de retorno mesmo para Void... :/
	}

	protected Void readBool(int varIdx) {
		int value;
	    do {
	        System.out.printf("read (bool - 0 = false, 1 = true): ");
	        value = in.nextInt();
	    } while (value != 0 && value != 1);
	    memory.storeb(varIdx, value == 1);
	    return null; // Java exige um valor de retorno mesmo para Void... :/
	}

	protected Void readStr(int varIdx) {
		System.out.printf("read (str): ");
		String s = in.next();
		int strIdx = st.addString(s);
		memory.storei(varIdx, strIdx);
		return null; // Java exige um valor de retorno mesmo para Void... :/
	}


	// Função auxiliar para converter a string com escapes.
	// Há várias formas de se fazer isso em Java mas preferi
	// deixar assim para não precisar de bibliotecas ou de uma
	// versão do Java mais recente.
	// Se você preferir, pode usar:
	// org.apache.commons.lang.StringEscapeUtils.unescapeJava()
	// ou
	// String.translateEscapes(), disponível a partir do Java 15.
	private String unescapeStr(String originalStr) {
		StreamTokenizer parser = new StreamTokenizer(new StringReader(originalStr));
		String unescapedStr = "";
		try {
      parser.nextToken();
      if (parser.ttype == '"') {
        unescapedStr = parser.sval;
      } else {
      unescapedStr = "ERROR at string conversion!";
      }
		} catch (IOException e) {
		  e.printStackTrace();
		}
		return unescapedStr;
	}

}
