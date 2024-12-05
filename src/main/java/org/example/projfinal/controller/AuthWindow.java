package org.example.projfinal.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.example.projfinal.controller.sceneloader.StageLoader;
import org.example.projfinal.controller.userViews.MainUserView;
import org.example.projfinal.domain.User;
import org.example.projfinal.service.Network;
import org.example.projfinal.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

public class AuthWindow implements Observer {

    @FXML
    private Tab loginPage;
    @FXML
    private Tab signupPage;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private TextField loginPasswordField;

    @FXML
    private TextField signupUsernameField;

    @FXML
    private TextField signupPasswordField;

    @FXML
    private TextField signupRealNameField;

    private Network network;
    private Long currentUserId;

    public void setNetwork(Network network) {
        this.network = network;
        this.network.addObserver(this);
    }
    //now that we have a network we could do the login :)

    @FXML
    public void loginButtonPress() throws IOException {
        System.out.println("Login button pressed");
        System.out.println("Username: " + loginUsernameField.getText());
        System.out.println("Password: " + loginPasswordField.getText());

        List<User> list = StreamSupport.stream(network.getUsers().spliterator(), false)
                .filter(user -> user.getUsername().equals(loginUsernameField.getText()) && user.getPassword().equals(loginPasswordField.getText()))
                .toList();

        if (list.isEmpty()) {
            System.out.println("This is not a valid user");
        } else if (list.size() > 1) {
            System.out.println("There is a problem here");
        } else {
            System.out.println("User found with id " + list.getFirst().getId());
            currentUserId = list.getFirst().getId();
            loginPasswordField.clear();
            loginUsernameField.clear();
            StageLoader stageLoader = new StageLoader();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projfinal/mainuserview.fxml"));
            Parent root = fxmlLoader.load();
            MainUserView controller = fxmlLoader.getController();
            controller.setCurrentUserId(currentUserId);
            controller.setNetwork(network);
            controller.setCredentials();
            stageLoader.switchScene(root);
        }
    }

    @FXML
    public void signupButtonPress() throws IOException {
        System.out.println("Signup button pressed");
        System.out.println("Username: " + signupUsernameField.getText());
        System.out.println("Password: " + signupPasswordField.getText());
        System.out.println("Real name: " + signupRealNameField.getText());

        User user = new User(signupUsernameField.getText().trim(), signupRealNameField.getText().trim(), signupPasswordField.getText().trim());

        try {
            network.addUser(user);
        } catch (Exception e) {
            System.out.println("User not added" + e);
            return;
        }

        user = network.getUserByUsername(signupUsernameField.getText().trim());

        if (user == null) {
            System.out.println("User not added");
            return;
        }

        signupUsernameField.clear();
        signupPasswordField.clear();
        signupRealNameField.clear();

        currentUserId = user.getId();
        StageLoader stageLoader = new StageLoader();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projfinal/mainuserview.fxml"));
        Parent root = fxmlLoader.load();
        MainUserView controller = fxmlLoader.getController();
        controller.setCurrentUserId(currentUserId);
        controller.setNetwork(network);
        controller.setCredentials();
        stageLoader.switchScene(root);
    }

    public void initialize() {
        loginPage.setOnSelectionChanged(event -> {
            if (loginPage.isSelected()) {
                System.out.println("Login page selected");
            }
        });
        signupPage.setOnSelectionChanged(event -> {
            if (signupPage.isSelected()) {
                System.out.println("Signup page selected");
            }
        });
    }


    @Override
    public void update() {

    }
}
