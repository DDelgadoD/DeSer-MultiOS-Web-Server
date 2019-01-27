package deSer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class SolicitudHttp implements Runnable
{	
	 //Primer un String per tancar un enviament, seguidament el socket de connexió que enviarem 
	 //per paràmetre y per últim la ruta a la carpeta del servidor que també vindrà per paràmetre.
	 final static String fi = "\r\n";
     Socket socket;
     String path;
     int entrega;
     

     // Constructor
     public SolicitudHttp(Socket socket, String path, int entrega) throws Exception 
     {
             this.socket = socket;
             this.path = path;
             this.entrega = entrega;
             
     }

     // Implementa el mètode run() producte de fer ús de Runnable.
     @Override
	public void run()
     {
    	 try {
             processaSollicitud();
    	 } catch (Exception e) {
             Msg.linia("Error processant la sol·licitud: "+ e.toString());
    	 }  
     }

     private void processaSollicitud() throws Exception
     {
    	 // Referencia al stream de sortida del socket i creem un Buffer per la trama d'entrada del socket
         DataOutputStream respostaServidor = new DataOutputStream(socket.getOutputStream());;
         BufferedReader peticioClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         
         // Recollim la petició del client, Mostra el número de sol·licitud per tenir un control de les trames 
         // i seguidament la línia de sol·licitud
         String lineaSol = peticioClient.readLine();
         Msg.linia("----- Sol·licitud " + entrega +" ----");
         Msg.linia(lineaSol);
         
         // Recollim la línia de capçalera
         //LLegim les línies de capçalera i les imprimim per pantalla
         String capcalera = null;
         while ((capcalera = peticioClient.readLine()).length() != 0) {
                 Msg.linia(capcalera);
         }
         
         // Ara trenquem la línia de sol·licitud per obtenir el nom del arxiu sol·licitat
         StringTokenizer tokenLinia = new StringTokenizer(lineaSol);
         
         //Creem els String per la resposta
         String estat = null;
         String tipusDeCont = null;
         String cosMissatge = null;
         
         //De moment ens limitarem al mètode GET pel que emetrem un missatge d'error en cas d'altre mètode
         String metode = tokenLinia.nextToken(); 
         if (!metode.equals("GET")){
        	 estat = "HTTP/1.0 405 Method not allowed" + fi;
             tipusDeCont = "Content-type: " + "text/html" + fi;
             cosMissatge = "<HTML>" + 
                     "<HEAD><TITLE>405 Method not allowed</TITLE></HEAD>" +
                     "<BODY STYLE=\"font-family:arial;text-align:center; margin-top:100px; margin-down: 50px\">"+
                     "<p><b>405</b> Method not allowed</p>"+
                     "<p style=\font-size:32px;>Aquest servidor solament aten a peticions <b>GET<b></p>"+
                     "</BODY></HTML>";	 
         }
         
         //Passem al següent "token" on hem de tenir el nom del arxiu sol·licitat
         String nomArxiu1 = tokenLinia.nextToken();
         
         //Si el nom del arxiu conté espais el navegador a la barra els canviara per %20 posem de nou espais 
         String nomArxiu = nomArxiu1.replaceAll("%20", " ");   
          
         //Si el arxiu acaba en / o es igual a res llavors retornem l'index del directori
         //Els directoris llavors caldrà que es possin amb una barra final
         if (nomArxiu.endsWith("/") || nomArxiu.equals(""))
         {
         	nomArxiu =  nomArxiu + "index.html" ;
         }
         
         //Afegim al nom del arxiu la ruta des d'on volem que treaballi el servidor
         nomArxiu =  path + nomArxiu;
         
         // Creem un Stream d'arxiu per encabir el nou arxiu a enviar i un boolea per determinar si el arxiu
         // existeix
         FileInputStream arxiuEnviar = null;
         boolean existeix = true;
        
         try {
        
        	 //Intentem obrir l'arxiu
        	 arxiuEnviar = new FileInputStream(nomArxiu);
         
         } catch (FileNotFoundException e) {
             
        	 //Si no es pot saltarà una excepció que controlarem posant el booleà en fals i emetem un missatge 
        	 existeix = false;
             Msg.linia("El arxiu demanat no existeix o no es troba");
         }
                  
         if (existeix) {
        	 
        	 //Si existeix carreguem el estat correcte i porcesem el tipus de contingut
        	 estat ="HTTP/1.0 200 Document Follows" + fi;     	 
             tipusDeCont = "Content-type: " + contentType( nomArxiu ) + fi;
         
         } else {
        	 
        	 //Si no existeix passem a carregar un no trobat.
             estat = "HTTP/1.0 404 Not Found" + fi;
             tipusDeCont = "Content-type: " + "text/html" + fi;
             cosMissatge = "<HTML>" + 
            		 	   "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
                           "<BODY STYLE=\"background:black; font-family:arial;text-align:center; margin-top:100px; margin-down: 50px\">"+
                           "<p style=\"font-size:56px;color:red\"><b>404</b> Not Found</p>"+
                           "<p style=\font-size:32px;color:white>Si heu sol·licitat un directori recordeu de posar la barra final \"\\\"</p>"+
                           "</BODY></HTML>";
         }
         
         //Passem a enviar el missatge que hem construit
         // Enviem l'estat, el tipus de contingut i la línia en blanc que tanca la capçalera.
         respostaServidor.writeBytes(estat);
         respostaServidor.writeBytes(tipusDeCont);
         respostaServidor.writeBytes(fi);
         
         // Enviem el cos del missatge
         if (existeix) {
        	 	
        	 // Per enviar els bytes que conformen el arxiu creem un buffer de 1KB
        	 byte[] buffer = new byte[1024];
        	 int bytes = 0;

        	 // Copiem l'arxiu sol·licitat cap el stream de sortida del socket de resposta.
        	 while((bytes = arxiuEnviar.read(buffer)) != -1 ) {
        	 	respostaServidor.write(buffer, 0, bytes);
        	 }
        	 	
        	 //Tanquem el Stream d'arxiu
        	 arxiuEnviar.close();
        	 	
         } else {
        	 
        	 // I si no hi ha arxiu el cos del missatge que hem creat pels errors.
             respostaServidor.writeBytes(cosMissatge);
         }
         
         
         // Tanquem els streams de resposta i petició i el socket.
         respostaServidor.close();
         peticioClient.close();
         socket.close();  
     }
     
     //Amb aquest mètode podem ampliar els nime-types 
     //Per l'elevat nombre de tipus he afegit els que he fet servir habitualment amb les proves
     private static String contentType(String nombreArchivo){
	
             if(nombreArchivo.endsWith(".htm") || nombreArchivo.endsWith(".html")) {
                     return "text/html";
             }
             if (nombreArchivo.endsWith(".jpg")){
            	 	return "image/jpeg";
             }
              if (nombreArchivo.endsWith(".gif")){ 
            	  	return "image/gif";
             }
             if (nombreArchivo.endsWith(".css")){
            	  	return "text/css";
             }
             if (nombreArchivo.endsWith(".png")){
            	 	return "image/png";
             }
             if (nombreArchivo.endsWith(".js")){
         	 	return "text/javascript";
             }
             if (nombreArchivo.endsWith(".js")){
          	 	return "text/javascript";
             }
            
             return "application/octet-stream";
     }
     
}
