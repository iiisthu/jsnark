/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package jsnark.examples.generators;

import jsnark.circuit.eval.CircuitEvaluator;
import jsnark.circuit.structure.CircuitGenerator;
import jsnark.circuit.structure.Wire;
import jsnark.examples.gadgets.FieldDivisionGadget;


public class FieldDivisionCircuitGenerator extends CircuitGenerator {

	private Wire[] a;
	private Wire[] b;
	private FieldDivisionGadget[] gadgets;
	private int gatesNum;
	
	public FieldDivisionCircuitGenerator(String circuitName, int gatesNum) {
		super(circuitName);
		this.gatesNum = gatesNum;
	}

	@Override
	protected void buildCircuit() {

		a = createInputWireArray(gatesNum, "Input a");
		b = createInputWireArray(gatesNum, "Input b");
		gadgets = new FieldDivisionGadget[gatesNum];
		for(int i = 0; i < gatesNum; i++){
			gadgets[i] = new FieldDivisionGadget(a[i], b[i], "Divison Gagdet#" + i); 
			Wire[] result = gadgets[i].getOutputWires();
			makeOutput(result[0], "Output of gate # "+i);
		}
	}

	@Override
	public void generateSampleInput(CircuitEvaluator circuitEvaluator) {		
		for (int i = 0; i < gatesNum; i++) {
			circuitEvaluator.setWireValue(a[i], 10+i);
			circuitEvaluator.setWireValue(b[i], 20+i);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		FieldDivisionCircuitGenerator generator = new FieldDivisionCircuitGenerator("division", 100);
		generator.generateCircuit();
		generator.evalCircuit();
		generator.prepFiles();
		generator.runLibsnark();	
	}
}
