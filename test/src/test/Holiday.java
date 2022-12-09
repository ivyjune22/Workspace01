package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Holiday {	
	public LocalDate getStartDay() {
		return LocalDate.now();
	}
	
	public List<LocalDate> getPeriod(int d) {
		List<LocalDate> period = new ArrayList<LocalDate>();
		LocalDate day = getStartDay();
		
		for(int i=0; i<d; i++) {
			period.add(day);
			day = day.plusDays(1);
		}
		
		return period;
	}
	
	public List<Integer> chkHoliday(List<LocalDate> period) {
		List<Integer> chkHoliday = new ArrayList<Integer>();
		LocalDate[] lawHoliday = {
				LocalDate.of(2022, 12, 25), // 올해 크리스마스
				LocalDate.of(2023, 1, 1), // 새해
		        LocalDate.of(2023, 1, 21),	// 설날
		        LocalDate.of(2023, 1, 22),	
		        LocalDate.of(2023, 1, 23),
		        LocalDate.of(2023, 1, 24),
		        LocalDate.of(2023, 3, 1),	// 삼일절
		        LocalDate.of(2023, 5, 5),	// 어린이날
		        LocalDate.of(2023, 5, 27),	// 부처님 오신 날
		        LocalDate.of(2023, 6, 6),	// 현충일
		        LocalDate.of(2023, 8, 15),	// 광복절
		        LocalDate.of(2023, 9, 28),	// 추석
		        LocalDate.of(2023, 9, 29),
		        LocalDate.of(2023, 9, 30),
		        LocalDate.of(2023, 10, 3),	// 개천절
		        LocalDate.of(2023, 10, 9),	// 한글날
		        LocalDate.of(2023, 12, 25)	// 내년 크리스마스
		};
		
		List<LocalDate> lawHolidayList = new ArrayList<>(Arrays.asList(lawHoliday));
		
		for(int i=0; i<period.size(); i++) {
			if(lawHolidayList.contains(period.get(i)) 
				|| Arrays.asList(6, 7).contains(period.get(i).getDayOfWeek().getValue())) {
				chkHoliday.add(1);
			} else chkHoliday.add(0);
		}
				
		return chkHoliday;
	}
}