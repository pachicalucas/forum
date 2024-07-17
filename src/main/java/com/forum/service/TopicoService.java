package com.forum.service;

import com.forum.entity.Topico;
import com.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    public Topico criarTopico(Topico topico) {
        if (topicoRepository.existsByTituloAndMensagem(topico.getTitulo(), topico.getMensagem())) {
            throw new RuntimeException("Tópico duplicado");
        }
        return topicoRepository.save(topico);
    }

    public Page<Topico> listarTopicos(Pageable pageable) {
        return topicoRepository.findAll(pageable);
    }

    public Optional<Topico> detalharTopico(Long id) {
        return topicoRepository.findById(id);
    }

    public Topico atualizarTopico(Long id, Topico topicoAtualizado) {
        return topicoRepository.findById(id)
                .map(topico -> {
                    topico.setTitulo(topicoAtualizado.getTitulo());
                    topico.setMensagem(topicoAtualizado.getMensagem());
                    topico.setAutor(topicoAtualizado.getAutor());
                    topico.setCurso(topicoAtualizado.getCurso());
                    return topicoRepository.save(topico);
                })
                .orElse(null);
    }

    public boolean deletarTopico(Long id) {
        if (topicoRepository.existsById(id)) {
            topicoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}