package org.example.projfinal.controller.userViews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.projfinal.HelloApplication;
import org.example.projfinal.controller.AuthWindow;
import org.example.projfinal.controller.sceneloader.StageLoader;
import org.example.projfinal.domain.*;
import org.example.projfinal.repository.filters.UserFilter;
import org.example.projfinal.service.Network;
import org.example.projfinal.utils.TimedAlert;
import org.example.projfinal.utils.observer.Observer;
import org.example.projfinal.utils.paging.Page;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class MainUserView implements Observer, Initializable {

    @FXML
    private Label usernameShowLabel;

    @FXML
    private Label realnameShowLabel;

    @FXML
    private Button generalPrev;

    @FXML
    private Button generalNext;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button deleteFriendButton;

    @FXML
    private Button startChatWithFriend;

    @FXML
    private ListView<String> userList;

    @FXML
    private ListView<String> friendsList;

    @FXML
    private ListView<String> potentialFriendsList;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private TextArea messageInput;

    @FXML
    private Button goToFriendRequests;

    @FXML
    private Button sendButton;

    @FXML
    private Label countFriendsRequests;

    private Network network;
    private Long currentUserId;
    private User currentUser;
    private int currentPageUser = 0;
    private int pageSize = 5;
    private ObservableList<String> users;
    private ObservableList<String> friendsDone;
    private ObservableList<String> potentialFriends;
    private ObservableList<String> chatList;

    /// //////////////////// DEPENDENCY INJECTION /////////////////////////////////////////////////////////////////////////////////////////////


    public void setNetwork(Network network) {
        this.network = network;
        this.network.addObserver(this);

        initEverythingRelatedUsers();
        initEverythingRelatedFriends();
        initEverythingRelatedChats();
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load the first page

    }

    /// //////////////////// GENERAL TAB /////////////////////////////////////////////////////////////////////////////////////////////

    public void setCredentials() {
        this.currentUser = network.getUser(currentUserId);
        usernameShowLabel.setText(currentUser.getUsername());
        realnameShowLabel.setText(currentUser.getRealName());
    }

    private void initEverythingRelatedUsers() {
        users = FXCollections.observableArrayList();
        userList.setItems(users);

        generalPrev.setOnAction(actionEvent -> {
            if (currentPageUser > 0) {
                currentPageUser--;
                updateDisplayedUsers(new HashMap<>());
            }
            updateButtonStates();
        });


        generalNext.setOnAction(actionEvent -> {
            int totalPages = getTotalPages();
            if (currentPageUser < totalPages - 1) {
                currentPageUser++;
                updateDisplayedUsers(new HashMap<>());
            }
            updateButtonStates();
        });

        updateDisplayedUsers(new HashMap<>());
        updateButtonStates();
    }

    private void updateDisplayedUsers(Map<String, String> val) {
        UserFilter filter = new UserFilter(val);
        Page<User> page = network.getPage(currentPageUser, pageSize, filter);

        users.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .map(User::getUsername)
                .toList());
    }


    private int getTotalPages() {
        UserFilter filter = new UserFilter(new HashMap<>());
        int totalUsers = network.getPage(0, Integer.MAX_VALUE, filter).getTotalElementCount();
        return (int) Math.ceil((double) totalUsers / pageSize);
    }

    private void updateButtonStates() {
        generalPrev.setDisable(currentPageUser == 0);
        generalNext.setDisable(currentPageUser == getTotalPages() - 1);
    }

    /// //////////////////// FRIENDS TAB /////////////////////////////////////////////////////////////////////////////////////////////

    private void initEverythingRelatedFriends() {
        friendsDone = FXCollections.observableArrayList();
        friendsList.setItems(friendsDone);

        potentialFriends = FXCollections.observableArrayList();
        potentialFriendsList.setItems(potentialFriends);

        addFriendButton.visibleProperty().bind(potentialFriendsList.getSelectionModel().selectedItemProperty().isNotNull());
        deleteFriendButton.visibleProperty().bind(friendsList.getSelectionModel().selectedItemProperty().isNotNull());
        startChatWithFriend.visibleProperty().bind(friendsList.getSelectionModel().selectedItemProperty().isNotNull());

        friendsList.setOnMouseClicked(mouseEvent -> {
            String selectedFriend = friendsList.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                System.out.println("Selected friend: " + selectedFriend);
            }
        });

        potentialFriendsList.setOnMouseClicked(mouseEvent -> {
            String selectedFriend = potentialFriendsList.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                System.out.println("Selected potential friend: " + selectedFriend);
            }
        });

        updateFriends();
        updateLabelCount();
    }

    @FXML
    public void pressGoToFriendRequests(){
        try {
            StageLoader stageLoader = new StageLoader();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projfinal/friendrequestview.fxml"));
            Parent root = fxmlLoader.load();
            FriendRequestView controller = fxmlLoader.getController();
            controller.setCurrentUserId(currentUserId);
            controller.setNetwork(network);
            stageLoader.switchScene(root);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    public void pressAddFriendButton(){
        try {
            String selectedFriend = potentialFriendsList.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                System.out.println("Selected potential friend: " + selectedFriend);
                User user = network.getUserByUsername(selectedFriend);
                System.out.println(user);
                Friendship friendship = new Friendship(currentUserId, user.getId(), currentUserId);
                System.out.println(friendship);
                network.addFriendship(friendship);
                updateFriends();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void pressDeleteFriendButton(){
        try {
            String selectedFriend = friendsList.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                System.out.println("Selected friend: " + selectedFriend);
                User user = network.getUserByUsername(selectedFriend);
                System.out.println(user);
                Friendship friendship = network.getFriendship(currentUserId, user.getId());
                if(friendship == null){
                    throw new Exception("Friendship not found");
                }
                System.out.println(friendship);
                network.removeFriendship(friendship.getId());
                updateFriends();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void pressStartChatWithFriend(){
        try {
            String selectedFriend = friendsList.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                System.out.println("Selected friend: " + selectedFriend);
                User user = network.getUserByUsername(selectedFriend);
                System.out.println(user);
                if(network.getFriendship(currentUserId, user.getId()) == null)
                    return;
                Group group = new Group("Chat with " + user.getUsername(), List.of(currentUserId, user.getId()));
                network.addGroup(group);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void updateFriends() {
        friendsDone.setAll(network.getFriends(currentUserId).stream()
                .map(User::getUsername)
                .toList());

        network.getFriendships().forEach(friendship -> {
            if (Duration.between(friendship.getDate(), LocalDateTime.now()).getSeconds() < 2
                    && (friendship.getUser1().equals(currentUserId) || friendship.getUser2().equals(currentUserId))
                    && friendship.getStatus() == FriendRequest.PENDING
                    && !friendship.getFromrequest().equals(currentUserId)) {
                // Your code here
                TimedAlert.showAlert("New friend request");
            }
        });

        potentialFriends.setAll(network.getPotentialFriends(currentUserId).stream()
                .map(User::getUsername)
                .toList());
    }

    private void updateLabelCount(){
        countFriendsRequests.setText("You have : " + network.countFriendRequests(currentUserId) + " friend requests");
    }

    /////////////////////// CHAT TAB /////////////////////////////////////////////////////////////////////////////////////////////

    private void initEverythingRelatedChats() {
        chatList = FXCollections.observableArrayList();
        chatListView.setItems(chatList);

        messageScrollPane.visibleProperty().bind(chatListView.getSelectionModel().selectedItemProperty().isNotNull());
        messageInput.visibleProperty().bind(chatListView.getSelectionModel().selectedItemProperty().isNotNull());
        sendButton.visibleProperty().bind(chatListView.getSelectionModel().selectedItemProperty().isNotNull());

        chatListView.setOnMouseClicked(mouseEvent -> {
            String selectedChat = chatListView.getSelectionModel().getSelectedItem();
            if (selectedChat != null) {
                System.out.println("Selected chat: " + selectedChat);

                loadMessagesForChat(selectedChat);
            }
        });

        updateChats();
    }

    private void loadMessagesForChat(String selectedChat) {
        List<Message> messages = network.getMessagesList(network.getGroupByName(selectedChat).getId());

        VBox messagesContainer = new VBox();
        messagesContainer.setSpacing(10);
        messagesContainer.setPadding(new Insets(10));

        messagesContainer.prefWidthProperty().bind(messageScrollPane.widthProperty());

        messages.forEach(message -> {
            HBox messageBox = new HBox();
            messageBox.setPadding(new Insets(5));
            messageBox.prefWidthProperty().bind(messagesContainer.widthProperty()); // Make HBox stretch to full width

            Label msgLabel = new Label(message.getMessage());
            msgLabel.setWrapText(true);
            msgLabel.setMaxWidth(300);
            HBox.setHgrow(msgLabel, Priority.ALWAYS);

            if (message.getUserId().equals(currentUserId)) {
                messageBox.setAlignment(Pos.BASELINE_RIGHT);
                msgLabel.setStyle("-fx-background-color: #c8e6c9; -fx-padding: 10; -fx-background-radius: 5;");
                msgLabel.setAlignment(Pos.CENTER_RIGHT);
            } else {
                messageBox.setAlignment(Pos.BASELINE_LEFT);
                msgLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-background-radius: 5;");
                msgLabel.setAlignment(Pos.CENTER_LEFT);
            }

            messageBox.getChildren().add(msgLabel);
            messagesContainer.getChildren().add(messageBox);
        });

        messageScrollPane.setContent(messagesContainer);
        messageScrollPane.setVvalue(1.0);
    }




    @FXML
    public void pressSendButton(){
        try {
            String selectedChat = chatListView.getSelectionModel().getSelectedItem();
            if (selectedChat != null) {
                System.out.println("Selected chat: " + selectedChat);

                Group group = network.getGroupByName(selectedChat);
                Message message = new Message(currentUserId, messageInput.getText(), group.getId());//new Message(currentUserId, group.getId(), messageInput.getText());
                network.addMessage(message);

                messageInput.clear();

                chatListView.getSelectionModel().select(selectedChat);

                loadMessagesForChat(selectedChat);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void updateChats(){
        String selectedChat = chatListView.getSelectionModel().getSelectedItem();

        chatList.setAll(network.getGroupsList(currentUserId).stream()
                .map(Group::getGroupName)
                .toList());

        network.getGroupsList(currentUserId).forEach(group -> {
            System.out.println(group.getGroupName());

            network.getMessageInAGroup(group.getId()).forEach(message -> {
                if (Duration.between(message.getDate(), LocalDateTime.now()).getSeconds() < 2) {
                    // Your code here
                    TimedAlert.showAlert("New message in " + group.getGroupName());
                }
            });

        });

        if (selectedChat != null) {
            chatListView.getSelectionModel().select(selectedChat);
            chatListView.requestFocus();
            loadMessagesForChat(selectedChat);
        }
    }

    /////////////////////// SETTINGS TAB /////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private TextField usernameField;

    @FXML
    private TextField realNameField;

    @FXML
    private TextField oldPasswordField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private CheckBox usernameUpdate;

    @FXML
    private CheckBox realNameUpdate;

    @FXML
    private CheckBox passwordUpdate;

    @FXML
    private Button updateInfoButton;

    @FXML
    private Button deleteAccountButton;

    @FXML
    public void pressUpdateInfoButton(){
        try {
            if(usernameUpdate.isSelected()){
                currentUser.setUsername(usernameField.getText());
            }
            if(realNameUpdate.isSelected()){
                currentUser.setRealName(realNameField.getText());
            }
            if(passwordUpdate.isSelected()){
                if(!oldPasswordField.getText().equals(currentUser.getPassword())){
                    throw new Exception("Old password is not correct");
                }
                if(!newPasswordField.getText().equals(confirmPasswordField.getText())){
                    throw new Exception("Passwords do not match");
                }
                currentUser.setPassword(newPasswordField.getText());
            }
            network.updateUser(currentUser);
            setCredentials();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void pressDeleteAccountButton(){
        try{
            network.removeUser(currentUserId);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /// //////////////////// OBSERVER TAB /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        System.out.println("Main user view updated");

        if(network.getUser(currentUserId) == null) {
            sendButton.getScene().getWindow().hide();
        }

        updateDisplayedUsers(new HashMap<>());
        updateFriends();
        updateLabelCount();
        updateChats();
    }
}
