package tv.floeze.Input4J;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * A class for storing (and saving/loading) Input Configurations for the
 * {@link InputSource}s.
 * 
 * @author Floeze
 *
 * @param <T> Class of identifier
 */
public class InputConfiguration<T> {

	/**
	 * player, {@link InputSource}, identifier -> {@link SourceConfiguration}s
	 */
	private final Map<Integer, Map<Class<? extends InputSource>, Map<T, List<SourceConfiguration>>>> config;

	/**
	 * Creates a new empty {@link InputConfiguration}
	 */
	public InputConfiguration() {
		config = new HashMap<Integer, Map<Class<? extends InputSource>, Map<T, List<SourceConfiguration>>>>();
	}

	/**
	 * Creates a new {@link InputConfiguration} and fills it with data from the
	 * provided {@link ConfigPOJO}.
	 * 
	 * @param source {@link ConfigPOJO} to load data from
	 * @throws ClassNotFoundException When the stored class of an
	 *                                {@link InputSource} could not be found
	 */
	@SuppressWarnings("unchecked")
	private InputConfiguration(ConfigPOJO source) throws ClassNotFoundException {
		this();
		if (source.players == null)
			return;
		for (PlayerPOJO p : source.players) {
			Map<Class<? extends InputSource>, Map<T, List<SourceConfiguration>>> pmap = new HashMap<Class<? extends InputSource>, Map<T, List<SourceConfiguration>>>();
			if (p.inputSourceConfigs == null)
				continue;
			for (InputSourcePOJO i : p.inputSourceConfigs) {
				Map<T, List<SourceConfiguration>> imap = new HashMap<T, List<SourceConfiguration>>();
				if (i.sourceConfigs == null || i.className == null)
					continue;
				for (SourceConfigPOJO<?> s : i.sourceConfigs) {
					if (s.key == null || s.configs == null)
						continue;
					imap.put((T) s.key, new ArrayList<SourceConfiguration>(Arrays.asList(s.configs)));
				}
				pmap.put((Class<? extends InputSource>) Class.forName(i.className), imap);
			}
			config.put(p.num, pmap);
		}
	}

	/**
	 * Returns a {@link Map} that stores the configuration for a player and a
	 * InputSource.
	 * 
	 * This can be used to read <b>and write</b> data from/to this
	 * {@link InputConfiguration}.
	 * 
	 * @param player player to get configuration for. If player doesn't exist yet, a
	 *               new player with that number is created.
	 * @param type   Class calling the method (to retrieve <b>its</b> stored values)
	 * @return a map storing the configuration for a player
	 */
	public Map<T, List<SourceConfiguration>> getConfiguration(int player, Class<? extends InputSource> type) {
		return config
				.computeIfAbsent(player,
						c -> new HashMap<Class<? extends InputSource>, Map<T, List<SourceConfiguration>>>())
				.computeIfAbsent(type, c -> new HashMap<T, List<SourceConfiguration>>());
	}

	/**
	 * Executes the handler for each player configured.
	 * 
	 * @param type    Class calling the method (to retrieve <b>its</b> stored
	 *                values)
	 * @param handler handler to execute (first argument is player number, second is
	 *                configuration)
	 */
	public void forEach(Class<? extends InputSource> type,
			BiConsumer<Integer, Map<T, List<SourceConfiguration>>> handler) {
		config.forEach(
				(k, v) -> handler.accept(k, v.computeIfAbsent(type, c -> new HashMap<T, List<SourceConfiguration>>())));
	}

	/**
	 * Clears all inputs for a player and a name.
	 * 
	 * @param player player to clear inputs for
	 * @param name   name to clear inputs for
	 */
	public void clearInput(int player, T name) {
		Map<Class<? extends InputSource>, Map<T, List<SourceConfiguration>>> pm = config.get(player);
		if (pm == null)
			return;
		pm.forEach((k, v) -> v.remove(name));
	}

	/**
	 * Cleans the {@link InputConfiguration} and removes duplicates.
	 * 
	 * This checks the equality of using {@link SourceConfiguration#equals(Object)}.
	 */
	public void clean() {
		config.forEach((player, classMaps) -> {
			classMaps.forEach((c, map) -> {
				map.replaceAll((key, list) -> {
					return list.stream().distinct().collect(Collectors.toList());
				});
			});
		});
	}

	/**
	 * Constructs a {@link ConfigPOJO} from this {@link InputConfiguration}.
	 * 
	 * @return a {@link ConfigPOJO} resembling this {@link InputConfiguration}
	 */
	private ConfigPOJO toPOJO() {
		ConfigPOJO c = new ConfigPOJO();
		c.players = config.entrySet().stream().map(e -> {
			PlayerPOJO p = new PlayerPOJO();
			p.num = e.getKey();
			p.inputSourceConfigs = e.getValue().entrySet().stream().map(e2 -> {
				InputSourcePOJO is = new InputSourcePOJO();
				is.className = e2.getKey().getName();
				is.sourceConfigs = e2.getValue().entrySet().stream().map(e3 -> {
					SourceConfigPOJO<T> sc = new SourceConfigPOJO<T>();
					sc.key = e3.getKey();
					sc.configs = e3.getValue().toArray(l -> new SourceConfiguration[l]);
					return sc;
				}).toArray(l -> new SourceConfigPOJO[l]);
				return is;
			}).toArray(l -> new InputSourcePOJO[l]);
			return p;
		}).toArray(l -> new PlayerPOJO[l]);
		return c;
	}

	/**
	 * Saves this {@link InputConfiguration} to a xml {@link String}.
	 * 
	 * @return xml {@link String}
	 * @throws JsonProcessingException
	 */
	public String save() throws JsonProcessingException {
		return new XmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true).writerWithDefaultPrettyPrinter()
				.writeValueAsString(toPOJO());
	}

	/**
	 * Saves this {@link InputConfiguration} to xml and writes it to an
	 * {@link OutputStream}.
	 * 
	 * @param s {@link OutputStream} to write xml to
	 * @throws IOException see {@link ObjectWriter#writeValue(OutputStream, Object)}
	 */
	public void save(OutputStream s) throws IOException {
		new XmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true).writerWithDefaultPrettyPrinter()
				.writeValue(s, toPOJO());
	}

	/**
	 * Saves this {@link InputConfiguration} to xml and writes it to a {@link File}.
	 * 
	 * @param f {@link File} to write to
	 * @throws IOException see {@link ObjectWriter#writeValue(File, Object)}
	 */
	public void save(File f) throws IOException {
		new XmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true).writerWithDefaultPrettyPrinter()
				.writeValue(f, toPOJO());
	}

	/**
	 * Loads an {@link InputConfiguration} from a xml {@link String} and types it to
	 * the given class.
	 * 
	 * @param <T> Class of identifier (to return typed {@link InputConfiguration})
	 * @param xml xml to load from
	 * @param c   Class of identifier (to return typed {@link InputConfiguration})
	 * @return {@link InputConfiguration} loaded from the given xml
	 * @throws JsonMappingException    see
	 *                                 {@link ObjectMapper#readValue(String, Class)}
	 * @throws JsonProcessingException see
	 *                                 {@link ObjectMapper#readValue(String, Class)}
	 * @throws ClassNotFoundException  When the stored class of an
	 *                                 {@link InputSource} could not be found
	 */
	public static <T> InputConfiguration<T> load(String xml, Class<T> c)
			throws JsonMappingException, JsonProcessingException, ClassNotFoundException {
		return new InputConfiguration<T>(new XmlMapper().readValue(xml, ConfigPOJO.class));
	}

	/**
	 * Loads an {@link InputConfiguration} from a xml {@link String}.
	 * 
	 * @param xml xml to load from
	 * @return {@link InputConfiguration} loaded from the given xml
	 * @throws JsonMappingException    see
	 *                                 {@link ObjectMapper#readValue(String, Class)}
	 * @throws JsonProcessingException see
	 *                                 {@link ObjectMapper#readValue(String, Class)}
	 * @throws ClassNotFoundException  When the stored class of an
	 *                                 {@link InputSource} could not be found
	 */
	public static InputConfiguration<?> load(String xml)
			throws JsonMappingException, JsonProcessingException, ClassNotFoundException {
		return new InputConfiguration<>(new XmlMapper().readValue(xml, ConfigPOJO.class));
	}

	/**
	 * Loads an {@link InputConfiguration} from a xml {@link InputStream} and types
	 * it to the given class.
	 * 
	 * @param <T> Class of identifier (to return typed {@link InputConfiguration})
	 * @param s   {@link InputStream} to read xml from
	 * @param c   Class of identifier (to return typed {@link InputConfiguration})
	 * @return {@link InputConfiguration} loaded from the given xml
	 * @throws IOException            see
	 *                                {@link ObjectMapper#readValue(String, Class)}
	 * @throws ClassNotFoundException When the stored class of an
	 *                                {@link InputSource} could not be found
	 */
	public static <T> InputConfiguration<T> load(InputStream s, Class<T> c) throws IOException, ClassNotFoundException {
		return new InputConfiguration<T>(new XmlMapper().readValue(s, ConfigPOJO.class));
	}

	/**
	 * Loads an {@link InputConfiguration} from a xml {@link InputStream}.
	 * 
	 * @param s {@link InputStream} to read xml from
	 * @return {@link InputConfiguration} loaded from the given xml
	 * @throws IOException            see
	 *                                {@link ObjectMapper#readValue(InputStream, Class)}
	 * @throws ClassNotFoundException When the stored class of an
	 *                                {@link InputSource} could not be found
	 */
	public static InputConfiguration<?> load(InputStream s) throws IOException, ClassNotFoundException {
		return new InputConfiguration<>(new XmlMapper().readValue(s, ConfigPOJO.class));
	}

	/**
	 * 
	 * Loads an {@link InputConfiguration} from a xml {@link File}.
	 * 
	 * @param <T> Class of identifier (to return typed {@link InputConfiguration})
	 * @param f   {@link File} to read the xml from
	 * @param c   Class of identifier (to return typed {@link InputConfiguration})
	 * @return {@link InputConfiguration} loaded from the given xml
	 * @throws IOException            see
	 *                                {@link ObjectMapper#readValue(File, Class)}
	 * @throws ClassNotFoundException When the stored class of an
	 *                                {@link InputSource} could not be found
	 */
	public static <T> InputConfiguration<T> load(File f, Class<T> c) throws IOException, ClassNotFoundException {
		return new InputConfiguration<T>(new XmlMapper().readValue(f, ConfigPOJO.class));
	}

	/**
	 * 
	 * Loads an {@link InputConfiguration} from a xml {@link File}.
	 * 
	 * @param f {@link File} to read the xml from
	 * @return {@link InputConfiguration} loaded from the given xml
	 * @throws IOException            see
	 *                                {@link ObjectMapper#readValue(File, Class)}
	 * @throws ClassNotFoundException When the stored class of an
	 *                                {@link InputSource} could not be found
	 */
	public static InputConfiguration<?> load(File f) throws IOException, ClassNotFoundException {
		return new InputConfiguration<>(new XmlMapper().readValue(f, ConfigPOJO.class));
	}

	@Override
	public String toString() {
		return config.toString();
	}

}

// ConfigPOJO:         PlayerPOJO[]
// PlayerPOJO:         Integer,                          InputSourcePOJO[]
// InputSourcePOJO:    Class<? extends InputSource>,     SourceConfigPOJO[]
// SourceConfigPOJO:   T,                                SourceConfiguration[]

/**
 * POJO for serializing {@link InputConfiguration}
 * 
 * @author Floeze
 *
 */
@JsonRootName("InputConfiguration")
final class ConfigPOJO {

	@JacksonXmlProperty(localName = "player")
	@JacksonXmlElementWrapper(useWrapping = false)
	public PlayerPOJO[] players;
}

/**
 * POJO for serializing {@link InputConfiguration}
 * 
 * @author Floeze
 *
 */
final class PlayerPOJO {
	@JacksonXmlProperty(isAttribute = true, localName = "num")
	public int num;

	@JacksonXmlProperty(localName = "configs")
	@JacksonXmlElementWrapper(useWrapping = false)
	public InputSourcePOJO[] inputSourceConfigs;
}

/**
 * POJO for serializing {@link InputConfiguration}
 * 
 * @author Floeze
 *
 */
final class InputSourcePOJO {

	@JacksonXmlProperty(isAttribute = true, localName = "for")
	public String className;

	@JacksonXmlProperty(localName = "key")
	@JacksonXmlElementWrapper(useWrapping = false)
	public SourceConfigPOJO<?>[] sourceConfigs;
}

/**
 * POJO for serializing {@link InputConfiguration}
 * 
 * @author Floeze
 *
 */
final class SourceConfigPOJO<T> {
	@JacksonXmlProperty(isAttribute = true, localName = "name")
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
	public T key;

	@JacksonXmlProperty(localName = "value")
	@JacksonXmlElementWrapper(useWrapping = false)
	public SourceConfiguration[] configs;
}