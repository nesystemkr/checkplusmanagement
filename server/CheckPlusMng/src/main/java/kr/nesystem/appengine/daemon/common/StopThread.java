package kr.nesystem.appengine.daemon.common;

public class StopThread extends Thread {
	public boolean isStop = false;
	public StopThread() {
		super();
		setDaemon(true);
	}
	public void setStop(boolean stop) {
		isStop = stop;
	}
}
