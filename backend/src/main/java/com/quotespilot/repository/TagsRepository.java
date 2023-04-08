package com.quotespilot.repository;

import com.quotespilot.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tags,Long> {
    Tags findTagByTag(String tag);

    @Query(value = "select t.* from tags t where t.tag in (:tags)"
            , nativeQuery = true)
    List<Tags> findTagsByTag(@Param("tags") List<String> tag);

    @Query(value = "select t.* from " +
            "tags t " +
            "inner join user_quote uq " +
            "inner join user u " +
            "inner join quote_tag qt " +
            "on qt.tag_id =t.id " +
            "and uq.quote_id=qt.quote_id " +
            "and u.id=uq.user_id " +
            "and u.name=:user " ,nativeQuery = true)
    List<Tags> findTagsByUsers(@Param("user") String user);
}
