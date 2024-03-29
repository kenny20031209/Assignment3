import org.json.simple.JSONObject;
import java.io.*;
import java.net.Socket;

public class ConnectionSocket {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ConnectionSocket(String serverAddress, int serverPort){
        try{
            this.socket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectionSocket(Socket socket) {
        try{
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Fail to create socket!");
        }
    }

    public void send(String request) throws IOException {
        System.out.println("Send: " + request);
        out.write(request);
        out.newLine();
        out.flush();
    }

    public String receive() throws IOException {
        String receive = in.readLine();
        System.out.println("Receive: " + receive);
        return receive;
    }

    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    public void createWhiteboard(String username) throws IOException {
        JSONObject object = new JSONObject();
        object.put("Response", Connection.Created);
        object.put("Manager Name", username);
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }

    public void managerExist() throws IOException {
        JSONObject object = new JSONObject();
        object.put("Response", Connection.managerExist);
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }

    public void joinRequest(String waitingName) throws IOException {
        JSONObject object = new JSONObject();
        object.put("Request", Connection.askJoinWhiteboard);
        object.put("Waiting Name", waitingName);
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }

    public void joinResult(String keyType, String key, boolean result, String waitingName) throws IOException {
        JSONObject object = new JSONObject();
        object.put(keyType, key);
        object.put("Username", waitingName);
        object.put("Result", Boolean.toString(result));
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }

    public void kickOutRequest() throws IOException {
        JSONObject object = new JSONObject();
        object.put("Request", Connection.kickOutUser);
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void managerAction(String action) throws IOException {
        JSONObject object = new JSONObject();
        object.put("Request", action);
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }

    public void receiveMessage(String message, String username) throws IOException {
        JSONObject object = new JSONObject();
        object.put("Response", message);
        object.put("Username", username);
        System.out.println("Send: " + object);
        out.write(object.toJSONString());
        out.newLine();
        out.flush();
    }
}
