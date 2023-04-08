package com.quotespilot.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quotespilot.dto.QuoteDTO;
import com.quotespilot.dto.TagsDTO;
import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;
import com.quotespilot.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quote")
public class QuoteRestController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteRestController.class);
    @Autowired
    private QuoteService quoteService;
    @Autowired
    WebClient quoteClient;

    @Value("${quote.search.limit}")
    private String searchLimit;
    
    //http://localhost:8080/quote/quotes-all
    @GetMapping("/quotes-all")
    public ResponseEntity<List<QuoteDTO>> getAllSavedQuotesForUser(){
        logger.info("**********************************getAllSavedQuotesForUser");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
         List<Quote> quotes=quoteService.getAllSavedQuotesForUser(userName);
        List<QuoteDTO> dtos = quotes.stream().map(q -> {
            QuoteDTO dto = new QuoteDTO();
            dto.setId(q.getId().intValue());
            dto.setText(q.getContent());
            dto.setAuthor(q.getAuthor());
            dto.setTags(q.getTags().stream().map(Tags::getTag).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    //http://localhost:8080/quote/tags
    @GetMapping("/tags")
    public ResponseEntity<List<TagsDTO>> getAllSavedTagsForUser(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Tags> tags = quoteService.getAllSavedTagsForUser(userName);
        List<TagsDTO> dtos = tags.stream().map(t -> {
            TagsDTO dto = new TagsDTO();
            dto.setId(t.getId());
            dto.setTag(t.getTag());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    //http://localhost:8080/quote/search-by-tag?tag=tag3
    @GetMapping("/search-by-tag")
    public ResponseEntity<List<QuoteDTO>> getSavedQuotesByTagForUser(@RequestParam String tag){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Quote> qt = quoteService.getSavedQuotesByTagForUser(userName,tag);
        logger.info(tag+" ********************** "+qt.size());
        List<QuoteDTO> dtos = qt.stream().map(q -> {
            QuoteDTO dto = new QuoteDTO();
            dto.setId(q.getId().intValue());
            dto.setText(q.getContent());
            dto.setAuthor(q.getAuthor());
            dto.setTags(q.getTags().stream().map(Tags::getTag).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    //http://localhost:8080/quote/add-quote/
    @PostMapping("/add-quote/")
    public ResponseEntity addQuoteForUser(@RequestBody QuoteDTO quoteDTO){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Long createdQuoteId=quoteService.addQuoteForUser(userName,quoteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuoteId);
    }
    
    //http://localhost:8080/remove-quote/{id}/
    @DeleteMapping("/remove-quote/{id}")
    public ResponseEntity removeSavedQuotesForUser(@PathVariable("id") Long id){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
       quoteService.removeSavedQuotesForUser(userName,id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //http://localhost:8080/delete-quote/{id}/
    @DeleteMapping("/delete-quote/{id}")
    public ResponseEntity deleteSavedQuotes(@PathVariable("id") Long id){
        System.out.println("****************************delete");
        quoteService.deleteSavedQuotes(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //http://localhost:8080/quote/random
    @GetMapping("/random")
     public Mono<QuoteDTO> getRandomQuoteFromInternet(){
        return quoteClient.get()
                 .uri("/random")
                 .retrieve()
                 .bodyToMono(String.class)
                //Mapping json response to Quote class 
                .map(response->{
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = null;
                    try {
                        rootNode = mapper.readTree(response);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    QuoteDTO quote = new QuoteDTO();
                    quote.setId(1);
                    quote.setText(rootNode.get("content").asText());
                    quote.setAuthor(rootNode.get("author").asText());
                    List<String> tags = new ArrayList<>();
                    for (JsonNode tagNode : rootNode.get("tags")) {
                        tags.add(tagNode.asText());
                    }
                    quote.setTags(tags);
                    return quote;
                });
     }
     
     //http://localhost:8080/quote/search?query=joy
    @GetMapping("/search")
    public Flux<QuoteDTO> searchQuoteFromInternet(@RequestParam String query){
        return quoteClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search/quotes")
                        .queryParam("query",query)
                        .queryParam("limit",searchLimit)
                        .build())
                .retrieve()
                //Mapping json response to Quote class 
                .bodyToMono(String.class)
                .flatMapMany(response -> {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = null;
                    try {
                        rootNode = mapper.readTree(response);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    JsonNode resultsNode = rootNode.get("results");
                    AtomicInteger count =new AtomicInteger(0);
                    return Flux.fromIterable(resultsNode)
                            .map(resultNode -> {
                                QuoteDTO quote = new QuoteDTO();
                                quote.setId(count.incrementAndGet());
                                List<String> tags = new ArrayList<>();
                                for (JsonNode tagNode : resultNode.get("tags")) {
                                    tags.add(tagNode.asText());
                                }
                                quote.setTags(tags);
                                quote.setAuthor(resultNode.get("author").asText());
                                quote.setText(resultNode.get("content").asText());
                                return quote;
                            });
                });
    }
}
