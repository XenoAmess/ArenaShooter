package arenashooter.engine.animation.tracks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Abstract class for a keyframe animation track
 * @param <T> Animated data type
 */
public abstract class AnimTrack<T> {
	protected double[] times;
	protected Object[] values;
	
	public AnimTrack(Map<Double, T> keyframes) {
		times = new double[keyframes.size()];
		values = new Object[keyframes.size()];
		
		//Make sure keyframes times are sorted
		ArrayList<Double> timesList = new ArrayList<>(keyframes.size());
		timesList.addAll(keyframes.keySet());
		timesList.sort( new Comparator<Double>() {
			public int compare(Double d1, Double d2) {
				return d1.compareTo(d2);
			}
		});

		for(int i=0; i<timesList.size(); i++) {
			times[i] = timesList.get(i);
			values[i] = keyframes.get(times[i]);
		}
	}

	/**
	 * Get the value of this track at a specified time
	 * @param time
	 * @return value
	 */
	public abstract T valueAt(double time);
	
	protected int prevKeyframe(double time) {
		for(int i=times.length-1; i>=0; i--) {
			if(times[i]<=time) return i;
		}
		return 0;
	}
	
	protected int nextKeyframe(double time) {
		for(int i=0; i<times.length; i++) {
			if(times[i]>time) return i;
		}
		return times.length-1;
	}
}
