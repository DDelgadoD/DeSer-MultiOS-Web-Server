package deSer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.FileDescriptor;
//import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main{

	public static void main(String[] args) {
		
		//El main es la sortida gr�fica podriem adaptar facilment servidor perque es pugui
		//executar des de la consola del S.O. canviant nom�s aquest arxiu.
		
		//Creem el panell i la cadena per enviar parametres al servidor
		Panel p = new Panel();
		String[] argServer = new String[10];
		
		//Creem un missatger perque envi� les dades de consola al panell gr�fic
		Msg m = new Msg(p.j);
		System.setOut(new PrintStream (m));
		
		//Afegim els elements al panell gr�fic
		p.addCajaText("Serv","Carpeta del servidor", "C:\\DeSer", 30);
		p.addCajaText("Port", "Port", "90", 10);
		p.addBoton("Encendre", "Aceptar i possar en marxa");
		//p.addBoton("Ocultar","Tancar el panell i deixar el servidor");
		p.addConsole();
		
		//Creem el panell gr�fic i el fem visible
		p.pintar();
		p.setVisible(true);
		
		//FUNCI� DEL BOT� ENCENDRE
		//Mitjan�ant l'addici� d'accions als buttons crearem el servidor i el posarem en marxa
		p.buttons[0].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(Server.running == false){
					argServer[0]=p.campsText[1].getText();
					argServer[1]=p.campsText[0].getText();
					Server serv;
					try {
						serv = new Server(argServer);
						serv.arranca();		 
					}		
					 catch (Exception er) {
						 Msg.linia("Error creant el servidor\n" + er.toString());
					}
				}
			}});
		
		/* Bot� per possible implementaci�: problemes (potser "features"...) que el proc�s queda
		 * executant-se sense poder-lo parar d'una forma elegant. Es pot fer un kill, per� per 
		 * aquesta ra� no l'implementat finalment.Podria ser �til per deixar-lo en un stand-alone.
		 * 
		//FUNCI� DEL BOT� OCULTAR
		//Al altre bot� li donem la funci� d'ocultar el panell del servidor
		p.buttons[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				p.setVisible(false);
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
			}		
		});
		*/
			
	}
}




