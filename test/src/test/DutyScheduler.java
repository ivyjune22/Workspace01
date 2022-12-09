package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.MemberDTO;
import test.Holiday;

public class DutyScheduler {
	Holiday holiday = new Holiday();
	
	public int getDutyZeroPriority(int j, List<Integer> dutyQueue) {
		if(dutyQueue.get(j) == 0) return 0;
		else return 1;
	}
	
	public int getTimeRatioPriority(int j, List<Integer> timeQueue, List<Integer> dutyQueue) {
		if((double)timeQueue.get(j)/dutyQueue.get(j) <= 0.34) return 0;
		else return 1;
	}
	
	public int getDutyMinPriority(int j, List<Integer> dutyQueue) {
		if(dutyQueue.get(j) == Collections.min(dutyQueue)) return 0;
		else return 1;
	}
	
	public int getDutyDayPriority(int j, int dutyDays, List<Integer> dutyQueue) {
		if(dutyQueue.get(j) <= dutyDays) return 0;
		else return 1;
	}
	
	public int getBirthdayPriority(int i, int j, List<MemberDTO> list, List<LocalDate> period) {
		if(!list.get(j).getBirthday().substring(5).equals(period.get(i).toString().substring(5))) return 0;
		else return 1;
	}
	
	public int getFormerDutyPriority(int i, int j, List<List<Integer>> timeSchedule, List<MemberDTO> list) {
		if(!timeSchedule.get(i).contains(list.get(j).getNum())) return 0;
		else return 1;
	}
	
	public int getDupPriority(int j, List<Integer> timeDuty, List<MemberDTO> list) {
		if(!timeDuty.contains(list.get(j).getNum())) return 0;
		else return 1;
	}
	
	public int getPriority(int i, int j, int dutyDays, List<MemberDTO> list,
			List<List<Integer>> timeSchedule, List<List<Integer>> lastTimeSchedule,
			List<List<Integer>> formerTimeSchedule,	List<Integer> timeDuty, List<Integer> timeQueue, 
			List<Integer> dutyQueue, List<LocalDate> period) {
		int k = 0, m = 0;
		
		switch(i % 3) {
		case 0: k = i/3-1; m = i/3-1; break;
		case 1: k = i/3-1; m = i/3; break;
		case 2: k = i/3; m = i/3; break;
		}
		
		int dzp = getDutyZeroPriority(j, dutyQueue);
		int trp = getTimeRatioPriority(j, timeQueue, dutyQueue);
		int dmp = getDutyMinPriority(j, dutyQueue);
		int ddp = getDutyDayPriority(j, dutyDays, dutyQueue);
		int bp = getBirthdayPriority(i/3, j, list, period);
		int fdp, ldp;
		if(i/3 > 0) {
		  fdp = getFormerDutyPriority(k, j, formerTimeSchedule, list);
		  ldp = getFormerDutyPriority(m, j, lastTimeSchedule, list);
		} else {
			fdp = 0;
			ldp = 0;
		}
		int dp = getDupPriority(j, timeDuty, list);
		
		int p = dzp + 2 * trp + 4 * dmp + 8 * ddp + 16 * bp + 32 * fdp + 64 * ldp + 128 * dp;
		
		return p;
	}
	
	public List<List<Integer>> r1Scheduler(List<LocalDate> period, List<MemberDTO> r1List) {
		List<Integer> chkHoliday = holiday.chkHoliday(period);
		int dutyDays = (int)(period.size() * 0.65) + 1;  
		List<Integer> dutyQueue = Arrays.asList(Collections.nCopies(r1List.size(), 0).toArray(new Integer[0]));
		
		List<List<Integer>> r1Schedule = new ArrayList<List<Integer>>();
		for(int i = 0; i < period.size(); i++) {
			List<Integer> todayDuty = new ArrayList<Integer>();
			int j = 0, dutyCount;
			if(chkHoliday.get(i) == 0) {
				dutyCount = r1List.size() * 3/4;
			} else {
				dutyCount = r1List.size() / 4;
			}
			while(j < r1List.size() * r1List.size() * 3/4) {	
				int k = j % r1List.size();
				j++;
				if(!r1List.get(k).getBirthday().substring(5).equals(period.get(i).toString().substring(5))
						&& dutyQueue.get(k) == Collections.min(dutyQueue)
						&& dutyQueue.get(k) <= dutyDays) {
					todayDuty.add(r1List.get(k).getNum());
					dutyQueue.set(k, dutyQueue.get(k) + 1);
					if(todayDuty.size() >= dutyCount) break;
				} else continue;
			}
			r1Schedule.add(todayDuty);
		}
		
		return r1Schedule;
	}
	
	public Map<Integer, List<List<Integer>>> r23Scheduler(List<LocalDate> period, List<MemberDTO> r23List) {
		List<Integer> chkHoliday = holiday.chkHoliday(period);
		int dutyDays = (int)(period.size() * 0.65) + 1;
		Map<Integer, List<List<Integer>>> dutyMap = new HashMap<Integer, List<List<Integer>>>();
		List<List<Integer>> daySchedule = new ArrayList<List<Integer>>();
		List<List<Integer>> eveSchedule = new ArrayList<List<Integer>>();
		List<List<Integer>> nightSchedule = new ArrayList<List<Integer>>();
		
		List<Integer> dutyQueue = Arrays.asList(Collections.nCopies(r23List.size(), 0).toArray(new Integer[0]));
		List<Integer> dayQueue = Arrays.asList(Collections.nCopies(r23List.size(), 0).toArray(new Integer[0]));
		List<Integer> eveQueue = Arrays.asList(Collections.nCopies(r23List.size(), 0).toArray(new Integer[0]));
		List<Integer> nightQueue = Arrays.asList(Collections.nCopies(r23List.size(), 0).toArray(new Integer[0]));
		
		for(int i=0; i<period.size()*3; i++) {
			List<Integer> timeDuty = new ArrayList<Integer>();
			int k=0, dutyCount;
			if(chkHoliday.get(i/3) == 0) {
				dutyCount = r23List.size() / 4;
			} else {
				dutyCount = r23List.size() / 12;
			}
			
			switch(i%3) {
			case 0:	
				while(k <= r23List.size() * dutyCount) {
					List<Integer> priority = new ArrayList<Integer>();
					int j = k % r23List.size();
					k++;
					for(int m = 0; m < r23List.size(); m++) {
						int p = getPriority(i, m, dutyDays, r23List,
								daySchedule, nightSchedule, eveSchedule, timeDuty,
								dayQueue, dutyQueue, period);
						priority.add(p);
					}
					if(priority.get(j) == Collections.min(priority)) {
						timeDuty.add(r23List.get(j).getNum());
						dutyQueue.set(j, dutyQueue.get(j) + 1);
						dayQueue.set(j, dayQueue.get(j) + 1);
						if(timeDuty.size() >= dutyCount) break;
					} else continue;
				}
				daySchedule.add(timeDuty); break;
			case 1:
				while(k <= r23List.size() * dutyCount) {
					List<Integer> priority = new ArrayList<Integer>();
					int j = k % r23List.size();
					k++;
					for(int m = 0; m < r23List.size(); m++) {
						int p = getPriority(i, m, dutyDays, r23List,
								eveSchedule, daySchedule, nightSchedule, timeDuty,
								eveQueue, dutyQueue, period);
						priority.add(p);
					}
					if(priority.get(j) == Collections.min(priority)) {
						timeDuty.add(r23List.get(j).getNum());
						dutyQueue.set(j, dutyQueue.get(j) + 1);
						eveQueue.set(j, eveQueue.get(j) + 1);
						if(timeDuty.size() >= dutyCount) break;
					} else continue;
				}
				eveSchedule.add(timeDuty); break;
			case 2:
				while(k <= r23List.size() * dutyCount) {
					List<Integer> priority = new ArrayList<Integer>();
					int j = k % r23List.size();
					k++;
					for(int m = 0; m < r23List.size(); m++) {
						int p = getPriority(i, m, dutyDays, r23List,
								nightSchedule, eveSchedule, daySchedule, timeDuty,
								nightQueue, dutyQueue, period);
						priority.add(p);
					}
					if(priority.get(j) == Collections.min(priority)) {
						timeDuty.add(r23List.get(j).getNum());
						dutyQueue.set(j, dutyQueue.get(j) + 1);
						nightQueue.set(j, nightQueue.get(j) + 1);
						if(timeDuty.size() >= dutyCount) break;
					} else continue;
				}
				nightSchedule.add(timeDuty); break;
			}
		}
		
		dutyMap.put(1, daySchedule);
		dutyMap.put(2, eveSchedule);
		dutyMap.put(3, nightSchedule);
		
		return dutyMap ;
	}
}
