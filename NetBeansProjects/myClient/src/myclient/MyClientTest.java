/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myclient;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author cian
 */
public class MyClientTest extends JFrame
{
   public static void main(String args[]){ 
       MyClient c;
       //localhost
       c = new MyClient("127.0.0.1");
       c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //once connected and created gui, run
       c.startRunning();
       
       
       
   }
 
    }
    

