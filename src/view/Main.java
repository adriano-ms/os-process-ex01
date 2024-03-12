package view;

import javax.swing.JOptionPane;

import controller.NetworksController;

public class Main {
	
	public static final String OPTIONS[] = {"IP Config","Ping"};


	public static void main(String[] args) {
		
		Object option = null;
		
		do {
			try {
				option = JOptionPane.showInputDialog(null, "Operations","Network Tools", JOptionPane.INFORMATION_MESSAGE, null, OPTIONS, OPTIONS[0]);
				NetworksController networksController = new NetworksController();
				if(option !=  null)
					if(option.toString() == "IP Config")
						networksController.ip();
					else
						networksController.ping("www.google.com.br");
				
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null,e.getMessage(), option.toString(), JOptionPane.ERROR_MESSAGE);
			}
		}while(!(option == null));
		
		System.out.println("\nEND");
	}
}
