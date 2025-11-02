package com.boletin.aplication.ocorrencia.services;

import com.boletin.aplication.ocorrencia.interfaceMETHOD.Crud;
import com.boletin.aplication.ocorrencia.models.BoletinModel;
import com.boletin.aplication.ocorrencia.repository.BoletinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BoletinService implements Crud<BoletinModel> {

    private final BoletinRepository boletinRepository;

    @Autowired
    public BoletinService(BoletinRepository boletinRepository) {
        this.boletinRepository = boletinRepository;
    }

    @Override
    public void Inserir(BoletinModel boletin) {
        boletinRepository.save(boletin);
    }

    @Override
    public void Update(Long id, BoletinModel boletinAtualizado) {
        Optional<BoletinModel> boletimOptional = boletinRepository.findById(id);

        if (boletimOptional.isPresent()) {

            BoletinModel boletimExistente = boletimOptional.get();

            boletimExistente.setTitle(boletinAtualizado.getTitle());
            boletimExistente.setDescription(boletinAtualizado.getDescription());

            boletinRepository.save(boletimExistente);

        }
    }

    @Override
    public void Deletar(Long id) {
        boletinRepository.deleteById(id);
    }

    @Override
    public List<BoletinModel> Read() { 
        return boletinRepository.findAllWithUsuario();
    }
    
    public BoletinModel buscarPorId(Long id) {
        return boletinRepository.findById(id).orElse(null);
    }
}