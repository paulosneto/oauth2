package com.systouch.domain;

import com.systouch.dto.PublicationsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_published")
public class Published {

    // ID da publicação para cada usuario
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pub_id")
    private Long idPublished;

    // Coluna do id do usuário
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // conteudo da publicação
    private String content;

    // Armazena o momento exato da criação
    @CreationTimestamp
    private Instant creationTimestamp;



}
