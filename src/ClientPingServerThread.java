import java.rmi.RemoteException;

public class ClientPingServerThread implements Runnable {
    private IServerImplementation stub;

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
        int sleep_time = 60 * 1000 * 2;
        int t = sleep_time;
        int cnt = 0;
        while (true) {
            try {
                System.out.println("Pinging");
                Thread.sleep(t);
                String message = stub.ping();
                t = sleep_time;
            } catch (Exception e) {
                e.printStackTrace();
                t = 5000;
                cnt++;
                if (cnt == 3) {
                    System.out.println("Server Down!!");
                    t = sleep_time;
                    cnt = 0;
                }
            }
        }
    }
}