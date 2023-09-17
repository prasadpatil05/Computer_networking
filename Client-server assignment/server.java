import java.util.*;
import java.io.*;
import java.net.*;


class server{
    public static void main(String args[])throws Exception {
        ServerSocket ss = new ServerSocket(5555);
        Socket s = ss.accept();
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));



        String str="", str2="";
        while(!str.equals("stop")){
            str = dis.readUTF();
            System.out.println("Client  :" + str);
            str2=br.readLine();
            dos.writeUTF(str2);
            dos.flush();
        }
        dis.close();
        s.close();
        ss.close();
    }
}