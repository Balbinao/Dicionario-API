package com.gft.dicionario.dicionario_api.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "word_id")
    private Long wordId;

    @Column(nullable = false, unique = true)
    private String term;

    //lista de etiquetas que a palavra pode pertencer
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_word_label",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labelList = new HashSet<>();

    public Word() {}

    public Word(String term) {
        this.term = term;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Set<Label> getLabelList() {
        return labelList;
    }

    public void setLabelList(Set<Label> labelList) {
        this.labelList = labelList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(wordId, word.wordId) && Objects.equals(term, word.term) && Objects.equals(labelList, word.labelList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordId, term, labelList);
    }

    @Override
    public String toString() {
        return "Word{" +
                "wordId=" + wordId +
                ", term='" + term + '\'' +
                ", labelList=" + labelList +
                '}';
    }


}
