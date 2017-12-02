/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package jsnark.circuit.operations.primitive;

import java.math.BigInteger;

import jsnark.circuit.config.Config;
import jsnark.circuit.structure.Wire;

public class AddBasicOp extends BasicOp {

	public AddBasicOp(Wire[] ws, Wire output, String...desc) {
		super(ws, new Wire[] { output }, desc);
		opcode = "add";
	}

	@Override
	public void compute(BigInteger[] assignment) {
		BigInteger s = BigInteger.ZERO;
		for (Wire w : inputs) {
			s = s.add(assignment[w.getWireId()]);
		}
		assignment[outputs[0].getWireId()] = s.mod(Config.FIELD_PRIME);
	}


}