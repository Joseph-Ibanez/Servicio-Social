import java.util.Properties;
import java.util.Arrays;
import java.util.Scanner; //Importamos librerías necesarias para la captura de datos del usuario 
import java.io.FileWriter; //Así como las necesarias para escribir sobre el archivo .dat
import java.io.IOException;
import java.io.FileReader;

public class DiferenciasFinitasBonito{

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

	public static double[] RellenarU(int puntos, double u0, double deltaphi, double m, double primerumenos1){
		double[] arreglou = new double[puntos+1];
		int i = 0;
		arreglou[0] = u0;
		arreglou[1] = umas1(deltaphi, arreglou[0], m, primerumenos1);
		for (i=2; i<puntos; i++) { //Ciclo for para calcular los puntos de u.
			arreglou[i] = umas1(deltaphi, arreglou[i-1], m, arreglou[i-2]);
		}
		return arreglou;
	}

	public static double[] RellenarPhi(int puntos, double phi0rad, double deltaphi){
		double[] arreglophi = new double[puntos+1];
		int i = 0;
		arreglophi[0] = phi0rad;
		for (i=1; i<puntos; i++) { //Ciclo for para calcular los puntos en phi. 
			arreglophi[i] = arreglophi[i-1] + deltaphi;
			}
		return arreglophi;
	}

	public static double[] RellenarR(int puntos, double[] arreglou){
		double[] arreglor = new double[puntos+1];
		int i = 0;
		for (i=0; i<puntos; i++) {
			arreglor[i] = 1./arreglou[i]; 
		}
		return arreglor;
	}

	public static double[] Rellenarx(int puntos, double[] arreglor, double[] arreglophi){
		double[] arreglox = new double[puntos+1];
		int i = 0;
		for(i=0; i<puntos; i++){
			arreglox[i] = arreglor[i]*Math.cos(arreglophi[i]);
		}
		return arreglox;
	}

	public static double[] Rellenary(int puntos, double[] arreglor, double[] arreglophi){
		double[] arregloy = new double[puntos+1];
		int i = 0;
		for(i=0; i<puntos; i++){
			arregloy[i] = arreglor[i]*Math.sin(arreglophi[i]);
		}
		return arregloy;
	}

	public static String[] ArreglosDoubleAString(int puntos, double[] arregloDouble){
		String[] arregloString = new String[puntos];
		int i = 0;
		for(i=0; i<puntos; i++){
			arregloString[i] = String.valueOf(arregloDouble[i]);
		}
		return arregloString;
	}

	public static double[] ArreglosXYFinales(int contador, double[] arregloxoy){
		double[] arregloxoyfinales = new double[contador];
		int i = 0; 
		for(i=0; i<contador; i++){
			arregloxoyfinales[i] = arregloxoy[i];
		}
		return arregloxoyfinales;
	}

	public static FileWriter EscribirFicheros(int puntos, String ruta, String[] primerArreglo, String[] segundoArreglo) throws IOException{
		FileWriter fichero = new FileWriter(ruta);
    	for(int x=0;x<puntos;x++){
        	fichero.write(primerArreglo[x] + "	" + segundoArreglo[x] + "\n");

    	} 
    	fichero.close();
    	return fichero;
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


		double[] arreglophi = RellenarPhi(puntos, phi0rad, deltaphi);
		primerumenos1 = PrimeraUmenos1(deltaphi, u0, m, Math.pow(b,2)); //Calculamos el punto fantasma.
		double[] arreglou = RellenarU(puntos, u0, deltaphi, m, primerumenos1); 
		double[] arreglor = RellenarR(puntos, arreglou);

 
		String[] rarreglo = ArreglosDoubleAString(puntos, arreglor);


		double[] arreglox = Rellenarx(puntos, arreglor, arreglophi); //Definimos los arreglos X,Y para graficar en este dominio. 
		double[] arregloy = Rellenary(puntos, arreglor, arreglophi);
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
		double[] arregloX = ArreglosXYFinales(contador, arreglox);
		double[] arregloY = ArreglosXYFinales(contador, arregloy);


		String[] arregloXS = ArreglosDoubleAString(contador, arregloX);
		String[] arregloYS = ArreglosDoubleAString(contador, arregloY);
		

		String[] xarreglo = ArreglosDoubleAString(puntos, arreglox);
		String[] yarreglo = ArreglosDoubleAString(puntos, arregloy);


		String[] phiarreglo = ArreglosDoubleAString(puntos, arreglophi);
		String[] uarreglo = ArreglosDoubleAString(puntos, arreglou);
		
		FileWriter fichero = EscribirFicheros(puntos, ruta1, phiarreglo, uarreglo);
		FileWriter fichero2 = EscribirFicheros(puntos, ruta2, phiarreglo, rarreglo);
		FileWriter fichero3 = EscribirFicheros(puntos, ruta3, xarreglo, yarreglo);
		FileWriter fichero4 = EscribirFicheros(contador, ruta4, arregloXS, arregloYS);



		}


}