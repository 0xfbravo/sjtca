package cacsv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Janela extends JFrame implements Alterdata{
	
	@SuppressWarnings("resource")
	public Janela(){
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("FCODE DEVELOPMENT STUDIO (.csv)", "csv");
		jfc.setCurrentDirectory(new java.io.File("."));
		jfc.setDialogTitle("Selecione o arquivo dos Autônomos (.CSV)");
		jfc.setVisible(true);
		jfc.setFileFilter(filter);
		if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			String linha = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jfc.getSelectedFile().getAbsoluteFile()),"ISO-8859-1"));
				JFrame jf = new JFrame("Aguarde...");
				jf.setLocationRelativeTo(null);
				jf.setSize(300,50);
				jf.setVisible(true);
				while((linha = br.readLine()) != null){
					String[] split = linha.split(";");
					System.out.println("Processando: "+split[2]+" ...");
					ImportarAutonomo(split[2].replaceAll("\"",""),
							split[3].replaceAll("\\.","").replaceAll("\\-","").replaceAll("\"",""),
							split[4].replaceAll("\\.","").replaceAll("\\-","").replaceAll("\"",""),
							split[5].replaceAll("\"",""),
							"123456","FCODE",
							split[11].replaceAll("\"",""));
				}
				jf.setVisible(false);
				JOptionPane.showMessageDialog(null,"Autônomos inseridos", "Todos os autônomos foram inseridos, favor realizar verificação.",JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
