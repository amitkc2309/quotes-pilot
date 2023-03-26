package com.quotespilot.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tags implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String tag;
    
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "quote_tag",
            joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "quote_id", referencedColumnName = "id"))
    private Set<Quote> quotes = new HashSet<>();
}
