package com.gft.dicionario.dicionario_api.controller;

import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.service.LabelService;
import com.gft.dicionario.dicionario_api.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WordControllerTest {

    private WordService wordService;
    private LabelService labelService;
    private WordController wordController;

    @BeforeEach
    void setUp() {
        wordService = mock(WordService.class);
        labelService = mock(LabelService.class);
        wordController = new WordController(wordService, labelService);
    }

    @Test
    void testGetAll() {
        Word word = new Word();
        word.setWordId(1L);
        word.setTerm("Computador");
        when(wordService.findAllWords()).thenReturn(List.of(word));

        var result = wordController.getAll();

        assertEquals(1, result.size());
        assertEquals("Computador", result.get(0).getTerm());
    }

    @Test
    void testGetById() {
        Word word = new Word();
        word.setWordId(1L);
        word.setTerm("Computador");
        when(wordService.findById(1L)).thenReturn(Optional.of(word));

        var result = wordController.getById(1L);

        assertEquals("Computador", result.getTerm());
    }

    @Test
    void testCreate() {
        Word word = new Word();
        word.setTerm("Mouse");
        Word savedWord = new Word();
        savedWord.setWordId(1L);
        savedWord.setTerm("Mouse");

        when(wordService.save(word)).thenReturn(savedWord);

        var result = wordController.create(word);

        assertEquals(1L, result.getWordId());
        assertEquals("Mouse", result.getTerm());
    }

    @Test
    void testUpdate() {
        Word existing = new Word();
        existing.setWordId(1L);
        existing.setTerm("Teclado");

        Word updated = new Word();
        updated.setTerm("Teclado Mecânico");

        when(wordService.findById(1L)).thenReturn(Optional.of(existing));
        when(wordService.save(existing)).thenReturn(existing);

        var result = wordController.update(1L, updated);

        assertEquals("Teclado Mecânico", result.getTerm());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        wordController.delete(id);

        verify(wordService, times(1)).deleteById(id);
    }

    @Test
    void testFindByLabel() {
        Label label = new Label();
        label.setLabelId(1L);
        label.setLabelName("Tecnologia");

        Word word = new Word();
        word.setWordId(1L);
        word.setTerm("Computador");
        word.setLabelList(Set.of(label));

        when(wordService.findByLabel("Tecnologia")).thenReturn(Set.of(word));

        var result = wordController.findByLabel("Tecnologia");

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(w -> w.getTerm().equals("Computador")));
    }

    @Test
    void testAddLabelToWord() {
        Word word = new Word();
        word.setWordId(1L);
        word.setTerm("Mouse");
        word.setLabelList(new HashSet<>());

        Label label = new Label();
        label.setLabelId(2L);
        label.setLabelName("Hardware");

        when(wordService.findById(1L)).thenReturn(Optional.of(word));
        when(labelService.findById(2L)).thenReturn(Optional.of(label));
        when(wordService.save(word)).thenReturn(word);

        var result = wordController.addLabelToWord(1L, 2L);

        assertEquals(1, result.getLabelList().size());
        assertTrue(result.getLabelList().contains("Hardware"));
    }
}
