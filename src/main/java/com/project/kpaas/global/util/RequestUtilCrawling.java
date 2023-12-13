package com.project.kpaas.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RequestUtilCrawling {

    public String sendRequest(Long popupId, String popupTitle, String popupDate) throws Exception{
        URI uri = UriComponentsBuilder
                .fromUriString("https://flask-review.k-paas.org")
                .path("/crawl")
                .encode()
                .build()
                .toUri();

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        // ObjectMapper 활용한 JSON 바인딩
        Map<String, String> body = new HashMap<>();
        body.put("popup_title", popupTitle);
        body.put("popup_date", popupDate);
        body.put("popup_id", popupId.toString());

        HttpEntity entity = new HttpEntity(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        String entityBody = responseEntity.getBody();

        // 요청 후 응답 확인
        log.info("header : {}", headers);
        log.info("body : {}", body);

        return entityBody;
    }
}
