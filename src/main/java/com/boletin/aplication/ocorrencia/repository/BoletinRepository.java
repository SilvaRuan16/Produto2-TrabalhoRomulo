package com.boletin.aplication.ocorrencia.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boletin.aplication.ocorrencia.models.BoletinModel;

@Repository
public interface BoletinRepository extends JpaRepository<BoletinModel, Long> {}