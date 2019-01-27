package deSer;

import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;


//Classe que crea un nou OutputStream per poder redirigir la sortida a la consola que hem
//creat a la classe Panel
public class Msg extends OutputStream {
	
private static JTextArea Text;
private PrintStream out;

//Constructor
public Msg(JTextArea Text) 
	{ Msg.Text = Text;}

//Funci� que transcriu els car�cters en la sortida	
@Override
public void write(int letra){
	out.write(letra);
}

//Funci� que escriu una l�nia
public static void linia(String s) {
	Text.append(s+"\n");
}	
	
}
