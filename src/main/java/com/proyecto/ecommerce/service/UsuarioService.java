package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> findbyId(Integer id);

}
