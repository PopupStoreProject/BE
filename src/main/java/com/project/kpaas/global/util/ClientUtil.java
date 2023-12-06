package com.project.kpaas.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
public class ClientUtil {

    @Value("${crawl.server.url}")
    private String url;

    public void requestToCrawlServer(Long popupId, String popupTitle, String popupDate) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Header set
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Body set
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("popupId", popupId.toString());
        body.add("popupTitle", popupTitle);
        body.add("popupDate", popupDate);

        // Message
        HttpEntity<?> requestMessage = new HttpEntity<>(body, httpHeaders);

        log.info("requestMessage {}",requestMessage);

    }

}
