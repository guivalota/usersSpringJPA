package com.example.arena.arena.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.arena.arena.entity.User;
import com.example.arena.arena.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmailAndSenha(String email, String senha) {
        return userRepository.findByEmailAndSenha(email, senha);
    }
}
