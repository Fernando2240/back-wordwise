package com.poo.wordwise.security;

import com.poo.wordwise.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticationService implements UserDetailsService {

    //UserDetailsService es una clase q ya usa spring para autenticar usuarios
    private IUsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmailAndActivoTrue(email);
    }

    @Autowired
    public void setUserRepository(IUsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }
}
