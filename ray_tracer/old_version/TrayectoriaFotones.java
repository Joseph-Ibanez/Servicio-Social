import java.util.*; //Importamos librerías necesarias para la captura de datos del usuario 
import java.io.FileWriter; //Así como las necesarias para escribir sobre el archivo .dat
import java.io.IOException;

public class TrayectoriaFotones{ //Iniciamos nuestra clase, el nombre de la clase debe tener el mismo nombre que 
						  //La del archivo .java

	public static double RHS(double lambda, double r, double dl, double num){ //Definimos la función con los parámetros
		double e_2 = 1.;
		double l_2 = 50.;
		double b_2 = l_2/e_2;
		double m = 1.;																 //los cuales son reales. 
		double r1 = r+(num*(dl/2.));
		double lambda1 = lambda + (dl/2.);
		//Haremos que los parámetros sean e=1 y l=1 para las primeras pruebas. 
		double res = Math.sqrt((l_2/b_2)-(l_2/Math.pow(r,2))+(l_2*2*m)/Math.pow(r,3));
		return res;
	}

	public static double YSiguiente(double r, double res1, double res2, double res3, double res4, double dl){
		//Definimos el método para calcular la ysiguiente de nuestra iteración. 
		double res5 = r + ((1./6.)*(res1 + 2.*res2 + 2.*res3 + res4)*dl);
		return res5;
	}

	public static double PotencialEfectivo(double m, double x){
		double pote = (1./Math.pow(m*x,2)) - (2.*m/Math.pow(m*x,3));
		return pote;
	}

	public static void main(String[] args) throws IOException{ //Iniciamos la secuencia principal del programa.
		double l0, r0, dl, lmax,masa; //Definimos todas las variables que vamos a necesitar.
		int puntos;
		double num, num1, num2, num3, num4,ysig;
		int i = 0;
		Scanner sc = new Scanner(System.in); //El objeto de tipo Scanner sirve para capturar los datos del usuario. 
		System.out.println("Introduce la masa para el potencial efectivo");
		masa = sc.nextDouble();
		System.out.println("Introduce los valores iniciales de la ecuación.");
		System.out.println("Valor inicial de lambda");
		l0 = sc.nextDouble(); //Aquí capturamos una variable real. 
		System.out.println("Valor máximo de lambda");
		lmax = sc.nextDouble();
		System.out.println("Valor inicial de r");
		r0 = sc.nextDouble();
		r0 = Math.abs(r0);
		System.out.println("Número de puntos");
		puntos = sc.nextInt();	 //Aquí se captura una variable entera.
		dl = Math.abs((lmax-l0))/puntos;
		System.out.println(dl);
		double[] ejex = new double[puntos+1]; //Definimos los arreglos en donde se van a guardar los puntos x y y.
		double[] ejey = new double[puntos+1];
		if (lmax < l0){
			while(lmax<=l0){ //La implementación del Runge Kutta de cuarto orden. 
			num = RHS(l0,r0,0,0);
			num2 = RHS(l0,r0,-dl,num);
			num3 = RHS(l0,r0,-dl,num2);
			num4 = RHS(l0,r0,2*-dl,num3);
			ysig = YSiguiente(r0,num,num2,num3,num4,-dl);
			r0 = ysig;
			ejey[i] = r0;
			l0 = l0 - dl;
			ejex[i] = l0;
			System.out.println("En la iteración "+ i + " 'r' es igual a " + r0);
			//System.out.println("El valor de 'x' en la iteración " + i + " es " + l0);
			i++;
		}
		}
		else{
		while(l0<=lmax){ //La implementación del Runge Kutta de cuarto orden. 
			num = RHS(l0,r0,0,0);
			num2 = RHS(l0,r0,dl,num);
			num3 = RHS(l0,r0,dl,num2);
			num4 = RHS(l0,r0,2*dl,num3);
			ysig = YSiguiente(r0,num,num2,num3,num4,dl);
			r0 = ysig;
			ejey[i] = r0;
			l0 = l0 +dl;
			ejex[i] = l0;
			System.out.println("En la iteración "+ i + " 'r' es igual a " + r0);
			//System.out.println("El valor de 'x' en la iteración " + i + " es " + l0);
			i++;
		}
		}	

		double[] potencialy = new double[1000];
		double[] potencialx = new double[1000];
		double contador = 0.01;
		for(i=0;i<1000;i++){
			potencialy[i] = PotencialEfectivo(masa,contador);
			potencialx[i] = contador;
			contador = contador+0.01;
		}


		//System.out.println(potencialx[100]);
		String[] ypotencial = new String[1001];
		String[] xpotencial = new String[1001];
		for(i=0; i<1000; i++){
			xpotencial[i] = String.valueOf(potencialx[i]); //Esta función asigna un String a un valor double. 
		}
		for(i=0; i<1000; i++){
			ypotencial[i] = String.valueOf(potencialy[i]);
		}


		String[] xeje = new String[puntos+1]; //Aquí definimos arreglos de tipo String para poder escribir los datos
		String[] yeje = new String[puntos+1]; //en el .dat.
		for(i=0; i<puntos+1; i++){
			xeje[i] = String.valueOf(ejex[i]); //Esta función asigna un String a un valor double. 
		}
		for(i=0; i<puntos+1; i++){
			yeje[i] = String.valueOf(ejey[i]);
		}


		double[] error = new double[puntos+1]; //Definimos el arreglo donde guardaremos el error. 
		for(i=0; i<puntos+1; i++){
			double pot = Math.pow(ejex[i],2);
			error[i] = Math.pow(Math.E, pot)- ejey[i];
		}

		String[] eRror = new String[puntos+1]; //Convertimos en un arreglo de String por lo mismo del .dat.
		for(i=0; i<puntos+1; i++){
			eRror[i] = String.valueOf(error[i]);
		}


		//Aquí vamos a escribir sobre el archivo .dat poniendo la ruta y el nombre del archivo que queremos.
		//El objeto tipo FileWriter es lo que nos permite escribir en el .dat.
		//Escribimos cada término del arreglo de Strings mediante el for.  
    	FileWriter fichero = new FileWriter ("/home/joseph/Documentos/Servicio Social/Código/fotones1.dat");
    	for(int x=0;x<puntos;x++){
        	fichero.write(xeje[x] + "	" + yeje[x] + "\n");

    	} 
    	fichero.close();


    	FileWriter fichero2 = new FileWriter ("/home/joseph/Documentos/Servicio Social/Código/errorfot1.dat");
    	for(int x=0;x<puntos; x++){
    		fichero2.write(xeje[x] + "	" + eRror[x] + "\n");

    	}
    	fichero2.close();

    	FileWriter fichero3 = new FileWriter ("/home/joseph/Documentos/Servicio Social/Código/potencial1.dat");
    	for(int x=0;x<1000;x++){
        	fichero3.write(xpotencial[x] + "	" + ypotencial[x] + "\n");

    	} 
    	fichero3.close();

	}
}