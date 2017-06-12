package org.freeplane.plugin.script.proxy;

// will be converted to json
public class TaskExternal implements Proxy.TaskExternal {
	public String getName() {
		return name;
	}
	public String getProject() {
		return project;
	}
	public boolean isDone() {
		return done;
	}
	public int getUrgency() {
		return urgency;
	}

	public TaskExternal(String name, String project, boolean done, int urgency) {
		this.name = name;
		this.project = project;
		this.done = done;
		this.urgency = urgency;
	}
	
	@Override
	public String toString() {
		return "[" + name + "|" + project + "|" + done + "|" + urgency + "]";
	}
	
	private String name;
	private String project;
	private boolean done;
	private int urgency;
}
