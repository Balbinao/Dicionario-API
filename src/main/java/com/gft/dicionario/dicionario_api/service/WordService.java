package com.gft.dicionario.dicionario_api.service;

import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.exceptions.BadRequestException;
import com.gft.dicionario.dicionario_api.exceptions.ResourceNotFoundException;
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

    public Word findById(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Palavra com ID " + id + " não encontrada."));
    }

    public Word save(Word word){
        if (word == null) {
            throw new BadRequestException("O corpo da requisição não pode ser nulo.");
        }

        if (word.getTerm() == null || word.getTerm().isBlank()) {
            throw new BadRequestException("O campo 'term' não pode ser vazio.");
        }

        return wordRepository.save(word);
    }

    public void deleteById(Long id){
        if (!wordRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Não foi possível deletar. Palavra com ID " + id + " não encontrada.");
        }

        wordRepository.deleteById(id);
    }

    public Set<Word> findByLabel(String labelName){
        if (labelName == null || labelName.isBlank()) {
            throw new BadRequestException("O parâmetro 'labelName' não pode ser vazio.");
        }

        Set<Word> words = wordRepository.findByLabelList_LabelName(labelName);

        if (words == null || words.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Nenhuma palavra encontrada com o label '" + labelName + "'.");
        }

        return wordRepository.findByLabelList_LabelName(labelName);
    }
}
