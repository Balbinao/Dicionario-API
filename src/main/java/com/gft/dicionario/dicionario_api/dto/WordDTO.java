package com.gft.dicionario.dicionario_api.dto;

import java.util.Set;

public class WordDTO {
    private long wordId;
    private String term;
    private Set<String> labelList;

    public WordDTO(long wordId, String term, Set<String> labelList) {
        this.wordId = wordId;
        this.term = term;
        this.labelList = labelList;
    }

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Set<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(Set<String> labelList) {
        this.labelList = labelList;
    }
}
