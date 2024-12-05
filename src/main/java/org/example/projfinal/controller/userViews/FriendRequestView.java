package org.example.projfinal.controller.userViews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.example.projfinal.domain.FriendRequest;
import org.example.projfinal.domain.Friendship;
import org.example.projfinal.domain.User;
import org.example.projfinal.service.Network;
import org.example.projfinal.utils.observer.Observer;

import java.util.stream.Collectors;

public class FriendRequestView implements Observer {

    private Network network;
    private Long currentUserId;
    private ObservableList<String> friendRequests;

    @FXML
    private ListView<String> friendRequestList;

    @FXML
    private Button acceptButton;

    @FXML
    private Button declineButton;

    @FXML
    private Button closeButton;

    public void setNetwork(Network network) {
        this.network = network;
        this.network.addObserver(this);

        friendRequests = FXCollections.observableArrayList();

        friendRequests = network.getFriendRequests(currentUserId).stream()
                .map(User::getUsername)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        friendRequestList.setItems(friendRequests);
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    @FXML
    public void pressDeclineFriendRequest(){
        String selectedFriend = friendRequestList.getSelectionModel().getSelectedItem();
        Friendship friendship = network.getFriendship(currentUserId, network.getUserByUsername(selectedFriend).getId());

        network.removeFriendship(friendship.getId());
    }

    @FXML
    public void pressAcceptFriendRequest(){
        String selectedFriend = friendRequestList.getSelectionModel().getSelectedItem();
        Friendship friendship = network.getFriendship(currentUserId, network.getUserByUsername(selectedFriend).getId());

        friendship.setStatus(FriendRequest.ACCEPTED);

        network.updateFriendship(friendship);
    }

    @FXML
    public void pressCloseButton(){
        closeButton.getScene().getWindow().hide();
    }

    @Override
    public void update() {
        System.out.println("Friend request view updated");

        friendRequests = network.getFriendRequests(currentUserId).stream()
                .map(User::getUsername)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        friendRequestList.setItems(friendRequests);
    }
}
