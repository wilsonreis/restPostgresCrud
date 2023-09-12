package br.com.wilson.restpostgrescrud.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.wilson.restpostgrescrud.dto.AlunoEntradaDTO;
import br.com.wilson.restpostgrescrud.dto.AlunoSaidaDTO;
import br.com.wilson.restpostgrescrud.models.AlunoEntity;
import br.com.wilson.restpostgrescrud.repository.AlunoRepository;
import br.com.wilson.restpostgrescrud.service.AlunoService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/aluno")
@Transactional(timeout = 2)
public class AlunoController {
	
	private final AlunoService alunoService;
	
	private ModelMapper modelMapper;	
	
	private final AlunoRepository alunoRepository;

	
	public AlunoController(AlunoService alunoService, AlunoRepository alunoRepository, ModelMapper modelMapper) {
		this.alunoService = alunoService;
		this.alunoRepository = alunoRepository;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping	
	public ResponseEntity<AlunoSaidaDTO> criarAluno(@RequestHeader(name = "Accept-Language", required = false) Locale value ,@Valid  @RequestBody AlunoEntradaDTO aluno ) {
		AlunoSaidaDTO alunoSaidaDTO = alunoService.criarAluno(aluno);
		return new ResponseEntity<>(alunoSaidaDTO, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<AlunoSaidaDTO>> listarAlunos() {		
		return new ResponseEntity<>(alunoRepository.findAll().stream().map(aluno -> modelMapper.map(aluno, AlunoSaidaDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AlunoSaidaDTO> buscarAluno(@PathVariable("id") UUID id) {

		Optional<AlunoEntity> alunoEntity  =  alunoRepository.findById(id);
		if (alunoEntity.isPresent()) {		
			return new ResponseEntity<>(modelMapper.map(alunoEntity.get(), AlunoSaidaDTO.class), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AlunoSaidaDTO> atualizarAluno(@PathVariable("id") UUID id, @RequestBody AlunoEntradaDTO aluno) {
		Optional<AlunoEntity> alunoSendoAtualizadoExistir = alunoRepository.findById(id);
		if (alunoSendoAtualizadoExistir.isPresent()) { 
			AlunoEntity alunoEntity = alunoSendoAtualizadoExistir.get();			
			alunoEntity.setDataAlteracao(new Timestamp(System.currentTimeMillis()));
			modelMapper.map(aluno, alunoEntity);
			alunoRepository.saveAndFlush(alunoEntity);			
			return  new ResponseEntity<>(modelMapper.map(alunoEntity, AlunoSaidaDTO.class), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirAluno(@PathVariable("id") UUID id) {
		Optional<AlunoEntity> alunoEntity  =  alunoRepository.findById(id);
		if (alunoEntity.isPresent()) {
			alunoRepository.delete(alunoEntity.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}	
	
}
