package org.jtool.shared;

import java.awt.Component;
import java.awt.Container;

public final class ComponentUtil {
	/**
	 * Avoid the class instantiation defining a private constructor.
	 */
	private ComponentUtil() {}
	
	
	/**
	 * Add multiples components to a given container.
	 * 
	 * @param container an awt container
	 * @param components that are gonna be added to the given container
	 */
	public static void add(Container container, Component...components) {
		for (Component component: components)
			container.add(component);
	}
}
