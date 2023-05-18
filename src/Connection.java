import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class Connection {
    private ConnectionSocket socket;
    public final static String createWhiteboard = "Create Whiteboard";
    final static String joinWhiteboard = "Join whiteboard";
    public final static String Rejected = "Rejected";
    public final static String Created = "Whiteboard Created Successfully";
    public final static String Joined = "Whiteboard Joined Successfully";
    public final static String managerClose = "Manager Close";
    public final static String userClose = "User Close";
    final static String AskJoinWhiteboard = "Ask to Join Whiteboard";
    final static String AskJoinResult = "Ask to Join Result";
    final static String kickOutUser = "Kick out user";

    public Connection(ConnectionSocket socket){
        this.socket = socket;
    }

    public void managerConnect(Whiteboard whiteboard, ChatWindow chatWindow,String managerName){
        JSONObject object = new JSONObject();
        JSONParser parser = new JSONParser();
        object.put("Request", createWhiteboard);
        object.put("Manager Name", managerName);

        String response = "";
        try {
            socket.send(object.toString());
            response = socket.receive();
            object = (JSONObject) parser.parse(response);
            String resType = (String) object.get("Response");
            if (Objects.equals(resType, Created)) {
                whiteboard.initial((String) object.get("Manager Name"));
                chatWindow.start((String) object.get("Manager Name"));
                System.out.println(managerName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void userConnect(Whiteboard whiteboard, ChatWindow chatWindow, String username) {
        JSONObject object = new JSONObject();
        JSONParser parser = new JSONParser();
        object.put("Request", joinWhiteboard);
        object.put("Username", username);

        String response = "";
        try {
            socket.send(object.toString());
            response = socket.receive();
            object = (JSONObject) parser.parse(response);
            String resType = (String) object.get("Response");
            if (Objects.equals(resType, Joined)) {
                whiteboard.initial((String) object.get("Username"));
                chatWindow.start((String) object.get("Username"));
            } else if (Objects.equals(resType, Rejected)) {
                whiteboard.rejected();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void managerDisconnect(String managerName) {
        JSONObject object = new JSONObject();
        object.put("Request", managerClose);
        object.put("Manager Name", managerName);

        try {
            socket.send(object.toString());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userDisconnect(String username) {
        JSONObject object = new JSONObject();
        object.put("Request", userClose);
        object.put("Username", username);

        try {
            socket.send(object.toString());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kickOut(String username) {
        JSONObject object = new JSONObject();
        object.put("Request", kickOutUser);
        object.put("Username", username);

        try{
            socket.send(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, String username){
        JSONObject object = new JSONObject();
        if (!message.isEmpty()){
            object.put("Request", message);
            object.put("Username", username);

            try {
                socket.send(object.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please input a massage!");
        }
    }
}
