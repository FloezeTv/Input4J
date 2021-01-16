package tv.floeze.Input4J;

/**
 * A source of input.
 * 
 * @author Floeze
 *
 */
public interface InputSource {

	/**
	 * Enables this {@link InputSource}.
	 */
	public void enable();

	/**
	 * Disables this {@link InputSource}.
	 */
	public void disable();

	/**
	 * Gets if the InputSource is enabled. This could be different from the
	 * requested state ({@link #enable()}, {@link #disable()}), because the source
	 * could not be enabled/disabled or because the enabling is handled
	 * asynchronously.
	 * 
	 * @return the enabled state of this {@link InputSource}
	 */
	public boolean isEnabled();

	/**
	 * Updates the InputSource and stores the inputs in an {@link InputMap}
	 * according to an {@link InputConfiguration}.
	 * 
	 * <b>WARNING</b>: this method may still be called even if disabled. If you want
	 * an implementation that handles checking if source is enabled, use
	 * {@link InputSourceImpl}.
	 * 
	 * @param <T>    Type of identifier
	 * @param map    {@link InputMap} to write values to
	 * @param config {@link InputConfiguration} storing the input configurations
	 */
	public <T> void update(InputMap<T> map, InputConfiguration<T> config);

	/**
	 * Saves the current inputs for a later use of
	 * {@link InputSource#setInput(int, Object, InputMap, InputConfiguration)}.
	 * 
	 * <b>WARNING</b>: this method may still be called even if disabled. If you want
	 * an implementation that handles checking if source is enabled, use
	 * {@link InputSourceImpl}.
	 */
	public void saveInputs();

	/**
	 * Checks if the state of an input has changed relative to the last call of
	 * {@link #saveInputs()} and adds it to the {@code config}.
	 * 
	 * <b>WARNING</b>: this method may still be called even if disabled. If you want
	 * an implementation that handles checking if source is enabled, use
	 * {@link InputSourceImpl}.
	 * 
	 * @param <T>    Type of identifier
	 * @param player index of player to change
	 * @param name   name of input to change
	 * @param value  value to set to
	 * @param config {@link InputConfiguration} to change
	 * @return true if input changed and the config was updated, false otherwise
	 */
	public <T> boolean setInput(int player, T name, short value, InputConfiguration<T> config);
}
