package com.techSystem.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.techSystem.Enuns.StatusLancamento;
import com.techSystem.Enuns.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String descricao;
		
		private Integer mes;
		
		private Integer ano;
		
		private BigDecimal valor;
		
		@Enumerated(value= EnumType.STRING)
		private TipoLancamento tipo;
		
		@Enumerated(value=EnumType.STRING)
		@Column(columnDefinition = "VARCHAR(60) CHECK (status IN ('PENDENTE', 'CANCELADO', 'EFETIVADO'))")
		private StatusLancamento status;
		
		@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
		private LocalDate dataCadastro;
		
		@ManyToOne
		@JoinColumn(name="id_Usuario")
		private Usuario usuario;
		
		
}