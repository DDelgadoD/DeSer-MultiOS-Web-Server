package deSer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.FileDescriptor;
//import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main{

	public static void main(String[] args) {
		
		//El main es la sortida gràfica podriem adaptar facilment servidor perque es pugui
		//executar des de la consola del S.O. canviant només aquest arxiu.
		
		//Creem el panell i la cadena per enviar parametres al servidor
		Panel p = new Panel();
		String[] argServer = new String[10];
		
		//Creem un missatger perque enviï les dades de consola al panell gràfic
		Msg m = new Msg(p.j);
		System.setOut(new PrintStream (m));
		
		//Afegim els elements al panell gràfic
		p.addCajaText("Serv","Carpeta del servidor", "C:\\DeSer", 30);
		p.addCajaText("Port", "Port", "90", 10);
		p.addBoton("Encendre", "Aceptar i possar en marxa");
		//p.addBoton("Ocultar","Tancar el panell i deixar el servidor");
		p.addConsole();
		
		//Creem el panell gràfic i el fem visible
		p.pintar();
		p.setVisible(true);
		
		//FUNCIÓ DEL BOTÓ ENCENDRE
		//Mitjançant l'addició d'accions als buttons crearem el servidor i el posarem en marxa
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
		
		/* Botó per possible implementació: problemes (potser "features"...) que el procès queda
		 * executant-se sense poder-lo parar d'una forma elegant. Es pot fer un kill, però per 
		 * aquesta raó no l'implementat finalment.Podria ser útil per deixar-lo en un stand-alone.
		 * 
		//FUNCIÓ DEL BOTÓ OCULTAR
		//Al altre botó li donem la funció d'ocultar el panell del servidor
		p.buttons[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				p.setVisible(false);
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
			}		
		});
		*/
			
	}
}




