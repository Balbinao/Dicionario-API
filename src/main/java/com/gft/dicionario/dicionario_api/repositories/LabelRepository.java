package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Label;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {


    Set<Label> findByWordList_Term(String term);

//    @Query("SELECT DISTINCT l FROM Label l JOIN FETCH l.wordList w WHERE w.term = :term")
//    Set<Label> findByWordList_Term(@Param("term") String term);

}
