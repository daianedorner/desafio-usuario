package com.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuario.model.Usuario;
import com.usuario.model.dto.UsuarioDTO;
import com.usuario.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping
	public ResponseEntity<Usuario> save(@RequestBody Usuario user) throws Exception {
		try {
			Usuario userRetorno = this.usuarioService.save(user);
			return new ResponseEntity<>(userRetorno, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@PostMapping(value = "/acessar")
	public ResponseEntity<Object> acessar(@RequestBody UsuarioDTO user) throws Exception {
		try {
			return this.usuarioService.acessar(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@GetMapping(value = "/consultar/{id}")
	public ResponseEntity<Object> consultar(@PathVariable("id") long id) throws Exception {
		try {
			return this.usuarioService.consultar(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
