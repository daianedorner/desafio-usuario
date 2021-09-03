package com.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.usuario.model.Telefone;
import com.usuario.model.Usuario;
import com.usuario.model.dto.UsuarioDTO;
import com.usuario.service.UsuarioService;

@SpringBootTest
class DesafioUsuarioApplicationTests {

	@Autowired
	private UsuarioService cadastroUsuarioService;

	@Test
	void salvarSucesso() {
		try {
			List<Telefone> telefones = new ArrayList<>();
			Telefone tel = new Telefone();
			tel.setDdd("81");
			tel.setNumber("88888888");
			telefones.add(tel);

			Usuario user = new Usuario();
			user.setName("João da Silva");
			user.setEmail("joao@silva.org");
			user.setPassword("hunter2");
			user.setPhones(telefones);

			Usuario userReturn = this.cadastroUsuarioService.save(user);

			System.out.println(">>>>>>>>>>>>>>>>>>>>> ID " + userReturn.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void salvarErro() {
		try {
			List<Telefone> telefones = new ArrayList<>();
			Telefone tel = new Telefone();
			tel.setDdd("81");
			tel.setNumber("88888888");
			telefones.add(tel);

			Usuario user = new Usuario();
			user.setName("João da Silva");
			user.setEmail("joao@silva.org");
			user.setPassword("hunter2");
			user.setPhones(telefones);

			Usuario userReturn = this.cadastroUsuarioService.save(user);

			Usuario user2 = new Usuario();
			user2.setName("João da Silva");
			user2.setEmail("joao@silva.org");
			user2.setPassword("hunter2");
			user2.setPhones(telefones);

			Usuario userReturn2 = this.cadastroUsuarioService.save(user2);

			System.out.println(">>>>>>>>>>>>>>>>>>>>> ID " + userReturn2.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void acessar() {
		try {
			List<Telefone> telefones = new ArrayList<>();
			Telefone tel = new Telefone();
			tel.setDdd("81");
			tel.setNumber("88888888");
			telefones.add(tel);

			Usuario user = new Usuario();
			user.setName("João da Silva");
			user.setEmail("joao@silva.org");
			user.setPassword("hunter2");
			user.setPhones(telefones);

			Usuario userReturn = this.cadastroUsuarioService.save(user);

			UsuarioDTO dto = new UsuarioDTO();
			dto.setEmail("joao@silva.org");
			dto.setPassword("hunter2");

			ResponseEntity<Object> usuarioLogado = this.cadastroUsuarioService.acessar(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void acessarNaoAutorizado() {
		try {
			List<Telefone> telefones = new ArrayList<>();
			Telefone tel = new Telefone();
			tel.setDdd("81");
			tel.setNumber("88888888");
			telefones.add(tel);

			Usuario user = new Usuario();
			user.setName("João da Silva");
			user.setEmail("joao@silva.org");
			user.setPassword("hunter2");
			user.setPhones(telefones);

			Usuario userReturn = this.cadastroUsuarioService.save(user);

			UsuarioDTO dto = new UsuarioDTO();
			dto.setEmail(userReturn.getEmail());
			dto.setPassword("1234");

			ResponseEntity<Object> usuarioLogado = this.cadastroUsuarioService.acessar(dto);

			if (usuarioLogado.getStatusCode() != HttpStatus.OK) {
				assertEquals(401, usuarioLogado.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("Usuário e/ou senha inválidos", e.getMessage());
		}
	}

	@Test
	void consultar() {
		try {
			List<Telefone> telefones = new ArrayList<>();
			Telefone tel = new Telefone();
			tel.setDdd("81");
			tel.setNumber("88888888");
			telefones.add(tel);

			Usuario user = new Usuario();
			user.setName("João da Silva");
			user.setEmail("joao@silva.org");
			user.setPassword("hunter2");
			user.setPhones(telefones);

			Usuario userReturn = this.cadastroUsuarioService.save(user);

			ResponseEntity<Object> usuarioLogado = this.cadastroUsuarioService.consultar(userReturn.getId());

			if (usuarioLogado.getStatusCode() != HttpStatus.OK) {
				assertEquals(401, usuarioLogado.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("Usuário e/ou senha inválidos", e.getMessage());
		}
	}

	@Test
	void consultarNaoAutorizado() {
		try {

			ResponseEntity<Object> usuarioLogado = this.cadastroUsuarioService.consultar(999999);

			if (usuarioLogado.getStatusCode() != HttpStatus.OK) {
				assertEquals(401, usuarioLogado.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(401, e.getMessage());
		}
	}

}
