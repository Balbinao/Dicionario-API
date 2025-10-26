package com.gft.dicionario.dicionario_api;

import com.gft.dicionario.dicionario_api.controller.LabelController;
import com.gft.dicionario.dicionario_api.dto.LabelDTO;
import com.gft.dicionario.dicionario_api.entities.Label;
import com.gft.dicionario.dicionario_api.service.LabelService;
import com.gft.dicionario.dicionario_api.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class LabelControllerTest {

    @Mock
    private LabelService labelService;

    @Mock
    private WordService wordService;

    @InjectMocks
    private LabelController labelController;

    @BeforeEach
    void setUp() {
        labelService = mock(LabelService.class);
        labelController = new LabelController(labelService, null);
    }

    @Test
    void testGetAll() {

        Label label = new Label();
        label.setLabelId(1L);
        label.setLabelName("Tecnologia");

        when(labelService.findAllLabels()).thenReturn(List.of(label));

        List<LabelDTO> labels = labelController.getAll();

        assertEquals(1, labels.size());
        assertEquals("Tecnologia", labels.get(0).getLabelName());
        verify(labelService, times(1)).findAllLabels();
    }

    @Test
    void testGetById() {
        // 1. Preparação (Arrange)
        Label label = new Label();
        label.setLabelId(1L);
        label.setLabelName("Tecnologia");
        when(labelService.findById(1L)).thenReturn(Optional.of(label));

        LabelDTO dto = labelController.getById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getLabelId());
        assertEquals("Tecnologia", dto.getLabelName());
        verify(labelService, times(1)).findById(1L);
    }

    @Test
    void testCreate() {

        Label labelParaSalvar = new Label();
        labelParaSalvar.setLabelName("Nova Label");

        Label labelSalva = new Label();
        labelSalva.setLabelId(1L);
        labelSalva.setLabelName("Nova Label");

        when(labelService.save(any(Label.class))).thenReturn(labelSalva);

        LabelDTO dto = labelController.create(labelParaSalvar);

        assertNotNull(dto);
        assertEquals(1L, dto.getLabelId());
        assertEquals("Nova Label", dto.getLabelName());
        verify(labelService, times(1)).save(labelParaSalvar);
    }

    @Test
    void testDelete() {
        Long labelId = 1L;

        labelController.delete(labelId);

        verify(labelService, times(1)).deleteById(labelId);
    }

}
