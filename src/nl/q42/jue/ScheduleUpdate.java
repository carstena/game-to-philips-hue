package nl.q42.jue;

import java.util.ArrayList;
import java.util.Date;

/**
 * Collection of updates to a schedule.
 */
public class ScheduleUpdate {
	private ArrayList<Command> commands = new ArrayList<Command>();
	
	String toJson() {
		StringBuilder json = new StringBuilder("{");
		
		for (int i = 0; i < commands.size(); i++) {
			json.append(commands.get(i).toJson());
			if (i < commands.size() - 1) json.append(",");
		}
		
		json.append("}");
		
		return json.toString();
	}
	
	/**
	 * Set the name of the schedule.
	 * @param name new name
	 * @return this object for chaining calls
	 */
	public ScheduleUpdate setName(String name) {
		if (Util.stringSize(name) > 32) {
			throw new IllegalArgumentException("Schedule name can be at most 32 characters long");
		}
		
		commands.add(new Command("name", name));
		return this;
	}
	
	/**
	 * Set the description of the schedule.
	 * @param description new description
	 * @return this object for chaining calls
	 */
	public ScheduleUpdate setDescription(String description) {
		if (Util.stringSize(description) > 64) {
			throw new IllegalArgumentException("Schedule description can be at most 64 characters long");
		}
		
		commands.add(new Command("description", description));
		return this;
	}
	
	/**
	 * Set the time of the schedule.
	 * @param time new time
	 * @return this object for chaining calls
	 */
	public ScheduleUpdate setTime(Date time) {
		commands.add(new Command("time", time));
		return this;
	}
}
