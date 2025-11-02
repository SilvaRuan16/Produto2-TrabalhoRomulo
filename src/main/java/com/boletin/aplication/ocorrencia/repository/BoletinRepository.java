package com.boletin.aplication.ocorrencia.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boletin.aplication.ocorrencia.models.BoletinModel;

@Repository
public interface BoletinRepository extends JpaRepository<BoletinModel, Long> {
   @Query("SELECT b FROM BoletinModel b JOIN FETCH b.usuarioModel")
   List<BoletinModel> findAllWithUsuario();
}