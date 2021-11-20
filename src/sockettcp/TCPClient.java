/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockettcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;



/**
 *
 * @author Denilson
 */
public class TCPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int porta_server = 6789;
        String ip = "localhost";
        Scanner s = new Scanner(System.in);
        
        System.out.println("Insira um nome:");
        String dados=s.nextLine();
        
        System.out.println("Insira PassWord:");
        dados= dados + ";" + s.nextLine();
        
        System.out.println(dados);
        
        Socket sc= new Socket(ip, porta_server); //estabelece a ligação ao servidor
        //envia a mensagem ou nome inserido pelo utilizador
        DataOutputStream  canalOut = new DataOutputStream(sc.getOutputStream());
        canalOut.writeUTF(dados);
        
        //ler resposta do servidor
        DataInputStream canalIn = new DataInputStream(sc.getInputStream());
        String resposta = canalIn.readUTF();
        System.out.println("Resposta do servidor:"+resposta);
    }
    
}
