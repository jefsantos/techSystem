package com.techSystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techSystem.Enuns.StatusLancamento;
import com.techSystem.Enuns.TipoLancamento;
import com.techSystem.dto.AtualizaStatusDTO;
import com.techSystem.dto.LancamentoDto;
import com.techSystem.exceptions.RegraNegocioException;
import com.techSystem.models.Lancamento;
import com.techSystem.models.Usuario;
import com.techSystem.services.LancamentoService;
import com.techSystem.services.UsuarioServices;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private UsuarioServices usuarioService;
	
	public LancamentoController(LancamentoService lancamentoService, UsuarioServices usuarioService) {
		this.lancamentoService = lancamentoService;
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("/todos")
	public ResponseEntity buscarTodosLancamento() {
		List<Lancamento> list = lancamentoService.findAll();
		
		if(!list.isEmpty()) {
			return ResponseEntity.ok(list);
		}else {
			return ResponseEntity.badRequest().body("SEM LANCAMENTOS NO SISTEMA, POR FAVOR EFETUE UM LANCAMENTO.");
		}
		
	}
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody LancamentoDto dto){
		
		try {
			Lancamento obj = converter(dto);
			obj = lancamentoService.salvar(obj);
			return new ResponseEntity<Object>(obj, HttpStatus.CREATED);
			
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@PutMapping("{id}")
	public ResponseEntity atualizar (@PathVariable("id") Long id, @RequestBody LancamentoDto dto){
	return	lancamentoService.obterPorId(id).map(entity -> {
		try {
			Lancamento lancamento = converter(dto);
			lancamento.setId(entity.getId());
			lancamentoService.atualizar(lancamento);
			return ResponseEntity.ok(lancamento);
		}catch(RegraNegocioException e) {
		return	ResponseEntity.badRequest().body(e.getMessage());
		}
	}).orElseGet(()-> 
	  new ResponseEntity<String>("teste", HttpStatus.BAD_REQUEST));
		
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		return lancamentoService.obterPorId(id).map(entity ->{
			StatusLancamento statusSelecionado =  StatusLancamento.valueOf(dto.getStatus());
			if(statusSelecionado == null ) {
				return ResponseEntity.badRequest().body("Não foi possivel, status Invalido");
			}
			
			try {
				entity.setStatus(statusSelecionado);
				lancamentoService.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch(RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(()-> 
		  new ResponseEntity("LANCAMENTO NÃO LOCALIZADO NA BASE", HttpStatus.BAD_REQUEST));
		
	}
	
	
	
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
	 	return lancamentoService.obterPorId(id).map(entidade ->{
	 		lancamentoService.deletar(entidade);
	 		return new ResponseEntity(HttpStatus.NO_CONTENT);
	 	}).orElseGet(() ->
	 	new ResponseEntity("LANCAMENTO NÃO ENCONTRADO, VERIFIQUE ANTES DE DELETAR!", HttpStatus.BAD_REQUEST));
	 	
	}	
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "usuario", required = true) Long idUsuario
			
			) {
		Lancamento lancamentoFilter = new Lancamento();
		lancamentoFilter.setDescricao(descricao);
		lancamentoFilter.setMes(mes);
		lancamentoFilter.setAno(ano);

		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("USUARIO NÃO LOCALIZADO");
		}else {
			
			lancamentoFilter.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentosList = lancamentoService.buscar(lancamentoFilter);
		return ResponseEntity.ok(lancamentosList);
	}
	
	

	
	
	
	private Lancamento converter(LancamentoDto dto) {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setId(dto.getId());
		
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(()-> 
		new RegraNegocioException("Usuario nao encontrado com esse ID:"));
		
		lancamento.setUsuario(usuario);
		
		if(dto.getTipo()!=null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));			
		}

		
		if(dto.getStatus()!=null) {

			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));;
		}

		
		return lancamento;
		
	}

}
