package com.quotespilot.repository;

import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote,Long> {
    @Query(value = "select q.* from user u inner join user_quote uq inner join quotes q " +
            "on q.id=uq.quote_id " +
            "and u.id=uq.user_id " +
            "and u.name=:user", nativeQuery = true)
    List<Quote> findAllByUsers(@Param("user") String user);

    @Query(value = "select q.* from " +
            "user u inner join user_quote uq inner join quotes q inner join tags t inner join quote_tag qt " +
            "on q.id=uq.quote_id " +
            "and qt.tag_id =t.id " +
            "and uq.quote_id=qt.quote_id " +
            "and u.id=uq.user_id " +
            "and u.name=:user " +
            "and t.tag=:tag", nativeQuery = true)
    List<Quote> findAllByTagsForUser(@Param("user") String user, @Param("tag") String tag);

    @Query(value = "select q.* from user u inner join user_quote uq inner join quotes q " +
            "on q.id=uq.quote_id " +
            "and u.id=uq.user_id " +
            "and q.id=:id " +
            "and u.name=:user", nativeQuery = true)
    Quote findOneByUsers(@Param("id") Long id,@Param("user") String user);
}   
