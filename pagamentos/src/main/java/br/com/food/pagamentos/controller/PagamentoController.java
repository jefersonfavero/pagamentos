package br.com.food.pagamentos.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.food.pagamentos.dto.PagamentoDto;
import br.com.food.pagamentos.service.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

	@Autowired
	private PagamentoService pagamentoService;

	@GetMapping
	public Page<PagamentoDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
		return pagamentoService.obterTodos(paginacao);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PagamentoDto> detalhar(@PathVariable @NotNull Long id) {
		PagamentoDto dto = pagamentoService.obterPorId(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public ResponseEntity<PagamentoDto> cadastrar(@RequestBody @Valid PagamentoDto dto,UriComponentsBuilder uriBuilder) {
		PagamentoDto pagamento = pagamentoService.criarPagamento(dto);
		URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
		return ResponseEntity.created(endereco).body(pagamento);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PagamentoDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto) {
		PagamentoDto atualizado = pagamentoService.atualizarPagamento(id, dto);
		return ResponseEntity.ok(atualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<PagamentoDto> remover(@PathVariable @NotNull Long id) {
		pagamentoService.excluirPagamento(id);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/confirmar")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
		pagamentoService.confirmarPagamento(id);
    }
}
