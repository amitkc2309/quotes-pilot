package com.quotespilot.repository;

import com.quotespilot.entity.Quote;
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

    @Query(value = "select q.* from user u inner join user_quote uq inner join quotes q " +
            "on q.id=uq.quote_id " +
            "and u.id=uq.user_id " +
            "and q.id=:id " +
            "and u.name=:user", nativeQuery = true)
    Quote findOneByUsers(@Param("id") Long id,@Param("user") String user);
}   
