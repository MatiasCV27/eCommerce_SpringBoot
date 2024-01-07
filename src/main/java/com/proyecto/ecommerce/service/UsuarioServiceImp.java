package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImp implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public Optional<Usuario> findbyId(Integer id) {
        return usuarioRepository.findById(id);
    }
}
