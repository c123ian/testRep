
package myclient;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;




public class MyClient extends JFrame
{
    private JTextField userText;
    private JTextArea chatWindow;
   
    //remember output flows out to other progran (server)

    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private String message = "";
    private String serverIP;
    private Socket connection;
    
    //server waiting, want everyone to access
    //client, dont want everyone to access, thus only though server host (server ip)
    //constructor
    public MyClient(String host)
    {
        super("Messanger Box");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent event){
                    //whatever is inside TextArea, sends to other user
                    sendMessage(event.getActionCommand());
                    //reset TextArea back to blank
                    userText.setText("");
                }
                }
        );
        //design of gui
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
        
    }
    //connect to server
    public void startRunning(){
        try{
            connectToMyServer();//connectes to one specific server
            SetupStreams();//once connected
            whileChatting();
    
    //Signals that an end of file or end of stream has been reached unexpectedly during input.
        }catch(EOFException Eof){
            showMessage("\n Client terminated the connection");
        }catch(IOException ioE){
            ioE.printStackTrace();
        }finally{
            closeSockets();
        }
    }
//connect to server
    private void connectToMyServer() throws IOException{
        showMessage("Attempting connection... \n");
        //create socket, need ip and port number
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to : " + connection.getInetAddress().getHostName());
    }
    
        //waitForConnection();
    
        private void SetupStreams() throws IOException{
        
        //outputstream
        out = new ObjectOutputStream(connection.getOutputStream());
        //flush leftover data from Stream
        //only sender can flush
        out.flush();
        
        //input Stream
        in = new ObjectInputStream(connection.getInputStream());
        
        showMessage("\n Steams now setup \n");
        
    }
        
        private void whileChatting() throws IOException{
		String message = " You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String) in.readObject();
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
			out.close(); //Closes the output path to the client
			in.close(); //Closes the input path to the server, from the client.
			connection.close(); //Closes the connection between you can the client
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
    
    //updates chatWindow
    private void showMessage(final String message){
        //want to change only text part of GUI
        //update parts (chatwindow) of GUI rather then whole GUI
        
        //update chat window
        //set aside a thread that updates part of GUI
        SwingUtilities.invokeLater(
                //create Thread
                new Runnable(){
                    public void run(){
                        //appends text at end of document
                        chatWindow.append(message);
                        
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
    
        private void sendMessage(String message){
        try{
            //create object and send it through output STREAM
            out.writeObject("CLIENT - " + message);
            out.flush();
            
            //display on screen (of full conversation
            showMessage("\nClient - " + message);
        }catch(IOException ioE){
            chatWindow.append("\n Error: cant send message!!!!");
        }

    
}
}
