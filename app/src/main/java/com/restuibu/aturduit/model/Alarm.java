package com.restuibu.aturduit.model;

public class Alarm {
	private int idAlarm;
	private long time;
	private int isRepeat;

	public Alarm(int idAlarm, long time, int isRepeat) {
		super();
		this.idAlarm = idAlarm;
		this.time = time;
		this.isRepeat = isRepeat;
	}

	public Alarm() {
	}

	public int getIdAlarm() {
		return idAlarm;
	}

	public void setIdAlarm(int idAlarm) {
		this.idAlarm = idAlarm;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

}
