package com.techSystem.usuarioService;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.assertj.core.api.*;
import com.techSystem.exceptions.RegraNegocioException;
import com.techSystem.models.Usuario;
import com.techSystem.repositories.UsuarioRepository;
import com.techSystem.services.UsuarioServices;
import com.techSystem.services.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioServices service;
	
	@Autowired
	UsuarioRepository repo;
	
	@Mock
	UsuarioRepository repoMock;
	
	@BeforeTestClass
	public void setUp(){
		repo = repoMock;
		service = new UsuarioServiceImpl(repo);
	}
	
	@Test
	public void deveValidarEmail() {
		repo.deleteAll();
		
		service.validarEmail("maria@maria");
		
	}
	
	@Test
	public void deveLancarErroQuandoExistirEmailCadastrdo() {
		Usuario usuario = new Usuario(null, "jose", "jose@jose", "123");
		repo.save(usuario);
		
		
		Assertions.assertThrows(RegraNegocioException.class, ()->{
			service.validarEmail("jose@jose");
		});
		
	}
	
	@Test
	public void deveValidarEmailComMockitoRetornandoFalse() {
		
		Mockito.when(repoMock.existsByEmail(Mockito.anyString())).thenReturn(false);
		
	}
	
	@Test
	public void deveRetornarEmailValidandoMockTrue() {
		
		Usuario usuario = new Usuario(null, "MAria", "guta@guta", "123");
		
		 repoMock.save(usuario);
		
		Mockito.when(repoMock.existsByEmail(Mockito.anyString())).thenReturn(true);
	}
	
	
//	@Test
//	public void deveAutenticarUsuarioComSucesso() {
//		String email = "abel@abel";
//		String senha = "123";
//		
//		Usuario usuario = new Usuario(1L, "maria", email, senha);
//		Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(usuario));
//		
//		
//		
//
//		
//		Assertions.assertThrows(Exception.class, ()->{
//	
//			Usuario result = service.autenticar(email, senha);	
//			
////			Assertions.assertThat(result).isNotNull();	
//			
//			
//		});
//		
//	}
	
//	@Test
//	public void deveRetornarUmEmailVazioParaQueOTesteFalhe() {
//		
//		Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
//		
//		Assertions.assertThrows(RegraNegocioException.class, ()->{
//			service.validarEmail("jose@maria");
//		});
//		
//		
//	}
	

}
