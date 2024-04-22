package com.cong.wego.config;

import com.cong.oauth.config.AuthConfig;
import com.cong.oauth.config.AuthDefaultSource;
import com.cong.oauth.request.AuthRequest;
import com.cong.oauth.request.code.AuthGithubRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Git Hub 配置
 *
 * @author cong
 * @date 2024/04/22
 */
@Configuration
public class GitHubConfig {
    @Value("${github.client-id}")
    private String clientId;
    @Value("${github.client-secret}")
    private String clientSecret;
    @Value("${github.redirect-uri}")
    private String redirectUri;


    public AuthRequest getAuthRequest(){
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .build(), AuthDefaultSource.GITHUB);
    }
}
