package com.gft.dicionario.dicionario_api.service;

import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.repositories.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> findAllWords(){
        return wordRepository.findAll();
    }

    public Optional<Word> findById(Long id){
        return wordRepository.findById(id);
    }

    public Word save(Word word){
        return wordRepository.save(word);
    }

    public void deleteById(Long id){
        wordRepository.deleteById(id);
    }

    public Set<Word> findByLabel(String labelName){
        return wordRepository.findByLabelList_LabelName(labelName);
    }
}
