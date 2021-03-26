package com.kry.servicepoller.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.hibernate.type.SerializationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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

    // The timeout for waiting for data
    private static final int SOCKET_TIMEOUT = 60000;
    private static final int MAX_TOTAL_CONNECTIONS = 50;
    private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;

    // Determines the timeout in milliseconds until a connection is established.
    private static final int CONNECT_TIMEOUT = 30000;

    // The timeout when requesting a connection from the connection manager.
    private static final int REQUEST_TIMEOUT = 30000;


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                HeaderElementIterator iterator =
                        new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (iterator.hasNext()) {
                    HeaderElement element = iterator.nextElement();
                    String parameter = element.getName();
                    String value = element.getValue();
                    if (value != null && parameter.equalsIgnoreCase("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                return DEFAULT_KEEP_ALIVE_TIME_MILLIS;
            }
        };
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Pooling Connection Manager Initialisation failure" +
                    "MESSAGE: {}, EXCEPTION : {}", e.getMessage(), e);
        }
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            log.error("Pooling Connection Manager Initialisation failure " +
                    "MESSAGE: {}, EXCEPTION : {}", e.getMessage(), e);
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        PoolingHttpClientConnectionManager poolingConnectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        return poolingConnectionManager;
    }

    @Bean
    public HttpComponentsClientHttpRequestWithBodyFactory clientHttpRequestFactory(@Autowired CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestWithBodyFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestWithBodyFactory();
        // clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
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
