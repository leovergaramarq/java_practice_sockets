package gui;

import info.Pack;
import justachat.Client;

public class UserInterface {

    public UserInterface() {}
    
    public UserInterface(Client client) {
        this.client = client;
        home = new HomeView(client);
    }
    
    public void init() {
        home.init();
    }
    
    public void join(String ip, int serverPort, int clientPort) {
        Chat chat = new Chat(ip, serverPort, clientPort);
        chat.init();
    }

    public void receivePack(String name, String ip, String body) {
        
    }

    public void userJoined(String name, String ip, String body) {
    }

    public void joinRejected(Pack pkg) {
    }
    
    public void userLeft(String name, String ip, String body) {
    }
    
    Client client;
    HomeView home;

    public void checkAproved(Pack pkg) {
        home.checkServerAproved(pkg);
    }

}
