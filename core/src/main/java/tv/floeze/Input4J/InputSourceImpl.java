package tv.floeze.Input4J;

/**
 * A basic implementation of {@link InputSource}. Subclasses have to override
 * the methods that handle enabling, disabling, updating, saving and setting of
 * inputs.
 * 
 * This implementation has some basic concepts built in, like saving the enabled
 * state and only updating, saving and setting inputs when enabled.
 * 
 * @author Floeze
 *
 */
public abstract class InputSourceImpl implements InputSource {

	private boolean enabled = false;

	/**
	 * This method is called, when this {@link InputSource} is asked to enable and
	 * not currently enabled.
	 * 
	 * See {@link InputSource#enable()}.
	 * 
	 * @return true if enabling succeeded, false if enabling failed
	 */
	protected abstract boolean handleEnable();

	/**
	 * This method is called, when this {@link InputSource} is asked to disable and
	 * currently enabled.
	 * 
	 * See {@link InputSource#disable()}.
	 * 
	 * @return true if disabling succeeded and this {@link InputSource} is now not
	 *         enabled, false if disabling failed
	 */
	protected abstract boolean handleDisable();

	/**
	 * This method is called when this {@link InputSource} is asked to update and
	 * currently enabled.
	 * 
	 * See {@link InputSource#update(InputMap, InputConfiguration)}
	 * 
	 * @param <T>    Type of identifier
	 * @param map    {@link InputMap} to write values to
	 * @param config {@link InputConfiguration} storing the configurations for the
	 *               players
	 */
	protected abstract <T> void handleUpdate(InputMap<T> map, InputConfiguration<T> config);

	/**
	 * This method is called, when this {@link InputSource} is asked to save inputs
	 * and currently enabled.
	 * 
	 * See {@link InputSource#saveInputs()}
	 * 
	 */
	protected abstract void handleSaveInputs();

	/**
	 * This method is called, when this {@link InputSource} is asked to set inputs
	 * and currently enabled.
	 * 
	 * See {@link InputSource#setInput(int, Object, short, InputConfiguration)}
	 * 
	 * @param <T>    Type of identifier
	 * @param player index of player to change
	 * @param name   name of input to change
	 * @param value  value to set to
	 * @param config {@link InputConfiguration} to change
	 * @return true if key changed, false otherwise
	 */
	protected abstract <T> boolean handleSetInput(int player, T name, short value, InputConfiguration<T> config);

	@Override
	public final void enable() {
		if (!enabled)
			enabled = handleEnable();
	}

	@Override
	public final void disable() {
		if (enabled)
			enabled = !handleDisable();
	}

	@Override
	public final boolean isEnabled() {
		return enabled;

	}

	@Override
	public final <T> void update(InputMap<T> map, InputConfiguration<T> config) {
		if (enabled)
			handleUpdate(map, config);
	}

	@Override
	public void saveInputs() {
		if (enabled)
			handleSaveInputs();
	}

	@Override
	public <T> boolean setInput(int player, T name, short value, InputConfiguration<T> config) {
		if (enabled)
			return handleSetInput(player, name, value, config);
		return false;
	}

}
