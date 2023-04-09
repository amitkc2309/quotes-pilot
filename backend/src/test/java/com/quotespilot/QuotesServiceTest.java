package com.quotespilot;

import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;
import com.quotespilot.repository.QuoteRepository;
import com.quotespilot.repository.TagsRepository;
import com.quotespilot.service.QuoteService;
import com.quotespilot.service.QuoteServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuotesServiceTest {
    
    @InjectMocks
    QuoteServiceImpl quoteService;
    
    @Mock
    QuoteRepository quoteRepository;

    @Mock
    TagsRepository tagsRepository;
    
    List<Quote> quoteList=new ArrayList();

    List<Tags> tagList=new ArrayList();
    
    SecurityContextHolder securityContextHolder;
    
    @Before
    public void mockQuote(){
        Quote q1=new Quote();
        q1.setId(Long.valueOf(1));
        q1.setAuthor("author1");
        q1.setContent("content");
        Tags t1=new Tags();
        t1.setTag("t1");
        q1.getTags().add(t1);
        quoteList.add(q1);

        Tags t2=new Tags();
        t2.setTag("t2");
        tagList.add(t2);
    }
    
    @Test
    public void testgetAllSavedQuotesForUser(){
        when(quoteRepository.findAllByUsers("testUser")).thenReturn(quoteList);
        Long id=quoteService.getAllSavedQuotesForUser("testUser").get(0).getId();
        assertEquals(Long.valueOf(1),id);
    }

    @Test
    public void testgetAllSavedTagsForUser(){
        when(tagsRepository.findTagsByUsers("testUser")).thenReturn(tagList);
        String t2 =quoteService.getAllSavedTagsForUser("testUser").get(0).getTag();
        assertEquals("t2",t2);
    }
}
