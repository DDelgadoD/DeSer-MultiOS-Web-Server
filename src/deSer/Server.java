package deSer;

import java.net.ServerSocket;
import java.net.Socket;

public final class Server 
{
	
	int port; 
	String path;
	static boolean running;
	ServerSocket servSocket;
	static int entrega = 0;
	
    
	public Server(String [] args) throws Exception {
		//Establim el port, si no hi ha per defecte serà el 90
		try{
			port = Integer.parseInt(args[0]);
			Msg.linia("Iniciado en puerto: "+ port);
			
		}catch(NumberFormatException e){
			port = 90;
			Msg.linia("Puerto por parametro incorrecto - Iniciado en puerto: "+ port);
		}
		//Establim la ruta de la carpeta del servidor, si no hi ha per defecte serà C:\DeSer
		if (args[1]!= null){
			path =args[1] ;
		}else{
			path = "C:\\DeSer";
		}
		
		Msg.linia("Carpeta de Servidor: "+ path);
		Msg.linia("");
		running = false;
    	
    }    
	
	        
    public void arranca() {
    	//Amb aquest Trhead obrim únicament 2 fils un que corre el servidor y un altre que fa que el botó no quedi encallat pel while.
    	//No s'obrirà cap més perquè si running es true el botó no torna a funcionar.
    	Thread serving = new Thread(new Runnable(){  
    	@Override
		public void run(){
    				
    		// Posem en marxa el Socket del servidor 
    		try{
    			servSocket = new ServerSocket(port); 
    			// Procesem les sol·licitus HTTP.
    			Msg.linia("Restem a la espera de que el client connecti");
	            while (true) {
	                Socket socketConnexio = servSocket.accept();
	            	// Processem el missatge de sol·licitud HTTP.
	                entrega+=1;
	                SolicitudHttp solicitud = new SolicitudHttp( socketConnexio, path, entrega);
	            	// Creem un nou fil per processar-la.
	            	//En aquest punt el nostre servidor es transforma en multi thread realment
	            	Thread filServer = new Thread(solicitud);
	            	// Iniciem el fil.
	            	filServer.start();
	            }
	        }catch (Exception e){
	        	//Si hi ha cap error el mostrem 
	        	Msg.linia("Error en servidor\n" + e.toString());
	        }
    	}
    	
    	});
    	//Obrim el nou fil
    	serving.start();
    	//Assignem un cert a la variable running per evitar que es posin en marxa més d'un
    	//procès servidor més enllà del que tanca i el que continua per enviar la senyal al
    	//butó d'encès
    	running = true;
    	//Passem un missatge de posada en marxa
    	Msg.linia("Posem en marxa el servidor");
    }
   

}
    	



