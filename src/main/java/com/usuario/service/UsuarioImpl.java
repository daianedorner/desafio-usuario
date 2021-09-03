package com.usuario.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.usuario.excecao.ErroSistema;
import com.usuario.model.Usuario;
import com.usuario.model.dto.UsuarioDTO;
import com.usuario.repository.UsuarioRepository;
import com.usuario.security.GenerateHashUtil;
import com.usuario.security.JwtTokenUtil;

@Service
public class UsuarioImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	@Transactional
	public Usuario save(Usuario user) throws Exception {
		// consultar e-mail cadastrado
		List<Usuario> lista = this.usuarioRepository.findAllEmail(user.getEmail());
		if (lista != null && lista.size() > 0)
			throw new Exception("E-mail já existente");

		// criptografia de senha
		String senhaCriptografada = GenerateHashUtil.generateHash(user.getPassword());
		user.setPassword(senhaCriptografada);

		// gerar token
		user.setToken(this.getToken(user));

		return this.usuarioRepository.save(user);
	}

	private String getToken(Usuario user) {
		final UserDetails userDetails = new User(user.getEmail(), user.getPassword(), new ArrayList<>());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return token;
	}

	@Override
	@Transactional
	public ResponseEntity<Object> acessar(UsuarioDTO user) throws Exception {
		// consultar e-mail cadastrado
		List<Usuario> lista = this.usuarioRepository.findAllEmail(user.getEmail());
		if (lista != null && lista.size() > 0) {
			String senhaCriptografada = GenerateHashUtil.generateHash(user.getPassword());
			if (senhaCriptografada.equals(lista.get(0).getPassword())) {
				Usuario usuario = lista.get(0);

				// gerar novo token
				usuario.setToken(this.getToken(usuario));
				usuario.setLastLogin(Calendar.getInstance().getTime());
				this.usuarioRepository.save(usuario);

				return new ResponseEntity<>(usuario, HttpStatus.OK);
			} else
				return new ResponseEntity<>(new ErroSistema("Usuário e/ou senha inválidos"), HttpStatus.UNAUTHORIZED);
		} else
			return new ResponseEntity<>(new ErroSistema("Usuário e/ou senha inválidos"), HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<Object> consultar(long id) {
		// Caso o token exista, e seja o mesmo persistido, buscar o usuário pelo id
		// passado no path.
		Usuario user = this.usuarioRepository.getById(id);
		if (user == null) {
			// Caso o usuário não seja encontrado pelo id, retornar com status e mensagem de
			// erro apropriados.
			return new ResponseEntity<>(new ErroSistema("Não autorizado"), HttpStatus.UNAUTHORIZED);
		}

		int diff = this.minutesDiff(Calendar.getInstance().getTime(), user.getLastLogin());
		if (diff > 30) {
			/*
			 * Verificar se o último login foi há MENOS de 30 minutos atrás. Caso não seja
			 * há MENOS de 30 minutos atrás, retornar erro com status apropriado e com a
			 * mensagem "Sessão inválida".
			 */
			return new ResponseEntity<>(new ErroSistema("Sessão inválida"), HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	private int minutesDiff(Date earlierDate, Date laterDate) {
		if (earlierDate == null || laterDate == null)
			return 0;

		return (int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000));
	}

}
