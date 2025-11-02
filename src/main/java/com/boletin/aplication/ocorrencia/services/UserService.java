package com.boletin.application.ocorrencia.services;

import com.boletin.application.ocorrencia.interfaces.Crud;
import com.boletin.application.ocorrencia.models.UserModel;
import com.boletin.application.ocorrencia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements Crud<UserModel> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void Inserir(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void Update(Long id, UserModel userAtualizado) {
        userRepository.findById(id).ifPresent(userExistente -> {
            userExistente.setUserName(userAtualizado.getUserName());
            
            if (userAtualizado.getPassword() != null && !userAtualizado.getPassword().isBlank()) {
                userExistente.setPassword(passwordEncoder.encode(userAtualizado.getPassword()));
            }
            
            userRepository.save(userExistente);
        });
    }

    @Override
    public void Deletar(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserModel> Read() {
        return userRepository.findAll();
    }

    // MÉTODO EXTRA (opcional, mas útil)
    public UserModel buscarPorId(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}