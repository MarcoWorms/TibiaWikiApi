package com.tibiawiki.process;

import com.tibiawiki.domain.factories.ArticleFactory;
import com.tibiawiki.domain.factories.JsonFactory;
import com.tibiawiki.domain.repositories.ArticleRepository;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RetrieveItems extends RetrieveAny {

    private static final String CATEGORY = "Items";

    public RetrieveItems() {
        super();
    }

    public RetrieveItems(ArticleRepository articleRepository, ArticleFactory articleFactory, JsonFactory jsonFactory) {
        super(articleRepository, articleFactory, jsonFactory);
    }

    public List<String> getItemsList() {
        final List<String> keysCategory = new ArrayList<>();
        for (String pageName : articleRepository.getMembersFromCategory(CATEGORY)) {
            keysCategory.add(pageName);
        }

        final List<String> listsCategory = new ArrayList<>();
        for (String pageName : articleRepository.getMembersFromCategory(CATEGORY_LISTS)) {
            listsCategory.add(pageName);
        }

        return keysCategory.stream()
                .filter(page -> !listsCategory.contains(page))
                .collect(Collectors.toList());
    }

    public Stream<JSONObject> getItemsJSON() {
        return getItemsJSON(ONE_BY_ONE);
    }

    public Stream<JSONObject> getItemsJSON(boolean oneByOne) {
        final List<String> keysList = getItemsList();

        return oneByOne
                ? obtainArticlesOneByOne(keysList)
                : obtainArticlesInBulk(keysList);
    }

    public Optional<JSONObject> getItemJSON(String pageName) {
        return super.getArticleJSON(pageName);
    }
}