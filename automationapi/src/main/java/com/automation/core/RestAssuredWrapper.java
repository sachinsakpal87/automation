package com.automation.core;

import com.automation.utils.JsonUtils;
import com.google.common.base.Strings;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static com.automation.reports.ExtentTestManager.logInfo;
import static com.google.common.base.Preconditions.*;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;

public class RestAssuredWrapper {
    private String requestUrl;
    private String baseUrl;
    private String oauthConsumer;
    private String oauthSecret;
    private String basicUsername;
    private String basicPassword;
    private ContentType contentType;
    private List<ContentType> acceptTypes = new ArrayList<>();
    private Authorization authorization;
    private Response response;
    private final Map<String, String> headers = new HashMap<>();

    public RestAssuredWrapper setAuthorizationType(Authorization type) {
        if (type != null) {
            logInfo("Set authorization type: %s", type.toString());
            authorization = type;
        }
        return this;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void buildRequestUrlWithRequiredPort(String port) {
        if (StringUtils.isNotBlank(port)) {
            logInfo("Build request url with port: %s", port);
            this.requestUrl = this.baseUrl + ":" + port;
            logInfo("request url is: %s", requestUrl);
        }else{
            this.requestUrl = this.baseUrl + ":" + port;
            logInfo("request url is: %s", requestUrl);
        }
    }

    public void buildRequestUrlFromRelativeUrl(String url) {
        checkArgument(url != null, "url should not be null");
        logInfo("Build request url with relative url: %s", url);
        requestUrl = requestUrl + url;
        logInfo("request url is: %s", requestUrl);
    }

    public Response getResponse() {
        checkNotNull(this.response, "response is null");
        return this.response;
    }

    public <T> T getResponseAttribute(String path) {
        checkArgument(path != null, "attribute path can not be null");

        T value;
        if (acceptTypes.contains(ContentType.XML)) {
            logInfo("Get attribute %s from xml response", path);
            value = getResponse().xmlPath().get(path);
        } else {
            logInfo("Get attribute %s from json response", path);
            value = getResponse().jsonPath().get(path);
        }

        checkNotNull(value, "Attribute " + path + " from response is empty or null");
        return value;
    }

    public <T> List<T> getResponseAttributeAsList(String path, boolean isNullAllowed) {
        checkArgument(path != null, "attribute path can not be null");

        List<T> value;
        if (acceptTypes.contains(ContentType.XML)) {
            logInfo("Get attribute %s from xml response", path);
            value = getResponse().xmlPath().getList(path);
        } else {
            logInfo("Get attribute %s from json response", path);
            value = getResponse().jsonPath().getList(path);
        }

        if (!isNullAllowed) {
            checkNotNull(value, "Attribute " + path + " from response is empty or null");
        }
        return value;
    }

    public RestAssuredWrapper requestPost(String body) {
        checkState(!Strings.isNullOrEmpty(requestUrl), "Request Url is null");
        RequestSpecification request = given();
        authorization(request);
        if (headers != null) {
            request.headers(headers);
        }
        request.config(newConfig());

        logInfo("Post request url: %s", requestUrl);
        if (!Strings.isNullOrEmpty(body)) {
            logInfo("Post request body: %s", body);
            request.body(body);
        }

        try {
            response = request.post(requestUrl);
        } catch (RuntimeException e) {
            throw e;
        }
        return this;
    }

    public RestAssuredWrapper setContentType(ContentType type) {
        checkArgument(type != null, "content type can't be null");
        contentType = type;
        if (contentType != null) {
            setHeader("Content-Type", contentType.toString());
        }
        return this;
    }

    public RestAssuredWrapper setAcceptType(ContentType... types) {
        checkArgument(types != null, "accept types can't be null");
        acceptTypes.clear();
        acceptTypes.addAll(Arrays.asList(types));
        if (acceptTypes != null && acceptTypes.size() > 0) {
            setHeader("Accept", acceptTypes.stream().map(t -> t.toString()).collect(Collectors.joining(", ", "", "")));
        }
        return this;
    }

    private void authorization(RequestSpecification request) {
        if (authorization.equals(Authorization.OAUTH1)) {
            checkArgument(oauthConsumer != null && oauthSecret != null, "oauth consumer key and secret are missing");
            request
                    .baseUri(requestUrl)
                    .auth()
                    .oauth(oauthConsumer, oauthSecret, "", "");
        } else if (authorization.equals(Authorization.BASIC)) {
            checkArgument(basicUsername != null && basicPassword != null, "Username or password is not defined");
            request
                    .baseUri(requestUrl)
                    .auth()
                    .preemptive()
                    .basic(basicUsername, basicPassword);
        } else {
            request.baseUri(requestUrl);
        }
    }

    public void setHeader(String headerName, String value) {
        headers.put(headerName, value);
    }

    public RestAssuredWrapper clearHeaders() {
        headers.clear();
        return this;
    }

    public <T> String deserializeToString(T requestObject) {
        checkNotNull(requestObject, "requestObject can not be null");
        if (ContentType.XML.equals(contentType)) {
            try {
                Marshaller m = JAXBContext.newInstance(requestObject.getClass()).createMarshaller();
                m.setProperty(Marshaller.JAXB_FRAGMENT, true);
                Writer writer = new StringWriter();
                m.marshal(requestObject, writer);
                return writer.toString();
            } catch (JAXBException e) {
                throw new IllegalStateException("Could not deserialize requestObject to XML");
            }
        } else {
            return JsonUtils.deserializeToJsonString(requestObject);
        }
    }

    public RestAssuredWrapper setBasicAuthCredentials(String userName, String password) {
        checkArgument(userName != null && password != null, "user name or password can't be null");
        logInfo("set basic authentication username: %s and password: %s", userName, password);
        this.basicUsername = userName;
        this.basicPassword = password;
        return this;
    }

    public RestAssuredWrapper setOauthCredentials(String authConsumerKey, String authConsumerSecret) {
        logInfo("set oauth key: %s and secret: %s", authConsumerKey, authConsumerSecret);
        this.oauthConsumer = authConsumerKey;
        this.oauthSecret = authConsumerSecret;
        return this;
    }
}