public interface ISender {
    void sendMessageToClient(String IP, int port, String msg) throws Exception;
}
