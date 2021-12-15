package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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

    @FXML
    private TextField inpEmail;

    @FXML
    private PasswordField inpPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private HBox lnkGoBack;

    @FXML
    private CheckBox chkFull;

    @FXML
    private Spinner<Integer> inpSpinner;

    private boolean isValidLogin;
    final int initialValue = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        waitPanel.setVisible(false);
        mainTabPane.getSelectionModel().select(0);
        // Value factory.
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, initialValue);
        inpSpinner.setValueFactory(valueFactory);
    }

    public void search(ActionEvent actionEvent) {
        if (inpSearch.getText().equals("")) {
            inpSearch.requestFocus();
        } else {
            if (chkFull.isSelected()) {
                inpSpinner.getEditor().clear();
                waitPanel.setVisible(true);
                Thread thread = new Thread(() -> {
                    System.out.printf("Query ->" + inpSearch.getText());
                    ArrayList<String> links = getAllAnsweredQuestionsUsingSeleniumFullRange(inpSearch.getText());
                    Platform.runLater(() -> {
                        waitPanel.setVisible(false);
                        inpSearch.clear();
                        if (links == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("No Result Found");
                            alert.setContentText("Something went wrong...");
                            alert.show();
                            mainTabPane.getSelectionModel().select(0);
                        } else if (links.size() == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("No Result Found");
                            alert.setContentText("No Answers found for the query " + inpSearch.getText());
                            alert.show();
                            mainTabPane.getSelectionModel().select(0);
                        } else {
                            mainTabPane.getSelectionModel().select(2);
                            lnkList.getItems().clear();
                            lnkList.getItems().addAll(links);
                            lnkList.getSelectionModel().select(0);
                            limitStatus.setText((lnkList.getSelectionModel().getSelectedItem() + 1) + " of " + links.size());
                            loadUrl();
                        }
                    });
                });
                thread.start();
            } else {
                if (inpSpinner.getEditor().getText().equals("")) {
                    inpSpinner.requestFocus();
                } else {
                    waitPanel.setVisible(true);
                    Thread thread = new Thread(() -> {
                        System.out.printf("Query ->" + inpSearch.getText());
                        ArrayList<String> links = getAllAnsweredQuestionsUsingSeleniumLimited(inpSearch.getText(), inpSpinner.getValue());
                        Platform.runLater(() -> {
                            waitPanel.setVisible(false);
                            inpSearch.clear();
                            if (links == null) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("No Result Found");
                                alert.setContentText("Something went wrong...");
                                alert.show();
                                mainTabPane.getSelectionModel().select(0);
                            } else if (links.size() == 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("No Result Found");
                                alert.setContentText("No Answers found for the query " + inpSearch.getText());
                                alert.show();
                                mainTabPane.getSelectionModel().select(0);
                            } else {
                                mainTabPane.getSelectionModel().select(2);
                                lnkList.getItems().clear();
                                lnkList.getItems().addAll(links);
                                lnkList.getSelectionModel().select(0);
                                limitStatus.setText((lnkList.getSelectionModel().getSelectedItem() + 1) + " of " + links.size());
                                loadUrl();
                            }
                        });
                    });
                    thread.start();
                }
            }
        }
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

    public void login(ActionEvent actionEvent) {
        boolean isValidForm = false;
        if (inpEmail.getText().equals("")) {
            inpEmail.requestFocus();
            isValidForm = false;
        } else {
            isValidForm = true;
        }
        if (inpPassword.getText().equals("")) {
            inpPassword.requestFocus();
            isValidForm = false;
        } else {
            isValidForm = true;
        }
        if (isValidForm) {
            waitPanel.setVisible(true);
            Thread thread = new Thread(() -> {
                isValidLogin = isValidLogin(inpEmail.getText(), inpPassword.getText());
                Platform.runLater(() -> {
                    if (isValidLogin) {
                        inpEmail.clear();
                        inpPassword.clear();
                        inpSearch.clear();
                        mainTabPane.getSelectionModel().select(1);
                        waitPanel.setVisible(false);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setContentText("Wrong Credentials please try again...");
                        alert.show();
                        inpEmail.clear();
                        inpPassword.clear();
                        inpSearch.clear();
                    }
                });
            });
            thread.start();
        }
    }


    public void goBack(MouseEvent mouseEvent) {
        mainTabPane.getSelectionModel().select(0);
    }

    public void restrictTabChange(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.TAB) || keyEvent.getCode().equals(KeyCode.UP) || keyEvent.getCode().equals(KeyCode.DOWN) || keyEvent.getCode().equals(KeyCode.LEFT) || keyEvent.getCode().equals(KeyCode.RIGHT)) {
            mainTabPane.getSelectionModel().select(0);
        } else {
            mainTabPane.getSelectionModel().select(-1);
        }
    }
}
