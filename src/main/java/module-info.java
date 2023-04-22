module com.github.sunnyst4r.lifeachievements {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires spring.data.commons;
    requires java.sql;
    requires java.persistence;

    opens com.github.sunnyst4r.lifeachievements to javafx.fxml;
    exports com.github.sunnyst4r.lifeachievements;
}