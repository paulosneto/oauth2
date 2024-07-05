package com.systouch.dto;


import java.util.List;

// DTO para montar o FEED
public record FeedDTO (List<FeedItemDTO> feedItem,
                       int page,
                       int pageSize,
                       int totalPages,
                       long totalElements){
}
