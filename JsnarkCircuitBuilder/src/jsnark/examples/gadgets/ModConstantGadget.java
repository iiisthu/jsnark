/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package jsnark.examples.gadgets;

import java.math.BigInteger;

import jsnark.circuit.eval.CircuitEvaluator;
import jsnark.circuit.eval.Instruction;
import jsnark.circuit.operations.Gadget;
import jsnark.circuit.structure.Wire;

/**
 * This gadget provides the remainder of a % b, where b is a circuit constant.
 *
 *
 */

public class ModConstantGadget extends Gadget {

	private Wire a;
	private BigInteger b;
	private Wire r;
	private Wire q;

	private int bitwidth; // a's bitwidth

	public ModConstantGadget(Wire a, int bitwidth, BigInteger b, String...desc) {
		super(desc);
		this.a = a;
		this.b = b;
		this.bitwidth = bitwidth;
		if(b.signum() != 1){
			throw new IllegalArgumentException("b must be a positive constant. Signed operations not supported yet.");
		}
		if(bitwidth < b.bitLength()){
			throw new IllegalArgumentException("a's bitwidth < b's bitwidth -- This gadget is not needed.");
		}
		// TODO: add further checks.
		
		buildCircuit();
	}

	private void buildCircuit() {
		
		r = generator.createProverWitnessWire("mod result");
		q = generator.createProverWitnessWire("division result");

		generator.specifyProverWitnessComputation(new Instruction() {
			@Override
			public void evaluate(CircuitEvaluator evaluator) {
				BigInteger aValue = evaluator.getWireValue(a);
				BigInteger rValue = aValue.mod(b);
				evaluator.setWireValue(r, rValue);
				BigInteger qValue = aValue.divide(b);
				evaluator.setWireValue(q, qValue);
			}

		});
		
		int bBitwidth = b.bitLength();
		r.restrictBitLength(bBitwidth);
		q.restrictBitLength(bitwidth - bBitwidth + 1);
		generator.addOneAssertion(r.isLessThan(b, bBitwidth));
		generator.addEqualityAssertion(q.mul(b).add(r), a);
	}

	@Override
	public Wire[] getOutputWires() {
		return new Wire[] { r };
	}

}
