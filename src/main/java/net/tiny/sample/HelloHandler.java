package net.tiny.sample;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HelloHandler implements HttpHandler {

    protected String path;
    protected List<Filter> filters = new ArrayList<>(); //Option
    protected Authenticator auth = null;  //Option

    @Override
    public void handle(HttpExchange he) throws IOException {
        final String method = he.getRequestMethod();
        final String name = getURIParameters(he, 0);
        final String responseBody = String.format("{\"method\":\"%s\", \"msg\":\"Hello %s\"}", method, name);
        final byte[] rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
        he.getResponseHeaders().add("Content-type", "application/json; charset=utf-8");
        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
        he.getResponseBody().write(rawResponseBody);
    }

    private String getURIParameters(final HttpExchange he, final int index) {
        final String requestPath = he.getHttpContext().getPath();
        final String uri = he.getRequestURI().toString();
        // easy case no sub item relative path
        int i = uri.indexOf(requestPath);
        if (i >= 0) {
            String uriParameters = (uri.length() > requestPath.length()) ?
                    uri.substring( i + requestPath.length() + 1 ) : uri.substring( i + requestPath.length());
            // "foo/fie/bar" --> "foo","fie","bar"
            try {
                return uriParameters.split("/")[index];
            } catch (Exception e) {}
        }
        return "";

    }

    private Map<String, List<String>> getQueryParameters(final HttpExchange he) throws IOException {
        final Map<String, List<String>> request = new LinkedHashMap<>();
        final String query = he.getRequestURI().getRawQuery();
        if (query != null) {
            final String[] parameters = query.split("[&;]", -1);
            for (final String prameter : parameters) {
                final String[] kv = prameter.split("=", 2);
                final String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8.name());
                request.putIfAbsent(key, new ArrayList<>());
                final String value = kv.length > 1 ?
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name()) : null;
                        request.get(key).add(value);
            }
        }
        return request;
    }

}
