package com.techSystem.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techSystem.models.Usuario;

@Service
public interface UsuarioServices {
	
	Usuario autenticar (String email, String senha) ;
	
	
	Usuario salvarUsuario (Usuario usuario);
	
	void validarEmail(String email);
	
	Optional <Usuario> obterPorId(Long id);
	
	
		
	

}
