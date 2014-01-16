package nl.q42.jue;

import java.util.Date;

@SuppressWarnings("unused")
class CreateScheduleRequest {
	private String name;
	private String description;
	private ScheduleCommand command;
	private Date time;
	
	public CreateScheduleRequest(String name, String description, ScheduleCommand command, Date time) {
		if (name != null && Util.stringSize(name) > 32) {
			throw new IllegalArgumentException("Schedule name can be at most 32 characters long");
		}
		
		if (description != null && Util.stringSize(description) > 64) {
			throw new IllegalArgumentException("Schedule description can be at most 64 characters long");
		}
		
		if (command == null) {
			throw new IllegalArgumentException("No schedule command specified");
		}
		
		this.name = name;
		this.description = description;
		this.command = command;
		this.time = time;
	}
}
