package com.reddit.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "subredditWithPostsAndUser",
        attributeNodes = {
                @NamedAttributeNode(value = "posts", subgraph = "postSubgraph"),
                @NamedAttributeNode(value = "user")
        },
        subgraphs = {
                @NamedSubgraph(name = "postSubgraph", attributeNodes = @NamedAttributeNode("user"))
        }
)
public class SubReddit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
