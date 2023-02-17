package test_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rawdata.RawdataDAO;
import var.VariableDTO;

public class EngCalClass {
	List<String> op = Arrays.asList("+", "-", "*", "/", "^", "(", ")");
	List<String> single_op = 
			Arrays.asList("sin", "cos", "tan", "sec", "csc", "cot", "sinh", "cosh", "tanh", "sech", "csch", "coth", "abs", "log", "ln", "sqrt");
	List<String> var = Arrays.asList("pi", "exp");
	Map<String, Double> varmap = Map.of("pi", Math.PI, "exp", Math.E);
	RawdataDAO dao = new RawdataDAO();
	
	public List<Double> string_cal(String formula, List<VariableDTO> var_list) {
		return calculator(var_load(infix_to_postfix(string_to_infix(formula)), var_list));		
	}
	
	public List<String> string_to_infix(String formula) {
		List<Integer> opindex = new ArrayList<Integer>();
		List<String> inf = new ArrayList<String>();
		
		formula = formula.replaceAll("\\s", "").replace("(-", "(n");
		
		for(int i=0; i<formula.length(); i++) {
			if(op.contains(String.valueOf(formula.charAt(i)))) {
				opindex.add(i);
			}
		}
		opindex.add(formula.length());
		
		int beginIndex = -1;
		for(int i=0; i<opindex.size(); i++) {
			int endIndex = opindex.get(i);
			if(beginIndex + 1 != endIndex) {
				inf.add(formula.substring(beginIndex + 1, endIndex));
			}
			if(endIndex != formula.length()) {
				inf.add(formula.substring(endIndex, endIndex + 1));
			}
			beginIndex = endIndex;
		}
		
		for(int i=0; i<inf.size(); i++) {
			if(inf.get(i).startsWith("n")) {
				inf.set(i, inf.get(i).replace("n", "-"));
			}
		}
		
		return inf;
	}
	
	private List<String> infix_to_postfix(List<String> inf) {
		List<String> psf = new ArrayList<String>();
		Stack<String> st = new Stack<String>();
		
		for(int i=0; i<inf.size(); i++) {
			if(!op.contains(inf.get(i)) && !single_op.contains(inf.get(i))) {
				psf.add(inf.get(i));
			} else if(st.empty() || inf.get(i).equals("(")) {
				st.push(inf.get(i));
			} else if(inf.get(i).equals(")")) {
				while(!st.isEmpty()) {
					String pop = st.pop();
					if(pop.equals("(")) break;
					psf.add(pop);
				}
			} else {
				while(getPriority(st.peek()) >= getPriority(inf.get(i))) {
					String pop = st.pop();
					psf.add(pop);
					if(st.empty()) break;
				} 
				st.push(inf.get(i));
			}
		}
			
		for(int j=0; j<st.size(); ) {
			String pop = st.pop();
			psf.add(pop);
		}
		
		return psf;
	}
	
	private int getPriority(String string) {
		int result = 0;
		
		switch(string) {
		case "sin":	case "cos": case "tan":	case "sec":	case "csc":	case "cot": 
		case "sinh": case "cosh": case "tanh": case "sech": case "csch": case "coth":
		case "abs" : case "log": case "ln":	case "sqrt" : result = 3; break;
		case "*": case "/": case "^":  result = 2; break;
		case "+": case "-": result = 1; break;
		}
		
		return result;
	}
	
	private List<List<String>> var_load(List<String> psf, List<VariableDTO> var_list) {
		List<List<String>> psf_list = new ArrayList<List<String>>();
		List<Integer> dbvar_index = new ArrayList<Integer>();
		int list_size = 1;
		
		for(int i=0; i<psf.size(); i++) {
			for(int j=0; j<var_list.size(); j++) {
				if(psf.get(i).equals(var_list.get(j).getVar_name())) {
					psf.set(i, var_list.get(j).getVar_value());
					if(psf.get(i).matches("^[^0-9]+\\w*\\.\\w+$")) {
						dbvar_index.add(i);
						list_size = dao.read(psf.get(i)).size();
					}
				}
			}
		}
		
		for(int i=0; i<list_size; i++) {
			List<String> psf_dbloaded = new ArrayList<String>();
			psf_dbloaded.addAll(psf);
			
			for(int j=0; j<dbvar_index.size(); j++) {
				psf_dbloaded.set(dbvar_index.get(j),String.valueOf(dao.read(psf.get(dbvar_index.get(j))).get(i)));
			}
			
			psf_list.add(psf_dbloaded);
		}
	
		return psf_list;
	}
	
	private List<Double> calculator(List<List<String>> psf_list) {
		List<Double> result = new ArrayList<Double>();
		Stack<String> st = new Stack<String>();
		
		for(int i=0; i<psf_list.size(); i++) {
			for(int j=0; j<psf_list.get(i).size(); j++) {
				if(op.contains(psf_list.get(i).get(j))) {
					double r = 0;
					double b = Double.valueOf(st.pop());
					double a = Double.valueOf(st.pop());
					
					switch(psf_list.get(i).get(j)) {
						case "+": r = a + b; break;
						case "-": r = a - b; break;
						case "*": r = a * b; break;
						case "/": r = a / b; break;
						case "^": r = Math.pow(a, b);
					}
					st.push(String.valueOf(r));
				} else if(single_op.contains(psf_list.get(i).get(j))){
					double r = 0;
					double a = Double.valueOf(st.pop());
					
					switch(psf_list.get(i).get(j)) {
					case "sin": r = Math.sin(a); break;
					case "cos": r = Math.cos(a); break;
					case "tan": r = Math.tan(a); break;
					case "sec": r = 1/Math.cos(a); break;
					case "csc": r = 1/Math.sin(a); break;
					case "cot": r = 1/Math.tan(a); break;
					case "sinh": r = Math.sinh(a); break;
					case "cosh": r = Math.cosh(a); break;
					case "tanh": r = Math.tanh(a); break;
					case "sech": r = 1/Math.cosh(a); break;
					case "csch": r = 1/Math.sinh(a); break;
					case "coth": r = 1/Math.tanh(a); break;
					case "abs" : r = Math.abs(a); break;
					case "log": r = Math.log10(a); break;
					case "ln": r = Math.log(a); break;
					case "sqrt" : r = Math.sqrt(a); break;
					}
					st.push(String.valueOf(r));
				} else if(var.contains(psf_list.get(i).get(j))) {
					double r = varmap.get(psf_list.get(i).get(j));
					
					st.push(String.valueOf(r));
				} else {
					st.push(psf_list.get(i).get(j));
				} 
			}
			result.add(Double.valueOf(st.pop()));
		} 
		
		return result;
	}
}
