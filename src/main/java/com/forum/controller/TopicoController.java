package com.forum.controller;

import com.forum.entity.Topico;
import com.forum.service.TopicoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/api/topicos")
public class TopicoController {

    private static final Logger logger = LoggerFactory.getLogger(TopicoController.class);

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<?> cadastrarTopico(@RequestBody @Valid Topico topico, UriComponentsBuilder uriBuilder) {
        Topico topicoSalvo = topicoService.criarTopico(topico);
        URI uri = uriBuilder.path("/api/topicos/{id}").buildAndExpand(topicoSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(topicoSalvo);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        logger.error("Erro ao processar requisição", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Topico> atualizarTopico(@PathVariable Long id, @RequestBody @Valid Topico topicoAtualizado) {
        logger.info("Recebida requisição PUT para /api/topicos/{}: {}", id, topicoAtualizado);
        try {
            Topico topico = topicoService.atualizarTopico(id, topicoAtualizado);
            if (topico != null) {
                logger.info("Tópico atualizado com sucesso: {}", topico);
                return ResponseEntity.ok(topico);
            } else {
                logger.warn("Tópico não encontrado com o ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar tópico", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Topico>> listarTopicos(@PageableDefault(sort = "dataCriacao", size = 10) Pageable paginacao) {
        Page<Topico> topicos = topicoService.listarTopicos(paginacao);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topico> detalharTopico(@PathVariable Long id) {
        logger.info("Recebida requisição GET para /api/topicos/{}", id);
        return topicoService.detalharTopico(id)
                .map(topico -> {
                    logger.info("Tópico encontrado: {}", topico);
                    return ResponseEntity.ok(topico);
                })
                .orElseGet(() -> {
                    logger.warn("Tópico não encontrado com ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTopico(@PathVariable Long id) {
        logger.info("Recebida requisição DELETE para /api/topicos/{}", id);
        try {
            boolean deletado = topicoService.deletarTopico(id);
            if (deletado) {
                logger.info("Tópico com ID {} deletado com sucesso", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Tópico não encontrado com o ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erro ao deletar tópico", e);
            return ResponseEntity.badRequest().body("Erro ao deletar tópico: " + e.getMessage());
        }
    }
}