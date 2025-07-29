package gift.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record TextTemplate(
    String objectType,
    String text,
    Link link
) {

    public TextTemplate(String text, String url) {
        this("text", text, new Link(url, url));
    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record Link(
        String webUrl,
        String mobileWebUrl
    ) {

    }
}

