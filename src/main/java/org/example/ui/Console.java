package org.example.ui;

import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.domain.validator.FriendshipValidation;
import org.example.domain.validator.UserValidation;
import org.example.repository.filerepo.AbstractFileRepo;
import org.example.repository.filerepo.FriendshipFileRepo;
import org.example.repository.filerepo.UserFileRepo;
import org.example.service.FriendshipGraph;
import org.example.service.SocialNetworkCrud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;

public class Console {

    AbstractFileRepo<Long, User> userRepo;
    AbstractFileRepo<Long, Friendship> friendRepo;
    SocialNetworkCrud network;

    FriendshipGraph graf;

    public Console() {
        this.userRepo = new UserFileRepo(new UserValidation(), "./data/users.txt");
        this.friendRepo = new FriendshipFileRepo(new FriendshipValidation(), "./data/friendships.txt");
        this.network = new SocialNetworkCrud(userRepo, friendRepo);
        this.graf = new FriendshipGraph(this.friendRepo, this.userRepo);
    }

    BufferedReader my_reader = new BufferedReader(new InputStreamReader(System.in));

    private void printMenu() {
        System.out.println("Welcome to the SocialNetwork:\n" +
                "1.View users\n" +
                "2.Add user\n" +
                "3.Delete user\n" +
                "4.View friendships\n" +
                "5.Add friendship\n" +
                "6.Delete friendship\n" +
                "7.Cate Comunitati\n" +
                "8.Comunitatea Sociabila");
    }

    private boolean validateChoice(Integer choice) {
        return choice >= 0 && choice <= 8;
    }

    public void run() {
        boolean stop = true;

        while (stop) {
            System.out.println(" ");
            System.out.println(" ");
            printMenu();
            try {
                System.out.print("Your Choice: ");
                String line = my_reader.readLine();
                Integer choice = Integer.parseInt(line);

                if (!validateChoice(choice)) {
                    throw new Exception("The choice input is worng");
                }

                switch (choice) {
                    case 1:
                        viewAllUsers();
                        break;
                    case 2:
                        addUser();
                        break;
                    case 3:
                        deleteUser();
                        break;
                    case 4:
                        viewFriendships();
                        break;
                    case 5:
                        addFriendship();
                        break;
                    case 6:
                        deleteFriendship();
                        break;
                    case 7:
                        countComunitati();
                        break;
                    case 8:
                        comunitateSociabila();
                        break;
                    default:
                        stop = false;
                }
            } catch (Exception e) {
                System.out.println("The error is " + e);
            }
        }

    }

    private void comunitateSociabila() {

        System.out.println("Lungimea este: " + graf.getLongestPath() + '\n');
        for(var it : graf.getComponentaSociabila()){
            System.out.println(network.findUser(it));
        }
    }

    private void countComunitati() {

        System.out.println(graf.getCountCC());

    }

    private void addUser() {
        try{

            System.out.print("Introduceti usernameul: ");
            String line = my_reader.readLine();
            String username = line;
            System.out.print("Introduceti numele real: ");
            line = my_reader.readLine();
            String realName = line;

            network.addUser(new User(username.trim(), realName.trim()));

        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void addFriendship() {
        try{
            System.out.print("Frist user for the friendship: ");
            String line = my_reader.readLine();
            Long user1 = Long.parseLong(line.trim());
            System.out.print("Second user for the friendship: ");
            line = my_reader.readLine();
            Long user2 = Long.parseLong(line.trim());

            Friendship friendship = new Friendship(user1, user2);

            network.addFriendship(friendship);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void deleteUser() {
        try {
            System.out.print("Id of the item you want to delete: ");
            String line = my_reader.readLine();
            Long id = Long.parseLong(line.trim());

            User user = network.findUser(id);

            network.deleteUser(user);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void viewFriendships() {
        StreamSupport.stream(network.getFriendships().spliterator(), false)
                .forEach(System.out::println);
    }

    private void deleteFriendship() {
        try {
            System.out.print("Id of the item you want to delete: ");
            String line = my_reader.readLine();
            Integer id = Integer.parseInt(line);

            Friendship friendship = network.findFriendship(Long.valueOf(id));

            network.deleteFriendship(friendship);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void viewAllUsers() {
        StreamSupport.stream(network.getUses().spliterator(), false)
                .forEach(System.out::println);
    }


}
