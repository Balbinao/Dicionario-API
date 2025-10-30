package com.gft.dicionario.dicionario_api.controller;


import com.gft.dicionario.dicionario_api.dto.LabelDTO;
import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.service.LabelService;
import com.gft.dicionario.dicionario_api.service.WordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/labels")
public class LabelController {

    private final LabelService labelService;
    private final WordService wordService;

    public LabelController(LabelService labelService, WordService wordService) {
        this.labelService = labelService;
        this.wordService = wordService;
    }
    @GetMapping
    public List<LabelDTO> getAll(){
        return labelService.findAllLabels().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LabelDTO getById(@PathVariable Long id){
        Label label = labelService.findById(id).orElseThrow();
        return toDTO(label);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public LabelDTO create (@RequestBody Label label){
        Label labelSaved = labelService.save(label);

        return toDTO(labelSaved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public LabelDTO update(@PathVariable Long id, @RequestBody Label updateLabel){
        Label label = labelService.findById(id).orElseThrow();
        label.setLabelName(updateLabel.getLabelName());
        label.setWordList(updateLabel.getWordList());
        Label labelSaved = labelService.save(label);
        return toDTO(labelSaved);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id){
        labelService.deleteById(id);
    }

    @GetMapping("/word/{term}")
    public Set<LabelDTO> findByWord(@PathVariable String term) {
        return labelService.findByWord(term)
                .stream().map(this::toDTO).collect(Collectors.toSet());
    }

    @PutMapping("/{labelId}/add-word/{wordId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public LabelDTO addWordToLabel(@PathVariable Long labelId, @PathVariable Long wordId) {
        Label label = labelService.findById(labelId).orElseThrow();
        Word word = wordService.findById(wordId).orElseThrow();

        word.getLabelList().add(label); // lado dono
        wordService.save(word);

        return toDTO(label);
    }

    private LabelDTO toDTO(Label label) {
        return new LabelDTO(
                label.getLabelId(),
                label.getLabelName(),
                label.getWordList().stream()
                        .map(w -> w.getTerm())
                        .collect(Collectors.toSet())
        );
    }
}
