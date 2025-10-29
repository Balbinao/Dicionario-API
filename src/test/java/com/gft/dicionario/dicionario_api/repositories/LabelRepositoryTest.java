package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.entities.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private WordRepository wordRepository;

    private Label animalLabel;
    private Word catWord;

    @BeforeEach
    //configuração inicial antes de cada chamada de função
    void setUp() {

        wordRepository.deleteAll();
        labelRepository.deleteAll();

        // Cria etiqueta
        animalLabel = new Label();
        animalLabel.setLabelName("animal");
        animalLabel = labelRepository.save(animalLabel);

        // Cria palavra associada à etiqueta
        catWord = new Word();
        catWord.setTerm("cat");
        catWord.getLabelList().add(animalLabel);
        catWord = wordRepository.save(catWord);
    }

    // ---------- CAMINHOS FELIZES ------------

    @Test
    @DisplayName("Salvar e recuperar etiqueta por id")
    void saveAndFindById() {
        Optional<Label> found = labelRepository.findById(animalLabel.getLabelId());
        assertThat(found).isPresent();
        assertThat(found.get().getLabelName()).isEqualTo("animal");
    }

    @Test
    @DisplayName("findByWordList_Term retorna etiqueta associada à palavra")
    void findByWordTerm_returnsLabel() {
        Set<Label> result = labelRepository.findByWordList_Term("cat");
        assertThat(result).isNotEmpty();
        assertThat(result).extracting(Label::getLabelName).contains("animal");
    }

    // ----------- CAMINHOS ALTERNATIVOS ----------

    @Test
    @DisplayName("Recuperar etiqueta por id inexistente retorna vazio")
    void findById_noMatch() {
        Optional<Label> found = labelRepository.findById(999L);
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("findByWordList_Term com palavra inexistente retorna vazio")
    void findByWordTerm_noMatch() {
        Set<Label> result = labelRepository.findByWordList_Term("dog");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deletar etiqueta existente")
    void deleteLabel_existing() {
        labelRepository.delete(animalLabel);
        assertThat(labelRepository.findById(animalLabel.getLabelId())).isNotPresent();
    }

    @Test
    @DisplayName("Deletar etiqueta inexistente")
    void deleteLabel_nonExisting() {
        labelRepository.deleteById(999L); // id que não existe
        // apenas verificamos que não lançou exception
    }
}
