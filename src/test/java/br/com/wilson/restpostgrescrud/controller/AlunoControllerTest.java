package br.com.wilson.restpostgrescrud.controller;

import br.com.wilson.restpostgrescrud.dto.AlunoEntradaDTO;
import br.com.wilson.restpostgrescrud.dto.AlunoSaidaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue; // Import para o assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PlatformTransactionManager transactionManager; // Injetando o gerenciador de transações
    

    private AlunoEntradaDTO alunoEntradaDTO;
    private UUID alunoId;  // Para rastrear o ID do aluno criado
	UUID alunoId2;
    @BeforeEach
    void setUp() {
        alunoEntradaDTO = new AlunoEntradaDTO();
        alunoEntradaDTO.setNome("John");
        alunoEntradaDTO.setSobreNome("Doe");
    }

    
    @AfterEach
    void tearDown() {
        // Exclui o aluno criado após cada teste
    	try {
            if (alunoId != null) {
                mockMvc.perform(delete("/aluno/{id}", alunoId))
                        .andExpect(status().isOk());
            }			
		} catch (Exception e) {
			// TODO: handle exception
		}

    }

    @Test
    void testCriarAluno() throws Exception {
        MvcResult createResult = extractedIncluirAluno();

        String createResponseContent = createResult.getResponse().getContentAsString();
        AlunoSaidaDTO alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();  // Rastreia o ID do aluno criado
    }
    @Test
    void testBuscarAluno() throws Exception {
        MvcResult result = extractedIncluirAluno();

        String responseContent = result.getResponse().getContentAsString();
        AlunoSaidaDTO alunoSaidaDTO = objectMapper.readValue(responseContent, AlunoSaidaDTO.class);

        mockMvc.perform(get("/aluno/{id}", alunoSaidaDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(alunoSaidaDTO.getId().toString()))
                .andExpect(jsonPath("$.nome").value(alunoSaidaDTO.getNome()))
                .andExpect(jsonPath("$.sobreNome").value(alunoSaidaDTO.getSobreNome()));
        
        String createResponseContent = result.getResponse().getContentAsString();
        alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();  // Rastreia o ID do aluno criado        
    }

    @Test
    void testBuscarAlunoNaoEncontrado() throws Exception {
        mockMvc.perform(get("/aluno/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testAtualizarAluno() throws Exception {
        MvcResult createResult = extractedIncluirAluno();

        String createResponseContent = createResult.getResponse().getContentAsString();
        AlunoSaidaDTO alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);

        AlunoEntradaDTO updatedAluno = new AlunoEntradaDTO();
        updatedAluno.setNome("Updated Name");
        updatedAluno.setSobreNome("Updated LastName");

        mockMvc.perform(put("/aluno/{id}", alunoSaidaDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAluno)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(alunoSaidaDTO.getId().toString()))
                .andExpect(jsonPath("$.nome").value(updatedAluno.getNome()))
                .andExpect(jsonPath("$.sobreNome").value(updatedAluno.getSobreNome()));
        
        createResponseContent = createResult.getResponse().getContentAsString();
        alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();  // Rastreia o ID do aluno criado        
        
    }
    
    @Test
    void testListarAlunos() throws Exception {
        MvcResult createResult = extractedIncluirAluno();

        String createResponseContent = createResult.getResponse().getContentAsString();
        AlunoSaidaDTO alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();

        // Teste para listar alunos
        MvcResult result = mockMvc.perform(get("/aluno"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<AlunoSaidaDTO> alunosListados = objectMapper.readValue(responseContent, new TypeReference<List<AlunoSaidaDTO>>() {});

        // Verifica se o aluno criado está na lista
        boolean alunoEncontrado = alunosListados.stream()
                .anyMatch(aluno -> aluno.getId().equals(alunoId));

        assertTrue(alunoEncontrado);
        
        createResponseContent = createResult.getResponse().getContentAsString();
        alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();  // Rastreia o ID do aluno criado        
        
    }    
    
    @Test
    void testAtualizarAlunoEncontrado() throws Exception {
        MvcResult createResult = extractedIncluirAluno();

        String createResponseContent = createResult.getResponse().getContentAsString();
        AlunoSaidaDTO alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();

        mockMvc.perform(put("/aluno/{id}", alunoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoEntradaDTO)))
                .andExpect(status().isOk());
        
        createResponseContent = createResult.getResponse().getContentAsString();
        alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
        alunoId = alunoSaidaDTO.getId();  // Rastreia o ID do aluno criado        
        
    }    
    
    
    @Test
    void testExcluirAluno() throws Exception {
        // Realiza a inclusão do aluno em uma transação
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                // Inclui o aluno em uma transação separada
                MvcResult createResult = extractedIncluirAluno();
            	
                String createResponseContent = createResult.getResponse().getContentAsString();
                AlunoSaidaDTO alunoSaidaDTO = objectMapper.readValue(createResponseContent, AlunoSaidaDTO.class);
                alunoId2 = alunoSaidaDTO.getId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });

        // Agora a transação está encerrada, então a exclusão não deve ter problemas
        mockMvc.perform(delete("/aluno/{id}", alunoId2))
                .andExpect(status().isOk());
    }

	private MvcResult extractedIncluirAluno() throws Exception, JsonProcessingException {
		MvcResult createResult = mockMvc.perform(post("/aluno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoEntradaDTO)))
                .andExpect(status().isCreated())
                .andReturn();
		return createResult;
	}
    
    @Test
    void testExcluirAlunoEncontrado() throws Exception {
        mockMvc.perform(delete("/aluno/{id}", "def611f0-05f9-4ce0-9d88-a1b558ce35c8"))
                .andExpect(status().isNotFound());
                
    }
    
}
