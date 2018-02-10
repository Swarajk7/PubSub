public interface ISender {
    void sendMessageToClient(String IP, int port, String msg) throws Exception;
    String getList(String IP, int port, String msg);
}
