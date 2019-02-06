package com.sds.cleancode.restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class BookingScheduler extends Observable {
	private int capacityPerHour;	
	private List<Schedule> schedules;	

	public BookingScheduler(int capacityPerHour) {
		this.schedules = new ArrayList<Schedule>();
		this.capacityPerHour = capacityPerHour;
	}
	
	public void addSchedule(Schedule schedule) {
		
		// throw an exception when booking time is on the hour.
		if(schedule.getDateTime().getMinuteOfHour() != 0 ){
			throw new RuntimeException("Booking should be on the hour.");
		}
		
		// throw an exception when capacity per hour is over
		int numberOfPeople = schedule.getNumberOfPeople();
		for ( Schedule bookedSchedule : schedules ) {
			if ( bookedSchedule.getDateTime().isEqual(schedule.getDateTime()) ) {
				numberOfPeople += bookedSchedule.getNumberOfPeople();
			}
		}
		if (numberOfPeople > capacityPerHour){
			throw new RuntimeException("Number of people is over restaurant capacity per hour");
		}
			
		// throw an exception on sunday
		DateTime now = getNow();
		if(now.getDayOfWeek() == DateTimeConstants.SUNDAY){
			throw new RuntimeException("Booking system is not available on sunday");
		}
		
		schedules.add(schedule);
		
		// send SMS to customer using Observer Pattern (SmsSender implements Observer)  
		// send E-Mail to customer using Observer Pattern (MailSender implements Observer)
		setChanged();
		notifyObservers(schedule);
	}

	public DateTime getNow() {
		return new DateTime();
	}
	
	public boolean hasSchedule(Schedule schedule) {
		return schedules.contains(schedule);
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
}
