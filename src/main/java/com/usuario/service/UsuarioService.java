package com.usuario.service;

import org.springframework.http.ResponseEntity;

import com.usuario.model.Usuario;
import com.usuario.model.dto.UsuarioDTO;

public interface UsuarioService {

	Usuario save(Usuario user) throws Exception;

	ResponseEntity<Object> acessar(UsuarioDTO user) throws Exception;

	ResponseEntity<Object> consultar(long id);

}
