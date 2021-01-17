module Input4J {
	exports tv.floeze.Input4J.example;
	exports tv.floeze.Input4J;
	
	opens tv.floeze.Input4J to com.fasterxml.jackson.databind;

	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.dataformat.xml;
	requires java.xml;
}