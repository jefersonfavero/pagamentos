package br.com.food.pagamentos.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.food.pagamentos.dto.PagamentoDto;
import br.com.food.pagamentos.http.PedidoClient;
import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;
import br.com.food.pagamentos.repository.PagamentoRepository;

@Service
public class PagamentoService {
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PedidoClient pedidoClient;
	
	 public Page<PagamentoDto> obterTodos(Pageable paginacao) {
	        return pagamentoRepository.findAll(paginacao).map(p -> modelMapper.map(p, PagamentoDto.class));
	    }
	 
	 public PagamentoDto obterPorId(Long id) {
	        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	        return modelMapper.map(pagamento, PagamentoDto.class);
	    }
	 
	 public PagamentoDto criarPagamento(PagamentoDto dto) {
	        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
	        pagamento.setStatus(Status.CRIADO);
	        pagamentoRepository.save(pagamento);

	        return modelMapper.map(pagamento, PagamentoDto.class);
	    }
	 
	  public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto) {
	        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
	        pagamento.setId(id);
	        pagamento = pagamentoRepository.save(pagamento);
	        return modelMapper.map(pagamento, PagamentoDto.class);
	    }
	  
	  public void excluirPagamento(Long id) {
		  pagamentoRepository.deleteById(id);
	    }
	  
	  public void confirmarPagamento(Long id){
	        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

	        if (!pagamento.isPresent()) {
	            throw new EntityNotFoundException();
	        }

	        pagamento.get().setStatus(Status.CONFIRMADO);
	        pagamentoRepository.save(pagamento.get());
	        pedidoClient.atualizaPagamento(pagamento.get().getPedidoId());
	    }
}
