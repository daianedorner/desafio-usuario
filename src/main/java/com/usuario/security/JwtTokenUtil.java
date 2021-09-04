package com.usuario.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.usuario.model.Usuario;
import com.usuario.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	@Autowired
	private UsuarioRepository cadastroUsuarioRepository;

	private static final long serialVersionUID = -7368146630387159295L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	private static String secret = "usersecretforjwtauthenticatedesafioconcretedesafioconcretedesafioconcretedesafioconcrete";

	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	// retorna o username do token jwt
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);

	}

	// para retornar qualquer informação do token nos iremos precisar da secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// gera token para user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// Cria o token e devine tempo de expiração pra ele
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			// faz parse do token
			String user = Jwts.parser().setSigningKey(secret).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}

	public boolean validateToken(String authToken, Long id) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);

			String username = getUsernameFromToken(authToken);

			// Caso o token seja diferente do persistido, retornar erro com status
			// apropriado e com a mensagem "Não autorizado".
			List<Usuario> user = this.cadastroUsuarioRepository.findAllEmail(username);
			if (user != null && user.size() > 0) {
				if (user.get(0).getToken() != null && user.get(0).getToken().equals(authToken) && id != null
						&& id.longValue() == user.get(0).getId()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
