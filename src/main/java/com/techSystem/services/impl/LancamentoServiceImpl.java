package com.techSystem.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techSystem.Enuns.StatusLancamento;
import com.techSystem.Enuns.TipoLancamento;
import com.techSystem.exceptions.RegraNegocioException;
import com.techSystem.models.Lancamento;
import com.techSystem.repositories.LancamentoRepository;
import com.techSystem.services.LancamentoService;


@Service
public class LancamentoServiceImpl implements LancamentoService{

	@Autowired
	private LancamentoRepository repo ;
	
	
	
	
	@Override
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repo.save(lancamento);
	}

	@Override
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repo.save(lancamento);
	}

	@Override
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repo.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repo.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		if(lancamento.getDescricao().isBlank() || lancamento.getDescricao().trim().isEmpty()) {
			throw new  RegraNegocioException("INFORME UMA DESCRIÇÃO VALIDA");
		}if(lancamento.getMes()==null || lancamento.getMes()<1 || lancamento.getMes() >12) {
			throw new RegraNegocioException("INFORME UM MES VALIDO");
		}if(lancamento.getAno()==null || lancamento.getAno().toString().length() !=4) {
			throw new RegraNegocioException("ANO ERRADO");
		}if(lancamento.getUsuario()==null || lancamento.getUsuario().getId()==null ) {
			throw new RegraNegocioException("INFORME UM USUARIO VALIDO");
			
		}if(lancamento.getValor()== null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1) {
			throw new RegraNegocioException("INFORME UM VALOR VALIDO.");
		}if(lancamento.getTipo()==null) {
			throw new RegraNegocioException("INFORME UM TIPO VALIDO");
		}

		
			
		
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		
		return repo.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		
		BigDecimal receitas =  repo.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		 BigDecimal despesas =  repo.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		 
		 if(receitas == null) {
			 receitas = BigDecimal.ZERO;
		 }
		 
		 if(despesas == null) {
			 despesas = BigDecimal.ZERO;
		 }
		 return receitas.subtract(despesas);
	}

	@Override
	public List<Lancamento> findAll() {
		
		return repo.findAll();
	}

}
