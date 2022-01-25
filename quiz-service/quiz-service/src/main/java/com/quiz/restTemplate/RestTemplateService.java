package com.quiz.restTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RestTemplateService {
    @Value("${service.account.api.host}")
    private String libraryServiceHost;

    private final String subpass = "/int/getnamebyid/{id}";
    @Autowired
    org.springframework.web.client.RestTemplate restTemplate;

    public Object getName(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);

        Map<String, Integer> params = new HashMap<>();
        params.put("accountId", id);

        Object name = restTemplate.getForObject(uriBuilder.toUriString() + subpass, List.class,params);
        return name;
    }
}
