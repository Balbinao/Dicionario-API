package com.gft.dicionario.dicionario_api.controller;


import com.gft.dicionario.dicionario_api.dto.WordDTO;
import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.service.LabelService;
import com.gft.dicionario.dicionario_api.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    private final LabelService labelService;

    public WordController(WordService wordService, LabelService labelService) {
        this.wordService = wordService;
        this.labelService = labelService;
    }

    @GetMapping
    public List<WordDTO> getAll(){
        return wordService.findAllWords().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WordDTO getById(@PathVariable  Long id){
        Word word = wordService.findById(id);
        return toDTO(word);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public WordDTO create(@RequestBody Word word){
        Word wordSaved = wordService.save(word);
        return toDTO(wordSaved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public WordDTO update(@PathVariable Long id, @RequestBody Word updatedWord){
        Word word = wordService.findById(id);
        word.setTerm(updatedWord.getTerm());
        Word wordSaved = wordService.save(word);
        return toDTO(wordSaved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id){
        wordService.deleteById(id);
    }

    @GetMapping("/label/{labelName}")
    public Set<WordDTO> findByLabel(@PathVariable String labelName){
        return wordService.findByLabel(labelName)
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @PutMapping("/{wordId}/add-label/{labelId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public WordDTO addLabelToWord(@PathVariable Long wordId, @PathVariable Long labelId) {
        Word word = wordService.findById(wordId);
        Label label = labelService.findById(labelId);

        word.getLabelList().add(label);
        Word saved = wordService.save(word);

        return toDTO(saved);
    }

    // Conversão para DTO
    private WordDTO toDTO(Word word) {
        return new WordDTO(
                word.getWordId(),
                word.getTerm(),
                word.getLabelList().stream()
                        .map(l -> l.getLabelName())
                        .collect(Collectors.toSet())
        );
    }
}
