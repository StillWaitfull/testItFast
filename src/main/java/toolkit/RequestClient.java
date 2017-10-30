package toolkit;

import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class RequestClient {
    private static final Logger log = LoggerFactory.getLogger(RequestClient.class);
    private static final String ENCODING = "UTF-8";

    private String responseText = "";
    private StringEntity body;
    private final List<BasicHeader> headers = new ArrayList<>();
    private static final RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
    private static final CookieStore cookieStore = new BasicCookieStore();
    private static final HttpClientContext context = HttpClientContext.create();
    private int statusCode;
    private static final CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(new PoolingHttpClientConnectionManager())
            .disableRedirectHandling()
            .setDefaultRequestConfig(globalConfig)
            .setSSLContext(createSslContext())
            .setDefaultCookieStore(cookieStore)
            .build();

    static {
        context.setCookieStore(cookieStore);
    }


    public RequestClient addHeader(String name, String value) {
        headers.add(new BasicHeader(name, value));
        return this;
    }

    public RequestClient setBody(String body) {
        this.body = new StringEntity(body, ENCODING);
        return this;
    }

    public String getResponseText() {
        return responseText;
    }

    private static SSLContext createSslContext() {
        try {
            return SSLContexts.custom()
                    .loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("error with ssl context");
    }


    public RequestClient sendRequest(METHODS method, String url, List<BasicNameValuePair> params) {
        HttpEntityEnclosingRequestBase request;
        try {
            URI uri = new URI(method.equals(METHODS.GET) ? (url + "?" + URLEncodedUtils.format(params, ENCODING)) : url);
            request = new HttpRequestWithBody(uri, method);
            request.setEntity(new UrlEncodedFormEntity(params, ENCODING));
            String requestLine = request.getRequestLine().toString();
            headers.forEach(request::addHeader);
            log.info("ApiRequest is " + requestLine);
            HttpResponse response = httpClient.execute(request, context);
            statusCode = response.getStatusLine().getStatusCode();
            responseText = EntityUtils.toString(response.getEntity(), ENCODING);
            log.info("Response is " + responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public RequestClient sendRequest(METHODS method, String url) {
        try {
            HttpEntityEnclosingRequestBase request = new HttpRequestWithBody(new URI(url), method);
            request.setEntity(body);
            headers.forEach(request::addHeader);
            log.info("ApiRequest is " + request.getRequestLine());
            HttpResponse response = httpClient.execute(request, context);
            responseText = EntityUtils.toString(response.getEntity(), ENCODING);
            log.info("Response is " + responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public String jsonPath(String path) {
        return JsonPath.read(responseText, path).toString();
    }

    public List<Cookie> getCookies() {
        return cookieStore.getCookies();
    }

    public RequestClient addCookies(List<Cookie> cookies) {
        cookies.forEach(cookie -> cookieStore.addCookie(cookie));
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public enum METHODS {
        GET, POST, PUT, HEAD, DELETE
    }


    class HttpRequestWithBody extends HttpEntityEnclosingRequestBase {
        String METHOD_NAME = "GET";

        public String getMethod() {
            return METHOD_NAME;
        }

        HttpRequestWithBody(final URI uri, RequestClient.METHODS method) {
            super();
            METHOD_NAME = method.name();
            setURI(uri);
        }
    }


}