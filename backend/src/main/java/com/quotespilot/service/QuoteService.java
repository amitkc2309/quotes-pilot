package com.quotespilot.service;

import com.quotespilot.dto.QuoteDTO;
import com.quotespilot.entity.Quote;
import com.quotespilot.entity.Tags;

import java.util.List;

public interface QuoteService {
    List<Quote> getAllSavedQuotesForUser();
    void removeSavedQuotesForUser(Long id);
    Long addQuoteForUser(QuoteDTO dto);
    List<Tags> getAllSavedTagsForUser();
    List<Quote> getSavedQuotesByTagForUser(String tag);
    void deleteSavedQuotes(Long id);
}
