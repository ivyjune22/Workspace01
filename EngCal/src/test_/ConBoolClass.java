package test_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import var.VariableDTO;

public class ConBoolClass {
	EngCalClass ecc = new EngCalClass();
	List<String> con_op = Arrays.asList(">", "<", "=", "!", "]", "[");
	
	public List<Boolean> condition_to_bool(String formula, List<VariableDTO> var_list) {
		return condition_statement(conForm_to_LRForm(formula), getConOp(formula), var_list);
	}
	
	public List<String> conForm_to_LRForm(String formula) { 
		List<String> lr_formula = new ArrayList<String>();
		int conOpIndex = 0;
		
		formula = formula.replaceAll("\\s", "").replace(">=", "]").replace("<=", "[").replace("!=", "!");
		
		for(int i=0; i<formula.length(); i++) {
			if(con_op.contains(String.valueOf(formula.charAt(i)))) {
				conOpIndex = i;
			}
		}
		
		String left_formula = formula.substring(0, conOpIndex);
		String right_formula = formula.substring(conOpIndex+1, formula.length());
		
		lr_formula.add(left_formula);
		lr_formula.add(right_formula);
		
		return lr_formula;
	}
		
	private char getConOp(String formula) {
		char conOp = 'E';
		
		for(int i=0; i<formula.length(); i++) {
			if(con_op.contains(String.valueOf(formula.charAt(i)))) {
				conOp = formula.charAt(i);
			}
		}
		
		return conOp;
	}
	
	private List<Boolean> condition_statement(List<String> lr_formula, char conOp, List<VariableDTO> var_list) {
		List<Boolean> result = new ArrayList<Boolean>();
		
		List<Double> left_result = ecc.string_cal(lr_formula.get(0), var_list);
		List<Double> right_result = ecc.string_cal(lr_formula.get(1), var_list);
		
		if(left_result.size() < right_result.size()) {
			for(int i=1; i<right_result.size(); i++) {
				left_result.add(left_result.get(0));
			}
		} else if(left_result.size() > right_result.size()) {
			for(int i=1; i<left_result.size(); i++) {
				right_result.add(right_result.get(0));
			}
		}
		
		for(int i=0; i<left_result.size(); i++) {
			boolean r = false;
			switch(String.valueOf(conOp)) {
				case ">": if(left_result.get(i) > right_result.get(i)) r = true; break;
				case "<": if(left_result.get(i) < right_result.get(i)) r = true; break;
				case "=": if(left_result.get(i) == right_result.get(i)) r = true; break;
				case "!": if(left_result.get(i) != right_result.get(i)) r = true; break;
				case "]": if(left_result.get(i) >= right_result.get(i)) r = true; break;
				case "[": if(left_result.get(i) <= right_result.get(i)) r = true; break;
			}			
			result.add(r);
		}
		
		return result;
	}
}
