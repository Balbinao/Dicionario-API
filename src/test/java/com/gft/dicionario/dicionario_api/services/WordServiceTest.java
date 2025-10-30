package com.gft.dicionario.dicionario_api.services;


import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import com.gft.dicionario.dicionario_api.repositories.WordRepository;
import com.gft.dicionario.dicionario_api.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


public class WordServiceTest {

    @Mock
    private WordRepository wordRepository;

    @InjectMocks
    private WordService wordService;

    private Word catWord;
    private Label animalLabel;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        animalLabel = new Label();
        animalLabel.setLabelName("animal");

        catWord = new Word();
        catWord.setWordId(1L);
        catWord.setTerm("cat");

        catWord.getLabelList().add(animalLabel);

    }

    //------- CAMINHOS FELIZES --------

    @Test
    @DisplayName("findAllWords deve retornar lista com palavras")
    void findAllWords_ReturnList(){
        when(wordRepository.findAll()).thenReturn(List.of(catWord));

        List<Word> result = wordService.findAllWords();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTerm()).isEqualTo("cat");
        verify(wordRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar palavra existente")
    void findById_existingWord() {
        when(wordRepository.findById(1L)).thenReturn(Optional.of(catWord));

        Optional<Word> result = wordService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTerm()).isEqualTo("cat");
        verify(wordRepository).findById(1L);
    }

    @Test
    @DisplayName("saveWord deve salvar e retornar palavra")
    void saveWord_shouldSaveSuccessfully() {
        when(wordRepository.save(catWord)).thenReturn(catWord);

        Word result = wordService.save(catWord);

        assertThat(result).isNotNull();
        assertThat(result.getTerm()).isEqualTo("cat");
        verify(wordRepository).save(catWord);
    }

    @Test
    @DisplayName("deleteById deve chamar repositório corretamente")
    void deleteById_shouldInvokeRepository() {
        doNothing().when(wordRepository).deleteById(1L);

        wordService.deleteById(1L);

        verify(wordRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findByLabel deve retornar palavras associadas à label")
    void findByLabel_existingLabel() {
        when(wordRepository.findByLabelList_LabelName("animal"))
                .thenReturn(Set.of(catWord));

        Set<Word> result = wordService.findByLabel("animal");

        assertThat(result).isNotEmpty();
        assertThat(result.iterator().next().getTerm()).isEqualTo("cat");
        verify(wordRepository).findByLabelList_LabelName("animal");
    }

    //---------- CAMINHOS ALTERNATIVOS --------

    @Test
    @DisplayName("findAllWordsEmpty deve retornar lista vazia")
    void findAllWords_ReturnEmptyList() {
        when(wordRepository.findAll()).thenReturn(Collections.emptyList());

        List<Word> result = wordService.findAllWords();

        assertThat(result).isEmpty();
        verify(wordRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar vazio para ID inexistente")
    void findById_nonExistingWord() {
        when(wordRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Word> result = wordService.findById(999L);

        assertThat(result).isEmpty();
        verify(wordRepository).findById(999L);
    }


    @Test
    @DisplayName("saveRepeatedWord deve dar conflito e retornar palavra")
    void saveRepeatedWord () {
        // Simula que já existe uma palavra com mesmo nome no repositório
        when(wordRepository.findAll()).thenReturn(List.of(catWord));

        // Implementação simulada do comportamento de detecção de duplicata
        when(wordRepository.save(any(Word.class))).thenAnswer(invocation -> {
            Word word = invocation.getArgument(0);
            boolean exists = wordRepository.findAll().stream()
                    .anyMatch(w -> w.getTerm().equalsIgnoreCase(word.getTerm()));
            if (exists) {
                throw new IllegalArgumentException("Palavra já existe no dicionário!");
            }
            return word;
        });

        assertThatThrownBy(() -> wordService.save(catWord))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Palavra já existe no dicionário!");

        verify(wordRepository, times(1)).save(catWord);
    }

    @Test
    @DisplayName("deleteByIdInvalid deve retornar ID inexistente")
    void deleteByIdInvalid() {
        doThrow(new IllegalArgumentException("ID inexistente")).when(wordRepository).deleteById(999L);

        assertThatThrownBy(() -> wordService.deleteById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID inexistente");

        verify(wordRepository, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("findByLabel deve retornar vazio para label inexistente")
    void findByLabel_nonExistingLabel() {
        when(wordRepository.findByLabelList_LabelName("inexistent"))
                .thenReturn(Collections.emptySet());

        Set<Word> result = wordService.findByLabel("inexistent");

        assertThat(result).isEmpty();
        verify(wordRepository).findByLabelList_LabelName("inexistent");
    }
}
