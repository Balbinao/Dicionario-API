package com.gft.dicionario.dicionario_api.dto;

import java.util.Set;

public class LabelDTO {

    private Long labelId;
    private String labelName;
    private Set<String> wordList;

    public LabelDTO(Long labelId, String labelName, Set<String> wordList) {
        this.labelId = labelId;
        this.labelName = labelName;
        this.wordList = wordList;
    }

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

    public Set<String> getWordList() {
        return wordList;
    }

    public void setWordList(Set<String> wordList) {
        this.wordList = wordList;
    }
}
