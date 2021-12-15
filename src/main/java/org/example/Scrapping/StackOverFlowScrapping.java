package org.example.Scrapping;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

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
    private By txtHeader = By.xpath("//h1[contains(text(),'Top Questions')]");

    public ArrayList<String> getAllAnsweredQuestionsUsingSeleniumLimited(String query, int limit) {
        ArrayList<String> ansLinkList = new ArrayList<>();
        try {
            getElement(inpSearch).sendKeys(query);
            getElement(inpSearch).sendKeys(Keys.ENTER);
            List<WebElement> webElements;
            int pageNumber = 1;
            try {
                getElement(btnNext);
                System.out.println("No of Pages->" + limit);
                while (pageNumber < limit) {
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
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
        return ansLinkList;
    }

    public ArrayList<String> getAllAnsweredQuestionsUsingSeleniumFullRange(String query) {
        ArrayList<String> ansLinkList = new ArrayList<>();
        try {
            getElement(inpSearch).sendKeys(query);
            getElement(inpSearch).sendKeys(Keys.ENTER);
            List<WebElement> webElements;
            int pageNumber = 1;
            try {
                getElement(btnNext);
                int limit = Integer.parseInt(getElement(btnLimit).getText());
                System.out.println("No of Pages->" + limit);
                while (pageNumber < limit) {
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
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
        return ansLinkList;
    }

    public boolean isValidLogin(String username, String password) {
        loadSeleniumSetup();
        getElement(inpEmail).sendKeys(username);
        getElement(inpPassword).sendKeys(password);
        getElement(btnLogin).click();
        if (getElement(txtHeader).isDisplayed()) {
            System.out.println("Logged in Successfully...!!");
            return true;
        }
        return false;
    }
}
