package test_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rawdata.RawdataDAO;
import var.VariableDTO;

public class InputValidChk {
	EngCalClass ecc = new EngCalClass();
	ConBoolClass cbc = new ConBoolClass();
	RawdataDAO dao = new RawdataDAO();
	List<String> op = Arrays.asList("+", "-", "*", "/", "^", "(", ")");
	List<String> block = Arrays.asList("(",")");
	
	public int var_name_chk(String vn) {
		int result = 0;
		
		if(!vn.matches("(.*)[A-Za-z](.*)")) {
			result = 1;
			System.out.println("변수는 문자를 포함하여 입력하세요.");
		}
		
		return result;
	}
	
	public int var_value_chk(String vv) {
		int result = 0;
		
		if(!vv.matches("^[^0-9]+\\w*\\.\\w+$") && !vv.matches("(-?[1-9]+[0-9]*\\.?[0-9]*)|(-?0\\.[0-9]*)|0")) {
			result = 1;
			System.out.println("변수값은 숫자 또는 (테이블명).(컬럼명)의 형태로 입력하세요.");
		} else if(vv.matches("^[^0-9]+\\w*\\.\\w+$")) {
			if(dao.read(vv).isEmpty()) {
				result = 1;
				System.out.println("테이블 또는 컬럼이 존재하지 않아 불러올 수 없습니다.");
			}
		}
		
		return result;
	}
	
	public int con_chk(String con, List<VariableDTO> var_list) {
		int result = 0;
		
		con = con.replaceAll("\\s", "");
		if(!con.matches("((.*)[<>!]{1}=?(.*))|((.*)={1}(.*))")) {
			result = 1;
			System.out.println("조건 연산자가 누락되었습니다.");
		}
		
		List<String> lrcon = cbc.conForm_to_LRForm(con);
		
		if(form_chk(lrcon.get(0), var_list) != 0 || form_chk(lrcon.get(1), var_list) != 0) {
			result = 1;
		}
		
		return result;
	}
	
	public int form_chk(String form, List<VariableDTO> var_list) {
		int result = 0;
		List<String> var_name = new ArrayList<String>();
		
		if(form.isEmpty()) {
			result = 1;
			System.out.println("수식이 입력되지 않았습니다.");
		}
			
		List<String> inf = ecc.string_to_infix(form);
		
		for(int i=0; i<var_list.size(); i++) {
			var_name.add(var_list.get(i).getVar_name());
		}
		
		for(int i=0; i<inf.size(); i++) {
			if(!inf.get(i).matches("(-?[1-9]+[0-9]*\\.?[0-9]*)|(-?0\\.[0-9]*)|0") && !op.contains(inf.get(i)))  {
				if(!var_name.contains(inf.get(i))) {
					result = 1;
					System.out.println("입력된 이름의 변수가 존재하지 않습니다. 변수명을 확인하세요.");
				}
			}
		}
		
		if(Collections.frequency(inf, "(") != Collections.frequency(inf, ")")) {
			result = 1;
			System.out.println("왼쪽/오른쪽 괄호의 짝이 맞지 않습니다.");
		}
		
		List<String> inf_copy = new ArrayList<>(inf);
		inf_copy.removeAll(block);
		
		if(inf_copy.size() > 1) {
			for(int i=0; i<inf_copy.size()-1; i++) {
				if(op.contains(inf_copy.get(i)) && op.contains(inf_copy.get(i+1))) {
					result = 1;
					System.out.println("연산자가 연속으로 사용되었습니다.");
				}
			}
		}
		
		return result;
	}
}
