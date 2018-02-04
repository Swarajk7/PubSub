import java.rmi.*;

public class Client{

    public static void main(String args[]){
        try{

            IServer stub=(IServer) Naming.lookup("rmi://localhost:5000/khadhaa");
//            System.out.println(stub.add(34,4));
            System.out.println(stub.ping());

        }catch(Exception e){System.out.println(e);}
    }

}
