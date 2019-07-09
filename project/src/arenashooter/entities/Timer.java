package arenashooter.entities;

import com.github.cliftonlabs.json_simple.JsonObject;

/**
 * @author Nathan Timer until a given integer<br/>
 *         To change the border integer, you have to create a new Timer
 */
public class Timer extends Entity {

	private double max;
	private double current = 0;
	private boolean over = false;
	public boolean inProcess = true;
	private boolean increasing = true;
	
	public double current() {
		return current;
	}

	public Timer(double timer) {
		max = timer;
	}
	
	public double getValueRatio() { return current/max; }
	
	public double getValue() { return current; }
	
	public void setValue(double newValue) {
		current = newValue;
	}

	public boolean isOver() {
		return over;
	}

	/**
	 * Reset all variables
	 */
	public void restart() {
		over = false;
		current = 0;
	}
	
	/**
	 * @author Marin C
	 * Stops processing
	 */
	public void stop() {
		inProcess = false;
	}
	
	/**
	 * Stops the timer and reset all his variables
	 */
	public void reset() {
		restart();
		inProcess = false;
	}
	

	public boolean isProcessing() {	return inProcess; }

	/**
	 * @param process = </br>
	 *                | {@code true} : keeps running or restart to run </br>
	 *                | {@code false} : put on pause
	 */
	public void setProcessing(boolean process) {
		inProcess = process;
	}

	public boolean isIncreasing() {
		return increasing;
	}

	public void setIncreasing(boolean increasing) {
		this.increasing = increasing;
	}
	
	public void setMax(double max) {
		this.max = max;
	}
	
	public double getMax() {
		return max;
	}

	@Override
	public void step(double d) {

		if (inProcess) {
			if (increasing) {
				current = Math.min(max, current + d);
			} else {
				current = Math.max(0, current - d);
			}
		}
		over = (current >= max);
		super.step(d);
	}
	
	@Override
	public String toJson() {
		JsonObject timer = new JsonObject();
		timer.putChain("timeUpTo", max);
		if (!getChildren().isEmpty())
			timer.putChain("children", new JsonObject(getChildren()));
		return timer.toJson();
	}

}
