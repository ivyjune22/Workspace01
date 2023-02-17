package test;

import test.Holiday;
import test.MemberDTO;
import test.MemberDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import test.DutyScheduler;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static MemberDAO dao = new MemberDAO(); 
	static DutyScheduler ds = new DutyScheduler();
	static Holiday hol = new Holiday();
	
	public static void mainMenu() {
		int m = 0;
		
		System.out.println("---------------");
		System.out.println("1. 근무자 관리");
		System.out.println("2. 근무표 짜기");
		System.out.println("3. 프로그램 종료");
		System.out.println("---------------");
		do {
			System.out.print("번호를 입력하세요 : ");
			m = sc.nextInt();
		} while(m < 1 || m > 3);
		switch(m) {
		case 1:
			memberMenu(); break;
		case 2:
			dutyMenu(); break;
		case 3:
			System.out.println("프로그램을 종료합니다.");
			System.exit(0);
		}
	}
	
	public static void memberMenu() {
		int m = 0;
		String name, birthday;
		int num, rank, result;
		MemberDTO dto = new MemberDTO();
		
		while(true) {
			System.out.println("---------------");
			System.out.println("1. 근무자 입력");
			System.out.println("2. 근무자 목록보기");
			System.out.println("3. 근무자 정보 수정");
			System.out.println("4. 근무자 삭제");
			System.out.println("5. 돌아가기");
			System.out.println("---------------");
			do {
				System.out.print("번호를 입력하세요 : ");
				m = sc.nextInt();
			} while(m < 1 || m > 5);
			switch(m) {
			case 1:
				System.out.print("이름을 입력하세요 : ");
				name = sc.next();
				System.out.print("직급을 입력하세요(1: 수간호사 / 2: 일반간호사 / 3: 간호조무사) : ");
				rank = sc.nextInt();
				System.out.print("생년월일을 입력하세요 : ");
				birthday = sc.next();
				dto.setName(name);
				dto.setRank(rank);
				dto.setBirthday(birthday);
				
				result = dao.insert(dto);
				if(result != 0) {
					System.out.println("입력되었습니다.");
				} else {
					System.out.println("입력 실패!");
				}
				break;
			case 2:
				List<MemberDTO> list = dao.list();
				System.out.println("번호\t이름\t직급\t생일");
				for(int i=0; i<list.size(); i++) {
					num = list.get(i).getNum();
					String mname = list.get(i).getName();
					String mrank = "";
					switch(list.get(i).getRank()) {
					case 1: mrank = "수간호사"; break;
					case 2:	mrank = "일반간호사"; break;
					case 3: mrank = "간호조무사"; break;
					}
					String mbirthday = list.get(i).getBirthday();
					
					System.out.println(num + "\t" + mname + "\t" + mrank + "\t" + mbirthday);
				}
				break;
			case 3:
				System.out.print("수정할 근무자의 번호를 입력하세요 : ");
				num = sc.nextInt(); 
				System.out.print("이름을 입력하세요 : ");
				name = sc.next();
				System.out.print("직급을 입력하세요(1: 수간호사 / 2: 일반간호사 / 3: 간호조무사) : ");
				rank = sc.nextInt();
				System.out.print("생년월일을 입력하세요 : ");
				birthday = sc.next();
				dto.setNum(num);
				dto.setName(name);
				dto.setRank(rank);
				dto.setBirthday(birthday);
				
				result = dao.update(dto);
				if(result != 0) {
					System.out.println("수정되었습니다.");
				} else {
					System.out.println("수정 실패!");
				}
				break;
			case 4:
				System.out.print("삭제할 근무자의 번호를 입력하세요 : ");
				num = sc.nextInt(); 
				
				result = dao.delete(num);
				if(result != 0) {
					System.out.println("삭제되었습니다.");
				} else {
					System.out.println("삭제 실패!");
				}
				break;
			case 5:
				mainMenu();
				break;
			}
		}
	}
	
	public static void dutyMenu() {
		int m = 0;
		List<MemberDTO> r1List = dao.rank1List();
		List<MemberDTO> r23List = dao.rank23List();
		List<MemberDTO> list = dao.list();
		int d;
		List<LocalDate> period;
		
		while(true) {
			System.out.println("---------------");
			System.out.println("1. 전체 근무표 보기");
			System.out.println("2. 개인 근무표 보기");
			System.out.println("3. 근무표 검증");
			System.out.println("4. 돌아가기");
			System.out.println("---------------");
			do {
				System.out.print("번호를 입력하세요 : ");
				m = sc.nextInt();
			} while(m < 1 || m > 4);
			switch(m) {
			case 1: 
				System.out.print("근무기간(일)을 입력하세요 : ");
				d = sc.nextInt();
				period = hol.getPeriod(d);
			
				List<List<Integer>> r1Schedule = ds.r1Scheduler(period, r1List);
				Map<Integer, List<List<Integer>>> r23Schedule = ds.r23Scheduler(period, r23List);
			
				for(int i=0; i<period.size(); i++) {
					System.out.println("\t" + period.get(i));
					
					System.out.print("D\t");
					for(int j=0; j<r1Schedule.get(i).size(); j++) {
						System.out.print(dao.findName(r1Schedule.get(i).get(j)) + " ");
					}
					for(int j=0; j<r23Schedule.get(1).get(i).size(); j++) {
						System.out.print(dao.findName(r23Schedule.get(1).get(i).get(j)) + " ");
					}
					System.out.println("");
					
					System.out.print("E\t");
					for(int j=0; j<r23Schedule.get(2).get(i).size(); j++) {
						System.out.print(dao.findName(r23Schedule.get(2).get(i).get(j)) + " ");
					}
					System.out.println("");
					
					System.out.print("N\t");
					for(int j=0; j<r23Schedule.get(3).get(i).size(); j++) {
						System.out.print(dao.findName(r23Schedule.get(3).get(i).get(j)) + " ");
					}
					System.out.println("");
				}
				break;
			case 2:
				int num, sum = 0, dsum = 0, esum = 0, nsum = 0;;
				List<Integer> psch = null;
				
				System.out.print("근무기간(일)을 입력하세요 : ");
				d = sc.nextInt();
				period = hol.getPeriod(d);
				System.out.print("근무자 번호를 입력하세요 : ");
				num = sc.nextInt();
				
				
				if(dao.findRank(num) == 1) {
					psch = ds.r1PersonalSch(period, r1List, num);
					for(int i=0; i<period.size(); i++) {
						System.out.print("\t" + period.get(i));
						if(psch.get(i) == 1) System.out.println("\t V");
						else System.out.println("");
						
						sum += psch.get(i);
					}
					System.out.println("근무일은 총 " + sum + "일 입니다.");
					System.out.println("휴무일은 총 " + (d-sum) + "일 입니다.");
				} else {
					psch = ds.r23PersonalSch(period, r23List, num);
					for(int i=0; i<period.size(); i++) {						
						System.out.print("\t" + period.get(i));
						if(psch.get(i) == 1) {
							System.out.println("\t D"); 
							dsum += 1;
						} else if(psch.get(i) == 2) {
							System.out.println("\t E");
							esum += 1;
						}
						else if(psch.get(i) == 3) { 
							System.out.println("\t N");
							nsum += 1;
						}
						else System.out.println("\t O");
					}
					System.out.println("주간 근무일은 총 " + dsum + "일 입니다.");
					System.out.println("야간 근무일은 총 " + esum + "일 입니다.");
					System.out.println("새벽 근무일은 총 " + nsum + "일 입니다.");
					System.out.println("휴무일은 총 " + (d-dsum-esum-nsum) + "일 입니다.");
				}
				break;
			case 3:
				System.out.print("근무기간(일)을 입력하세요 : ");
				d = sc.nextInt();
				period = hol.getPeriod(d);
				List<Integer> dutyChkList = ds.dutyChk(period, r1List, r23List);
				
				for(int i=0; i<list.size(); i++) {
					System.out.print(list.get(i).getName() + " ");
					System.out.println(dutyChkList.get(i));
				}
				break;
			case 4:
				mainMenu();
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		mainMenu();
	}
}
