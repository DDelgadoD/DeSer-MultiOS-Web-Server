package deSer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Panel extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	//Declarem un panell per posar els botons i la caixa de text
	JPanel principal;
	JTextArea j;
	JScrollPane jScrollPane1;
	JTextField[] campsText;
	int numCamps;
	JButton[] buttons;
	int numButtons;
	
	//Constructor
	public Panel(){
		super("Delia Server");
				
		//Panell
		principal = new JPanel(new FlowLayout());
		principal.setPreferredSize(new Dimension(730, 375));
		
		//Consola
		j =new JTextArea();
		jScrollPane1 = new JScrollPane();
		
		//Contenidors pels buttons i Camps de text (per possibles canvis)
		campsText = new JTextField[10];
		numCamps = 0;
		buttons = new JButton[10];
		numButtons= 0;
		
		//Escoltadors d'events
		cerrar();
		
	}

//Funció per afegir un botó
public void addBoton(String name, String toolTip){
	//Agafem el següent butó lliure de l'array
	buttons[numButtons] = new JButton(name);
	//L'afegim al principal
	principal.add(buttons[numButtons]);
	//Indiquem la ayuda
	buttons[numButtons].setToolTipText(toolTip);
	//Incrementem el apuntador de l'array de butons
	numButtons+=1;
}
//Funció per afegir una caixa de text amb la seva etiqueta
public void addCajaText(String tField, String desc, String valorDef, int llarg){
	//Agafem el següent butó lliure de l'array
	campsText[numCamps]= new JTextField(valorDef, llarg);
	//Li assigmen el nom
	campsText[numCamps].setName(tField);
	//Li afegim una etiqueta
	JLabel lab = new JLabel(desc);
	//Afegim tot al principal
	principal.add(lab);
	principal.add(campsText[numCamps]);
	//Augmentem el número de l'apuntador de l'array de caixes de text
	numCamps+=1;
}

//Funció per afegir un ScrollPanel que faci de Consola
public void addConsole(){
	//Determinem el color de fons i de les lletres de l'area de text
	j.setBackground(new Color(255, 255, 255));	
	j.setForeground(new Color(0, 0, 0));
	//Determimen el tipus de voral
	j.setBorder(BorderFactory.createLoweredBevelBorder());
	//Afegim un text d'ajuda
	j.setToolTipText("Consola del Servidor");
	//Evitem que es pugui escriure al area de text
	j.setEditable(false);
	//Determinem colummnes i files
	j.setColumns(65);
	j.setRows(20);
	//Afegim el area de text al panell Scroll
	jScrollPane1.getViewport().add(j);
	//Afegim el panell Scroll al principal
	principal.add(jScrollPane1);
}

//Funció per fer visible el panell
public void pintar(){
	//Afegim el principal al panell
	getContentPane().add(principal);
	//Ho "empaquetem
	pack();
	//Evitem que es pugui canviar la mida del panell
	setResizable(false);
}

//Funció que inclou un escoltador per assegurar que el usuari vol sortir
public void cerrar(){
	//Quan fem clic a l'aspa no fa res
	this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	//Pero afegim un escoltador per quan passi això 
	addWindowListener(new WindowAdapter(){
		@Override
		public void windowClosing(WindowEvent e){
			//Y fem que s'obri una finestra si/no
			int valor= JOptionPane.showConfirmDialog(principal,"Esteu segurs que voleu apagar el servidor?","Advertencia", JOptionPane.YES_NO_OPTION);
			//Si pitgem Sí, llavors sortim del programa
			if (valor==JOptionPane.YES_OPTION){
				System.exit(1);				
			}
		}
	});
	principal.setVisible(true);
}
}

