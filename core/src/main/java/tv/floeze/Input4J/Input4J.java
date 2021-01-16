package tv.floeze.Input4J;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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

	private final Map<T, InputSource> inputSources;

	private final InputConfiguration<T> config;

	/**
	 * Creates a new instance of {@link Input4J} with an empty
	 * {@link InputConfiguration}.
	 */
	public Input4J() {
		inputSources = new HashMap<T, InputSource>();
		config = new InputConfiguration<T>();
	}

	/**
	 * Creates a new instance of {@link Input4J} and loads an
	 * {@link InputConfiguration} from a xml {@link String}.
	 * 
	 * See {@link InputConfiguration#load(String)}
	 * 
	 * @param xml {@link String} to load {@link InputConfiguration} from
	 * @throws JsonMappingException    see {@link InputConfiguration#load(String)}
	 * @throws JsonProcessingException see {@link InputConfiguration#load(String)}
	 * @throws ClassNotFoundException  see {@link InputConfiguration#load(String)}
	 */
	@SuppressWarnings("unchecked")
	public Input4J(String xml) throws JsonMappingException, JsonProcessingException, ClassNotFoundException {
		inputSources = new HashMap<T, InputSource>();
		config = (InputConfiguration<T>) InputConfiguration.load(xml);
	}

	/**
	 * Creates a new instance of {@link Input4J} and loads an
	 * {@link InputConfiguration} from a xml {@link InputStream}.
	 * 
	 * See {@link InputConfiguration#load(InputStream)}
	 * 
	 * @param xml {@link InputStream} to load {@link InputConfiguration} from
	 * @throws JsonMappingException    see {@link InputConfiguration#load(String)}
	 * @throws JsonProcessingException see {@link InputConfiguration#load(String)}
	 * @throws ClassNotFoundException  see {@link InputConfiguration#load(String)}
	 */
	@SuppressWarnings("unchecked")
	public Input4J(InputStream xml) throws ClassNotFoundException, IOException {
		inputSources = new HashMap<T, InputSource>();
		config = (InputConfiguration<T>) InputConfiguration.load(xml);
	}

	/**
	 * Creates a new instance of {@link Input4J} and loads an
	 * {@link InputConfiguration} from a xml {@link File}.
	 * 
	 * See {@link InputConfiguration#load(File)}
	 * 
	 * @param xml {@link File} to load {@link InputConfiguration} from
	 * @throws JsonMappingException    see {@link InputConfiguration#load(String)}
	 * @throws JsonProcessingException see {@link InputConfiguration#load(String)}
	 * @throws ClassNotFoundException  see {@link InputConfiguration#load(String)}
	 */
	@SuppressWarnings("unchecked")
	public Input4J(File xml) throws ClassNotFoundException, IOException {
		inputSources = new HashMap<T, InputSource>();
		config = (InputConfiguration<T>) InputConfiguration.load(xml);
	}

	/**
	 * Updates all inputs and collects them in an {@link InputMap}.
	 * 
	 * @return an {@link InputMap} filed with the current inputs.
	 */
	public InputMap<T> update() {
		InputMap<T> map = new InputMap<T>();
		inputSources.forEach((name, source) -> source.update(map, config));
		return map;
	}

	/**
	 * See {@link InputSource#saveInputs()}.
	 */
	public void saveInputs() {
		inputSources.forEach((sourceName, source) -> source.saveInputs());
	}

	/**
	 * See {@link InputSource#saveInputs()}.
	 * 
	 * This only saves the inputs for the {@link InputSource}s with the specified
	 * names.
	 * 
	 * @param names names of {@link InputSource}s to save inputs
	 */
	public void saveInputs(@SuppressWarnings("unchecked") T... names) {
		inputSources.entrySet().stream().filter(e -> {
			for (T n : names)
				if (e.getKey().equals(n))
					return true;
			return false;
		}).forEach((e) -> e.getValue().saveInputs());
	}

	/**
	 * 
	 * See {@link InputSource#setInput(int, Object, short, InputConfiguration)}.
	 */
	public boolean setInput(int player, T name, short value) {
		for (InputSource s : inputSources.values()) {
			boolean r = s.setInput(player, name, value, config);
			if (r)
				return true;
		}
		return false;
	}

	/**
	 * See {@link InputSource#setInput(int, Object, short, InputConfiguration)}.
	 * 
	 * @param names names of {@link InputSource}s to set inputs
	 */
	public boolean setInput(int player, T name, short value, @SuppressWarnings("unchecked") T... names) {
		InputSource[] is = inputSources.entrySet().stream().filter(e -> {
			for (T n : names)
				if (e.getKey().equals(n))
					return true;
			return false;
		}).map(e -> e.getValue()).toArray(s -> new InputSource[s]);
		for (InputSource s : is) {
			boolean r = s.setInput(player, name, value, config);
			if (r)
				return true;
		}
		return false;
	}

	/**
	 * Adds an {@link InputSource} using an {@link InputSourceBuilder}.
	 * 
	 * @param name    name of {@link InputSource} to add. If name was already taken,
	 *                the previous {@link InputSource} will be disabled and
	 *                discarded.
	 * @param builder {@link InputSourceBuilder} to build the {@link InputSource}.
	 */
	public void addInputSource(T name, InputSourceBuilder builder) {
		inputSources.compute(name, (key, value) -> {
			if (value != null)
				value.disable();
			return builder.build();
		});
	}

	/**
	 * See {@link InputConfiguration#clearInput(int, Object)}
	 */
	public void clearInput(int player, T name) {
		config.clearInput(player, name);
	}

	/**
	 * See {@link InputConfiguration#clean()}
	 */
	public void cleanInputConfiguration() {
		config.clean();
	}

	/**
	 * {@link InputConfiguration#save()}
	 */
	public String save() throws JsonProcessingException {
		return config.save();
	}

	/**
	 * {@link InputConfiguration#save(OutputStream)}
	 */
	public void save(OutputStream s) throws IOException {
		config.save(s);
	}

	/**
	 * {@link InputConfiguration#save(File)}
	 */
	public void save(File f) throws IOException {
		config.save(f);
	}

}
