package test_;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import var.VariableDAO;
import var.VariableDTO;

public class CalMenu {
	Scanner sc = new Scanner(System.in);
	
	List<String> var_name = new ArrayList<String>();
	List<VariableDTO> var_list = new ArrayList<VariableDTO>();
	VariableDAO vdao = new VariableDAO();
	
	List<String> con_list = new ArrayList<String>();
	List<String> form_list = new ArrayList<String>();
	
	ConBoolClass cbc = new ConBoolClass();
	EngCalClass ecc = new EngCalClass();
	 
	InputValidChk ivc = new InputValidChk();
	 
	private void simpleCalMenu() {
		String formula = "";
		
		try {
			do {
				System.out.print("계산할 식을 입력하세요 : ");
				formula = sc.nextLine();
			} while(ivc.form_chk(formula, var_list) != 0);
		} catch (Exception e) {
			sc = new Scanner(System.in);
		}
		
		System.out.println(formula + "의 계산 결과는 " + ecc.string_cal(formula, var_list).get(0) + "입니다.");
		
		mainMenu();
	}
	
	private void varControlMenu() {
		int m = 0;
		String vn = "", vv = "";
		
		while(true) {
			System.out.println("------------------");
			System.out.println("1. 변수 입력하기");
			System.out.println("2. 변수 수정하기");
			System.out.println("3. 변수 삭제하기");
			System.out.println("4. 이전 메뉴");
			System.out.println("------------------");
			do {
				try {
					System.out.print("번호를 입력하세요 : ");
					m = sc.nextInt();
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
			} while(m < 1 || m > 4);
			switch(m) {
			case 1:
				try {
					do {
						System.out.print("변수명을 입력하세요 : ");
						vn = sc.next();
					} while(ivc.var_name_chk(vn) != 0);
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
				try {
					do {
						System.out.print("변수값을 입력하세요 : ");
						vv = sc.next();
					} while(ivc.var_value_chk(vv) != 0);
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
				
				var_name.add(vn.toUpperCase());
				var_list = vdao.write(vn.toUpperCase(), vv, var_list); break;
			case 2:
				try {
					do {
						System.out.print("수정할 변수명을 입력하세요 : ");
						vn = sc.next();
					} while(ivc.var_name_chk(vn) != 0);
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
				try {
					do {
						System.out.print("수정할 변수값을 입력하세요 : ");
						vv = sc.next();
					} while(ivc.var_value_chk(vv) != 0);
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
				
				var_list = vdao.edit(vn.toUpperCase(), vv, var_list); break;
			case 3: 
				try {
					do {
						System.out.print("삭제할 변수명을 입력하세요 : ");
						vn = sc.next();
					} while(ivc.var_name_chk(vn) != 0);
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
				
				var_name.remove(vn);
				var_list = vdao.delete(vn.toUpperCase(), var_list); break;
			case 4:
				mainMenu(); break;
			}
		}
	}
	
	private void formControlMenu() {
		int m = 0;
		int num = 0;
		String con, form;
		
		while(true) {
			System.out.println("------------------");
			System.out.println("1. 계산식 입력하기");
			System.out.println("2. 계산식 수정하기");
			System.out.println("3. 계산식 삭제하기");
			System.out.println("4. 이전 메뉴");
			System.out.println("------------------");
			do {
				try {
					System.out.print("번호를 입력하세요 : ");
					m = sc.nextInt();
				} catch (InputMismatchException e) {
					sc = new Scanner(System.in);
				}
				sc.nextLine();
			} while(m < 1 || m > 4);
			switch(m) {
			case 1:
				do {
					System.out.print("조건식을 입력하세요 : ");
					con = sc.nextLine();
				} while(ivc.con_chk(con, var_list) != 0);
				do {
					System.out.print("계산식을 입력하세요 : ");
					form = sc.nextLine();
				} while(ivc.form_chk(form, var_list) != 0);
				
				con_list.add(con);
				form_list.add(form); break;
			case 2:
				do {
					System.out.print("수정할 조건식을 입력하세요 : ");
					con = sc.nextLine();
				} while(ivc.con_chk(con, var_list) != 0);
				do {
					System.out.print("수정할 계산식을 입력하세요 : ");
					form = sc.nextLine();
				} while(ivc.form_chk(form, var_list) != 0);
				
				for(int i=0; i<con_list.size(); i++) {
					if(con_list.get(i).equals(con)) {
						num = i;
					}
				}
				
				form_list.set(num, form); break;
			case 3: 
				do {
					System.out.print("삭제할 조건식을 입력하세요 : ");
					con = sc.nextLine();
				} while(ivc.con_chk(con, var_list) != 0);
				
				con_list.remove(con);
				
				for(int i=0; i<con_list.size(); i++) {
					if(con_list.get(i).equals(con)) {
						num = i;
					}
				}
				
				form_list.remove(num); break;
			case 4:
				mainMenu(); break;
			}
		}
	}
	
	private void view() {
		System.out.println("------------------");
		System.out.println("변수명 \t 변수값");
		if(var_list != null) {
			for(int i=0; i<var_list.size(); i++) {
				System.out.println(var_name.get(i) + " \t " + vdao.get_valuebyname(var_name.get(i), var_list));			
			}
		}
		
		System.out.println("------------------");
		System.out.println("조건식 \t 계산식");
		if(con_list != null) {
			for(int j=0; j<con_list.size(); j++) {
				System.out.println(con_list.get(j) + " \t " + form_list.get(j));
			}
		}
		System.out.println("------------------");
		
		mainMenu();
	}
	
	private void calculate() {
		List<List<Boolean>> conResultList = new ArrayList<List<Boolean>>();
		List<List<Double>> calResultList = new ArrayList<List<Double>>();
		
		for(int i=0; i<con_list.size(); i++) {
			List<Boolean> conResult = cbc.condition_to_bool(con_list.get(i), var_list);
			conResultList.add(conResult);
			List<Double> calResult = ecc.string_cal(form_list.get(i), var_list);
			calResultList.add(calResult);
		}
		
		System.out.println("조건(참) \t 계산식 \t 계산 결과");
		for(int j=0; j<conResultList.size(); j++) {
			for(int k=0; k<conResultList.get(j).size(); k++) {
				if(conResultList.get(j).get(k)) {
					System.out.println(con_list.get(j) + " \t " + form_list.get(j) + " \t " + calResultList.get(j).get(k));
				}
			}
		}
		
		mainMenu();
	}

	public void mainMenu() {
		int m = 0;
		
		System.out.println("------------------");
		System.out.println("1. 일반 계산기 사용하기");
		System.out.println("2. 변수 설정하기");
		System.out.println("3. 조건/계산식 설정하기");
		System.out.println("4. 변수 및 함수 목록 보기");
		System.out.println("5. 조건/계산식 계산하기");
		System.out.println("6. 프로그램 종료");
		System.out.println("------------------");
		do {
			System.out.print("번호를 입력하세요 : ");
			try {
				m = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				sc = new Scanner(System.in);
			}
		} while(m < 1 || m > 6);
		switch(m) {
		case 1: 
			simpleCalMenu(); break;
		case 2:
			varControlMenu(); break;
		case 3:
			formControlMenu(); break;
		case 4: 
			view(); break;
		case 5:
			calculate(); break;
		case 6:
			System.out.println("프로그램을 종료합니다.");
			System.exit(0);
		}
	}
}
