package org.example.Scrapping;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class StackOverFlowScrapping extends Setup {

    private By outLIst = By.xpath("//div[@class='status answered-accepted']//following::a[1]");
    private By btnLimit = By.xpath("//*[@id=\"mainbar\"]/div[5]/a[5]");
    private By btnNext = By.xpath("//a[text()=' Next']");
    private By btnPageLimit = By.xpath("//*[@id=\"mainbar\"]/div[6]/a[3]4");
    private By lnkPagination = By.xpath("//div[@class='s-pagination site1 themed pager float-left']");

    public ArrayList<String> getAllAnsweredQuestions(String query) {
        ArrayList<String> ansLinkList = new ArrayList<>();
        loadSetup(query);
        List<WebElement> webElements;
        int pageNumber = 1;
        try {
            getElement(btnNext);
            int limit = Integer.parseInt(getElement(btnLimit).getText());
            System.out.println("No of Pages->" + limit);
            while (pageNumber < 2) {
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
        return ansLinkList;
    }

    public static void main(String[] args) {
        StackOverFlowScrapping stackOverFlowScrapping = new StackOverFlowScrapping();
        stackOverFlowScrapping.getAllAnsweredQuestions("JavaFx");
    }
}
