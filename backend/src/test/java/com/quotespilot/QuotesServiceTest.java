package com.quotespilot;

import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;
import com.quotespilot.repository.QuoteRepository;
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
    
    List<Quote> quoteList=new ArrayList();
    
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
    }

    @Before
    public void mockAuthentication() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testUser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    public void testgetAllSavedQuotesForUser(){
        when(quoteRepository.findAllByUsers("testUser")).thenReturn(quoteList);
        Long id=quoteService.getAllSavedQuotesForUser("testUser").get(0).getId();
        assertEquals(Long.valueOf(1),id);
    }

    @Test
    public void testgetAllSavedTagsForUser(){
        when(quoteService.getAllSavedQuotesForUser("testUser")).thenReturn(quoteList);
        String t1 =quoteService.getAllSavedTagsForUser("testUser").get(0).getTag();
        assertEquals("t1",t1);
    }
}
