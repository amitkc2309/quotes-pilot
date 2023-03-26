package com.quotespilot.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "quotes")
public class Quote implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private String author;
    
    @ManyToMany(mappedBy = "quotes",cascade = {CascadeType.PERSIST})
    private Set<Tags> tags = new HashSet<>();
    
    @ManyToMany
    @JoinTable(name = "user_quote",
            joinColumns = @JoinColumn(name = "quote_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();
}
