package com.boletin.aplication.ocorrencia.services;

import com.boletin.aplication.ocorrencia.interfaceMETHOD.Crud;
import com.boletin.application.ocorrencia.interfaces.Crud;
import com.boletin.application.ocorrencia.models.BoletinModel;
import com.boletin.application.ocorrencia.repository.BoletinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public void Update(Long id, UserModel userAtualizado) {
        return userRepository.findById(id).map(userExistente -> {
            userExistente.setUser(userAtualizado.getUser());
            userExistente.setPassword(userAtualizado.getPassword());
            return userRepository.save(userExistente);
        }).orElse(null);
    }

    @Override
    public void Deletar(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void Read() {
        return userRepository.findAll();
    }
}