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
    //Using JPQL
    @Query(value = "SELECT q FROM Quote q " +
            "JOIN q.users u " +
            //Notice JOIN FETCH of JPQL. It will solve N+1 problem. i.e 
            // Tags will be fetched along with Quote only in a single query 
            // instead of firing separate SQL query for tags
            "JOIN FETCH q.tags t " +
            "WHERE u.name =:user " )
    List<Quote> findAllByUsers(@Param("user") String user);

    @Query(value = "SELECT q FROM Quote q " +
            "JOIN q.users u " +
            "JOIN FETCH q.tags t " +
            "WHERE u.name =:user " +
            "and t.tag=:tag" )
    List<Quote> findAllByTagsForUser(@Param("user") String user, @Param("tag") String tag);

    @Query(value = "select q.* from user u inner join user_quote uq inner join quotes q " +
            "on q.id=uq.quote_id " +
            "and u.id=uq.user_id " +
            "and q.id=:id " +
            "and u.name=:user", nativeQuery = true)
    Quote findOneByUsers(@Param("id") Long id,@Param("user") String user);
}   
