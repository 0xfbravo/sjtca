package cacsv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javax.swing.JOptionPane;

public interface Alterdata {
	
	default void ImportarAutonomo(String Nome,String Cpf,String Matricula, String ValorServico, String Identidade,
								String OrgaoEmissor, String DataNascimento){
		if(Cpf.isEmpty() || Matricula.isEmpty() || Matricula.length() > 11 || Cpf.length() > 11){
			JOptionPane.showMessageDialog(null,"Problemas ao Cadastrar: "+Nome+". Este não será adicionado","Houston, we have a problem...",JOptionPane.WARNING_MESSAGE);
			return;
		}
		// Remover eventos feitos pelo Programa
		// delete from wdp.IM00738 where IdMovimento LIKE '%FCODE%'
		
		/* Conexão com SQL */
		//String urlAlterdata = "jdbc:sqlserver://177.207.207.178:49398;databaseName=ALTERDATA";
		String urlAlterdata = "jdbc:sqlserver://192.168.100.200:49398;databaseName=ALTERDATA"; /* Working DB */
		//String urlAlterdata = "jdbc:sqlserver://192.168.100.204:49398;databaseName=ALTERDATA"; /* Testing DB*/
		String userDBAlterdata = "sa";
		String passDBAlterdata = "#abc123#";
		String numeroEmpresa = "00738";
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(urlAlterdata,userDBAlterdata,passDBAlterdata);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		LocalDate localDate = LocalDate.now().withMonth(LocalDate.now().getMonthValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Cadastro do Autônomo
		// Tabela: wdp.CadAuto
		// O Autônomo já está cadastrado?
		try {
			if(!st.executeQuery("select * from wdp.CadAuto where CdMatricula='"+Matricula+"'").next()){
				try {
					st.executeUpdate("DECLARE @nascimento datetime;"
						+ "set @nascimento = (select CONVERT(datetime,'"+DataNascimento+"',103));"
						+ "INSERT INTO "
						+ "wdp.CadAuto(CdMatricula,NmSocio,DtNascimento,NrIdentidade,NrCPF,NmOrgaoExp,vlSalario,NrCategoria,CdRetencaoIRRF)"
						+ " values "
						+ "('"+Matricula+"',"
						+ "'"+Nome+"',"
						+ "@nascimento,"
						+ "'"+Identidade+"',"
						+ "'"+Cpf+"',"
						+ "'"+OrgaoEmissor+"',"
						+ "'"+ValorServico.replaceAll("\\.","").replaceAll("\\,",".")+"',"
						+ ""+13+","+"0588"+");");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,"Problemas ao Cadastrar: "+Nome,"Houston, we have a problem...",JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					if(JOptionPane.showConfirmDialog(null,"Deseja parar o processo?") == JOptionPane.OK_OPTION)
						System.exit(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Vínculo Empresa x Autônomo
		// Tabela: wdp.TB(NUMERO)
		// NUMERO == numero da empresa
		// O Autônomo já está vinculado?
		try {
			if(!st.executeQuery("select * from wdp.TB"+numeroEmpresa+" where CdMatricula='"+Matricula+"'").next()){
				try {
					st.executeUpdate("DECLARE @id int;"
							+ "set @id = (select max(IdentificaContrib) from wdp.TB"+numeroEmpresa+");"
							+ "insert into wdp.TB"+numeroEmpresa+"(IdentificaContrib,CdMatricula,StTipo,StPagaEscritorio,NmSituacao,TpRecolhimento,NrCategoria,stPagaSobreTabela,stFGTS,stINSS,stDeducao,stDomestEntraGFIP,NrCdRetencaoIRRF,CdCBONovo,StFuncionario,endereco_tipo,controlar_no_gerente_esocial)"+
							"values (@id+1,'"+Matricula+"','A','S','0-Contribuinte normal','M',13,'S','N','S',0,'N','0588','262505','N',0,1);");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,"Problemas ao Vincular: "+Nome, "Houston, we have a problem...",JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					if(JOptionPane.showConfirmDialog(null,"Deseja parar o processo?","Erro", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						System.exit(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Lançamento do Autônomo na Folha
		// Tabela: wdp.IM(NUMERO)
		// NUMERO == numero da empresa
		// GET 'IdentificaContrib'
		// Realizar lançamento do Mês
		Double servico = Double.parseDouble(ValorServico.replaceAll("\\.","").replaceAll("\\,","."));
		StringBuffer codigo = new StringBuffer("FCODE");
		codigo.append(Math.random() * 99999 + 140894);
			try {
				st.executeUpdate("DECLARE @inicio datetime;"+
					"DECLARE @fim datetime;"+
					"DECLARE @id int;"+
					"set @id = (select IdentificaContrib from wdp.TB"+numeroEmpresa+" where CdMatricula='"+Matricula+"');"+
					"set @inicio = (select CONVERT(datetime,'"+localDate.with(TemporalAdjusters.firstDayOfMonth()).format(formatter).toString()+"',103));"+
					"set @fim = (select CONVERT(datetime,'"+localDate.with(TemporalAdjusters.lastDayOfMonth()).format(formatter).toString()+"',103));"+
					"insert into wdp.IM"+numeroEmpresa+" values ('"+codigo+"',@id,@inicio,@fim,'A','"+Matricula+"','0010000003',"+servico+",NULL,0);");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Problemas ao Fazer Lançamento para: "+Nome, "Houston, we have a problem...",JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
				if(JOptionPane.showConfirmDialog(null,"Deseja parar o processo?","Erro", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					System.exit(1);
			}
	}
}
