package com.quotespilot.service;

import com.quotespilot.dto.Constants;
import com.quotespilot.dto.QuoteDTO;
import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;
import com.quotespilot.entity.User;
import com.quotespilot.repository.QuoteRepository;
import com.quotespilot.repository.TagsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuoteServiceImpl implements QuoteService{

    private static final Logger logger = LoggerFactory.getLogger(QuoteServiceImpl.class);
    
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UserService userService;
    
    @Transactional
    public List<Quote> getAllSavedQuotesForUser(String userName) {
        return quoteRepository.findAllByUsers(userName.trim());
    }

    @Cacheable(value = "userTags", key = "#userName")
    @Transactional
    public List<Tags> getAllSavedTagsForUser(String userName) {
        return tagsRepository.findTagsByUsers(userName)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        /**
         * Below code will work but it will hit multiple sql queries i.e for each quote
         * it will call sql query to get it's tags
         */
       /* return this.getAllSavedQuotesForUser()
                .stream()
                .flatMap(q-> q.getTags().stream())
                .distinct()
                .collect(Collectors.toList());*/
    }
    
    @Transactional
    public List<Quote> getSavedQuotesByTagForUser(String userName, String tag) {
        return quoteRepository.findAllByTagsForUser(userName,tag);
        /**
         * Below Code works but it will call mutiple sql queries
         */
        /*return this.getAllSavedQuotesForUser()
                .stream()
                .filter(q->{
                    Set<Tags> tags = q.getTags();
                    for(Tags t:tags){
                        if(t.getTag().equalsIgnoreCase(tag)) return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());*/
    }

    /**
     * remove the link between User who has called this method and Quote. Quote will stay in DB
     * @param id
     */
    @CacheEvict(value = "userTags", key = "#userName")
    @Transactional
    public void removeSavedQuotesForUser(String userName,Long id) {
        Quote quote = quoteRepository.findOneByUsers(id,userName);
        User u = userService.findByName(userName);
        if(quote != null){
            quote.getUsers().remove(u);
            u.getQuotes().remove(quote);
            quoteRepository.save(quote);
        }
    }

    @Transactional
    public void deleteSavedQuotes(Long id) {
        Optional<Quote> quote = quoteRepository.findById(id);
        if(quote.isPresent()){
            Quote q=quote.get();
            for(Tags t:q.getTags()){
                t.getQuotes().remove(q);
            }
            quoteRepository.delete(q);
        }
    }

    /**
     * Add Quote to DB. Maps new quote to tags and User on which method is called
     * @param` dto
     * @return
     */
    @CacheEvict(value = "userTags", key = "#userName")
    @Transactional
    public Long addQuoteForUser(String userName,QuoteDTO dto) {
        Quote quote=new Quote();
        User u = userService.findByName(userName);
        quote.setAuthor(dto.getAuthor());
        quote.setContent(dto.getText());
        quote.getUsers().add(u);
        //If Tag already exist then map quote to existing Tags.
        List<Tags> savedTags=tagsRepository.findTagsByTag(dto.getTags());
        for(Tags t:savedTags){
            String tagName=t.getTag();
            if(dto.getTags().contains(tagName)){
                t.getQuotes().add(quote);
                quote.getTags().add(t);
                dto.getTags().remove(tagName);
            }
        }
        //If tag is new
        for(String t:dto.getTags()){
            Tags newTag=new Tags();
            newTag.setTag(t);
            newTag.getQuotes().add(quote);
            quote.getTags().add(newTag);
        }
        u.getQuotes().add(quote);
        return quoteRepository.save(quote).getId();
    }
    
    
    /*@PostConstruct
    private void initSampleQuotes() {
        if(userService.findByName("admin")==null) {
            //create admin user
            User user3 = new User();
            user3.setName("admin");
            user3.setHashedPassword("$2a$10$CQkYdHqrPBnGHDaLBGWI5O.6nDkyBrhuKP0WdB0YL2bQdzHlXLIOi");
            user3.setSalt("salt123");
            user3.setRole(Constants.ROLE_ADMIN);

            Tags tag1 = new Tags();
            tag1.setTag("foo");
            Tags tag2 = new Tags();
            tag2.setTag("boo");
            Tags tag3 = new Tags();
            tag3.setTag("chu");
            Quote quote1 = new Quote();
            quote1.setAuthor("myself");
            quote1.setContent("foo boo blablabla");
            Quote quote2 = new Quote();
            quote2.setAuthor("myself 2");
            quote2.setContent("chu blablabla");
            Quote quote3 = new Quote();
            quote3.setAuthor("myself 3");
            quote3.setContent("blablabla boo");

            quote1.getTags().add(tag1);
            quote1.getTags().add(tag2);

            quote2.getTags().add(tag3);

            quote3.getTags().add(tag2);

            tag1.getQuotes().add(quote1);
            tag2.getQuotes().add(quote1);
            tag2.getQuotes().add(quote3);
            tag3.getQuotes().add(quote2);

            User user1 = new User();
            user1.setName("ram");
            user1.setHashedPassword("$2a$10$te0CUkHhRI97MA/ebHlQZOBQL08LAV6chTyN6GF811/EPdt7MOgFG");
            user1.setSalt("");
            user1.setRole(Constants.ROLE_USER);

            User user2 = new User();
            user2.setName("krishna");
            user2.setHashedPassword("$2a$10$KqYYHu9/HDGWaZoPHijmXeUf/yBkKuukjG9WF9WSYpOKs35awiBHK");
            user2.setSalt("");
            user2.setRole(Constants.ROLE_USER);

            user1.getQuotes().add(quote1);
            user1.getQuotes().add(quote2);
            user2.getQuotes().add(quote1);
            user2.getQuotes().add(quote3);

            quote1.getUsers().add(user1);
            quote1.getUsers().add(user2);
            quote2.getUsers().add(user1);
            quote3.getUsers().add(user2);


            userService.save(user1);
            userService.save(user2);
            userService.save(user3);

            quoteRepository.save(quote1);
            quoteRepository.save(quote2);
            quoteRepository.save(quote3);

            //Don't need this part because we have done CascadeType.PERSIST in Quote/Tag mapping
        *//*tagsRepository.save(tag1);
        tagsRepository.save(tag2);
        tagsRepository.save(tag3);*//*
        }
    }*/



}
