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

    @Query(value = "SELECT t FROM Tags t " +
            "JOIN t.quotes q " +
            "JOIN q.users u " +
            "WHERE u.name =:user ")
    List<Tags> findTagsByUsers(@Param("user") String user);
}
