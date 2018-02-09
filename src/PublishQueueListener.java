import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketException;

public class PublishQueueListener implements Runnable {
    private ISender sender;
    private DataRepository repository;
    private String name;

    PublishQueueListener(String name) throws IOException {
        //store name and get repository, socket which uses singleton
        this.name = name;
        ConfigManager configManager = ConfigManager.create();
        sender = new Sender(UDPSocket.createSocket(Integer.parseInt(configManager.getValue(ConfigManager.UDP_SERVER_PORT))));
        repository = DataRepository.create();
        new Thread(this, name).start();
    }

    @Override
    public void run() {
        while (true) {
            Pair<String, String> itemToPublish = repository.getHeadItemFromPublishQueue(); //thread safe
            if(itemToPublish==null) {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                    System.out.println("Thread Name:" + this.name + " " + ex.getMessage());
                }
                continue;
            }
            String article = itemToPublish.getValue();
            String tokens[] = article.split(";", -1);
            ClientDetails clientDetails = repository.validateAndGetClientForPublish(itemToPublish.getKey(), tokens);
            if (clientDetails != null) {
                try {
                    sender.sendMessageToClient(clientDetails.IP, clientDetails.port, tokens[3]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Thread Name:" + this.name + " " + ex.getMessage());
                    System.out.println("Client IP:" + clientDetails.IP + "Client Port:" + clientDetails.port + "\n");
                }
            }
        }
    }
}
