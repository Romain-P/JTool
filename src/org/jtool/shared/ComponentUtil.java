package org.jtool.shared;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	/**
	 * Perform an action on multiples components when a key on the keyboard is pressed and released
	 * 
	 * @param action a runnable that will be applied for each key pressed on each given component
	 * @param components the listening components
	 */
	public static void onKeyReleased(Runnable action, Component... components) {
		for (Component component: components) {
			component.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					action.run();
				}
			});
		}
	}
	
	/**
	 * Perform an action on multiples components when the mouse is released
	 * 
	 * @param action a runnable that will be applied when the mouse is released on a component
	 * @param components the listening components
	 */
	public static void onMouseReleased(Runnable action, Component... components) {
		for (Component component: components) {
			component.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					action.run();
				}
			});
		}
	}
}
