# Input4J

Input4J is a [Godot](https://github.com/godotengine/godot)-like Input System for Java.

## Description

Input4J aims to bring an easy to use input-system to Java.
It is inspired by the input-system the Godot game engine is using.
Input4J lets you add unified input events using easy identifiers (like Strings) and add input actions to them, so you don't have to listen to them separately.

## Features

- receive input events based on easy to use names
- save/load input configs

### Planned

- keyboard/mouse input from AWT/swing
- controller input using [libSDL](https://www.libsdl.org/)
- *swing-based input change GUI* (not certain)

## [Getting started](https://github.com/FloezeTv/Input4J/wiki/Getting-started)

### Installation

#### Maven

To install this library, you have to clone this repository and open the folder you cloned it to.  
In the folder, you have to run `mvn install`, which will install the library to your local Maven repository.

After that, you have to add the following to the `pom.xml` of your project:
```xml
<dependencies>

    <!-- other dependencies -->

    <dependency>
        <groupId>tv.floeze</groupId>
        <artifactId>Input4J</artifactId>
        <version>0.1.1-SNAPSHOT</version>
    </dependency>

    <!-- other dependencies -->

</dependencies>
```

### Usage

#### [Using the library with input sources](https://github.com/FloezeTv/Input4J/wiki/Getting-started:-using-the-library)

For this example, we will assume that you have defined your own custom `InputSource`, because this library currently doesn't have any on its own.

Here is a basic configuration for our InpuSource.
Remember to change `package` to your package.

```xml
<?xml version='1.1' encoding='UTF-8'?>
<InputConfiguration>
  <player num="0">
    <configs for="package.ExampleInputSource">
      <key name="input1">
        <value class="package.ExampleInputSource..Config">
          <value>256</value>
        </value>
      </key>
    </configs>
  </player>
</InputConfiguration>
```

```java
import java.io.IOException;

import tv.floeze.Input4J.Input4J;

public class Example {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// Create a new instance of Input4J and load a default configuration from a file (config.xml in same package).
		Input4J<String> input4j = new Input4J<String>(Example.class.getResourceAsStream("config.xml"));
		
		// Add a InputSource and name it 'source1'.
		input4j.addInputSource("source1", new ExampleInputSource.Builder(1.5d));
		
		// Enable all InputSources.
		// If you only want to enable specific InputSources, use input4j.enable("source1", "source2", ...);
		input4j.enableAll();
		
		// You can get the current InputMap by calling input4j.update().
		// We'll just print the InputMap.
		System.out.println("InputMap: " + input4j.update());
		
		// Save current state of inputs, to later configure a new input.
		// If you only want to save the inputs on specific InputSources, use input4j.saveInputs("source1", "source2", ...);
		input4j.saveInputs();
		
		// Set a new input.
		// This will make the InputSources check if something changed since the last call of input4j.saveInputs() and add that input.
		input4j.setInput(0, "input2", (short) 128);
		
		// Let's again print the InputMap to see the current state of inputs.
		// You should see a newly added value for "input2"
		System.out.println("InputMap: " + input4j.update());
		
		// You can save the new config with the added input by calling one of Input4J's save methods.
		// We will just print it to the console.
		// Normally, you would save this and load it at the start of your application.
		System.out.println("Configuration:\n" + input4j.save());
	}

}
```

When running this example the output should look like this:

```
InputMap: {0={input1=384}}
InputMap: {0={input2=192, input1=384}}
Configuration:
<?xml version='1.1' encoding='UTF-8'?>
<InputConfiguration>
  <player num="0">
    <configs for="package.ExampleInputSource">
      <key name="input2">
        <value class="package.ExampleInputSource..Config">
          <value>128</value>
        </value>
      </key>
      <key name="input1">
        <value class="package.ExampleInputSource..Config">
          <value>256</value>
        </value>
      </key>
    </configs>
  </player>
</InputConfiguration>
```

The first line is the InputMap with only the configuration from the loaded config.  
The second line is the InputMap with the newly added configuration.  
The rest is the new configuration, that would normally be saved and loaded the next time.

#### [Defining a custom input source](https://github.com/FloezeTv/Input4J/wiki/Getting-started:-create-your-own-InputSource)

To learn how to define your own input source, got to the [wiki](https://github.com/FloezeTv/Input4J/wiki/Getting-started:-create-your-own-InputSource).

## Folders

### `core`

This is the core library of Input4J.
It contains the code used to interact with Input4J, but no input source of its own.  
To get an input source, you have to define one yourself.

## Branches

| Branch name                | branch from | merge into      | description                               |
| -------------------------- | ----------- | --------------- | ----------------------------------------- |
| **master**                 |             |                 | current released version                  |
| **develop**                |             |                 | current development version               |
| **release/`version`**      | develop     | master, develop | prepares for releases (bump version, ...) |
| **fix/`issue`-`name`**     | develop     | develop         | fix for a bug                             |
| **hotfix/`issue`-`name`**  | master      | master, develop | hotfix for a currently released version   |
| **feature/`issue`-`name`** | develop     | develop         | new feature                               |

With:
- `version`: a release version of the project
- `issue`: a issue reference that tracks that bug/hotfix/feature
- `name`: a short descriptive name of the bug/hotfix/feature