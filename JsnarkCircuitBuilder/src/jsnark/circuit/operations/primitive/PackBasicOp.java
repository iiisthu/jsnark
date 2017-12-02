/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package jsnark.circuit.operations.primitive;

import java.math.BigInteger;

import jsnark.util.Util;
import jsnark.circuit.config.Config;
import jsnark.circuit.structure.Wire;

public class PackBasicOp extends BasicOp {

	public PackBasicOp(Wire[] inBits, Wire out, String...desc) {
		super(inBits, new Wire[] { out }, desc);
		opcode = "pack";
		numMulGates = 1;
	}

	@Override
	public void checkInputs(BigInteger[] assignment) {
		super.checkInputs(assignment);
		boolean check = true;
		for(int i = 0; i < inputs.length; i++){
			check &= Util.isBinary(assignment[inputs[i].getWireId()]);
		}
		if (!check){
			System.err.println("Error - Input(s) to Pack are not binary. "
					+ this);
			throw new RuntimeException("Error During Evaluation");

		}
	}
	
	@Override
	public void compute(BigInteger[] assignment) {
		BigInteger sum = BigInteger.ZERO;
		for (int i = 0; i < inputs.length; i++) {
			sum = sum.add(assignment[inputs[i].getWireId()].multiply(
					new BigInteger("2").pow(i)));
		}
		assignment[outputs[0].getWireId()]= sum.mod(Config.FIELD_PRIME);
	}



}