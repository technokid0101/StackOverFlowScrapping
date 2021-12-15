package org.example.Scrapping;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
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
    private FluentWait<PhantomJSDriver> fluentWait1;
    private WebDriverWait explicitWait;
    private PhantomJSDriver driver;

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

    public void loadSeleniumSetup() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        try {
            // Killing Chrome Driver
            Process process = Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /F");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            InputStream is = Setup.class.getResourceAsStream("chromedriver.exe");
            OutputStream os = new FileOutputStream("chromedriver.exe");
            byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            //flush OutputStream to write any buffered data to file
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        // create object of chrome options
        chromeOptions = new ChromeOptions();
        // add the headless argument
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--headless");
        webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        webDriver.get("https://stackoverflow.com/users/login");
        webDriver.manage().window().maximize();
        fluentWait = new FluentWait<>(webDriver);
        fluentWait.pollingEvery(Duration.ofSeconds(60));
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

    @Deprecated
    public void loadPhantomJsSetup() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        try {
            System.out.println(new File(Setup.class.getResource("phantomjs.exe").toURI()));
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, String.valueOf(new File(Setup.class.getResource("phantomjs.exe").toURI())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        driver = new PhantomJSDriver(caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get("https://stackoverflow.com/users/login");
        fluentWait1 = new FluentWait<>(driver);
        fluentWait1.pollingEvery(Duration.ofSeconds(30));
        fluentWait1.withTimeout(Duration.ofSeconds(5));
        fluentWait1.ignoring(NoSuchElementException.class); // make sure that this exception is ignored
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public WebElement getPhantomElement(By xPath) {
        Function<PhantomJSDriver, WebElement> function = arg0 -> {
            WebElement element = null;
            try {
                element = arg0.findElement(xPath);
            } catch (NoSuchElementException noSucEleExe) {
                System.out.println("No Element Found");
            }
            return element;
        };
        return fluentWait1.until(function);
    }

    @Deprecated
    public List<WebElement> getPhantomElements(By xPath) {
        Function<PhantomJSDriver, List<WebElement>> function = arg0 -> {
            List<WebElement> elements = arg0.findElements(xPath);
            return elements;
        };
        return fluentWait1.until(function);
    }

    public WebElement getElement(By xPath) {
        Function<WebDriver, WebElement> function = arg0 -> {
            WebElement element = null;
            try {
                element = arg0.findElement(xPath);
            } catch (NoSuchElementException noSucEleExe) {
                System.err.println("No Element Found" + noSucEleExe.getMessage());
            }
            return element;
        };
        return fluentWait.until(function);
    }

    @Deprecated
    public String getText(By xpath) {
        WebElement element = getElement(xpath);
        if (element != null) {
            return element.getText();
        }
        return "";
    }

    @Deprecated
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

    @Deprecated
    public void setTextUsingJavaScript(By xpath, String text) {
        WebElement myElement = webDriver.findElement(xpath);
        String js = "arguments[0].setAttribute('value','" + text + "')";
        ((JavascriptExecutor) webDriver).executeScript(js, myElement);
    }

    public List<WebElement> getElements(By xPath) {
        Function<WebDriver, List<WebElement>> function = arg0 -> {
            List<WebElement> elements = arg0.findElements(xPath);
            return elements;
        };
        return fluentWait.until(function);
    }
}
