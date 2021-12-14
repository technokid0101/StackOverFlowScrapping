package org.example.Scrapping;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class Setup {

    private WebDriver webDriver;
    private ChromeOptions chromeOptions;
    private WebElement element;
    private Wait<WebDriver> wait;
    private FluentWait<WebDriver> fluentWait;
    private WebDriverWait explicitWait;

    public WebDriverWait getExplicitWait() {
        return explicitWait;
    }

    public void setExplicitWait(WebDriverWait explicitWait) {
        this.explicitWait = explicitWait;
    }

    public Wait<WebDriver> getWait() {
        return wait;
    }

    public void setWait(Wait<WebDriver> wait) {
        this.wait = wait;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ChromeOptions getChromeOptions() {
        return chromeOptions;
    }

    public void setChromeOptions(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public void loadSetup(String query) {
        System.out.println(Setup.class.getResource("chromedriver.exe").getFile());
        try {
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            System.setProperty("webdriver.chrome.driver",
                    String.valueOf(new File(Setup.class.getResource("chromedriver.exe").toURI())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // create object of chrome options
        chromeOptions = new ChromeOptions();
        // add the headless argument
        // chromeOptions.addArguments("headless");
        webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        webDriver.get("https://stackoverflow.com/search?q=" + query);
        webDriver.manage().window().maximize();
        fluentWait = new FluentWait<>(webDriver);
        fluentWait.pollingEvery(Duration.ofSeconds(30));
        fluentWait.withTimeout(Duration.ofSeconds(5));
        fluentWait.ignoring(NoSuchElementException.class); // make sure that this exception is ignored
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        explicitWait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
        setWebDriver(webDriver);
        setChromeOptions(chromeOptions);
        setWait(explicitWait);
    }

    public boolean elementInvisible(By xPath) {
        try {
            return !explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(xPath));
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getElement(By xPath) {
        Function<WebDriver, WebElement> function = arg0 -> {
            WebElement element = null;
            try {
                element = arg0.findElement(xPath);
            } catch (NoSuchElementException noSucEleExe) {
                System.out.println("No Element Found");
            }
            return element;
        };
        return fluentWait.until(function);
    }

    public String getText(By xpath) {
        WebElement element = getElement(xpath);
        if (element != null) {
            return element.getText();
        }
        return "";
    }

    public boolean setText(By xpath, String text) {
        WebElement element = getElement(xpath);
        if (element != null) {
            try {
                element.sendKeys(text);
                return true;
            } catch (Exception e) {
                setTextUsingJavaScript(xpath, text);
            }
        }
        return false;
    }

    public void setTextUsingJavaScript(By xpath, String text) {
        WebElement myElement = webDriver.findElement(xpath);
        String js = "arguments[0].setAttribute('value','" + text + "')";
        ((JavascriptExecutor) webDriver).executeScript(js, myElement);
    }

    public List<WebElement> getElements(By xPath) {
        Function<WebDriver, List<WebElement>> function = new Function<WebDriver, List<WebElement>>() {
            public List<WebElement> apply(WebDriver arg0) {
                List<WebElement> elements = arg0.findElements(xPath);
                return elements;
            }
        };
        return fluentWait.until(function);
    }
}
