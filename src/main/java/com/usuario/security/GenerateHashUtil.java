package com.usuario.security;

import java.security.MessageDigest;

public abstract class GenerateHashUtil {

	public static String generateHash(String password) throws Exception {
		// Hash das senhas utilizando o algoritmo SHA-2
		MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
		byte messageDigest[] = algorithm.digest(password.getBytes("UTF-8"));
		return new String(messageDigest, "UTF-8");
	}

}
