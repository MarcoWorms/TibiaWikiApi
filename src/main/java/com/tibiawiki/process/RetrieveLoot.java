package com.tibiawiki.process;

import benjaminkomen.jwiki.core.NS;
import com.tibiawiki.domain.factories.ArticleFactory;
import com.tibiawiki.domain.factories.JsonFactory;
import com.tibiawiki.domain.repositories.ArticleRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class RetrieveLoot extends RetrieveAny {

    private static final String LOOT_STATISTICS_CATEGORY_NAME = "Loot Statistics";

    @Autowired
    public RetrieveLoot(ArticleRepository articleRepository, ArticleFactory articleFactory, JsonFactory jsonFactory) {
        super(articleRepository, articleFactory, jsonFactory);
    }

    public List<String> getLootList() {
        final List<String> lootStatisticsCategory = articleRepository.getPageNamesFromCategory(LOOT_STATISTICS_CATEGORY_NAME, new NS(112));

        return new ArrayList<>(lootStatisticsCategory);
    }

    public Stream<JSONObject> getLootJSON() {
        return getArticlesFromLoot2TemplateAsJSON(getLootList());
    }

    public Optional<JSONObject> getLootJSON(String pageName) {
        return getLootArticleAsJSON(pageName);
    }

    private Stream<JSONObject> getArticlesFromLoot2TemplateAsJSON(List<String> pageNames) {
        return Stream.of(pageNames)
                .flatMap(lst -> articleRepository.getArticlesFromCategory(lst).entrySet().stream())
                .map(e -> {
                    var lootPartOfArticle = articleFactory.extractLootPartOfArticle(e);
                    return jsonFactory.convertLootPartOfArticleToJson(e.getKey(), lootPartOfArticle);
                });
    }

    private Optional<JSONObject> getLootArticleAsJSON(String pageName) {
        return Optional.ofNullable(articleRepository.getArticle(pageName))
                .map(articleContent -> articleFactory.extractLootPartOfArticle(pageName, articleContent))
                .map(lootPartOfArticle -> jsonFactory.convertLootPartOfArticleToJson(pageName, lootPartOfArticle));
    }
}
