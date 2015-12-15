package cacsv;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CACSV {

	@SuppressWarnings("unused")
	public static void main(String[] args){
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
	    catch (UnsupportedLookAndFeelException e) {}
	    catch (ClassNotFoundException e) {}
	    catch (InstantiationException e) {}
	    catch (IllegalAccessException e) {}
		
		Janela j = new Janela();
	}
}
