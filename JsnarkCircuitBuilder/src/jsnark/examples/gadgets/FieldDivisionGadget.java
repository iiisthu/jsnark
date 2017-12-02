/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package jsnark.examples.gadgets;

import java.math.BigInteger;

import jsnark.circuit.config.Config;
import jsnark.circuit.eval.CircuitEvaluator;
import jsnark.circuit.eval.Instruction;
import jsnark.circuit.operations.Gadget;
import jsnark.circuit.structure.Wire;

public class FieldDivisionGadget extends Gadget {

	private Wire a;
	private Wire b;
	private Wire c;

	public FieldDivisionGadget(Wire a, Wire b, String... desc) {
		super(desc);
		this.a = a;
		this.b = b;
		buildCircuit();
	}

	private void buildCircuit() {

		c = generator.createProverWitnessWire(debugStr("division result"));
		generator.specifyProverWitnessComputation(new Instruction() {
			@Override
			public void evaluate(CircuitEvaluator evaluator) {
				BigInteger aValue = evaluator.getWireValue(a);
				BigInteger bValue = evaluator.getWireValue(b);
				BigInteger cValue = aValue.multiply(bValue.modInverse(Config.FIELD_PRIME)).mod(Config.FIELD_PRIME);
				evaluator.setWireValue(c, cValue);
			}

		});
		generator.addAssertion(b, c, a, debugStr("Assertion for division result"));
	}

	@Override
	public Wire[] getOutputWires() {
		return new Wire[] { c };
	}

}
