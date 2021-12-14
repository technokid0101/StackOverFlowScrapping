module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.support;
    requires org.jsoup;

    opens org.example to javafx.fxml;
    exports org.example;
}