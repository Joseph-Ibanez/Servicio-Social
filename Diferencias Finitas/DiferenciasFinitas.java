import java.util.*; //Importamos librerías necesarias para la captura de datos del usuario 
import java.io.FileWriter; //Así como las necesarias para escribir sobre el archivo .dat
import java.io.IOException;
import java.io.FileReader;

public class DiferenciasFinitas{

	public static double umas1(double deltaphi, double u, double m, double umenos1){ //La forma de calcular la u_+1
		double umas_1 = Math.pow(deltaphi,2)*(3.*m*Math.pow(u,2)-u) + 2.*u - umenos1;
		return umas_1;
	}

	public static double PrimeraUmenos1(double deltaphi, double u, double m, double b2){ //Función para calcular u_-1
		// cuando tengamos a u_0.
		double a = 1.;
		double b = -2.*u;
		double c = (Math.pow(u,2.)-(Math.pow(deltaphi,2))*((1./b2)-(1-2.*m*u)*Math.pow(u,2)));
		double umenos_1 = (-b - Math.sqrt(Math.pow(b,2) - 4.*a*c))/2.*a;
		return umenos_1;	 

	}
	public static String LeerParametros(String parametro) throws IOException{
		Properties p = new Properties();
		p.load(new FileReader("/home/joseph/Documentos/Servicio Social/Código/Diferencias Finitas/ParametrosDifin.properties"));
		String ruta = p.getProperty(parametro);
		return ruta; 
	}

	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in); //Función necesaria para leer datos del usuario de la terminal.
		double m, x0, y0, r0, u0, phi0rad, b, phi0grad, phimax, deltaphi, xmin, ymin;
		double primerumenos1;
		int i = 0, puntos, contador =0;
		
		String x0S = LeerParametros("x0");
		String y0S = LeerParametros("y0");
		String xminS = LeerParametros("xmin");
		String yminS = LeerParametros("ymin"); 
		String ruta1 = LeerParametros("ruta1");
		String ruta2 = LeerParametros("ruta2");
		String ruta3 = LeerParametros("ruta3");	
		String ruta4 = LeerParametros("ruta4");
		String masaS = LeerParametros("m");
		String puntosS = LeerParametros("puntos");
		String phiMaxS = LeerParametros("phimax");
		String deltaphiS = LeerParametros("deltaphi");

		m = Double.parseDouble(masaS);
		x0 = Double.parseDouble(x0S);
		y0 = Double.parseDouble(y0S);
		xmin = Double.parseDouble(xminS);
		ymin = Double.parseDouble(yminS);
		puntos = Integer.parseInt(puntosS);
		r0 = Math.sqrt(Math.pow(x0,2)+Math.pow(y0,2)); //Definimos r0 a partir de los puntos en coordenadas cartesianas. 
		u0 = 1./r0; //Definimos el punto inicial del integrador
		b = y0; //La variable b^2.
		phi0rad = Math.asin(b/r0); //Java nos arroja resultados en radianes.
		phimax = Math.PI*Double.parseDouble(phiMaxS); // Agregar al archivo de parámetros.
		deltaphi = Math.abs((phimax-phi0rad))/puntos; //Definimos el tamaño de paso. 
		//deltaphi = Double.parseDouble(deltaphiS);


		double[] arreglophi = new double[puntos+1]; //Definimos los arreglos en los que vamos a guardar los puntos.
		double[] arreglou = new double[puntos+1];
		arreglophi[0] = phi0rad; //Definimos el punto inicial en phi.
		arreglou[0] = u0; //Definimos punto inicial en u. 
		primerumenos1 = PrimeraUmenos1(deltaphi, arreglou[0], m, Math.pow(b,2)); //Calculamos el punto fantasma. 
		arreglou[1] = umas1(deltaphi, arreglou[0], m, primerumenos1); //Definimos u_1 para poder aplicar la recursividad de manera adecuada. 

		for (i=2; i<puntos; i++) { //Ciclo for para calcular los puntos de u.
			arreglou[i] = umas1(deltaphi, arreglou[i-1], m, arreglou[i-2]);
		}
		for (i=1; i<puntos; i++) { //Ciclo for para calcular los puntos en phi. 
			arreglophi[i] = arreglophi[i-1] + deltaphi;
			}

		double[] arreglor = new double[puntos+1]; //Vamos a crear un arreglo con los valores de r. 
		for (i=0; i<puntos; i++) {
			arreglor[i] = 1./arreglou[i]; 
		}


		String[] rarreglo = new String[puntos]; //Convertimos los double a Strings para que el .dat se lea de manera adecuada. 
		for(i=0; i<puntos; i++){
			rarreglo[i] = String.valueOf(arreglor[i]);
		}


		double[] arreglox = new double[puntos+1]; //Definimos los arreglos X,Y para graficar en este dominio. 
		double[] arregloy = new double[puntos+1];
		for(i=0; i<puntos; i++){
			arreglox[i] = arreglor[i]*Math.cos(arreglophi[i]);
			arregloy[i] = arreglor[i]*Math.sin(arreglophi[i]);
		}
		for (i=0; i<puntos; i++) {
			if(arreglox[i]>=xmin && arreglox[i]<=x0+0.0001 && arregloy[i]>=ymin && arregloy[i]<=-ymin+0.0001 && Math.pow(arreglox[i],2)+Math.pow(arregloy[i],2)>Math.pow(2.*m,2)){
				arreglox[i] = arreglox[i];
				arregloy[i] = arregloy[i];
			}
			else{
				contador = i;
				Arrays.fill(arreglox,i,puntos,arreglox[i-1]);
				Arrays.fill(arregloy,i,puntos,arregloy[i-1]);
			}
		}
		double[] arregloX = new double[contador];
		double[] arregloY = new double[contador];
		for(i=0; i<contador; i++){
			arregloX[i] = arreglox[i];
			arregloY[i] = arregloy[i];
		}


		String[] arregloXS = new String[contador];
		String[] arregloYS = new String[contador];
		for(i=0; i<contador;i++){
			arregloXS[i] = String.valueOf(arregloX[i]);
			arregloYS[i] = String.valueOf(arregloY[i]);
		}
		



		String[] xarreglo = new String[puntos]; //Convertimos el arreglo X y el Y en arreglos de Strings. 
		String[] yarreglo = new String[puntos];
		for(i=0; i<puntos; i++){
			xarreglo[i] = String.valueOf(arreglox[i]);
			yarreglo[i] = String.valueOf(arregloy[i]);
		}



		String[] phiarreglo = new String[puntos];
		String[] uarreglo = new String[puntos];
		for(i=0; i<puntos; i++){
			phiarreglo[i] = String.valueOf(arreglophi[i]); //Esta función asigna un String a un valor double. 
		}
		for(i=0; i<puntos; i++){
			uarreglo[i] = String.valueOf(arreglou[i]);
		}

		FileWriter fichero = new FileWriter (ruta1);
    	for(int x=0;x<puntos;x++){
        	fichero.write(phiarreglo[x] + "	" + uarreglo[x] + "\n");

    	} 
    	fichero.close();
		

		FileWriter fichero2 = new FileWriter (ruta2);
    	for(int x=0;x<puntos;x++){
        	fichero2.write(phiarreglo[x] + "	" + rarreglo[x] + "\n");

    	} 
    	fichero2.close();
		
    	FileWriter fichero3 = new FileWriter (ruta3);
    	for(int x=0;x<puntos;x++){
    		fichero3.write(xarreglo[x] + "		" + yarreglo[x] + "\n");
    	}
    	fichero3.close();

    	FileWriter fichero4 = new FileWriter (ruta4);
    	for(int x=0;x<contador;x++){
    		fichero4.write(arregloXS[x] + "		" + arregloYS[x] + "\n");
    	}
    	fichero4.close();




		}


}