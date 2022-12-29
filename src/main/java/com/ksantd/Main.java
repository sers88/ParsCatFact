package com.ksantd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        List<CatFakt> catFakts = getResponse(URI);
        catFakts.stream().filter(catFakt -> catFakt.getUpvotes() != null && catFakt.getUpvotes() > 0)
                .map(CatFakt::getText)
                .forEach(System.out::println);
    }

    public static List<CatFakt> getResponse(String uri) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet(uri);
        try {
            HttpResponse response = httpClient.execute(request);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}