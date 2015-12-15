package cacsv;

public class Autonomo {
	private String nome;
	private String matricula;
	private String nascimento;
	private String identidade;
	private String cpf;
	private String valorServico;
	private int categoria;
	private String codRetencao;
	
	public Autonomo(String nome, String matricula, String nascimento, String identidade, String cpf, String valorServico){
		this.setNome(nome);
		this.setMatricula(matricula);
		this.setNascimento(nascimento);
		this.setIdentidade(identidade);
		this.setCpf(cpf);
		this.setValorServico(valorServico);
		this.setCategoria(13);
		this.setCodRetencao("0588");
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNascimento() {
		return nascimento;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getValorServico() {
		return valorServico;
	}

	public void setValorServico(String valorServico) {
		this.valorServico = valorServico;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public String getCodRetencao() {
		return codRetencao;
	}

	public void setCodRetencao(String codRetencao) {
		this.codRetencao = codRetencao;
	}
}
