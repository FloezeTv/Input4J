package tv.floeze.Input4J;

/**
 * Input4J is the main class to handle input from various sources. <br />
 * 
 * Inputs are stored in an {@link InputMap} and can be retrieved using
 * {@link InputMap#get(int, Object)}.
 * 
 * @author Floeze
 *
 * @param <T> Class of identifier
 */
public class Input4J<T> {

	/**
	 * Creates a new Instance of {@link Input4J}
	 */
	public Input4J() {

	}

	/**
	 * Updates all inputs and collects them in an {@link InputMap}.
	 * 
	 * @return an {@link InputMap} filed with the current inputs.
	 */
	public InputMap<T> update() {
		return new InputMap<T>();
	}

}
