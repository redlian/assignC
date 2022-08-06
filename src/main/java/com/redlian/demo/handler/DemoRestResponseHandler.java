package com.redlian.demo.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.redlian.demo.exception.DemoException;

@Component
public class DemoRestResponseHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(final ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == Series.CLIENT_ERROR
                || response.getStatusCode().series() == Series.SERVER_ERROR;
    }

    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        final HttpStatus statusCode = response.getStatusCode();
        System.out.println("DEMO HANDLER");
        throw new DemoException("ResponseError", statusCode);
    }

}
