package com.systouch.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_published")
public class Published {

    // ID da publicação para cada usuario
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pub_id")
    private Long idPublished;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @CreationTimestamp
    private Instant creationTimestamp;



}
