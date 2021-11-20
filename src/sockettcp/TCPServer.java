/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockettcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author Denilson
 */
public class TCPServer {
    
    public static String generateHash(String password){
        String hash = null;
        
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }        
        return hash;
    }
    
    public static void main(String []args) throws IOException{
    
        int porta_server = 6789;
        ServerSocket ss = new ServerSocket(porta_server);
        System.out.println(". . . Servidor disponivel. . .");
        
        DataInputStream canalIn=null;
        DataOutputStream canalOut=null;
        String mensagem=null;
        while(true){
            Socket conn = ss.accept(); //aceitar pedidos do cliente
            //lê o pedido do cliente e mostra
            canalIn= new DataInputStream(conn.getInputStream());
            mensagem = canalIn.readUTF();
            
            String[] dados = mensagem.split(";");
            
            String username = dados[0].trim();
            String password = dados[1].trim();
            
            String db_user = System.getenv("BD_USER");
            String db_pass = System.getenv("BD_PASS");
            
            password = generateHash(password);
            
            System.out.println("\nDados Recebido");
            
            try {
                // create a mysql database connection
                String myUrl = "jdbc:mysql://localhost:3306/teste?useTimezone=true&serverTimezone=UTC";
                //Class.forName(myDriver);
                Connection connect = DriverManager.getConnection(myUrl, db_user, db_pass);
                
                
                                 // the mysql insert statement
                String query = "insert into user (Nome,password) values (?,?)";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = connect.prepareStatement(query);
                preparedStmt.setString(1, username);
                preparedStmt.setString(2, password);

                // execute the preparedstatement
                preparedStmt.execute();

                connect.close();
                String resposta = "Dados Guardados na BD";
                System.err.println("\nDados Guardados na BD");
                
            } catch (Exception e) {
                System.err.println("\nGot an exception!" + e.getMessage());
                canalOut= new DataOutputStream(conn.getOutputStream());
                canalOut.writeUTF("Error!!");
            }
            
        }
    }   
}
