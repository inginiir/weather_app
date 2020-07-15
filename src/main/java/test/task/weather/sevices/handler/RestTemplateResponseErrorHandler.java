package test.task.weather.sevices.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import test.task.weather.sevices.exceptions.CityNotFoundException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse
                .getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR || httpResponse
                .getStatusCode()
                .series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse
                .getStatusCode()
                .series() == HttpStatus.Series.SERVER_ERROR) {
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        } else if (httpResponse
                .getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CityNotFoundException();
            }
        }
    }
}
