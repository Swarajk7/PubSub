import java.rmi.*;

public class Server {
    public static void main(String args[]){
        try{

            ServerImplementation stub=new ServerImplementation();
            Naming.rebind("rmi://localhost:5000/khadhaa",stub);

        }catch(Exception e){System.out.println(e);}
    }

}
