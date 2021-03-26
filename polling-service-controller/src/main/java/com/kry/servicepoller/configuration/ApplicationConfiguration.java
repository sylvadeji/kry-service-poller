package com.kry.servicepoller.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.hibernate.type.SerializationException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

@Configuration
@Slf4j
public class ApplicationConfiguration {
    private static final String ERROR_SUFFIX = " failed {}";
    public static final String EXCEPTION = "EXCEPTION";

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Scope("prototype")
    public RestTemplate getRestTemplatePrototype() {
        return new RestTemplate(new HttpComponentsClientHttpRequestWithBodyFactory());
    }

    private static final class HttpComponentsClientHttpRequestWithBodyFactory extends HttpComponentsClientHttpRequestFactory {
        @Override
        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
            if (httpMethod == HttpMethod.GET) {
                return new HttpGetRequestWithEntity(uri);
            }
            return super.createHttpUriRequest(httpMethod, uri);
        }
    }

    private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
        public HttpGetRequestWithEntity(final URI uri) {
            super.setURI(uri);
        }

        @Override
        public String getMethod() {
            return HttpMethod.GET.name();
        }
    }

    //For ZoneDate
    private static final String UNSUPPORTED_DATE_TIME_FORMAT = "Unsupported date time format ";

    private static final String METHOD_NAME = "HttpClientConfiguration StdSerializer";

    @SuppressWarnings("serial")
    public static class CustomZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

        private static final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]");

        public CustomZonedDateTimeDeserializer() {
            this(null);
        }

        public CustomZonedDateTimeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ZonedDateTime deserialize(JsonParser jsonparser, DeserializationContext context)
                throws IOException {
            String date = jsonparser.getText();
            TemporalAccessor dt;
            try {
                dt = formatter.parseBest(date, ZonedDateTime::from, LocalDateTime::from);
            } catch (DateTimeParseException e) {
                log.error(METHOD_NAME + ERROR_SUFFIX, EXCEPTION, UNSUPPORTED_DATE_TIME_FORMAT + date);
                throw new SerializationException(UNSUPPORTED_DATE_TIME_FORMAT + date, e);
            }
            if (dt instanceof LocalDateTime) {
                dt = ZonedDateTime.of((LocalDateTime) dt, ZoneOffset.UTC);
                dt = ((ZonedDateTime) dt).withZoneSameInstant(ZoneId.systemDefault());
            } else if (dt instanceof ZonedDateTime) {
                ZonedDateTime zdt = (ZonedDateTime) dt;
                if (!zdt.getOffset().equals(ZoneOffset.systemDefault())) {
                    dt = zdt.withZoneSameInstant(ZoneId.systemDefault());
                }
            } else {
                log.error(METHOD_NAME + ERROR_SUFFIX, EXCEPTION, UNSUPPORTED_DATE_TIME_FORMAT + date);
                throw new SerializationException(UNSUPPORTED_DATE_TIME_FORMAT + date, null);
            }
            return (ZonedDateTime) dt;
        }
    }
}
