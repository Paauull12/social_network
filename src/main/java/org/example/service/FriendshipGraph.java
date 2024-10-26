package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.repository.filerepo.AbstractFileRepo;
import org.example.utils.Graph;

import java.util.Set;

public class FriendshipGraph {

    private final AbstractFileRepo<Long, Friendship> networkFriends;
    private final AbstractFileRepo<Long, User> networkUser;
    private final Graph graf;

    Long countCC, longestPath, startingNode;

    public FriendshipGraph(AbstractFileRepo<Long, Friendship> network, AbstractFileRepo<Long, User> networkUser) {
        this.networkFriends = network;
        this.networkUser = networkUser;
        graf = new Graph();
        refresh();
    }

    private void loadGraph(){
        for(var it : networkUser.findAll()){
            graf.addNode(it.getId());
        }
        for(var it : networkFriends.findAll()){
            graf.addNode(it.getUser1());
            graf.addNode(it.getUser2());
            graf.addEdge(it.getUser1(), it.getUser2());
        }
    }

    private void getInfo(){
        Long[] info = graf.countComponentsAndGetLongestRoad();

        this.countCC = info[0];
        this.longestPath = info[1];
        this.startingNode = info[2];
    }

    public void refresh(){
        loadGraph();
        getInfo();
    }

    public Long getCountCC() {
        refresh();
        graf.printGraph();
        return countCC;
    }

    public Long getLongestPath() {
        refresh();
        graf.printGraph();

        return longestPath;
    }

    public Long getStartingNode() {
        refresh();
        graf.printGraph();

        return startingNode;
    }

    public Set<Long> getComponentaSociabila(){
        return graf.getAllFriendsExtendedToo(startingNode);
    }

}
