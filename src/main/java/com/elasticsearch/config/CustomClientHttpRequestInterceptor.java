package com.elasticsearch.config;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        //打印请求明细
        logRequestDetails(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        //打印响应明细
        logResponseDetails(response);
        return response;
    }

    private void logRequestDetails(HttpRequest request, byte[] body) {
        log.info("Headers: {} body: {} {}：{}", request.getHeaders(), new String(body, Charsets.UTF_8), request.getMethod(), request.getURI());
    }

    private void logResponseDetails(ClientHttpResponse response) throws IOException {

        log.info("Status code  : {}    Status text  : {} Headers      : {}  Response body: {}", response.getStatusCode(), response.getStatusText(),
                response.getHeaders(), StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));

    }
}