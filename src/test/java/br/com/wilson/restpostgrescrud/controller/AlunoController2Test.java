package br.com.wilson.restpostgrescrud.controller;


import br.com.wilson.restpostgrescrud.dto.AlunoEntradaDTO;
import br.com.wilson.restpostgrescrud.dto.AlunoSaidaDTO;
import br.com.wilson.restpostgrescrud.models.AlunoEntity;
import br.com.wilson.restpostgrescrud.repository.AlunoRepository;
import br.com.wilson.restpostgrescrud.service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlunoController2Test {

    private AlunoService alunoService;
    private AlunoRepository alunoRepository;
    private ModelMapper modelMapper;
    private AlunoController alunoController;

    @BeforeEach
    void setUp() {
        alunoService = mock(AlunoService.class);
        alunoRepository = mock(AlunoRepository.class);
        modelMapper = mock(ModelMapper.class);
        alunoController = new AlunoController(alunoService, alunoRepository, modelMapper);
    }

    @Test
    void criarAluno_shouldReturnCreatedStatus() {
        AlunoEntradaDTO alunoEntradaDTO = new AlunoEntradaDTO();
        AlunoSaidaDTO alunoSaidaDTO = new AlunoSaidaDTO();
        when(alunoService.criarAluno(alunoEntradaDTO)).thenReturn(alunoSaidaDTO);

        ResponseEntity<AlunoSaidaDTO> response = alunoController.criarAluno(Locale.ENGLISH, alunoEntradaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(alunoSaidaDTO, response.getBody());
    }

    @Test
    void listarAlunos_shouldReturnListOfAlunos() {
        AlunoEntity alunoEntity = new AlunoEntity();
        AlunoSaidaDTO alunoSaidaDTO = new AlunoSaidaDTO();
        when(alunoRepository.findAll()).thenReturn(List.of(alunoEntity));
        when(modelMapper.map(alunoEntity, AlunoSaidaDTO.class)).thenReturn(alunoSaidaDTO);

        ResponseEntity<List<AlunoSaidaDTO>> response = alunoController.listarAlunos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(alunoSaidaDTO), response.getBody());
    }

    @Test
    void buscarAluno_shouldReturnAlunoIfExists() {
        UUID id = UUID.randomUUID();
        AlunoEntity alunoEntity = new AlunoEntity();
        AlunoSaidaDTO alunoSaidaDTO = new AlunoSaidaDTO();
        when(alunoRepository.findById(id)).thenReturn(Optional.of(alunoEntity));
        when(modelMapper.map(alunoEntity, AlunoSaidaDTO.class)).thenReturn(alunoSaidaDTO);

        ResponseEntity<AlunoSaidaDTO> response = alunoController.buscarAluno(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alunoSaidaDTO, response.getBody());
    }

    @Test
    void buscarAluno_shouldReturnNoContentIfNotExists() {
        UUID id = UUID.randomUUID();
        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<AlunoSaidaDTO> response = alunoController.buscarAluno(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void atualizarAluno_shouldReturnUpdatedAlunoIfExists() {
        UUID id = UUID.randomUUID();
        AlunoEntradaDTO alunoEntradaDTO = new AlunoEntradaDTO();
        AlunoEntity alunoEntity = new AlunoEntity();
        AlunoSaidaDTO alunoSaidaDTO = new AlunoSaidaDTO();
        when(alunoRepository.findById(id)).thenReturn(Optional.of(alunoEntity));
//        doReturn(alunoEntity).when(modelMapper).map(alunoEntradaDTO, alunoEntity);
        when(alunoRepository.saveAndFlush(alunoEntity)).thenReturn(alunoEntity);
        when(modelMapper.map(alunoEntity, AlunoSaidaDTO.class)).thenReturn(alunoSaidaDTO);

        ResponseEntity<AlunoSaidaDTO> response = alunoController.atualizarAluno(id, alunoEntradaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alunoSaidaDTO, response.getBody());
    }

    @Test
    void atualizarAluno_shouldReturnNoContentIfNotExists() {
        UUID id = UUID.randomUUID();
        AlunoEntradaDTO alunoEntradaDTO = new AlunoEntradaDTO();
        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<AlunoSaidaDTO> response = alunoController.atualizarAluno(id, alunoEntradaDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void excluirAluno_shouldReturnOkIfExists() {
        UUID id = UUID.randomUUID();
        AlunoEntity alunoEntity = new AlunoEntity();
        when(alunoRepository.findById(id)).thenReturn(Optional.of(alunoEntity));

        ResponseEntity<Void> response = alunoController.excluirAluno(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(alunoRepository, times(1)).delete(alunoEntity);
    }

    @Test
    void excluirAluno_shouldReturnNotFoundIfNotExists() {
        UUID id = UUID.randomUUID();
        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = alunoController.excluirAluno(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}