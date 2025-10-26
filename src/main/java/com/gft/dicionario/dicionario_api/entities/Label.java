package com.gft.dicionario.dicionario_api.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_label")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "label_id")
    private Long labelId;

    @Column(nullable = false, unique = true)
    private String labelName;

    //lista de palavras que a etiqueta pode pertencer
    @ManyToMany(mappedBy = "labelList", fetch = FetchType.LAZY)
    private Set<Word> wordList = new HashSet<>();


    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Set<Word> getWordList() {
        return wordList;
    }

    public void setWordList(Set<Word> wordList) {
        this.wordList = wordList;
    }


}
