package org.zsz.mybatiscodegen.controller;

import cn.hutool.core.bean.BeanUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zsz.mybatiscodegen.entity.DbUsers;
import org.zsz.mybatiscodegen.entity.DbUsersWithOptions;
import org.zsz.mybatiscodegen.entity.GenParam;
import org.zsz.mybatiscodegen.service.DbService;
import org.zsz.mybatiscodegen.service.MainService;

import java.util.List;
import java.util.Optional;

@Component
public class MainWindowController {

    @FXML
    private Label label;
    @FXML
    private TableView<DbUsersWithOptions> dbList;

    @Autowired
    private DbService dbService;
    @Autowired
    private MainService mainService;

    @FXML
    private void initDbList(ActionEvent event) {
        List<DbUsers> dbUsersList = dbService.selectList();
        ObservableList<DbUsersWithOptions> data = FXCollections.observableArrayList();
        for (DbUsers dbUsers : dbUsersList) {
            DbUsersWithOptions dbUsersWithOptions = BeanUtil.copyProperties(dbUsers, DbUsersWithOptions.class);
            Button button = getButton(data);
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
    }

    private Button getButton(ObservableList<DbUsersWithOptions> data) {
        Button button = new Button("生成");
        button.setOnAction(event -> {
            for (DbUsersWithOptions row : data) {
                if (row.getOp() == button) {
                    Integer usersId = row.getUsersId();
                    TextInputDialog dialog = new TextInputDialog("");
                    dialog.setTitle("生成");
                    dialog.setHeaderText("表名");
                    dialog.setContentText("表名:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> {
                        GenParam param = new GenParam();
                        param.setId(usersId);
                        param.setTableName(name);
                        param.setUseLombok(true);
                        param.setUseComment(true);
                        try {
                            mainService.genMybatisCode(param);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("成功");
                            alert.setHeaderText("成功");
                            alert.setContentText("成功");
                            alert.showAndWait();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }
            }
        });
        return button;
    }
}
