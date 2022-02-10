package com.quiz.restTemplate;

import com.quiz.Dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class RestTemplateService {
    @Value("${service.account.api.host}")
    private String libraryServiceHost;

    private final String subpass = "/{id}";

    @Autowired
    RestTemplate restTemplate;

    public AccountDto getName(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(libraryServiceHost);

        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);

        AccountDto user = restTemplate.getForObject(uriBuilder.toUriString() + subpass, AccountDto.class,params);
        return user;
    }
}
