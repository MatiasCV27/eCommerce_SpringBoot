package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImp implements IUsuarioService {

    @Autowired
    private IUsuarioRepository IUsuarioRepository;


    @Override
    public List<Usuario> findAll() {
        return IUsuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findbyId(Integer id) {
        return IUsuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return IUsuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return IUsuarioRepository.findByEmail(email);
    }
}
