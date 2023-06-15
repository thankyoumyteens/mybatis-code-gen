package org.zsz.mybatiscodegen.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zsz.mybatiscodegen.service.DbService;

@Component
public class MainWindowController {

    @FXML
    private Label label;

    @Autowired
    private DbService dbService;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println(dbService.selectList());
        label.setText("按钮被点击了！");
    }
}
