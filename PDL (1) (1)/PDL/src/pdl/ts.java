package pdl;

import java.util.ArrayList;
import java.util.List;

public class ts {
	static List<List<List<String>>> tablas=new ArrayList<>();
	static int numTabla=0;
	static boolean funcion=false;
	static boolean tablaGlobal=false;
	static int nG=0;
	static boolean nomF=false;
	static int pos=0;
	static boolean inicializado=false;
	static int desplG=0; //desplazamiento de la tabla global
	static int desplL=0; //desplazamiento tablas locales
	static int numfun=0;//numero de funciones
	static int cont=4;//comenzamos a meter elementos desde la posicion 5
	
	public static void crearTabla(String token) {
		//comprobamos si es una funcion-->nueva tabla
		if(token.equals("3")) {
			tablas.add(new ArrayList()); // creas nueva tabla a la lista
			//aumentas el numero de tabla
			pos=0;
			numTabla++;
			funcion=true;	
			numfun++;
			desplL=0;
		}else if(token.equals("14")) {
			//si no se ha creado la tabla global y no estamos dentro de una funcion-->nueva Tabla (Tabla Global)
			if(!tablaGlobal) {
				//ya creamos la tabla global-->activamos para no poder volver a crear tablas si no hay funcion
				tablaGlobal=true;
				pos=0;
				desplG=0;
				//creamos la nueva tabla 
				tablas.add(new ArrayList<>());
				//Actualizamos el numero de tabla
				numTabla++; 
				nG=numTabla;// nos quedamos con el numero de la tabla global-->por si luego lo necesitamos
			}
		}
	}
	/* casos:
	 * 		-->no inicializado-->busco-->ninguna tabla-->error
	 * 								  -->esta en una tabla-->acabo
	 * 		-->inicializado-->busco solo en esa tabla y si no esta lo meto en la tabla actual
	 * 		--> nombre funcion-->meto en la tabla global si hay
	 */

	public static Pair<String,Integer> insertarTabla(String token) { // que añada en una lista el lexema y en que tabla está
		int res=-1;
		Pair<String,Integer> pack=null;
		if(nomF && tablaGlobal){ // no añadido y hay tabla global-->se añade
			//hago hueco
			tablas.get(nG-1).add(new ArrayList());
			tablas.get(nG-1).get(tablas.get(nG-1).size()-1).add(0,token); // añadimos el nombre de la funcion
			pos++;
			res=pos;
			//nomF=false;
			pack=new Pair(tablaGlobal,res);
		}else // si no esta inicializado 
		
		if(!inicializado){ 
			//buscamos si esta en la tabla de simbolos si no esta error!!!
			if(buscarTabla(token).getRigth()== -1) {
				//Error()
				//System.out.println("ERROR SEMÁNTICO: identificador no esta inicializado");
				//System.exit(1);
			}else {
					pack=buscarTabla(token); //si esta --> me quedo con la posicion y numero de tabla
			}
		}else {
		// si se esta inicializando --> lo meto
		if(inicializado) {
			//creo el hueco-->fila por identificador
			tablas.get(numTabla-1).add(new ArrayList());
			//añado el identificador a la ultima posicion de esa tabla
			tablas.get(numTabla-1).get(tablas.get(numTabla-1).size()-1).add(token);
			pos++;
			res=pos;
			pack=new Pair((Integer.toString(numTabla-1)),res); // guardo el numero de tabla en el que está y su posicion
		}
		// si hay tabla global y no se ha añadido el nombre de la funcion a la que vamos a entrar se añade
		}
		return pack;
	}
	
	public static Pair<String,Integer> buscarTabla(String token) {
		boolean encontrar=false;
		Pair<String,Integer> pack=null;
		String tabla=null;
		int res=-1;
		if(!tablas.isEmpty()) {
		for(int i=0;i<tablas.size()&&!encontrar;i++) { // tabla por tabla
			for(int j=0;j<tablas.get(i).size() && !encontrar;j++) { // fila por cada tabla
				// tabla[i]fila[j]elem0 
					if(tablas.get(i).get(j).get(0).equals(token)) { 
						res=j; // devuelve la posición en la que está
						tabla=Integer.toString(i);
						encontrar=true;
						pack= new Pair(tabla,res);
					}
			}
		
		}
		}
		if(pack==null) {
			pack= new Pair(tabla,res);
		}
		return pack;
	}
	public static String tipo(String tipo) {
		String t;
		//casos: -->entero --> 6
		//		 -->boolean --> 1
		//		 -->string --> 10
		if(tipo.equals("6")) {
			t="entero";
			
		}else if(tipo.equals("1")) {
			t="logico";
			
		}else  {
			t="cadena";
		}
		return t;
	}
	
	public static int desplazamiento(String tipo) {
		int res;
		//casos: -->entero --> 6
		//		 -->boolean --> 1
		//		 -->string --> 10
		if(tipo.equals("entero")) { //entero
			res=1;
			
		}else if(tipo.equals("logico")) {//logico
			res=1;
			
		}else  { //cadena
			res=64;
		}
		return res;
	}
	
	public static void insertarTipo(int tabla,int pos,String t) {
		//inserto el tipo en la tabla actual en la posicion correspondiente
		if(pos>=0 && pos<tablas.get(tabla).size()) {
			System.exit(1);
		}else {
			tablas.get(tabla).get(tablas.get(tabla).size()-1).add(1,t);
		}
	}
	
	public static void insertarDesplazamiento(int tabla,int pos,int despl) {
		//inserto el tipo en la tabla actual en la posicion correspondiente
		if(pos>=0 && pos<tablas.get(tabla).size()) {
			System.exit(1);
		}else {
			tablas.get(tabla).get(tablas.get(tabla).size()-1).add(2,Integer.toString(despl));
		}
	}
	
	public static void insertarNumParam(int numParam) {
		int nGlobal=nG-1;
			tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(1,null);
			tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(2,null);
			tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(3,Integer.toString(numParam));
	}
	
	public static void insertarTipoParam(List<String> params) {
		int nGlobal=nG-1;
		for(int i=0;i<params.size();i++) {
			if(i==0) {
				tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(4,params.get(i));
			}else {
				tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(cont,params.get(i));
				cont++;
			}
		}
	}
	
	public static void insertarRetorno(String retorno) {
		int nGlobal=nG-1;
			tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(cont,retorno);
			cont++;
	}
	public static void insertarEtiqueta(String etiqueta) {
			int nGlobal=nG-1;
			String res="Et"+numfun+"_"+etiqueta;
			tablas.get(nGlobal).get(tablas.get(nGlobal).size()-1).add(cont,res);
			cont++;
	}

	public static String BuscaTipo(int tabla,int pos) {
		String tipo="";
		tipo=tablas.get(tabla).get(pos).get(1);	
		return tipo;
	}
	
	public static int despl(String tipo) {
		int res=0;;
		if(tipo.equals("6")) { //entero
			res=2;
			
		}else if(tipo.equals("1")) {//logico
			res=2;
			
		}else {
			if(tipo.equals("10")) { //cadena
				res=128;
			}
		}
		return res;
	}
	
	public static void liberarTabla() {
		
	}

}
