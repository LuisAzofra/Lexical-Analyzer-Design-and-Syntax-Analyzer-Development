package pdl;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class proc {
	static int cont = 0;
	static String sig_token = "";
	static String parse="Descendente ";
	static int n=1; //numero de token
	static int posTs=0;
	static String t;
	static Pair<Integer,Pair<String,Integer>> token;
	static int tablaEsta=0;
	static String ladoDer="";
	static boolean  e_logico=true;
	static int despl=0; //lo que va sumando
	static int numParam=0;
	static List<String> parametros_funcion= new ArrayList<>();
	static String etiqueta="";
	static String tRetorno="";
	static String res="";
	static boolean retornos=false;
	
	    public static String analizador_sintactico() throws IOException {
	    	 
	    		//guardamos el token que se nos pasa
               token = pdl.analisis_lexicoo();
               //nos quedamos con el codigo-->primera parte
               sig_token=Integer.toString(token.getLeft());
               //nos quedamos con la posicion de la Tabla de Símbolos si es un identificador
               if(sig_token.equals("14")) { 
            	   posTs=token.getRigth().getRigth();
            	   ladoDer=token.getRigth().getLeft();
            	   tablaEsta=Integer.parseInt(String.valueOf(ladoDer));
            	   
               }
            
	    	//System.out.println( n +"  token: "+ conversion(sig_token));
	      
	        P1(); // Comienza el análisis sintáctico desde el axioma aumentado
	        
	        if (sig_token.equals("-1")) { // si hemos conseguido el $ -->acabo
	            System.out.println("-ANÁLISIS SINTÁCTICO COMPLETADO CORRECTAMENTE");
	            System.out.println("\t -->Parse finalizado correctamente \n");
	            
	            System.out.println("-ANÁLISIS SEMÁNTICO COMPLETADO CORRECTAMENTE\n");
	        } 
	        return parse;
	    }
	    static void P1() {
	    	ts.crearTabla("14");
	    	ts.desplG=0;
	    	P();
	    	//libero la tabla
	    	ts.liberarTabla();
	    }
	    static void P() {
	    	//System.out.println("empiezo P");
	        if (sig_token.equals("7") || sig_token.equals("5") || sig_token.equals("2") || sig_token.equals("14") || sig_token.equals("9") || sig_token.equals("8") || sig_token.equals("4")) {
	           //let / if / for / id / return/ put / get
	        	parse+="1 ";
	        	B();
	        	P();
	        } else if (sig_token.equals("3")) { //function
	        	parse+="2 ";
	            F();
	            P();
	        } else if (sig_token.equals("-1")) { //final de fichero
	        	parse+="3 ";
	        	equipara("-1");
	          // System.out.println("SE HA LLEGADO AL FINAL");
	        }else {
	        	//ERROR
	        	error(1);
	        	System.exit(1);
	        }
	       // System.out.println("acabo P");
	    }     

	    static void B() { //no caso de error -->S
	    	//System.out.println("empiezo B");
	    	int pos=0;
	    	 if (sig_token.equals("7")) { //let
	    		 parse+="4 ";
	    		ts.inicializado=true; // zona_decl=true;
	    		 equipara("7");
	    		 pos=posTs;
	    		 equipara("14"); //id zona_decl=false;
	    		ts.inicializado=false;
	    		//guardamos el tipo
	    		t=T();
	    		equipara("18"); //;
	    		ts.insertarTipo(tablaEsta,pos,t);
	    		despl=ts.desplazamiento(t);
	    		if(tablaEsta == ts.nG) { // si estamos en la tabla global-->añadimos desplG
	    			ts.desplG=ts.desplG+despl;
	    			ts.insertarDesplazamiento(tablaEsta, pos, ts.desplG);
	    		}else { // sino aumentamos el desplazamiento de la tabla local
	    			ts.desplL=ts.desplL+despl;
	    			ts.insertarDesplazamiento(tablaEsta, pos, ts.desplL);
	    		}
	    		
	    	 } else if(sig_token.equals("5")) { //if
	    		 parse+="5 ";
	    		 	equipara("5");
	    			equipara("19");	//(
	    			e_logico=true;
	    			t=E();
	    			t="logico";
	    			equipara("20"); //)
	    			if(!t.equals("logico")) {
	    			  System.out.println("ERROR SEMÁNTICO: linea" +pdl.getNumLineas()+" el tipo es incorrecto, E() debe ser de tipo lógico ");
	    			  System.exit(1);//paro
	    			}
	    			
	    			t=S();
	    		
	    			
	    	 }else if(sig_token.equals("2")) { //for
	    		 parse+="7 ";
	    		 equipara("2");
	    		 equipara("19");		//(
	    		 W();
	    		 equipara("18");		//;
	    		 t=Y();
	    		 
	    		 /*if(!t.equals("logico")) {
	    		  * System.out.println("ERROR SEMÁNTICO: falla el tipo");
	    			 System.exit(1); 
	    		 }
	    		 */
	    		 
	    		 equipara("18");		//;
	    		 Z();
	    		 equipara("20");		//)
	    		 equipara("21");		//{
	    		 C();
	    		 equipara("22");		//}
	 
	    		 
	    	 }else if(sig_token.equals("14") || sig_token.equals("9") || sig_token.equals("8") || sig_token.equals("4")) {
	    		 //id /return/put /get
	    		 parse+="6 ";
	    		 t=S();
	    	 }else {
	    		 //ERROR
	    		 error(2);
	    		 System.exit(1);
	    	 }
	    	  //System.out.println("acabo B");
	    }

	    static void F() {
	    	String retorno="";
	    	//System.out.println("empiezo F");
	    	if (sig_token.equals("3")) { //function
	    		parse+="26 ";
	    		//creo la tabla de la funcion
	        	ts.crearTabla("3");
	        	ts.desplL=0;
	        	ts.nomF=true;
	        	equipara("3"); //function
	        	equipara("14"); //id
	        	ts.nomF=false;
	        	retorno=H();
	        	equipara("19"); //(
	        	ts.inicializado=true;
	        	A();
	        	ts.inicializado=false;
	        	equipara("20"); //)
	        	ts.insertarNumParam(numParam);
	        	ts.insertarTipoParam(parametros_funcion);
	        	if(retornos) {
	        	ts.insertarRetorno(retorno);
	        	retornos=false;
	        	}
	        	ts.insertarEtiqueta(etiqueta);
	        	equipara("21"); //{
	        	C();
	        	equipara("22"); //}
	        	if(tRetorno!=""&&!tRetorno.equals(retorno)) {
	        		System.out.println("ERROR SEMÁNTICO: linea"+ pdl.getNumLineas()+" el tipo de retorno no coincide");
	        		System.exit(1);
	        	}
	        
	        	ts.funcion=false;
	    	
	    	}else {
	    		//ERROR
	    		error(3);
	    		System.exit(1);
	    	}
	    	//System.out.println("acabo F");
	    }
	    static String T() {
	    	//System.out.println("empiezo T");
	    	String tipo= "";
	    	if (sig_token.equals("6")) { //int
	    		parse+="8 ";
	    		equipara("6");
	    		tipo=ts.tipo("6");
	    		
	    	}else if (sig_token.equals("1")) { //boolean
	    		parse+="9 ";
	    		equipara("1");
	    		tipo=ts.tipo("1");
	    		
	    		
	    	}else if (sig_token.equals("10")) { //string
	    		parse+="10 ";
	    		equipara("10");
	    		tipo=ts.tipo("10");
	    
	    		
	    	}else {
	    		//ERROR
	    		error(4);
	    		System.exit(1);
	    	}
	    	//System.out.println("acabo T");
		     return tipo;
	    }
	    static String E() {
	    	//System.out.println("empiezo E");
	    	String tipo="";
	    	if(sig_token.equals("14") || sig_token.equals("19") || sig_token.equals("12") || sig_token.equals("13")) { 
	    		//id / ( / entero / cadena
	    		parse+="33 ";
	    		t=R();  
	    		E1();
	    		
	    		tipo=t;
	   
	    	}else {
	    		//ERROR
	    		error(5);
	    		System.exit(1);
	    	}
	    	//System.out.println("acabo E");
	    	return tipo;
	    }
	    
	    static String S() {
	    	//System.out.println("empiezo S");
	    	String tipo="";
	    	if (sig_token.equals("14")) { //id
	    		parse+="11 ";
	    		tipo=ts.BuscaTipo(tablaEsta,posTs);
	    		equipara("14");
	    		t=S1();
	    		 //System.out.println(t);
	    		 //System.out.println(tipo);
	    		 
	    		if(t.equals(tipo)){
	    		 	tipo=t;
	    		 }else{
	    			System.out.println("ERROR SEMÁNTICO: linea" +pdl.getNumLineas() +" no coinciden los tipos");
	    		 	System.exit(1);
	    		 }
	    		
	    	}else if (sig_token.equals("9")) { //return
	    		parse+="12 ";
	    		retornos=true;
	    		equipara("9");
	    		t=X();
	    		equipara("18"); //;
	    		tipo=t;
	    		tRetorno=t;
	    		
	    	}else if (sig_token.equals("8")){  //put
	    		parse+="13 ";
	    		equipara("8"); //put
	    		t=E();
	    		if(!t.equals("entero")&& !t.equals("cadena")) {
	    			System.out.println("ERROR SEMÁNTICO: linea"+pdl.getNumLineas()+" el tipo debe ser entero o cadena y no lo es");
	    			System.exit(1);
	    		}
	    		equipara("18"); //;
	    		tipo="tipo_ok";
	    		
	    	}else if (sig_token.equals("4")) { //get
	    		parse+="14 ";
	    		equipara("4"); //get
	    		t=ts.BuscaTipo(tablaEsta,posTs);
	    		equipara("14"); //id
	    		equipara("18"); //;
	    	
	    			if(!t.equals("entero")&& !t.equals("cadena")){
	    				System.out.println("ERROR SEMÁNTICO: linea"+pdl.getNumLineas()+" el tipo debe ser entero o cadena y no lo es");
	    				System.exit(1);
	    			}else{
	    				tipo="tipo_ok";
	    			}
	    		
	    	}else {
	    		//ERROR
	    		error(6);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo S");
	    	return tipo;
	    }
	    static String W() {
	    	//System.out.println("empiezo W");
	    	String tipo="";
	    	if(sig_token.equals("14")) { //id
	    		parse+="47 ";
	    		t=ts.BuscaTipo(tablaEsta,posTs);
	    		equipara("14");//id
	    		equipara("15"); //=
	    		equipara("12"); //entero
	    	
	    		 if(!t.equals("entero")){
	    			System.out.println("ERROR SEMÁNTICO: linea"+pdl.getNumLineas()+" debe ser tipo entero");
	    		 	System.exit(1);
	    		 }else {
	    			 tipo="entero";
	    		 }
	    		
	    	}else if(sig_token.equals("18")){ //;
	    		parse+="46 ";
	    		
	    	}else if(!sig_token.equals("18")){ //!=;
	    		//ERROR
	    		error(7);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo W");
	    	return tipo;
	    }
	    static String Y() {
	    	//System.out.println("empiezo Y");
	    	String tipo="";
	    	if (sig_token.equals("14")) {//id
	    		parse+="48 ";
	    		t=ts.BuscaTipo(tablaEsta,posTs);
	    		equipara("14");//id
	    		equipara("25");//==
	    		equipara("12");//entero
	  
	    		 if(!t.equals("entero")){
	    			 System.out.println("ERROR SEMÁNTICO: linea" +pdl.getNumLineas() +" el id debe ser tipo entero");
	    		 	System.exit(1);
	    		 }else{
	    		 	tipo="logico";
	    		 }
	    	}else {
	    		//ERROR
	    		error(8);
	    		System.exit(1);
	    	}
	    	 //System.out.println("acabo Y");
	    	return tipo;
	    }
	    static String Z() {
	    	//System.out.println("empiezo Z");
	    	
	    	String tipo="";
	    	if (sig_token.equals("14")) { //id
	    		parse+="49 ";
	    		t=ts.BuscaTipo(tablaEsta,posTs);
	    		equipara("14");		//id
	    		equipara("15");		//=
	    		tipo=ts.BuscaTipo(tablaEsta,posTs);
	    		if(!t.equals(tipo)){
	    			System.out.println("ERROR SEMÁNTICO: linea" +pdl.getNumLineas()+" los tipos no coinciden");
	    		 	System.exit(1);
	    		 }
	    		equipara("14");		//id
	    		Z1();
	    		equipara("12"); // entero
	    		if(!t.equals("entero")){
	    			System.out.println("ERROR SEMÁNTICO: linea" + pdl.getNumLineas()+" el id debe ser tipo entero");
	    		 	System.exit(1);
	    		 }
	    	
	    	}else if (sig_token.equals("20")){ // )
	    		//caso lambda
	    		parse+="50 ";
	    		
	    	}else if (!(sig_token.equals("20"))){ // !=)
	    		//ERROR
	    		error(9);
	    		System.exit(1);
	    	}
	    	//System.out.println("acabo Z");
	    	return tipo;
		     
	    }
	    static void C() {
	    	 if (sig_token.equals("7") || sig_token.equals("5") || sig_token.equals("2") || sig_token.equals("14") || sig_token.equals("9") || sig_token.equals("8") || sig_token.equals("4")) {
	    		//let / if/ for/ id/ return /put/get
	    		 parse+="20 ";
	    		 if(sig_token.equals("return")) {
	    			 
	    		 }
	    		B();
	    		C();
	    	}else if(sig_token.equals("22")){ //}
			     //lambda
	    			parse+="21 ";
		    }else if(!sig_token.equals("22")){ //}
		     //ERROR
	    		error(10);
	    		System.exit(1);
	    	}
	    }
	    static void A() {
		    int pos=0;
		     if (sig_token.equals("6") || sig_token.equals("1") || sig_token.equals("10")) {
		    	 // int / boolean / string
		    	 	parse+="29 ";
		    		t=T();
		    		if(!t.equals("")) {
		    			parametros_funcion.add(t);
		    		}
		    		numParam++;
		    		pos=posTs;
		    		equipara("14"); //id
		    		ts.insertarTipo(tablaEsta,pos,t);
		    		despl=ts.desplazamiento(t);
		    		if(tablaEsta == ts.nG) { // si estamos en la tabla global-->añadimos desplG
		    			ts.desplG=ts.desplG+despl;
		    			ts.insertarDesplazamiento(tablaEsta, pos, ts.desplG);
		    		}else { // sino aumentamos el desplazamiento de la tabla local
		    			ts.desplL=ts.desplL+despl;
		    			ts.insertarDesplazamiento(tablaEsta, pos, ts.desplL);
		    		}
		    		ts.inicializado=true;
		    		K();
		    		ts.inicializado=false;
		    		
		     }else if(sig_token.equals("11")) { //void
		    	 	parse+="30 ";
		    	 	equipara("11");
		     }else {
		    	 //ERROR
		    	 error(11);
		    	 System.exit(1);
		     }
		    // System.out.println("acabo A");   
	    }
	    static void K() {
	    	int pos=0;
	    	 if (sig_token.equals("17")) { //,
	    		 	parse+="31 ";
	    		 	equipara("17"); 
		    		t=T();
		    		parametros_funcion.add(t);
		    		numParam++;
		    		pos=posTs;
		    		ts.insertarTipo(tablaEsta,pos,t);
		    		despl=ts.desplazamiento(t);
		    		if(tablaEsta == ts.nG) { // si estamos en la tabla global-->añadimos desplG
		    			ts.desplG=ts.desplG+despl;
		    			ts.insertarDesplazamiento(tablaEsta, pos, ts.desplG);
		    		}else { // sino aumentamos el desplazamiento de la tabla local
		    			ts.desplL=ts.desplL+despl;
		    			ts.insertarDesplazamiento(tablaEsta, pos, ts.desplL);
		    		}
		    		equipara("14");//id
		    		K();
		    		
	    	 }else if(sig_token.equals("20")) { //)
	    		//lambda
	    		 parse+="32 ";
	    	 }else if(!sig_token.equals("20")) { //!=)
	    		 //ERROR
	    		 error(12);
	    		 System.exit(1);
	    	 }
	    }
	    
	    static String  H() {
	    	//System.out.println("empiezo H");
	    	String tipo="";
	    	if (sig_token.equals("6") || sig_token.equals("1") || sig_token.equals("10")) {
	    		 // int / boolean / string
	    		parse+="27 ";
	    		tipo=T();
	    	}else if(sig_token.equals("11")) { //void
	    		parse+="28 ";
	    		equipara("11");
	    		tipo="void";
	    	}else{
	    		//ERROR
	    		error(13);
	    		System.exit(1);
	    	}
	    	//  System.out.println("acabo H");
		     return tipo;
	    }
	    static String R() {
	    	//System.out.println("empiezo R");
	    	String tipo="";
	    	if (sig_token.equals("14") || sig_token.equals("19") || sig_token.equals("12") || sig_token.equals("13")) {
	    		//id/(/entero/cadena
	    		parse+="36 ";
	    		t=V();
	    		tipo=t;
	    		t=R1();
	    		System.out.println("--" +t);
	    	}else {
	    		//ERROR
	    		error(14);
	    		System.exit(1);
	    	}
	    	//  System.out.println("acabo R");
	    	return tipo;
	    }
	    
	    static String E1() {
	    	//System.out.println("empiezo E1");
	    	String tipo="";
	    	String t2="";
	    	if (sig_token.equals("25") ) { //==
	    		parse+="34 ";
	    		equipara("25");
	    		t=R();
	    		t2=E1();
	    		//System.out.println("-->" +t);
	    		
	    		if(!t.equals("entero")) {
	    			System.out.println("ERROR SEMÁNTICO: linea"+pdl.getNumLineas()+" el tipo debe ser entero");
	    			System.exit(1);
	    		}else {
	    			tipo=t;
	    			//System.out.println(tipo);
	    		}
	    		
	    	}else if (sig_token.equals("18") || sig_token.equals("20") || sig_token.equals("17")) { //;,), ,
	    		// lambda
	    		parse+="35 ";
	    	}else if (!(sig_token.equals("18") || sig_token.equals("20") || sig_token.equals("17"))) {  //!= ;,), ,
	    		//ERROR
	    		error(15);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo E1");
	    	return tipo;
	    }

	    static String S1() {
	    	//System.out.println("empiezo S1");
	    	String tipo="";
	    	if (sig_token.equals("15") ) { //=
	    		//System.out.println("=");
	    		parse+="15 ";
	    		equipara("15");
	    		t=E();
	    		tipo=t;
	    		equipara("18"); //;
	    		
	    	}else if (sig_token.equals("19") ){ //(
	    		parse+="16 ";
	    		equipara("19");
	    		//t=L();
	    		equipara("20"); //)
	    		//tipo=t;
	    		
	    	}else if(sig_token.equals("16") ) { // |=
	    		parse+="17 ";
	    		equipara("16");
	    		t=E();
	    		tipo=t;
	    		equipara("18"); //;
	    		
	    	}else {
	    		//ERROR
	    		error(16);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo S1");
	    	return tipo;
	    }
	    static String X() {
	    	//System.out.println("empiezo X");
	    	String tipo="";
	    	if (sig_token.equals("14") || sig_token.equals("19") || sig_token.equals("12") || sig_token.equals("13")) {
	    		//id / ( / entero / cadena
	    		parse+="18 ";
		    	t=E();
		    	tipo=t;
	
		    	
	    	}else if(sig_token.equals("18")){ //lambda-->FOLLOW X
	    		parse+="19 ";
	    		
	    	}else if (!(sig_token.equals("18"))){ //!=;
	    		//ERROR
	    		error(17);
	    		System.exit(1);
	    	}
	    	//  System.out.println("acabo X");
		     return tipo;
	    }
	    static String L() {
	    	//System.out.println("empiezo L");
	    	String tipo="";
	    	String t2="";
	    	if (sig_token.equals("14") || sig_token.equals("19") || sig_token.equals("12") || sig_token.equals("13")) {
	    		//id / ( / entero / cadena
	    		parse+="22 ";
		    	t=E();
		    	Q();
		    	
	    	}else if(sig_token.equals("20")){//lambda
	    		parse+="23 ";
	 
	    	}else if(!(sig_token.equals("20"))){//)
	    		
	    		//ERROR
	    		error(18);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo L");
	    	return tipo;
	    }

	    static String V() { 
	    	//System.out.println("empiezo V");
	    	String tipo="";
	    	if (sig_token.equals("14")) { //id
	    		parse+="40 ";
	    		tipo=ts.BuscaTipo(tablaEsta,posTs);
	    		equipara("14");
	    		V1();
	    
	    	}else if(sig_token.equals("19")) { //(
	    		parse+="41 ";
	    		equipara("19");
	    		tipo=E();
	    		equipara("20");
	    	
	    	}else if(sig_token.equals("12")) { //entero
	    		parse+="42 ";
	    		equipara("12");
	    		tipo="entero";
	    		//System.out.println("paso por V,entero");
	    		
			}else if (sig_token.equals("13")) { //cadena
				parse+="43 ";
	    		equipara("13");
	    		tipo="cadena";
	    		
	    	}else {
	    		//ERROR
	    		error(19);
	    		System.exit(1);
	    	}
	    	//System.out.println("acabo V");
	    	return tipo;
	    }
	    
	    static String V1() { 
	    	//System.out.println("empiezo V1");
	    	String tipo="";
	    	if (sig_token.equals("19")) { // (
	    		parse+="44 ";
			   equipara("19");
			   t=L();
			   tipo=t;
			   equipara("20"); // )
			  
	    	}else if(sig_token.equals("23") || sig_token.equals("24") || sig_token.equals("25") || sig_token.equals("18")|| sig_token.equals("20") || sig_token.equals("17")){
	    		//lambda
	    		parse+="45 ";
	    		
	    	}else if(!(sig_token.equals("23") || sig_token.equals("24") || sig_token.equals("25") || sig_token.equals("18")|| sig_token.equals("20") || sig_token.equals("17"))){
	    		// != + / - / ==  / ; / ) /, 
	    		//ERROR
	    		error(20);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo V1");
	    	return tipo;
	    }
	    
	    static void Q() {
	    	String tipo="";
	    	String t2="";
	    	if (sig_token.equals("17")) { //,
	    		parse+="24 ";
	    		equipara("17");
	    		t=E();
	    		Q();
	    		
	    	}else if(sig_token.equals("20")){// )
	    		parse+="25 ";
	    	}else if(!(sig_token.equals("20"))){// )
	    		//ERROR
	    		error(21);
	    		System.exit(1);
	    	}
		     //return tipo;
	    }
	    
	    static String R1() {
	    	//System.out.println("empiezo R1");
	    	String tipo="";
	    	if (sig_token.equals("23")) { //+
	    		parse+="37 ";
	    		equipara("23");
	    		t=V();
	    		//System.out.println("-->" +t);
	    		if(!t.equals("entero")) {
	    			System.out.println("ERROR SEMÁNTICO: linea"+pdl.getNumLineas()+ " el tipo debe ser entero");
	    			System.exit(1);//paro
	    		}
	    		R1();
	    		tipo=t;
	    	}else if (sig_token.equals("24")) { //-
	    		parse+="38 ";
	    		equipara("24");
	    		t=V();
	    		if(!t.equals("entero")) {
	    			System.out.println("ERROR SEMÁNTICO: linea"+pdl.getNumLineas()+ " el tipo falla, debe ser entero");
	    			System.exit(1);//paro
	    		}
	    		R1();
	    		tipo=t;
	    		
	    	}else if(sig_token.equals("18") || sig_token.equals("20") || sig_token.equals("17") || sig_token.equals("25")) { // ; || ) || , ==
	    		
	    		parse+="39 ";
	    	
	    	}else if(!(sig_token.equals("18") || sig_token.equals("20") || sig_token.equals("17") || sig_token.equals("25"))) { // ; || ) || , ==
	    		//ERROR
	    		error(22);
	    		System.exit(1);
	    	}
	    	 // System.out.println("acabo R1");
	    	return tipo;
	    }
	    
	  /*
	    static void W1() {
	    	if( sig_token.equals("6")) {  //int
	    		parse+="49 ";
	    		equipara("6");
	    		
	    	}else if(sig_token.equals("14")) { //lambda
	    		
	    		parse+="48 ";
	    
	    	}else if(!sig_token.equals("14")) { //id
	    		//ERROR
	    		error(23);
	    	}
	    }
	    static void W2() {
	    	if(sig_token.equals("14")) { //id
	    		parse+="50 ";
	    		equipara("14");
	    		equipara("15"); //=
	    		equipara("12"); //entero
	    		
	    	}else {
	    		error(24);
	    	}
	    }
	    */
	 
	    static String Z1() {
	    	//System.out.println("empiezo Z1");
	    	String tipo="";
	    	if(sig_token.equals("23")) { //+
	    		parse+="52 ";
	    		equipara("23");
	    		
	    	}else if(sig_token.equals("24")) { //-
	    		parse+="53 ";
	    		equipara("24");
	    		
	    	}else {
	    		//ERROR
	    		error(25);
	    		System.exit(1);
	    	}
	    	// System.out.println("acabo Z1");
	    	return tipo;
	    }
	
	    static void equipara(String tokenEsperado) {
	    	Pair<String,Integer> izq=null;
	        if(tokenEsperado.equals(sig_token)) {
	           try {
	        	  
	            	if (!(sig_token.equals("-1"))) {	 
	            		//guardamos el token que se nos pasa
	                    token = pdl.analisis_lexicoo();
	                    if(token!=null && token.getLeft()!=null && token.getRigth()!=null) {
	                    //nos quedamos con el codigo-->primera parte
	                    	//sig_token=Integer.toString(token.getLeft());
	                    	sig_token = String.valueOf(token.getLeft());
	                    //nos quedamos con la posicion de la Tabla de Símbolos si es un identificador
	                  //nos quedamos con la posicion de la Tabla de Símbolos si es un identificador
	                   
	                    	if(sig_token.equals("14")) { 
	                    	izq=token.getRigth();
	                    	if(izq!=null) {
	                    		if(izq.getRigth()!=null) {
	                    		try {
	                    		ladoDer = String.valueOf(izq.getLeft());
	                    		tablaEsta=Integer.parseInt(ladoDer);
	                    		}catch (NumberFormatException e){
	                    			
	                    		}
	                    		posTs=izq.getRigth();
	                    		}
	                    	}
	                    }else {
	 	            	   posTs=0;
	 	               }
	                    }
	                n++;
	            	}
	               //System.out.println( n +"  token: "+ conversion(sig_token));
	     
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	        }/*else {
	            pdl.setError(true);
	            System.out.println("Error de sintaxis: " + " token: " + n + " Se esperaba " + conversion(tokenEsperado) + " pero se encontró " + conversion(sig_token));
	            System.exit(1); // Salir-->parar
	        }
	        */
	    }


	private static void error(int codigo) {
		int numLineas=pdl.getNumLineas();
		pdl.setError(true);
		switch(codigo) {
		
		case 1: //P
			System.out.println("-->Error sintactico en linea " + numLineas );
			System.out.println("Se esperaba: let / if / for / identificador / return/ put / get / function/ $ (fin de fichero) \n Se ha leido:  " + conversion(sig_token));break;
		case 2: //B
			System.out.println("-->Error sintactico en linea " + numLineas );
			System.out.println("Se esperaba: let / if / for \n Se ha leido:  " + conversion(sig_token));break;
		case 3: //F
			System.out.println("-->Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: function \n Se ha leido:  " + conversion(sig_token));break;
		case 4: //T
			System.out.println("-->Error sintactico en linea " + numLineas );
			System.out.println("Se esperaba: int/ boolean/ string \n Se ha leido:  " + conversion(sig_token));break;
		case 5: //E
			System.out.println("-->Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador / ( / entero / cadena \n Se ha leido:  " + conversion(sig_token));break;
		case 6: //S
			System.out.println("-->Error sintactico en linea " + numLineas );
			System.out.println("Se esperaba: identificador / return / put / get \n Se ha leido:  " + conversion(sig_token));break;
		case 7: //W
			System.out.println("-->Error sintactico en linea " + numLineas );
			System.out.println("Se esperaba: identificador/ ; \n Se ha leido:  " + conversion(sig_token));break;
		case 8: //Y
			System.out.println("-->Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador \n Se ha leido:  " + conversion(sig_token));break;
		case 9: //Z
			System.out.println("-->Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador / ) \n Se ha leido:  " + conversion(sig_token));break;
		case 10: //C
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: let / if / for / identificador / return/ put / get / } \n Se ha leido:  " + conversion(sig_token));break;
		case 11: //A
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: int/ boolean / string / void \n Se ha leido:  " + conversion(sig_token));break;
		case 12: //K
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: , / ) \n y llego " + conversion(sig_token));break;
		case 13: //H
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: int/ boolean / string / void \n Se ha leido:  " + conversion(sig_token));break;
		case 14: //R
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador / entero / cadena / ( \n Se ha leido:  " + conversion(sig_token));break;
		case 15: //E1
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: == / ; / ) / , \n Se ha leido:  " + conversion(sig_token));break;
		case 16: //S1
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: = / ( / |= \n Se ha leido:  " + conversion(sig_token));break;
		case 17: //X
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador / ( / entero / cadena / ; \n Se ha leido:  " + conversion(sig_token));break;
		case 18: //L
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador / ( / entero / cadena / ) \n Se ha leido:  " + conversion(sig_token));break;
		case 19: //V
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: identificador / ( / entero / cadena \n Se ha leido:  " + conversion(sig_token));break;
		case 20: //V1
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: + / - / ==  / ; / ) /, / ( \n Se ha leido:  " + conversion(sig_token));break;
		case 21: //Q
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: , / ) \n y llego " + conversion(sig_token));break;
		case 22: //R1
			System.out.println("--> Error sintactico en linea " + numLineas);
			System.out.println("Se esperaba: + / - / ; / , / ) / == \n Se ha leido: " + conversion(sig_token));break;
		/*case 23: //W1
			System.out.println("[!] Error sintactico en linea " + numLineas +  " case: " + codigo );
			System.out.println("Se esperaba: identificador / int \n Se ha leido: " + conversion(sig_token));break;
		case 24: //W2
			System.out.println("[!] Error sintactico en linea " + numLineas +  " case: " + codigo );
			System.out.println("Se esperaba: identificador \n Se ha leido: " + conversion(sig_token));break;
		*/
		case 23: // Z1
			System.out.println("--> Error sintactico en linea " + numLineas +  " case: " + res );
			System.out.println("Se esperaba: + / - / = \n Se ha leido: " + conversion(sig_token));break;
		
		}
	}
	
	 private static String conversion(String sig_token) {
		 int codigo=Integer.parseInt(sig_token);
		 switch(codigo) {
		 case 1: res="boolean";break;
		 case 2: res="for";break;
		 case 3: res="function";break;
		 case 4: res="get";break;
		 case 5: res="if";break;
		 case 6: res="int";break;
		 case 7: res="let";break;
		 case 8: res="put"; break;
		 case 9: res="return";break;
		 case 10: res="string";break;
		 case 11: res="void";break;
		 case 12: res="entero";break;
		 case 13: res="cadena";break;
		 case 14: res="identificador";break;
		 case 15: res="=";break;
		 case 16: res="|=";break;
		 case 17: res=",";break;
		 case 18: res=";";break;
		 case 19: res="(";break;
		 case 20: res=")";break;
		 case 21: res="{";break;
		 case 22: res="}";break;
		 case 23: res="+";break;
		 case 24: res="-";break;
		 case 25: res="==";break;
		 case -1: res="$";break;
	 
		 }
		 return res;
	 }
	/*private void parse(int regla) {
		parse+=Integer.toString(regla) + " ";
	}
	*/
}
