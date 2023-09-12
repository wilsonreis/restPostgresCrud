package br.com.wilson.restpostgrescrud.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wilson.restpostgrescrud.models.AlunoEntity;

public interface AlunoRepository extends JpaRepository<AlunoEntity, UUID> {

}
