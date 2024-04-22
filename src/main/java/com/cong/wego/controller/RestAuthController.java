package com.cong.wego.controller;


import com.cong.oauth.request.AuthRequest;
import com.cong.wego.config.GitHubConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/oauth")
public class RestAuthController {
    @Resource
    private GitHubConfig gitHubConfig;

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = gitHubConfig.getAuthRequest();
        response.sendRedirect(authRequest.authorize("wegoGithub"));
    }

}
