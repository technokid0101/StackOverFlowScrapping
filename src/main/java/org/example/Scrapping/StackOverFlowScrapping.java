package org.example.Scrapping;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StackOverFlowScrapping extends Setup {

    private By inpEmail = By.xpath("//input[@id='email']");
    private By inpPassword = By.xpath("//input[@id='password']");
    private By inpSearch = By.xpath("//input[@name='q']");
    private By btnLogin = By.xpath("//button[@id='submit-button']");
    private By outLIst = By.xpath("//div[@class='status answered-accepted']//following::a[1]");
    private By btnLimit = By.xpath("//*[@id=\"mainbar\"]/div[5]/a[5]");
    private By btnNext = By.xpath("//a[text()=' Next']");
    private By btnPageLimit = By.xpath("//*[@id=\"mainbar\"]/div[6]/a[3]4");
    private By lnkPagination = By.xpath("//div[@class='s-pagination site1 themed pager float-left']");
    private By txtHeader = By.xpath("//h1[@class='flex--item fl1 fs-headline1']");
    private Document doc = null;

    public ArrayList<String> getAllAnsweredQuestionsUsingSelenium(String query) {
        ArrayList<String> ansLinkList = new ArrayList<>();
        loadSeleniumSetup(query);

        getElement(inpEmail).sendKeys("technokid0101@gmail.com");
        getElement(inpPassword).sendKeys("Technokid@123");
        getElement(btnLogin).click();
        if (getElement(txtHeader).isDisplayed()) {
            getElement(inpSearch).sendKeys(query);
            getElement(inpSearch).sendKeys(Keys.ENTER);

            List<WebElement> webElements;
            int pageNumber = 1;
            try {
                getElement(btnNext);
                int limit = Integer.parseInt(getElement(btnLimit).getText());
                System.out.println("No of Pages->" + limit);
                while (pageNumber < 20) {
                    webElements = getElements(outLIst);
                    for (WebElement element : webElements) {
                        System.out.println(element.getAttribute("href"));
                        ansLinkList.add(element.getAttribute("href"));
                    }
                    getElement(btnNext).click();
                    System.out.println("Page Number- > " + pageNumber);
                    pageNumber = pageNumber + 1;
                }
            } catch (Exception exception) {
                webElements = getElements(outLIst);
                for (WebElement element : webElements) {
                    System.out.println(element.getAttribute("href"));
                    ansLinkList.add(element.getAttribute("href"));
                }
            }
        }
        return ansLinkList;
    }

    public ArrayList<String> getAllAnswersUsingJsoup(String query) {
        ArrayList<String> ansLinkList = new ArrayList<>();
        try {
            doc = Jsoup.connect("https://stackoverflow.com/search?q=" + query).get();
            System.out.println(doc.body());
            Elements elements = doc.selectXpath("//div[@class='status answered-accepted']//following::a[1]");
            System.out.println(elements.size());
            for (Element element : elements) {
                System.out.println(element.attr("href"));
                ansLinkList.add(element.attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ansLinkList;
    }

    public static void main(String[] args) {
        StackOverFlowScrapping stackOverFlowScrapping = new StackOverFlowScrapping();
        stackOverFlowScrapping.getAllAnsweredQuestionsUsingSelenium("Disable javascript from running in javafx webview");
        //stackOverFlowScrapping.getAllAnswersUsingJsoup("JavafX");
    }
}
