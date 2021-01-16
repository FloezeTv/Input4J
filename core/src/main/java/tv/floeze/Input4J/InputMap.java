package tv.floeze.Input4J;

import java.util.HashMap;
import java.util.Map;

/**
 * Map storing the input values.
 * 
 * <br />
 * 
 * There is the option to have multiple players identified by an {@code int}.
 * Although this can theoretically be any {@code int}, it is easiest to start
 * from 0 counting up without any skips. <br />
 * If you only want/need to use on player, just pass the same number everytime.
 * A recommended value for that would be {@code 0}.
 * 
 * @author Floeze
 *
 * @param <T> Class of identifier
 */
public class InputMap<T> {

	/**
	 * Map storing the inputs.
	 * 
	 * player -> identifier -> value
	 */
	private final Map<Integer, Map<T, Short>> map;

	/**
	 * Creates a new empty {@link InputMap}
	 */
	public InputMap() {
		map = new HashMap<Integer, Map<T, Short>>();
	}

	/**
	 * Sets the value of an input.<br />
	 * <b>This should only be done by input sources!</b> If you don't define a new
	 * input source, don't call this method!<br />
	 * <br />
	 * This method makes the largest absolute value have priority. This means that
	 * if there was a 100 stored and you
	 * <ul>
	 * <li>call this method with a 200, the new value would be 200</li>
	 * <li>call this method with a -300, the new value would be -300</li>
	 * <li>call this method with a 50, the value would stay 100</li>
	 * </ul>
	 * 
	 * @param player     player to set value for
	 * @param identifier identifier of input
	 * @param value      value of input to set to
	 */
	public void set(int player, T identifier, short value) {
		map.computeIfAbsent(player, k -> new HashMap<T, Short>())//
				.compute(identifier, (k, v) -> (v == null || Math.abs(value) > Math.abs(v)) ? value : v);
	}

	/**
	 * Gets the value for an input.
	 * 
	 * @param player     player to get input for
	 * @param identifier identifier of input
	 * @return value of input. If the player or identifier has no inputs set, this
	 *         returns 0.
	 */
	public short get(int player, T identifier) {
		Map<T, Short> m = map.get(player);
		if (m == null)
			return 0;
		return m.getOrDefault(identifier, (short) 0);
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
