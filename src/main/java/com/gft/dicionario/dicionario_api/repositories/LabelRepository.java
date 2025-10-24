package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Label;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    //Set<Label> findByWordList(String term);

    //@Query("SELECT l FROM Label l JOIN l.words w WHERE w.term = :term")
    //Set<Label> findByWord(@Param("term") String term);

}
