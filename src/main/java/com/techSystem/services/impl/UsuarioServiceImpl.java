package com.techSystem.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techSystem.exceptions.ErroAutentication;
import com.techSystem.exceptions.RegraNegocioException;
import com.techSystem.models.Usuario;
import com.techSystem.repositories.UsuarioRepository;
import com.techSystem.services.UsuarioServices;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioServices {

	@Autowired
	private UsuarioRepository repo;
	
	public UsuarioServiceImpl(UsuarioRepository repo) {
		super();
		this.repo = repo;
		
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repo.findByEmail(email);

		if (!usuario.isPresent()) {

			throw new ErroAutentication("Usuario não encontrado");
		}
		if (!usuario.get().getSenha().equals(senha)) {

			throw new ErroAutentication("Senha Incorreta");
		}

		return usuario.get();

	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repo.saveAndFlush (usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repo.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Email já cadastrado");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repo.findById(id);
		
	}

}
