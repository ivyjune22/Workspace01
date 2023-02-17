package var;

import java.util.List;

public class VariableDAO {
	public List<VariableDTO> write(String var_name, String var_value, List<VariableDTO> var_list) {
		VariableDTO vdto = new VariableDTO();
		int dup = 0;
		
		for(int i=0; i<var_list.size(); i++) {
			if(var_name.equals(var_list.get(i).getVar_name())) {
				dup = 1;
			} 
		}
		
		if(dup == 0) {
			vdto.setVar_name(var_name);
			vdto.setVar_value(var_value);
			
			var_list.add(vdto);
		} else {
			System.out.println("변수명이 중복되어 입력할 수 없습니다.");
		}
		
		return var_list;
	}
	
	public List<VariableDTO> delete(String var_name, List<VariableDTO> var_list) {
		for(int i=0; i<var_list.size(); i++) {
			if(var_list.get(i).getVar_name().equals(var_name)) {
				var_list.remove(i);
			}
		}
		
		return var_list;
	}
	
	public List<VariableDTO> edit(String var_name, String var_value, List<VariableDTO> var_list) {
		VariableDTO vdto = new VariableDTO();
		int result = 0;
		
		for(int i=0; i<var_list.size(); i++) {
			if(var_list.get(i).getVar_name().equals(var_name)) {
				vdto.setVar_name(var_name);
				vdto.setVar_value(var_value);
				var_list.set(i, vdto);
				
				result = 1;
			}
		}
		
		if(result == 0) {
			System.out.println("입력한 이름의 변수가 없어 수정할 수 없습니다.");
		}
		
		return var_list;
	}
	
	public String get_valuebyname(String var_name, List<VariableDTO> var_list) {
		String var_value = "";
		
		for(int i=0; i<var_list.size(); i++) {
			if(var_list.get(i).getVar_name().equals(var_name)) {
				var_value = var_list.get(i).getVar_value();
			}
		}
		
		return var_value;
	}
}
