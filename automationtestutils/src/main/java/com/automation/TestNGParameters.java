package com.automation;

import com.automation.enums.Browser;
import com.automation.enums.URLS;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.util.Objects;

@Getter
public class TestNGParameters {

    private static final String BROWSER_PROPERTY = "BROWSER";
    private final Browser browser;
    private final URLS urls;

    /**
     * Set value of Browser and URLS from System Properties OR Suite XML.
     *
     * @param iTestContext iTestContext.
     */
    TestNGParameters(ITestContext iTestContext) {
        browser = getBrowserFromXMLFile(iTestContext.getCurrentXmlTest(), Browser.valueOf(RuntimeParameters.getSystemProperty(BROWSER_PROPERTY, Browser.CHROME.toString())));
        urls = getURLFromXMLFile(iTestContext.getCurrentXmlTest(), URLS.valueOf(RuntimeParameters.getSystemProperty("URLS", URLS.HOME.getName())));
    }

    /**
     * Fetch URLS from main suite or from child suite XML.
     *
     * @param xmlTest      xmlTest.
     * @param defaultValue default value of URLS.
     * @return URLS.
     */
    private URLS getURLFromXMLFile(XmlTest xmlTest, URLS defaultValue) {
        if (Objects.isNull(xmlTest.getParameter("URLS"))) {
            return defaultValue;
        }
        String url = xmlTest.getParameter("URLS");
        if (StringUtils.isBlank(url) && Objects.nonNull(xmlTest.getSuite().getParameter("URLS"))) {
            url = xmlTest.getSuite().getParameter("URLS");
        }
        return StringUtils.isBlank(url) ? defaultValue : URLS.valueOf(url);
    }

    /**
     * Fetch Browser from main suite or from child suite XML.
     *
     * @param xmlTest      xmlTest.
     * @param defaultValue default value of browser.
     * @return Browser.
     */
    private Browser getBrowserFromXMLFile(XmlTest xmlTest, Browser defaultValue) {
        if (Objects.isNull(xmlTest.getParameter(BROWSER_PROPERTY))) {
            return defaultValue;
        }
        String browserProperty = xmlTest.getParameter(BROWSER_PROPERTY);
        if (StringUtils.isBlank(browserProperty) && !Objects.isNull(xmlTest.getSuite().getParameter(BROWSER_PROPERTY))) {
            browserProperty = xmlTest.getSuite().getParameter(BROWSER_PROPERTY);
        }
        return StringUtils.isBlank(browserProperty) ? defaultValue : Browser.valueOf(browserProperty);
    }
}
