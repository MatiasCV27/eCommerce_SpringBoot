package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.Usuario;

import java.util.Optional;

public interface IUsuarioService {

    Optional<Usuario> findbyId(Integer id);

    Usuario save(Usuario usuario);

    Optional<Usuario> findByEmail(String email);

}
