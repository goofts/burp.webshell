package com.ms509.ui;

import java.awt.EventQueue;
import com.ms509.util.InitConfig;

public class Cknife {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InitConfig();
				new MainFrame();
			}
		});
	}
}
