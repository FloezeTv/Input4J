package tv.floeze.Input4J;

/**
 * A Builder for building {@link InputSource}s.
 * 
 * @author Floeze
 *
 */
public abstract class InputSourceBuilder {

	/**
	 * This method has to build the {@link InputSource}. Arguments should be given
	 * in the constructor (or set with methods).
	 * 
	 * @return an Object of the {@link InputSource} this builder is builder for
	 */
	protected abstract InputSource build();

}
