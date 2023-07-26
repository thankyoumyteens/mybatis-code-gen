package org.zsz.mybatiscodegen.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.service.DbService;

import java.util.List;

@Component
public class MainWindowController {

    @FXML
    private Label label;
    @FXML
    private TableView<DbUsers> dbList;

    @Autowired
    private DbService dbService;

    @FXML
    private void initDbList(ActionEvent event) {
        List<DbUsers> dbUsersList = dbService.selectList();
        ObservableList<DbUsers> data = FXCollections.observableArrayList();
        data.addAll(dbUsersList);
        dbList.setItems(data);
        // 设置列
        TableColumn<DbUsers, Integer> usersId = new TableColumn<>("ID");
        usersId.setCellValueFactory(new PropertyValueFactory<>("usersId"));
        dbList.getColumns().add(usersId);
        TableColumn<DbUsers, String> usersUrl = new TableColumn<>("url");
        usersUrl.setCellValueFactory(new PropertyValueFactory<>("usersUrl"));
        dbList.getColumns().add(usersUrl);
        TableColumn<DbUsers, String> usersPort = new TableColumn<>("端口");
        usersPort.setCellValueFactory(new PropertyValueFactory<>("usersPort"));
        dbList.getColumns().add(usersPort);
        TableColumn<DbUsers, String> usersUid = new TableColumn<>("用户名");
        usersUid.setCellValueFactory(new PropertyValueFactory<>("usersUid"));
        dbList.getColumns().add(usersUid);
        TableColumn<DbUsers, String> usersDb = new TableColumn<>("数据库");
        usersDb.setCellValueFactory(new PropertyValueFactory<>("usersDb"));
        dbList.getColumns().add(usersDb);
        TableColumn<DbUsers, String> usersDriver = new TableColumn<>("jdbc驱动");
        usersDriver.setCellValueFactory(new PropertyValueFactory<>("usersDriver"));
        dbList.getColumns().add(usersDriver);
        label.setText("按钮被点击了！");
    }
}
