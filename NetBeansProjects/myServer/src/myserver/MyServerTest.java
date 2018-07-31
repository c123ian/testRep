/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myserver;
import javax.swing.JFrame;
/**
 *
 * @author cian
 */
public class MyServerTest {

	public static void main(String[] args) {
		MyServer s = new MyServer();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.startServer();
	}
}
    

