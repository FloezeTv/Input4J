package tv.floeze.Input4J;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	private final InputConfiguration<T> config;

	/**
	 * Creates a new instance of {@link Input4J} with an empty
	 * {@link InputConfiguration}.
	 */
	public Input4J() {
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
		config = (InputConfiguration<T>) InputConfiguration.load(xml);
	}

	/**
	 * Updates all inputs and collects them in an {@link InputMap}.
	 * 
	 * @return an {@link InputMap} filed with the current inputs.
	 */
	public InputMap<T> update() {
		return new InputMap<T>();
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
