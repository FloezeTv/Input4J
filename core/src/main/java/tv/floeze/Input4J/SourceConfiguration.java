package tv.floeze.Input4J;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Classes storing information with {@link InputConfiguration} implement this
 * interface.
 * 
 * @author Floeze
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public interface SourceConfiguration {
}
