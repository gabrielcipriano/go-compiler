package code;

import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import tables.StrTable;
import tables.VarTable;

public class Cpu {
	private final DataStack stack;
	private final Memory memory;
	private final StrTable st;
	private final VarTable vt;

	public Cpu(DataStack stack, Memory mem, StrTable st, VarTable vt) {
		this.stack = stack;
		this.memory = mem;
		this.st = st;
		this.vt = vt;
	}

	void doIntUnaryOperation(UnaryOperator<Integer> operation) {
		// remember, remember, the reverse polish notation
		int value = stack.popi();
		stack.push(operation.apply(value));
	}

	void doIntOperation(BinaryOperator<Integer> operation) {
		// remember, remember, the reverse polish notation
		int b = stack.popi();
		int a = stack.popi();
		stack.push(operation.apply(a,b));
	}

	void doIntPredicate(BiPredicate<Integer, Integer> predicate) {
		// remember, remember, the reverse polish notation
		int b = stack.popi();
		int a = stack.popi();
		stack.push(predicate.test(a,b));
	}

	void doFloatOperation(BinaryOperator<Float> operation) {
		// remember, remember, the reverse polish notation
		float b = stack.popf();
		float a = stack.popf();
		stack.push(operation.apply(a,b));
	}

	void doFloatPredicate(BiPredicate<Float, Float> predicate) {
		// remember, remember, the reverse polish notation
		float b = stack.popf();
		float a = stack.popf();
		stack.push(predicate.test(a,b));
	}

	void doBoolPredicate(BiPredicate<Boolean, Boolean> predicate) {
		// remember, remember, the reverse polish notation
		boolean b = stack.popb();
		boolean a = stack.popb();
		stack.push(predicate.test(a,b));
	}

	void doBoolSimplePredicate(Predicate<Boolean> predicate) {
		// remember, remember, the reverse polish notation
		boolean value = stack.popb();
		stack.push(predicate.test(value));
	}

  protected Void doStrPredicate(BiPredicate<String, String> predicate) {
		String b = st.get(stack.popi());
		String a = st.get(stack.popi());
		
		stack.push(predicate.test(a,b));
		return null;
	}

  protected Void doStrOperation(BinaryOperator<String> operation) {
		String b = st.get(stack.popi());
		String a = st.get(stack.popi());
		int idx = st.addString(operation.apply(a, b));
		stack.push(idx);
		return null;
	}

}
