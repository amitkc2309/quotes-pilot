package com.quotespilot.service;

import com.quotespilot.dto.QuoteDTO;
import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;

import java.util.List;

public interface QuoteService {
    List<Quote> getAllSavedQuotesForUser(String userName);
    void removeSavedQuotesForUser(String userName,Long id);
    Long addQuoteForUser(String userName,QuoteDTO dto);
    List<Tags> getAllSavedTagsForUser(String userName);
    List<Quote> getSavedQuotesByTagForUser(String userName,String tag);
    void deleteSavedQuotes(Long id);
}
