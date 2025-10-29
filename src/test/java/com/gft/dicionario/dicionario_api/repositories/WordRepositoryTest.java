package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private LabelRepository labelRepository;

    private Label animalLabel;
    private Word catWord;

    @BeforeEach
    //configuração inicial antes de cada chamada de função
    void setUp() {

        wordRepository.deleteAll();
        labelRepository.deleteAll();

        // cria uma etiqueta
        animalLabel = new Label();
        animalLabel.setLabelName("animal");
        animalLabel = labelRepository.save(animalLabel);

        // cria uma palavra e associa a etiqueta
        catWord = new Word();
        catWord.setTerm("cat");

        catWord.getLabelList().add(animalLabel);
        catWord = wordRepository.save(catWord);
    }


    // ---------- CAMINHOS FELIZES ------------

    @Test
    @DisplayName("Salvar e recuperar por id")
    void saveAndFindById() {
        Optional<Word> found = wordRepository.findById(catWord.getWordId());
        assertThat(found).isPresent();
        assertThat(found.get().getTerm()).isEqualTo("cat");
    }

    @Test
    @DisplayName("findByLabelList_LabelName retorna palavra associada à etiqueta")
    void findByLabelName_returnsWord() {
        Set<Word> result = wordRepository.findByLabelList_LabelName("animal");
        assertThat(result).isNotEmpty();
        assertThat(result).extracting(Word::getTerm).contains("cat");
    }

    @Test
    @DisplayName("Deletar palavra existente")
    void deleteWord_existing() {
        wordRepository.delete(catWord);
        assertThat(wordRepository.findById(catWord.getWordId())).isNotPresent();
    }

    // ----------- CAMINHOS ALTERNATIVOS ----------

    @Test
    @DisplayName("Recuperar por id inexistente retorna vazio")
    void findById_noMatch(){
        Optional<Word> found = wordRepository.findById(999l);
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("findByLabelList_LabelName com etiqueta inexistente retorna vazio")
    void findByLabelName_noMatch() {
        Set<Word> result = wordRepository.findByLabelList_LabelName("nonexistent");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deletar palavra inexistente")
    void deleteWord_nonExisting() {
        wordRepository.deleteById(999L); // id que não existe
        // aqui só verificamos que não lançou exception
    }

}
