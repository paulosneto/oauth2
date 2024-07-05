package com.systouch.controller;

import com.systouch.domain.Published;
import com.systouch.domain.Role;
import com.systouch.dto.FeedDTO;
import com.systouch.dto.FeedItemDTO;
import com.systouch.dto.PublicationsDTO;
import com.systouch.repository.PublishedRepository;
import com.systouch.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/publications")
public class PublishedController {

    // Repositorys para injeção de dependências
    private final PublishedRepository publishedRepository;
    private final UserRepository userRepository;

    // Construtor para injeção de dependências
    public PublishedController(PublishedRepository publishedRepository, UserRepository userRepository) {
        this.publishedRepository = publishedRepository;
        this.userRepository = userRepository;
    }

    // Faz a criação de novas publicações
    @PostMapping
    public ResponseEntity<Void> newPublication(@RequestBody PublicationsDTO dto, JwtAuthenticationToken token) {

        // Verifica se o usuario passado no login existe na base
        var user = this.userRepository.findById(UUID.fromString(token.getName()));

        // Criando objeto para fazer a publicação
        var pub = new Published();
        pub.setUser(user.get());
        pub.setContent(dto.content());

        //Salvando objeto
        publishedRepository.save(pub);

        //Retornando status para requisição do usuario
        return ResponseEntity.ok().build();
    }

    // Faz o retorno de todos os "feeds" cadastrados por todos os usuarios
    @GetMapping("/feed")
    public ResponseEntity<FeedDTO> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

            // Dentro do metodo de busca "findAll" faz uma paginação para ordenar o total de itens do maior para o menor
       var feeds = this.publishedRepository.findAll(PageRequest.of(page,
                       pageSize,
                       Sort.Direction.DESC,
                       "creationTimestamp"))
               .map(feed -> new FeedItemDTO(feed.getIdPublished(),
                                            feed.getContent(),
                                            feed.getUser().getUserName())
                    );

       return ResponseEntity.ok(new FeedDTO(feeds.getContent(), page, pageSize, feeds.getTotalPages(), feeds.getTotalElements()));

    }

    // Faz delete de publicações pelo id informado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable("id") Long idPub, JwtAuthenticationToken token){

        // verifica se existe o post com o id informado
        var pub = this.publishedRepository.findById(idPub).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Verifica se o usuario logado existe
        var user = this.userRepository.findById(UUID.fromString(token.getName()));

        // Se o usuario logado for "admin" terá a possibilidade de apagar qualquer registro
        var isAdmin = user.get().getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        //Faz verificação para saber se é admin ou basic e ai procedo com a deleção
        if(isAdmin || pub.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            this.publishedRepository.deleteById(idPub);
        }else{
            // Caso o usuario seja basico e nao seja dono do post a tentar deletar, retorna esse status
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();

    }



}
