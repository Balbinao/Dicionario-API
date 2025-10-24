package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

   Set<Word> findByLabelList_LabelName(String labelName);

//    @Query("SELECT l FROM Label l JOIN FETCH l.wordList WHERE l.labelName = :labelName")
//    Set<Word> findByLabelList_LabelName(@Param("labelName") String labelName);
}
