package pdl;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class pdl{
	//variables globales 
	 static boolean error=false;
	 static boolean comentario = false;
	 static boolean comillas = false;
	 static int codigo = 0;
	 static int cont = 0;
	 static boolean borrar ;
	 static boolean controlador = true;
	 static File archivo_entrada;
	 static File archivo_salida;
	 static File tabla_simbolos;
	 static File archivo_parse;
	 static Scanner scanner;
	 static String linea = "";
	 static int numLineas=0;
	 static ArrayList<Character> caracter = new ArrayList<Character>();
	 static ArrayList<String> fichSal = new ArrayList<String>();
	 static int estado=0;
	 static String parse = "";
	static int lineaCome=0;
	static int lineaComilla=0;
	static boolean ini=false;
	
	 static List<List<String>> tablaS = new ArrayList<>();
	 static BufferedWriter TS;
	 
	 static int contTS=0;
	 static int despl = 0; //numero de tokens
	 static int n_fun = 0; //numero de tabla / funcion
	 static int n_actual=0;// saber en que tabla estamos
	 
	 static boolean tabla=false;
	 static boolean funcion=false;
	 static int num_funciones=0;
	 static int n_tabla=0;
	 static boolean anterior=false;
	 
	 //static Pair<Integer,String> res;
	 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	 
	 
	public static void main(String[] args) throws IOException {
		//importar archivo
		archivo_salida = new File("/Users/laura/Desktop/salida.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(archivo_salida));
		archivo_entrada = new File("/Users/laura/Desktop/entrada.txt"); // Reemplaza con la ruta del archivo
        scanner = new Scanner(archivo_entrada);
        tabla_simbolos = new File("/Users/laura/Desktop/tabla_simbolos.txt");
        TS = new BufferedWriter(new FileWriter(tabla_simbolos));
       archivo_parse = new File("/Users/laura/Desktop/parse.txt");
       BufferedWriter parse_s = new BufferedWriter(new FileWriter(archivo_parse));
       /* // inicializacion tabla main-->no
        //tablaS.add(new ArrayList<String>());
       // tablaS.get(0).add("#");
       // tablaS.get(0).add("1:");
        */
        //Llamada al sintáctico
		while(controlador && !error){ // bucle lector
			codigo = 0;
			parse=proc.analizador_sintactico();
			analisis_lexicoo();
		}
		// escritura en ficheros de salida
		parse_s.write(parse);
		fichTabla(TS);
		fichSalida(writer);
		parse_s.close();
		// fin escritura
		scanner.close();
		TS.close();
		writer.close();
		if(!error){
			System.out.println("-ANÁLISIS LÉXICO COMPLETADO CORRECTAMENTE");
			System.out.println("\t-->Numero de caracteres leidos = "+ cont + "\n \t-->Numero de lineas leidas = "+ numLineas+"\n");
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Pair<Integer, Pair<String, Integer>> analisis_lexicoo() throws IOException{
		int contcar = 0;
		int num=0;
		int aux=0;
		String token = ""; // donde vamos a ir guardando caracter a caracter
		char car = '#';
		boolean avanzar = true;
		boolean escribir = true;
		int a=0;
		Pair<Integer,Pair<String,Integer>> res=null;
		Pair<String,Integer> pack = null;
		codigo = 0;
		estado=0;

		//Mientras pueda avanzar y no haya ningun error-->sigo evaluando tokens
		while (avanzar && !error ){
				//Si no hay mas lineas y 'caracter' esta vacio--> paro
			  if (!scanner.hasNextLine() && caracter.isEmpty()) {
				   avanzar = false;
				   controlador = false;
				   //Compruebo posibles errores:
				   if(comentario) {
					   // Error cierre de comentario
					   error=true;
					   System.out.println("NO se ha cerrado el comentario, en la línea: "+ lineaCome);
					   System.exit(1);
				   }else if(comillas) {
					// Error cierre de comillas
					   error=true;
					   System.out.println("NO se ha cerrado las comillas, en la línea: " + lineaComilla);
					   System.exit(1);
				   }
			   }
			  //Si hay mas lineas y 'caracter' esta vacio, es decir, ya he analizado toda la línea anterior -->sigo evaluando los tokens
				   if (scanner.hasNextLine() && caracter.isEmpty()){
					  //Me quedo con la línea
				   linea = scanner.nextLine();
				   numLineas++; 
				   		//Añado caracter a caracter a la línea para evaluarlo luego
				    	for (int i=0; i<linea.length(); i++){
				    		//caracter[i]=linea.charAt(i);
				    		caracter.add(i,linea.charAt(i)); //metes en lista 
				    	}
				    }
				   
			borrar = true;
			//voy consiguiendo caracter a caracter, según hago esto elimino el ya analizado
			if(!caracter.isEmpty()) {
				car = caracter.get(0);
				
			}else {
				car = '#';
			}
			if(estado ==0 && car == '$'){  
				codigo = -1;
				avanzar=false;
				//escribir=true;
				controlador=false;
				//escribir=false;
				
				
			}else if ((car == '0' || car == '1' || car == '2' || car == '3' || car == '4' || car == '5' 
					|| car == '6' || car == '7' || car == '8' || car == '9') && estado==0){
				estado=1;
				token+=car;
				aux=(int)car-48;
				num=aux;
			
			}else if (ascii(car) > 64 && ascii(car) < 123 && estado == 0){
				estado=21;
				token+=car;
			}else if(car == '=' && estado == 0){ 
				estado=3;
				token+=car;
			}else if(car == '|' && estado == 0){
				estado=6; //9
				token+=car;
			}else if(car == ',' && estado == 0){
				estado=0;
				token+=car;
				avanzar=false;
				//FINAL//
				codigo = 17;
				
			
			}else if(car == ';' && estado == 0) {
				estado=0;
				token+=car;
				avanzar=false;
				//FINAL
				codigo = 18;
			
				
			}else if(car == '+' && estado == 0) {
				estado=0;
				token+=car;
				avanzar=false;
				//FINAL 
				codigo = 23;
				
			}else if(car == '-' && estado == 0) {
				estado=0;
				token+=car;
				avanzar=false;
				//FINAL 
				codigo = 24;
				
				
			}else if(car == '(' && estado == 0) { 		///////////FINAL
				estado=0;
				token+=car;
				avanzar=false;
				codigo = 19;
				
			}else if(car == ')' && estado == 0) { 		///////////FINAL
				estado=0;
				token+=car;
				avanzar=false;
				codigo = 20;
				
				
			}else if(car == '{' && estado == 0) {		///////////FINAL
				estado=0;
				token+=car;
				avanzar=false;
				codigo = 21;
				
				
			}else if(car == '}' && estado == 0) {		///////////FINAL
				estado=0;
				token+=car;
				avanzar=false;
				codigo = 22;
				//anadirToken(token);
				
			}else if(car == '\'' && estado == 0 ){ //comilla simple
				estado=16;//28
				token+=car;
				contcar--;
				comillas=true;
				
			}else if(car == '/' && estado == 0 ){ //comentarios
				estado=18;
				
			}else if (estado ==1) {
				if(car == '0' || car == '1' || car == '2' || car == '3' || car == '4' || car == '5' 
		    			|| car == '6' || car == '7' || car == '8' || car == '9') {
					estado=1;
					token+=car;
					aux=(int)car-48;
					num=num*10+aux;
					if(num>32767) {
						error=true;
						System.out.println("ERROR: Número leído no válido");
						System.exit(1);
						
					}
					//System.out.println(num);
				}else {
					///////////FINAL
					estado=0;
					avanzar=false;
					borrar = false;
					codigo = 12;

		    		
				}
			}else if (estado == 21){
				if(car == '0' || car == '1' || car == '2' || car == '3' || car == '4' || car == '5' 
		    			|| car == '6' || car == '7' || car == '8' || car == '9') {
					estado=21;
					token+=car;
				}else if (ascii(car) > 64 && ascii(car) < 123){
					estado=21;
					token+=car;
				}else{
					///////////FINAL
					estado=0;
					avanzar=false;
					borrar = false;
					
					//COMPROBAMOS SI ES UNA PALABRA RESERVADA SINO-->IDENTIFICADOR
					
					if(token.equals("boolean")) {
						codigo = 1;
					}else if(token.equals("for")) {
						codigo = 2;
					}else if(token.equals("function")) {
						codigo = 3;
						//anadirToken(token);
						ts.nomF=true;
					}else if(token.equals("get")) {
						codigo = 4;
					}else if(token.equals("if")) {
						codigo = 5;
					}else if(token.equals("int")) {
						codigo = 6;
					}else if(token.equals("let")) {
						codigo = 7;
					}else if(token.equals("put")) {
						codigo = 8;
					}else if(token.equals("return")) {
						codigo = 9;
					}else if(token.equals("string")) {
						codigo = 10;
					}else if(token.equals("void")) {
						codigo = 11;
					}else {
						codigo = 14;
						if(ts.nomF) {
							proc.etiqueta=token;
						}
						//ts.insertarTabla(token);
						//pack = ts.insertarTabla(token);
						//token =Integer.toString(anadirToken(token));
						
					}
				}
			}else if (estado == 3) {
				if(car == '=') { //== 		///////////FINAL
					estado=0;
					token+=car;
					avanzar=false;
					codigo = 25;
		
				}else { //= ///////////FINAL
					estado=0;
					avanzar=false;
					borrar = false;
					codigo = 15;
				
				}
			}else if (estado == 6) { //9
				if(car == '=') {
					estado=0;
					token+=car;
					avanzar=false;
					///////////FINAL
					
					codigo = 16;
		    
				}else {
					// caso error |=
					error=true;
					System.out.println("ERROR: Se ha leido correctamente:" + token);
		    		System.out.println("\t-->En el estado 6 llega un carácter distinto del '=', el que llega es:" + car 
		    				+ "\n El error se encuentra en la línea " + numLineas + "en la posicion:   " + cont);
		    		System.exit(1);
				}
				
			
			}else if (estado == 16) {
				if(contcar > 64) {
					// error
					error=true;
					System.out.println("ERROR:Se pasa del rango de caracteres permitido");
					System.exit(1);
				}else if(car != '\'' ) { //comilla simple  
					estado=16;
					token+=car;
 
				}else if (car == '\''){
					///////////FINAL
					estado=0;
					token+=car;
					avanzar=false;			
					comillas=false;
					contcar --;
					codigo = 13;
		
				}
			}else if(estado == 18){ 
					if(car == '*') { // /*-->inicio de comentario
						estado = 19;
						comentario=true;
					}else { // Se ha leido solo /--> no esta reconocido en  nuestro lenguaje
						error=true;
			    		System.out.println("ERROR: se ha  leido correctamente: " + token);
			    		System.out.println("\t -->En el estado 18 llega un carácter distinto de los permitidos, el que llega es: "+ car 
			    				+ "\nEl error se encuentra en la línea " + numLineas + " en la posicion: " + cont);
			    		System.exit(1);
					}
				}else if(estado == 19) { // /* +cualquier cosa excepto *
					if(car == '*') {		/*hola*lol*/
						estado = 20;
					
					}else {
						estado = 19;
				
					}			
					
				}else if(estado == 20) { 
					if(car == '*') {
						estado = 20;
					
					}else if(car== '/'){ // */-->fin de comentario
						estado = 0;
						comentario = false;
						
					}else {
						estado=19;
						
					}
				
				}else if (car == ' ' || car == '\t') {
			
				}else if(car == '\n'){
					
				
				}else if( car == '#'){
					estado = 0;
					avanzar = false;
					escribir = false;
				}else if(estado==0){
		    		error=true;
		    		System.out.println("ERROR: No se ha leido correctamente: " + token);
		    		System.out.println("\t-->En el estado 0 llega un carácter distinto de los permitidos, el que llega es: "+ car 
		    				+ "\nEl error se encuentra en la línea " + numLineas + " en la posicion: " + cont);
		    		System.exit(1);
				}else {
					codigo = 99;
					System.out.println("Se ha leído: " + token);
					System.out.println("ERROR: de ejecucion");
				}
				contcar++;
				cont++;
				if(borrar && !caracter.isEmpty()) {
					caracter.remove(0);
				}
				
				if (comillas && a==0) {
					lineaComilla=numLineas;
					a=1;
				
				}else if (comentario && a==0 ) {
					lineaCome=numLineas;
					a=1;
					
				}
			}// fin while
	
		

		if(codigo==12) { // numeros
			pack= new Pair(token,null);
			res=new Pair(codigo,pack);
			if(escribir) {
			token = "<12,"+token+'>';
			}
			
		}else if(codigo==13) { // cadena
			pack= new Pair(token,null);
			res=new Pair(codigo,pack);
			if(escribir) {
			token = "<12,"+token+'>';
			}
			
		}else if(codigo==14){ //identificadores
			pack = ts.insertarTabla(token);
			if(pack!=null && pack.getRigth()!=null) {
				int pos=pack.getRigth();
				res=new Pair(codigo,pack);
				if(escribir) {
					token = "<14,"+ pos+'>';
				}
			}
		}else {
			res=new Pair(codigo," ");
			if(escribir) {
			token = '<'+Integer.toString(codigo)+", >"; //<function,< , >>
			}
	
		}
		
		/*if(escribir) {
			if(codigo == 12 ){
				token = "<12,"+token+'>';
			}else if(codigo == 13){
				token = "<13,"+token+'>';
				// variable tabla de simbolos
			}else if(codigo == 14){
				token = "<14,"+ pack.getRigth()+'>';
			}else {
				token = '<'+Integer.toString(codigo)+", >";
			}
		}
		*/
		
		anadirToken(token);
		
		return res;
		//pair<codigo,numTabla/nada,<casos:num,cadena y pos identificadores>>
	}
	private static int ascii (char car) {
     	//int ascii= (int)caracter[car];
     	int ascii = (int)car;
		return ascii;
     
	}
	private static void anadirToken(String token) {
		if(!token.equals("")) {
	
			pdl.fichSal.add(token);
		}
	}
	
	private static void fichSalida(BufferedWriter writer){
		for(int i = 0;i<fichSal.size();i++) {
			if(i == fichSal.size()-1){
				fichSal.remove(i);
			}else if(i == fichSal.size()-1) {
				try {
					writer.write(fichSal.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				try {
					writer.write(fichSal.get(i));
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void fichTabla(BufferedWriter TS) throws IOException{
		int cont=1;// numeros de parametros de funcion
		for(int i = 0;i<ts.tablas.size();i++) { // num tabla
			TS.write("#"+(i+1));
			TS.newLine();
			for(int j=0;j<ts.tablas.get(i).size();j++) { // num fila
			//if(i == ts.tablas.size()-1) {
				/*if(ts.tablas.get(i).get(j).get(0).equals("#")) {
					TS.write(ts.tablas.get(i).get(j).get(0)); //#
					TS.write(ts.tablas.get(i).get(j).get(1)); // num:
				}else {
				*/
					// ts.tablas.get(i).get(j).size()
					for(int k=0;k<ts.tablas.get(i).get(j).size();k++) { // recorremos cada atributo de cada ide
						if(k==0 && ts.tablas.get(i).get(j).get(0)!=null) {
							TS.write("*'");
							TS.write(ts.tablas.get(i).get(j).get(0)); //*' -->token
							TS.write("'");
							TS.newLine();
						}else
						if(k==1 && ts.tablas.get(i).get(j).get(1)!=null) {
							TS.write("+tipo:  '");
							TS.write(ts.tablas.get(i).get(j).get(1)+ "'"); // tipo
							TS.newLine();
						}else
						if(k==2 && ts.tablas.get(i).get(j).get(2)!=null) {
							TS.write("+despl: ");
							TS.write(ts.tablas.get(i).get(j).get(2)); // despl
							TS.newLine();
						}else
						if(k==3 && ts.tablas.get(i).get(j).get(3)!=null) {
							TS.write("+numParam:  ");
							TS.write(ts.tablas.get(i).get(j).get(3)); // numParams funcion
							TS.newLine();
						}else
						if(k>=4 && k<ts.tablas.get(i).get(j).size()-2 &&  ts.tablas.get(i).get(j).size()>6 &&ts.tablas.get(i).get(j).get(k)!=null) {
							TS.write("+TipoParam" + cont + ":  '");
							TS.write(ts.tablas.get(i).get(j).get(k)+ "'"); // tipoParams funcion
							cont++;
							TS.newLine();
						}else
						
						if(k== (ts.tablas.get(i).get(j).size()-2) && ts.tablas.get(i).get(j).get(k)!=null) {
							TS.write("+TipoRetorno:  ");
							TS.write(ts.tablas.get(i).get(j).get(k)); // tipo retorno funcion
							TS.newLine();
						}else
						if(k== (ts.tablas.get(i).get(j).size()-1) && ts.tablas.get(i).get(j).get(k)!=null) {
							TS.write("+EtiqFuncion:  ");
							TS.write(ts.tablas.get(i).get(j).get(k)); // etiqueta funcion
							TS.newLine();
						}
					}
					TS.write("--------- ----------");
					TS.newLine();
				}
			//}
		}
	
	}

	public static int getNumLineas() {
		return numLineas;
	}
	public static void setError(boolean x) {
		error = x;
	}

}