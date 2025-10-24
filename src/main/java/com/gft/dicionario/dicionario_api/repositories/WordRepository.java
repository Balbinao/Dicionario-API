package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    //Set<Word> findByLabelList(String labelName);

    //@Query("SELECT w FROM Word w JOIN w.labels l WHERE l.name = :labelName")
    //Set<Word> findByLabel(@Param("labelName") String labelName);
}
