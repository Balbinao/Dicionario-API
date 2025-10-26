package com.gft.dicionario.dicionario_api.service;


import com.gft.dicionario.dicionario_api.entities.Label;
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

    public Optional<Label> findById(Long id){
        return labelRepository.findById(id);
    }

    public Label save(Label label){
        return labelRepository.save(label);
    }

    public void deleteById(Long id){
        labelRepository.deleteById(id);
    }

    public Set<Label> findByWord(String term){
        return labelRepository.findByWordList_Term(term);
    }
}
