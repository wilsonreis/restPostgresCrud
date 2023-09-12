package br.com.wilson.restpostgrescrud.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AlunoEntradaDTO {
    @NotNull(message = "{nome} {jakarta.validation.constraints.NotNull.message}")
    @Size(min = 3, max= 25, message = "{nome} {jakarta.validation.constraints.Size.message}")
	private String nome;
    @NotNull(message = "{sobre.nome} {jakarta.validation.constraints.NotNull.message}")
    @Size(min = 3, max= 60, message = "{sobre.nome} {jakarta.validation.constraints.Size.message}")
    private String sobreNome;
   
    
	
	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getSobreNome() {
		return sobreNome;
	}



	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}


	    
}
