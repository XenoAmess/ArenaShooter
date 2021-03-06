package arenashooter.engine.ui;

public interface Navigable {
	
	/**
	 * @return true if an action has been done
	 */
	public boolean upAction();

	/**
	 * @return true if an action has been done
	 */
	public boolean downAction();

	/**
	 * @return true if an action has been done
	 */
	public boolean rightAction();

	/**
	 * @return true if an action has been done
	 */
	public boolean leftAction();
	
	/**
	 * @return true if an action has been done
	 */
	public boolean selectAction();
	
	/**
	 * @return true if an action has been done
	 */
	public boolean continueAction();
	
	/**
	 * @return true if an action has been done
	 */
	public boolean cancelAction();
	
	/**
	 * @return true if an action has been done
	 */
	public boolean changeAction();
	
	/**
	 * @return true if an action has been done
	 */
	public boolean backAction();
	
	public void update(double delta);
	
	public void draw();

}
