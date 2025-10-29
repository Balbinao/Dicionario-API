package com.gft.dicionario.dicionario_api.services;

import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.repositories.LabelRepository;
import com.gft.dicionario.dicionario_api.service.LabelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class LabelServiceTest {

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelService labelService;

    private Label label1;
    private Label label2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        label1 = new Label();
        label1.setLabelId(1L);
        label1.setLabelName("BASIC");

        label2 = new Label();
        label2.setLabelId(2L);
        label2.setLabelName("ADVANCED");
    }

    // ---------- CAMINHOS FELIZES ----------

    @Test
    @DisplayName("findAllLabels retorna lista de etiquetas")
    void findAllLabels_ReturnListOfLabels() {
        when(labelRepository.findAll()).thenReturn(List.of(label1, label2));

        List<Label> result = labelService.findAllLabels();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("labelName").containsExactly("BASIC", "ADVANCED");
        verify(labelRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna uma etiqueta")
    void findById_ReturnLabel() {
        when(labelRepository.findById(1L)).thenReturn(Optional.of(label1));

        Optional<Label> result = labelService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getLabelName()).isEqualTo("BASIC");
        verify(labelRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save salva uma etiqueta com sucesso")
    void save_SaveLabelSuccessfully() {
        when(labelRepository.save(label1)).thenReturn(label1);

        Label result = labelService.save(label1);

        assertThat(result).isNotNull();
        assertThat(result.getLabelName()).isEqualTo("BASIC");
        verify(labelRepository, times(1)).save(label1);
    }

    @Test
    @DisplayName("deleteById deleta uma etiqueta com sucesso")
    void deleteById_DeleteSuccessfully() {
        doNothing().when(labelRepository).deleteById(1L);

        labelService.deleteById(1L);

        verify(labelRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findByWord retorna etiquetas relacionadas com a palavra")
    void findByWord_ShouldReturnLabelsByWord() {
        when(labelRepository.findByWordList_Term("hello")).thenReturn(Set.of(label1, label2));

        Set<Label> result = labelService.findByWord("hello");

        assertThat(result).hasSize(2);
        assertThat(result).extracting("labelName").contains("BASIC", "ADVANCED");
        verify(labelRepository, times(1)).findByWordList_Term("hello");
    }

    // ---------- CAMINHOS ALTERNATIVOS ----------

    @Test
    @DisplayName("findAllLabels retorna lista vazia de etiquetas quando etiquetas não existem")
    void findAllLabels_ReturnEmptyList() {
        when(labelRepository.findAll()).thenReturn(Collections.emptyList());

        List<Label> result = labelService.findAllLabels();

        assertThat(result).isEmpty();
        verify(labelRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById quando etiqueta não é encontrada")
    void findById_WhenLabelNotFound() {

        when(labelRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Label> result = labelService.findById(999L);

        assertThat(result).isNotPresent();
        verify(labelRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("save retorna 'Etiqueta já existe no sistema'")
    void save_WhenLabelExists() {
        when(labelRepository.findAll()).thenReturn(List.of(label1));

        when(labelRepository.save(any(Label.class))).thenAnswer(invocation -> {
            Label label = invocation.getArgument(0);
            boolean exists = labelRepository.findAll().stream()
                    .anyMatch(l -> l.getLabelName().equalsIgnoreCase(label.getLabelName()));
            if (exists) {
                throw new IllegalArgumentException("Label já existe no sistema!");
            }
            return label;
        });

        assertThatThrownBy(() -> labelService.save(label1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Label já existe no sistema!");

        verify(labelRepository, times(1)).save(label1);
    }

    @Test
    @DisplayName("deleteById quando id não existe")
    void deleteById_WhenIdNotFound() {
        doThrow(new IllegalArgumentException("ID inexistente")).when(labelRepository).deleteById(999L);

        assertThatThrownBy(() -> labelService.deleteById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID inexistente");

        verify(labelRepository, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("findByWord quando não há etiqueta relacionada a palavra ")
    void findByWord_WhenNoLabelsFound() {

        when(labelRepository.findByWordList_Term("unknownWord")).thenReturn(Collections.emptySet());

        Set<Label> result = labelService.findByWord("unknownWord");

        // Verificações
        assertThat(result).isEmpty();
        verify(labelRepository, times(1)).findByWordList_Term("unknownWord");
    }
}
