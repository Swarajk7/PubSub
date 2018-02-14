import java.rmi.RemoteException;

public class ClientPingServerThread implements Runnable {
    private IServerImplementation stub;
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    ClientPingServerThread(IServerImplementation stub, String IP, int port) {
        this.stub = stub;
        new Thread(this, "PingThread").start();
    }

    @Override
    public void run() {
        //sleep for 2 minutes
        //if ping fails, try again in 5 secs 3 times.
        //if it fails for 3 times, sleep for 2 minutes
        //extra logic can be added.
        int sleep_time = 60 * 1000 * 1;
        int t = sleep_time;
        int cnt = 0;
        while (true) {
            try {
                System.out.println(ANSI_GREEN + "Pinging" + ANSI_RESET);
                Thread.sleep(t);
                String message = stub.ping();
                t = sleep_time;
            } catch (Exception e) {
                System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
                t = 5000;
                cnt++;
                if (cnt == 3) {
                    System.out.println(ANSI_RED + "Server Down!!" + ANSI_RESET);
                    t = sleep_time;
                    cnt = 0;
                }
            }
        }
    }
}