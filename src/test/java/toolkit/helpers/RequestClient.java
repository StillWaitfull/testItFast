package toolkit.helpers;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.List;


public class RequestClient {
    private String responseText = "";
    private String baseUrl = YamlConfigProvider.getStageParameters("baseUrl");
    private static Logger log = Logger.getLogger(RequestClient.class);
    private RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
    private CookieStore cookieStore = new BasicCookieStore();
    private HttpClientContext context = HttpClientContext.create();
    private CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultRequestConfig(globalConfig)
            .setDefaultCookieStore(cookieStore)
            .build();

    {
        context.setCookieStore(cookieStore);
    }


    public RequestClient sendRequest(METHODS method, String url, List<BasicNameValuePair> params, BasicHeader... headers) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("").setPath(url);
        if (params != null)
            params.forEach(param -> builder.setParameter(param.getName(), param.getValue()));
        HttpRequestBase request;
        try {
            request = method.getMethod(builder.build());
            String requestLine = request.getRequestLine().toString();
            if (headers != null)
                for (BasicHeader header : headers) request.addHeader(header);
            log.info("Request is " + requestLine);
            HttpResponse response = httpClient.execute(request, context);
            responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
            log.info("Response is " + responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    public RequestClient sendPostRequestWithEntity(String url, List<BasicNameValuePair> params, String token) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(baseUrl).setPath(url);
        builder.setParameter("token", token);
        try {
            HttpPost request = new HttpPost(builder.build());
            request.setEntity(new UrlEncodedFormEntity(params));
            log.info("Request is " + request.getRequestLine());
            HttpResponse response = httpClient.execute(request, context);
            responseText = new BasicResponseHandler().handleResponse(response);
            log.info("Response is " + responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    private enum METHODS {

        GET {
            @Override
            public HttpRequestBase getMethod(URI uri) {
                return new HttpGet(uri);
            }
        }, POST {
            @Override
            public HttpRequestBase getMethod(URI uri) {
                return new HttpPost(uri);
            }
        }, PUT {
            @Override
            public HttpRequestBase getMethod(URI uri) {
                return new HttpPut(uri);
            }
        }, DELETE {
            @Override
            public HttpRequestBase getMethod(URI uri) {
                return new HttpDelete(uri);
            }
        };

        public abstract HttpRequestBase getMethod(URI uri);

    }


}
