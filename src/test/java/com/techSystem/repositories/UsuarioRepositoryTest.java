package com.techSystem.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.techSystem.models.Usuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	

	
	@Autowired
	UsuarioRepository repo;
	
	
	@Autowired
	TestEntityManager entity;
	
	
	
	@Test
	public void verificaExistenciaEmail() {
		
		Usuario usuario = criaUsuario( );
		entity.persist(usuario);
		
		Optional<Usuario> result = repo.findByEmail("zeluca@zeluca");		
		Assertions.assertThat(result.isPresent()).isTrue();
		


	
		
	}
	
	@Test
	public void retornaFalseSeNaoEncontraEmailnaBase() {
	
	boolean result = repo.existsByEmail("maria@maria");
	
	Assertions.assertThat(result).isFalse();
	
	}
	
	
	@Test
	public void devePersistirUsuarioNoBanco() {
		Usuario usuario = criaUsuario();
	    Usuario usuarioSalvo =	repo.save(usuario);
		
	    Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test 
	public void buscaPorEmail() {
		Usuario usuario = criaUsuario();
		entity.persist(usuario);
		
		 Optional<Usuario> result = repo.findByEmail("zeluca@zeluca");
		 
		 //Verificação
		 Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	
	
	@Test
	public void deveRetornarFalsoNaoExisteUsuarioComEsseEmail() {
		Optional<Usuario> result = repo.findByEmail("zeluca@zeluca") ;
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	
	@Test
	public void devePersistirUsuarioNaBase() {
		Usuario usuario = criaUsuario();
		
		Usuario usuarioSalvo = repo.save(usuario);
		
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Cria Usuario para as outras operaçoes
	public static Usuario criaUsuario( ) {
		Usuario usuario = new Usuario(null, "ze lucas", "zeluca@zeluca", "123");
		return	 usuario;
	}
	

}
