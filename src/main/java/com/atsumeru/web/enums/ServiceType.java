package com.atsumeru.web.enums;

import com.atsumeru.web.model.database.DatabaseFields;
import com.atsumeru.web.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ServiceType {
    // Объявление сервисов с регулярными выражениями
    MYANIMELIST("mal", "https://myanimelist.net/manga/%s", Pattern.compile("manga/(\\d+)"), DatabaseFields.MAL_ID),
    SHIKIMORI("shiki", "https://shikimori.me/mangas/%s", Pattern.compile("mangas/(\\d+)"), DatabaseFields.SHIKIMORI_ID),
    KITSU("kt", "https://kitsu.io/manga/%s", Pattern.compile("manga/([\\w-]+)"), DatabaseFields.KITSU_ID),
    ANILIST("al", "https://anilist.co/manga/%s", Pattern.compile("manga/(\\d+)"), DatabaseFields.ANILIST_ID),
    MANGAUPDATES("mu", "https://www.mangaupdates.com/series/%s", Pattern.compile("series/([\\w-]+)"), DatabaseFields.MANGAUPDATES_ID),
    ANIMEPLANET("ap", "https://www.anime-planet.com/manga/%s", Pattern.compile("manga/([\\w-]+)"), DatabaseFields.ANIMEPLANET_ID),
    COMICVINE("cv", "https://comicvine.gamespot.com/comic/%s/", Pattern.compile("(\\d+-\\d+)"), DatabaseFields.COMICVINE_ID),
    COMICSDB("cdb", "https://comicsdb.ru/publishers/%s", Pattern.compile("publishers/(.*)"), DatabaseFields.COMICSDB_ID),
    HENTAG("htg", "https://hentag.com/vault/%s", Pattern.compile("vault/(.*)"), DatabaseFields.HENTAG_ID);

    // Объявление логгера
    private static final Logger logger = LoggerFactory.getLogger(ServiceType.class);

    @Getter
    private final String simpleName;
    private final String formatUrl;
    private final Pattern idPattern;
    @Getter
    private final String dbFieldName;

    // Конструктор
    ServiceType(String simpleName, String formatUrl, Pattern idPattern, String dbFieldName) {
        this.simpleName = simpleName;
        this.formatUrl = formatUrl;
        this.idPattern = idPattern;
        this.dbFieldName = dbFieldName;
    }

    // Метод для извлечения ID
    public String extractId(String str) {
        logger.info("Extracting ID from URL: " + str);  // Логирование входящего URL
        Matcher matcher = idPattern.matcher(str);
        if (matcher.find()) {
            String firstGroup = matcher.group(1);
            String secondGroup = null;
            try {
                secondGroup = matcher.group(2);
            } catch (Exception ignored) {
            }
            String extractedId = StringUtils.getFirstNotEmptyValue(firstGroup, secondGroup);
            logger.info("Extracted ID: " + extractedId);  // Логирование извлечённого ID
            return extractedId;
        }
        logger.warn("No ID found in URL: " + str);  // Логирование, если ID не извлечён
        return null;
    }

    // Метод для создания URL по ID
    public String createUrl(String id) {
        return String.format(formatUrl, id);
    }

    // Поиск типа сервиса по простому имени
    public static @Nullable ServiceType getTypeBySimpleName(@Nullable String name) {
        return Arrays.stream(ServiceType.values())
                .filter(serviceType -> StringUtils.equalsIgnoreCase(serviceType.name(), name) || StringUtils.equalsIgnoreCase(serviceType.getSimpleName(), name))
                .findFirst()
                .orElse(null);
    }

    // Получение имени поля базы данных для простого имени сервиса
    public static @Nullable String getDbFieldNameForSimpleName(@Nullable String name) {
        return Optional.ofNullable(getTypeBySimpleName(name))
                .map(ServiceType::getDbFieldName)
                .orElse(null);
    }
}
