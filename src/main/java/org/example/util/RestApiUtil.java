package org.example.util;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Map;

public class RestApiUtil {

    public void RestUtil() {
    }

    public static HttpResponse<String> call(String method, Map<String, String> headers, String url, String body) {
        HttpResponse<String> httpResponse;
        if ("POST".equals(method)) {
            httpResponse = Unirest.post(url)
                    .headers(headers)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();

        } else if ("PUT".equals(method)) {
            httpResponse = Unirest.put(url)
                    .headers(headers)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();

        } else if ("DELETE".equals(method)) {
            httpResponse = Unirest.delete(url)
                    .headers(headers)
                    .asString();

        } else {
            httpResponse = Unirest.get(url)
                    .headers(headers)
                    .asString();
        }
        return httpResponse;
    }
}
