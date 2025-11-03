package com.gft.dicionario.dicionario_api.service;


import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.exceptions.BadRequestException;
import com.gft.dicionario.dicionario_api.exceptions.ResourceNotFoundException;
import com.gft.dicionario.dicionario_api.repositories.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> findAllLabels(){
        return labelRepository.findAll();
    }

    public Label findById(Long id){
        return labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Etiqueta com ID " + id + " não encontrada."
        ));
    }

    public Label save(Label label){
        if (label == null) {
            throw new BadRequestException("O corpo da requisição não pode ser nulo.");
        }

        if (label.getLabelName() == null || label.getLabelName().isBlank()) {
            throw new BadRequestException("O campo não pode ser vazio.");
        }
        return labelRepository.save(label);
    }

    public void deleteById(Long id){
        if (!labelRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Não foi possível deletar. Etiqueta com ID " + id + " não encontrada.");
        }

        labelRepository.deleteById(id);
    }

    public Set<Label> findByWord(String term){
        if (term == null || term.isBlank()) {
            throw new BadRequestException("O parâmetro 'labelName' não pode ser vazio.");
        }

        Set<Label> words = labelRepository.findByWordList_Term(term);

        if (words == null || words.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Nenhuma palavra encontrada com o label '" + term + "'.");
        }

        return labelRepository.findByWordList_Term(term);
    }
}
