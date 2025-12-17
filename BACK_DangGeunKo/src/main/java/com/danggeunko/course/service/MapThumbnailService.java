package com.danggeunko.course.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.danggeunko.course.dto.MapPoint;

@Service
public class MapThumbnailService {

    private static final String RENDER_URL = "http://localhost:4001/render";

    public byte[] render(List<MapPoint> points) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("points", points);
        body.put("width", 400);
        body.put("height", 300);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response =
                restTemplate.exchange(
                        RENDER_URL,
                        HttpMethod.POST,
                        request,
                        byte[].class
                );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new IllegalStateException("Map render failed");
        }

        return response.getBody();
    }
}
