package org.zsz.mybatiscodegen.controller;

import cn.hutool.core.bean.BeanUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.DbUsersWithOptions;
import org.zsz.mybatiscodegen.service.DbService;

import java.util.List;

@Component
public class MainWindowController {

    @FXML
    private Label label;
    @FXML
    private TableView<DbUsersWithOptions> dbList;

    @Autowired
    private DbService dbService;

    @FXML
    private void initDbList(ActionEvent event) {
        List<DbUsers> dbUsersList = dbService.selectList();
        ObservableList<DbUsersWithOptions> data = FXCollections.observableArrayList();
        for (DbUsers dbUsers : dbUsersList) {
            DbUsersWithOptions dbUsersWithOptions = BeanUtil.copyProperties(dbUsers, DbUsersWithOptions.class);
            Button button = new Button("生成");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (DbUsersWithOptions row : data) {
                        if (row.getOp() == button) {
                            System.out.println(row.getUsersId());
                            break;
                        }
                    }
                }
            });
            dbUsersWithOptions.setOp(button);
            data.add(dbUsersWithOptions);
        }
        dbList.setItems(data);
        // 设置列
        TableColumn<DbUsersWithOptions, Integer> usersId = new TableColumn<>("ID");
        usersId.setCellValueFactory(new PropertyValueFactory<>("usersId"));
        dbList.getColumns().add(usersId);
        TableColumn<DbUsersWithOptions, String> usersUrl = new TableColumn<>("url");
        usersUrl.setCellValueFactory(new PropertyValueFactory<>("usersUrl"));
        dbList.getColumns().add(usersUrl);
        TableColumn<DbUsersWithOptions, String> usersPort = new TableColumn<>("端口");
        usersPort.setCellValueFactory(new PropertyValueFactory<>("usersPort"));
        dbList.getColumns().add(usersPort);
        TableColumn<DbUsersWithOptions, String> usersUid = new TableColumn<>("用户名");
        usersUid.setCellValueFactory(new PropertyValueFactory<>("usersUid"));
        dbList.getColumns().add(usersUid);
        TableColumn<DbUsersWithOptions, String> usersDb = new TableColumn<>("数据库");
        usersDb.setCellValueFactory(new PropertyValueFactory<>("usersDb"));
        dbList.getColumns().add(usersDb);
        TableColumn<DbUsersWithOptions, String> usersDriver = new TableColumn<>("jdbc驱动");
        usersDriver.setCellValueFactory(new PropertyValueFactory<>("usersDriver"));
        dbList.getColumns().add(usersDriver);
        TableColumn<DbUsersWithOptions, Button> op = new TableColumn<>("操作");
        op.setCellValueFactory(new PropertyValueFactory<>("op"));
        dbList.getColumns().add(op);
        label.setText("按钮被点击了！");
    }
}
