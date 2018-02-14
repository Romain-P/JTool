package org.jtool.core;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jtool.frontend.MainView;

import java.awt.Toolkit;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private final Container defaultView;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		applyDefaultSystemLook();
		
		EventQueue.invokeLater(() -> {
			try {
				Main frame = new Main();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setTitle("JTool");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		
		this.defaultView = new MainView();
		setContentPane(this.defaultView);
	}

	/**
	 *  Apply the default system look e.g beautiful windows for WindowsOs.
	 *  By default, a java user-interface application is taking built-in java style-sheets, that are for the most so old and ugly.
	 */
	private static void applyDefaultSystemLook() {
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
	}
}
