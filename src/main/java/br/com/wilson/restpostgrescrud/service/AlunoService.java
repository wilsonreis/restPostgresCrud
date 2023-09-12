package br.com.wilson.restpostgrescrud.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wilson.restpostgrescrud.dto.AlunoEntradaDTO;
import br.com.wilson.restpostgrescrud.dto.AlunoSaidaDTO;
import br.com.wilson.restpostgrescrud.models.AlunoEntity;
import br.com.wilson.restpostgrescrud.repository.AlunoRepository;



@Service
public class AlunoService {
	private final AlunoRepository alunoRepository;

	@Autowired
	private ModelMapper modelMapper;	

	public AlunoService(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}
	
	@Transactional
	public AlunoSaidaDTO criarAluno(AlunoEntradaDTO aluno) {
		AlunoEntity alunoEntity = new AlunoEntity(aluno);
		alunoRepository.save(alunoEntity);
		return modelMapper.map(alunoEntity, AlunoSaidaDTO.class);	
	}
	

}
