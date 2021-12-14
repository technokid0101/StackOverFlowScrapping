package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import org.example.Scrapping.StackOverFlowScrapping;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Dashboard extends StackOverFlowScrapping implements Initializable {

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;

    @FXML
    private Button btnSearch;

    @FXML
    private TextField inpSearch;

    @FXML
    private ListView<String> lnkList;

    @FXML
    private WebView webView;

    @FXML
    private Label limitStatus;

    @FXML
    private AnchorPane waitPanel;

    @FXML
    private TabPane mainTabPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        waitPanel.setVisible(false);
        mainTabPane.getSelectionModel().select(0);
    }

    public void search(ActionEvent actionEvent) {
        waitPanel.setVisible(true);
        Thread thread = new Thread(() -> {
            ArrayList<String> links = getAllAnsweredQuestionsUsingSelenium(inpSearch.getText());
            Platform.runLater(() -> {
                waitPanel.setVisible(false);
                lnkList.getItems().clear();
                lnkList.getItems().addAll(links);
                lnkList.getSelectionModel().select(0);
                limitStatus.setText((lnkList.getSelectionModel().getSelectedItem() + 1) + " of " + links.size());
                loadUrl();
                mainTabPane.getSelectionModel().select(1);
            });
        });
        thread.start();
    }

    public void loadUrl() {
        waitPanel.setVisible(true);
        webView.getEngine().load(lnkList.getSelectionModel().getSelectedItem());
        webView.getEngine().setJavaScriptEnabled(true);
        limitStatus.setText((lnkList.getSelectionModel().getSelectedIndex() + 1) + " of " + lnkList.getItems().size());
        waitPanel.setVisible(false);
    }

    public void nextPrev(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if (button.equals(btnNext)) {
            int index = lnkList.getSelectionModel().getSelectedIndex();
            if (index < lnkList.getItems().size()) {
                lnkList.getSelectionModel().select(index + 1);
                limitStatus.setText((lnkList.getSelectionModel().getSelectedIndex() + 1) + " of " + lnkList.getItems().size());
                loadUrl();
            }
        } else {
            int index = lnkList.getSelectionModel().getSelectedIndex();
            if (index > 0) {
                lnkList.getSelectionModel().select(index - 1);
                limitStatus.setText((lnkList.getSelectionModel().getSelectedIndex() + 1) + " of " + lnkList.getItems().size());
                loadUrl();
            }
        }
    }

}
