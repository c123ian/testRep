
package myserver;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//extending JFrame to allow for GUI


public class MyServer extends JFrame{
    
    //VAR
    //input
    private JTextField userText;
    //display messages
    private JTextArea chatWindow;
    
    //communicate to 2 streams (output <- sennd out
    //and input <- recieve
    private ObjectOutputStream output;
    private ObjectInputStream input;
    //will wait for clients to connect, configure port, waiting 
    private ServerSocket server;
    //socket = connection
    private Socket connection;  
    
    //constructor (super = parent class)
    public MyServer(){
        //CREATING THE GUI BY CODE
        super("Cian's Instant Messenger App");
        userText = new JTextField();
        //run a method (by default, if not connected cant type message
        //into messsage box
        userText.setEditable(false);
        
        userText.addActionListener(
                new ActionListener(){
                    //when user hits Enter
                    public void actionPerformed(ActionEvent event){
                        //pass String that was entered into text field
                        
                        sendMessage(event.getActionCommand());
                        //main message area resets to blank, waiting for next message
                        userText.setText("");
                     
         //** whatever is typed into ActionEvent is passed to sendMessage()               
                    }
                }
        );
        //border layout lays out a container, arranging and resizing its components
        add(userText, BorderLayout.NORTH);
           chatWindow = new JTextArea();
                        add(new JScrollPane(chatWindow));
                        setSize(300,150);
                        setVisible(true);
    }
    
    //setup and run server, assign port number, once GUI has been created
    public void startServer(){
        try{
            //(port number, 100 users can wait and sit on port max/backlog)
            server = new ServerSocket(6789, 100);
            //loop over and over
            while(true){
                //connect and have conversation
                try{
                    // start and wait
                    waitForConnection();
                   // setup stream bvetween comp
                    SetupStreams();
                   // pass messages back and forth
                    whileChatting();
                    
                    
                    //EOF = EndOF STREAM
                }catch(EOFException eofE){
                    showMessage("\n Server ended the connection ");
                    
                }finally{
                    closeSockets();
                }
            }
        }catch(IOException ioE){
            ioE.printStackTrace();
            
        }
    }
    
    //waitForConnection();
    private void waitForConnection() throws IOException{
        showMessage(" waiting for another user to connect .. \n");
        //Socket = connection

        //While loop keeps looping, Socket created between server and client once "accepted"
        connection = server.accept();
        
        //getAdress = returns the address
        //getHost name (ip address as string)
         showMessage("now connected to " +connection.getInetAddress().getHostAddress() + "\n");

    }
    
    //get stream to send/recieve data
    private void SetupStreams() throws IOException{
        
        //outputstream
        output = new ObjectOutputStream(connection.getOutputStream());
        //flush leftover data from Stream
        //only sender can flush
        output.flush();
        
        //input Stream
        input = new ObjectInputStream(connection.getInputStream());
        
        showMessage("\n Steams now setup \n");
        
    }
    //send message to Client (dif to showMessage = displays the sentMessage)
    private void sendMessage(String message){
        try{
            //create object and send it through output STREAM
            output.writeObject("SERVER - " + message);
            output.flush();
            
            //display on screen (of full conversation
            showMessage("\nServer - " + message);
        }catch(IOException ioE){
            chatWindow.append("\n Error: cant send message");
            
            
            
      //** whatever is typed into ActionEvent is passed to sendMessage() 
      //** then passed to String message variable (which is then changed into an Object for Stream
      //write object to output stream, sends to client
      
      

        }
    }//during the chat conversation
	private void whileChatting() throws IOException{
		String message = " You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("The user has sent an unknown object!");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	public void closeSockets(){
		showMessage("\n Closing Connections... \n");
		ableToType(false);
		try{
			output.close(); //Closes the output path to the client
			input.close(); //Closes the input path to the server, from the client.
			connection.close(); //Closes the connection between you can the client
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
    
    //updates chatWindow
    private void showMessage(final String text){
        //want to change only text part of GUI
        //update parts (chatwindow) of GUI rather then whole GUI
        
        //update chat window
        //set aside a thread that updates part of GUI
        SwingUtilities.invokeLater(
                //create Thread
                new Runnable(){
                    public void run(){
                        //appends text at end of document
                        chatWindow.append(text);
                        
                    }
                }
        );
        
    }
  
    //able to type method setEditable() (makes textField editable
    //setEditable(false) <- passed as ablett <- priginally to stop user typing if not connected
    //let user type
   
    //ablett passed as true
    private void ableToType(final boolean ablett){
        SwingUtilities.invokeLater(
                //create Thread
                new Runnable(){
                    public void run(){
                       userText.setEditable(ablett);
                        
                    }
                }
        );
    }
    
    }


    
