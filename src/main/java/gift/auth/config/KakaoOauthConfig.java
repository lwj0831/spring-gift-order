package gift.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KakaoOauthProperties.class)
public class KakaoOauthConfig {

}
