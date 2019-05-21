package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.opencsv.CSVReader;
import model.data_structures.ArregloDinamico;
import model.data_structures.BFS;
import model.data_structures.Bag;
import model.data_structures.DFS;
import model.data_structures.DijkstraDios;
import model.data_structures.DijkstraSP;
import model.data_structures.Edge;
import model.data_structures.Graph;
import model.data_structures.KruskalMST;
import model.data_structures.LinearProbing;
import model.data_structures.MaxHeapCP;
import model.data_structures.MinHeapCP;
import model.data_structures.PrimMST;
import model.data_structures.RedBlackBST;
import model.data_structures.SeparateChaining;
import model.data_structures.Stack;
import model.data_structures.Vertex;
import model.vo.VOMovingViolations;
import view.MovingViolationsManagerView;

public class Controller {

	// Componente vista (consola)
	private MovingViolationsManagerView view;

	private ArregloDinamico<Long> arregloIdsGrafo;

	//	private boolean empezo;
	//
	//	private boolean highWay;
	//
	//	private boolean repetido;
	//
	//	private ArregloDinamico<Long> nodos ;
	//hash del grafo
	private Graph<Long, String, Double> grafo;

	private Graph<Long, String, Double> grafoR2y9;

	private Graph<Long, String, Double> grafoR8;

	private Graph<Long,String,Double> grafo3;

	private ArregloDinamico<Vertex<Long,String,Double>> cuadricula;

	private MaxHeapCP<Vertex<Long, String, Double>> heap;

	public static final double R = 6372.8;
	/**
	 * Metodo constructor
	 */
	public Controller()
	{
		view = new MovingViolationsManagerView();
		grafo = new Graph<Long, String, Double>();	
		arregloIdsGrafo=new ArregloDinamico<>(3000);
		heap= new MaxHeapCP<>();
		grafoR2y9= new Graph<Long, String, Double>();
		grafo3= new Graph<>();
		grafoR8= new Graph<>();
	}
	/**
	 * Metodo encargado de ejecutar los  requerimientos segun la opcion indicada por el usuario
	 */
	public void run(){

		long startTime;
		long endTime;
		long duration;

		Scanner sc = new Scanner(System.in);
		boolean fin = false;


		while(!fin){
			view.printMenu();

			int option = sc.nextInt();
			long idVertice1 = 0;
			long idVertice2 = 0;


			switch(option){

			case 0:
				String RutaArchivoVertices = "";

				RutaArchivoVertices = "./data/finalGraph.json"; //TODO Dar la ruta del archivo de la ciudad completa
				startTime = System.currentTimeMillis();
				loadJSONVertices(RutaArchivoVertices);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				// TODO Informar el total de vértices y el total de arcos que definen el grafo cargado
				break;

			case 1:
				view.printMessage("Ingrese El id del primer vertice (Ej. 901839): ");
				idVertice1 = sc.nextLong();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				idVertice2 = sc.nextLong();

				grafoR2y9.crearTablas();
				startTime = System.currentTimeMillis();
				caminoCostoMinimoA1(idVertice1, idVertice2);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/* 
				TODO Consola: Mostrar el camino a seguir con sus vértices (Id, Ubicación Geográfica),
				el costo mínimo (menor cantidad de infracciones), y la distancia estimada (en Km).

				TODO Google Maps: Mostrar el camino resultante en Google Maps 
				(incluyendo la ubicación de inicio y la ubicación de destino).
				 */
				break;

			case 2:
				view.printMessage("2A. Consultar los N v�rtices con mayor n�mero de infracciones. Ingrese el valor de N: ");
				int n = sc.nextInt();


				startTime = System.currentTimeMillis();
				mayorNumeroVerticesA2(n);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/* 
				TODO Consola: Mostrar la informacion de los n vertices 
				(su identificador, su ubicación (latitud, longitud), y el total de infracciones) 
				Mostra el número de componentes conectadas (subgrafos) y los  identificadores de sus vertices 

				TODO Google Maps: Marcar la localización de los vértices resultantes en un mapa en
				Google Maps usando un color 1. Destacar la componente conectada más grande (con
				más vértices) usando un color 2. 
				 */
				break;

			case 3:			

				view.printMessage("Ingrese El id del primer vertice (Ej. 901839): ");
				idVertice1 = sc.nextInt();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				idVertice2 = sc.nextInt();

				startTime = System.currentTimeMillis();
				caminoLongitudMinimoaB1(idVertice1, idVertice2);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");

				/*
				   TODO Consola: Mostrar  el camino a seguir, informando
					el total de vértices, sus vértices (Id, Ubicación Geográfica) y la distancia estimada (en Km).

				   TODO Google Maps: Mostre el camino resultante en Google Maps (incluyendo la
					ubicación de inicio y la ubicación de destino).
				 */
				break;

			case 4:		
				double lonMin;
				double lonMax;
				view.printMessage("Ingrese la longitud minima (Ej. -87,806): ");
				lonMin = sc.nextDouble();
				view.printMessage("Ingrese la longitud m�xima (Ej. -87,806): ");
				lonMax = sc.nextDouble();

				view.printMessage("Ingrese la latitud minima (Ej. 44,806): ");
				double latMin = sc.nextDouble();
				view.printMessage("Ingrese la latitud m�xima (Ej. 44,806): ");
				double latMax = sc.nextDouble();

				view.printMessage("Ingrese el n�mero de columnas");
				int columnas = sc.nextInt();
				view.printMessage("Ingrese el n�mero de filas");
				int filas = sc.nextInt();


				startTime = System.currentTimeMillis();
				definirCuadriculaB2(lonMin,lonMax,latMin,latMax,columnas,filas);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar el número de vértices en el grafo
					resultado de la aproximación. Mostar el identificador y la ubicación geográfica de cada
					uno de estos vértices. 

				   TODO Google Maps: Marcar las ubicaciones de los vértices resultantes de la
					aproximación de la cuadrícula en Google Maps.
				 */
				break;

			case 5:

				startTime = System.currentTimeMillis();
				arbolMSTKruskalC1();
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar los vértices (identificadores), los arcos incluidos (Id vértice inicial e Id vértice
					final), y el costo total (distancia en Km) del árbol.

				   TODO Google Maps: Mostrar el árbol generado resultante en Google Maps: sus vértices y sus arcos.
				 */

				break;

			case 6:
				startTime = System.currentTimeMillis();
				arbolMSTPrimC2();
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar los vértices (identificadores), los arcos incluidos (Id vértice inicial e Id vértice
				 	final), y el costo total (distancia en Km) del árbol.

				   TODO Google Maps: Mostrar el árbol generado resultante en Google Maps: sus vértices y sus arcos.
				 */
				break;

			case 7:
				startTime = System.currentTimeMillis();
				caminoCostoMinimoDijkstraC3();
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar de cada camino resultante: su secuencia de vértices (identificadores) y su costo (distancia en Km).

				   TODO Google Maps: Mostrar los caminos de costo mínimo en Google Maps: sus vértices
					y sus arcos. Destaque el camino más largo (en distancia) usando un color diferente
				 */
				break;

			case 8:
				view.printMessage("Ingrese El id del primer vertice (Ej. 901839): ");
				idVertice1 = sc.nextInt();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				idVertice2 = sc.nextInt();

				startTime = System.currentTimeMillis();
				caminoMasCortoC4(idVertice1, idVertice2);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar del camino resultante: su secuencia de vértices (identificadores), 
				   el total de infracciones y la distancia calculada (en Km).

				   TODO Google Maps: Mostrar  el camino resultante en Google Maps: sus vértices y sus arcos.	  */
				break;

			case 9: 	
				fin = true;
				sc.close();
				break;
			}
		}
	}
	private ArregloDinamico<Long> arregloDinamicoGrafo() 
	{
		Iterator<Long> it = grafo.getV().keys();
		ArregloDinamico<Long> vertices = new ArregloDinamico<>(3000);
		while(it.hasNext())
		{
			Long i = it.next();
			vertices.agregar(i);
		}
		return vertices;
	}

	private void cargarInfracciones(int numeroSemestre) {
		//		sem1 = new String[6];
		//		sem2 = new String[6];
		//
		//		int enero= 0;
		//		int f=0;
		//		int a =0;
		//		int m =0;
		//		int mayo =0;
		//		int j =0;
		//		int ju =0;
		//		int ag = 0;
		//		int s =0;
		//		int o =0;
		//		int n = 0;
		//		int d = 0;
		//		int totSem = 0;
		//		int contMes=0;
		//		for(int i = 0; i<6;i++){
		//			if(i == 0){
		//				sem1[i] = rutaEnero;
		//				sem2[i] = rutaJulio;
		//			}
		//			else if(i == 1){
		//				sem1[i] = rutaFebrero;
		//				sem2[i] = rutaAgosto;
		//			}
		//			else if(i == 2){
		//				sem1[i] = rutaMarzo;
		//				sem2[i] = rutaSeptiembre;
		//			}
		//			else if(i == 3){
		//				sem1[i] = rutaAbril;
		//				sem2[i] = rutaOctubre;
		//			}
		//			else if(i == 4){
		//				sem1[i] = rutaMayo;
		//				sem2[i] = rutaNoviembre;
		//			}
		//			else if(i == 5){
		//				sem1[i] = rutaJunio;
		//				sem2[i] = rutaDiciembre;
		//			}
		//		}
		//		try{
		//			if(numeroSemestre ==2){
		//				for(int i = 0;i<sem2.length;i++){
		//					contMes = 0;
		//					String mes = sem2[i];
		//					int latitud = -1;
		//					int longitud = -1;
		//
		//					if(i==10 || i==11 || i==12) {
		//						latitud = 19;
		//						longitud = 20; 
		//					}
		//					else {
		//						latitud = 18;
		//						longitud = 19;
		//					}
		//					CSVReader lector = new CSVReader(new FileReader(mes), ';');
		//					String[] linea = lector.readNext();
		//
		//					while ((linea = lector.readNext()) != null) 
		//					{
		//						String obID = linea[0];
		//						String lat = linea[latitud];
		//						String lon = linea[longitud];
		//						VOMovingViolations vo = new VOMovingViolations(obID,lat, lon);
		//						arreglo.agregar(vo);
		//						totSem++;
		//						contMes++;
		//						if(i == 0){
		//							ju=contMes;
		//
		//						}
		//						else if(i == 1){
		//							ag=contMes;
		//						}
		//						else if(i == 2){
		//							s=contMes;
		//
		//						}
		//						else if(i == 3){
		//							o=contMes;
		//
		//						}
		//						else if(i == 4){
		//							n=contMes;
		//
		//						}
		//						else if(i == 5){
		//							d=contMes;
		//
		//						}
		//					}
		//
		//					lector.close();
		//
		//				}
		//				System.out.println("Total de infracciones del semestre " + totSem);
		//				System.out.println("Infracciones de: ");
		//				System.out.println("Julio " + ju);
		//				System.out.println("Agosto " + ag);
		//				System.out.println("Septiembre " + s);
		//				System.out.println("Octubre" + o);
		//				System.out.println("Noviembre " + n);
		//				System.out.println("Diciembre " + d);
		//			}
		//
		//			else{
		//				for(int i = 0;i<sem1.length;i++){
		//					contMes = 0;
		//					String mes = sem1[i];
		//					int latitud = -1;
		//					int longitud = -1;
		//
		//					if(i==0) {
		//						latitud = 17;
		//						longitud = 18; 
		//					}
		//					else {
		//						latitud = 18;
		//						longitud = 19;
		//					}
		//					CSVReader lector = new CSVReader(new FileReader(mes), ';');
		//
		//					String[] linea = lector.readNext();
		//					while ((linea = lector.readNext()) != null) {
		//
		//						String obID = linea[1];
		//						String lat = linea[latitud];
		//						String lon = linea[longitud];
		//						VOMovingViolations vo = new VOMovingViolations(obID,lat, lon);
		//						arreglo.agregar(vo);	
		//						totSem++;
		//						contMes++;
		//						if(i == 0){
		//							enero=contMes;
		//						}
		//						else if(i == 1){
		//							f=contMes;
		//						}
		//						else if(i == 2){
		//							m=contMes;
		//
		//						}
		//						else if(i == 3){
		//							a=contMes;
		//
		//						}
		//						else if(i == 4){
		//							mayo=contMes;
		//
		//						}
		//						else if(i == 5){
		//							j=contMes;
		//
		//						}
		//					}
		//					lector.close();
		//				}
		//				System.out.println("Total de infracciones del semestre " + totSem);
		//				System.out.println("Infracciones de: ");
		//				System.out.println("Enero " + enero);
		//				System.out.println("Febrero " + f);
		//				System.out.println("Mazro " + m);
		//				System.out.println("Abril " + a);
		//				System.out.println("Mayo " + mayo);
		//				System.out.println("Junio " + j);
		//			}
		//		}
		//		catch (IOException e) {
		//
		//			e.printStackTrace();
		//		}
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia


	/**
	 * Cargar el Grafo No Dirigido de la malla vial: Downtown o Ciudad Completa
	 * @param rutaArchivo 
	 */

	public void loadJSONVertices(String rutaArchivo) 
	{
		try {

			JSONParser parser = new JSONParser();
			JSONArray a = (JSONArray) parser.parse(new FileReader(rutaArchivo));
			int cantidadInfracciones=0;
			for (Object o : a)
			{
				JSONObject actual = (JSONObject) o;
				String id=  (String) actual.get("id");
				Object lat = actual.get("lat");
				String lon=(String) actual.get("lon").toString();

				JSONArray ja= (JSONArray) actual.get("infractions");
				Iterator <String> it = ja.iterator();
				ArregloDinamico<String> ar = new ArregloDinamico<>(6);
				while (it.hasNext())
				{
					String i = it.next();
					ar.agregar(i);
				}
				cantidadInfracciones+=ar.darTamano();

				JSONArray ja2= (JSONArray) actual.get("adj");
				Iterator <String> it2 = ja2.iterator();
				Bag<Long> ar2 = new Bag<>();
				while (it2.hasNext())
				{
					String i = it2.next();
					ar2.add(Long.parseLong(i));
				}
				grafo.addVertex(Long.parseLong(id), lat+"|"+lon,ar,ar2);

				grafoR2y9.addVertex(Long.parseLong(id), lat+"|"+lon,ar,ar2);

				grafoR8.addVertex(Long.parseLong(id), lat+"|"+lon,ar,ar2);

				for(Long adj:ar2)
				{
					grafo.addEdge(Long.parseLong(id), adj, 0.0);

					Vertex<Long, String, Double> inicio = grafoR2y9.getV().get(Long.parseLong(id));
					grafoR2y9.addDirectedEdge(Long.parseLong(id), adj, inicio.getCantidadInfracciones());

					grafoR8.addDirectedEdge(Long.parseLong(id), adj, 0.0);

				}
				heap.agregar(new Vertex<Long, String, Double>(Long.parseLong(id), lat+"|"+lon, ar,ar2));
				arregloIdsGrafo.agregar(Long.parseLong(id));
			}
			SeparateChaining<Long, ArregloDinamico<Edge<Long, String, Double>>> ed = grafo.getEdges();
			Iterable<Long> ittt = ed.keys();
			Iterator<Long> it2 = ittt.iterator();
			while(it2.hasNext())
			{
				Long t=it2.next();//indice vertice inicio
				Vertex<Long, String, Double> r = grafo.getV().get(t);
				ArregloDinamico<Edge<Long, String, Double>> ver = ed.get(t);
				for(int i=0;i<ver.darTamano();i++)
				{
					Edge<Long, String, Double> e = ver.darElem(i);
					Long idFin=e.getEndVertexId();
					Vertex<Long, String, Double> r2 = grafo.getV().get(idFin);
					double peso= haversine(Double.parseDouble(r.getLatitud()), Double.parseDouble(r.getLongitud()), Double.parseDouble(r2.getLatitud()), Double.parseDouble(r2.getLongitud()));
					grafo.setInfoEdge(t, idFin, peso);
					grafoR8.setInfoEdge(t, idFin, peso);
				}
			}
			System.out.println("Se cargaron "+cantidadInfracciones+" infracciones.");
			System.out.println("Vertices cargados "+grafo.V());
			System.out.println("Arcos cargados "+grafo.E());
			System.out.println("Grafo Dirigido tiene "+grafoR2y9.V()+" vertices y "+grafoR2y9.E()+" arcos.");
			System.out.println("Grafo Dirigido 8 tiene "+grafoR8.V()+" vertices y "+grafoR8.E()+" arcos.");
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1A: Encontrar el camino de costo m�nimo para un viaje entre dos ubicaciones geogr�ficas.
	 * @param idVertice2 
	 * @param idVertice1 
	 * @return 
	 */
	public void caminoCostoMinimoA1(long idVertice1, long idVertice2)
	{
		DijkstraSP d= new DijkstraSP(grafoR2y9, idVertice1);
		Iterable<Edge<Long, String, Double>> p = d.pathTo(grafoR2y9.getIndiceT().get(idVertice2));
		Iterator<Edge<Long, String, Double>> i = p.iterator();
		long id2=0;
		String lon2="";
		String lat2="";
		int infras=0;
		double kms=0;
		while(i.hasNext())
		{
			Edge<Long,String,Double> e = i.next();
			long id = e.getStartVertexId();
			String lon=grafo.getV().get((long)e.getStartVertexId()).getLongitud();
			String lat=grafo.getV().get((long)e.getStartVertexId()).getLatitud();
			infras+=grafo.getV().get((long)e.getStartVertexId()).getCantidadInfracciones();
			id2 = e.getEndVertexId();
			lon2=grafo.getV().get((long)e.getEndVertexId()).getLongitud();
			lat2=grafo.getV().get((long)e.getEndVertexId()).getLatitud();
			kms += grafo.getInfoEdge(id, id2);

			System.out.println("Vertice con id "+id+ " con latitud "+lat+" con longitud "+lon);
		}
		System.out.println("Vertice con id "+id2+ " con latitud "+lat2+" con longitud "+lon2);
		System.out.println("Total de infracciones en el camino: "+infras);
		System.out.println("Cantidad aproximada de kms: "+kms);
		MapBoxDraw map2 = new MapBoxDraw("./docs/req1.html", p, grafoR2y9);
		map2.drawIterable();
	}

	/**
	 * Requerimiento 2A: Determinar los n v�rtices con mayor n�mero de infracciones. Adicionalmente identificar las
	 * componentes conectadas (subgrafos) que se definan �nicamente entre estos n v�rtices
	 * @param  int n: numero de vertices con mayor numero de infracciones  
	 */
	public void mayorNumeroVerticesA2(int n) {
		
//		ArregloDinamico<Vertex<Long, String, Double>> mayores= new ArregloDinamico<>(20);
//		for(int i=0;i<n;i++)
//		{
//			Vertex<Long, String, Double> v= heap.delMax();
//			mayores.agregar(v);
//			System.out.println(v.darInfoVertice());
//		}
//		Graph<Long,String,Double> grafo1= new Graph<>();
//		for (int i = 0; i < mayores.darTamano(); i++)
//		{
//			Vertex<Long, String, Double> v = mayores.darElem(i);
//			grafo1.addVertex(v.getId(), v.getLatitud()+"|"+v.getLongitud(), v.getInfracciones(),v.getIds());
//		}
//		LinearProbing<Long, Vertex<Long,String,Double>> lin= grafo1.getV();
//		LinearProbing<Long, Vertex<Long,String,Double>> linGrande= grafo.getV();
//		Iterator <Long> it = linGrande.keys();
//		while(it.hasNext())
//		{
//			Long i= it.next();
//			if(lin.get(i)!=null)
//			{
//				Vertex<Long, String, Double> vertice=linGrande.get(i);
//				Bag<Long> adjs= vertice.getIds();
//				for(Long a: adjs)
//				{
//					if(lin.get(a)!=null)
//					{
//						Vertex<Long, String, Double> verticeFin=lin.get(a);
//						double peso= haversine(Double.parseDouble(vertice.getLatitud()), Double.parseDouble(vertice.getLongitud()), Double.parseDouble(verticeFin.getLatitud()), Double.parseDouble(verticeFin.getLongitud()));
//						grafo1.addEdge(vertice.getId(), verticeFin.getId(), peso);
//					}
//				}
//			}
//		}
//		LinearProbing<Long, Boolean> marked = new LinearProbing<Long, Boolean>(grafo1.V());
//		Iterator<Long> itt = lin.keys();
//		while(itt.hasNext())
//		{
//			Long i = itt.next();
//			marked.put(i, false);
//		}
//		Iterator<Long> ite= marked.keys();
//		int componentes = 0;
//		MaxHeapCP<Bag<Long>> h= new MaxHeapCP<Bag<Long>>();
//		while(ite.hasNext())
//		{
//			Long a = ite.next();
//			if(marked.get(a)==false)
//			{
//				componentes++;
//				DFS<Long,String,Double> d= new DFS<Long, String, Double>(grafo1, a, marked);
//				marked= d.darMarcados();
//				h.agregar(d.darBagDeVertices());
//			}
//		}
//		Bag<Long> max = h.delMax();
//		for(Long d: max)
//		{
//			String lat=linGrande.get(d).getLatitud();
//			String lon=linGrande.get(d).getLongitud();
//			ArregloDinamico<String> arr = linGrande.get(d).getInfracciones();
//			Bag<Long> b = linGrande.get(d).getIds();
//			grafo3.addVertex(d, lat+"|"+lon, arr, b);
//		}
//		LinearProbing<Long, Vertex<Long, String, Double>> ie = grafo3.getV();
//		Iterator<Long> i = ie.keys();
//		while(i.hasNext())
//		{
//			Long a = i.next();
//			Vertex<Long, String, Double> ver = ie.get(a);
//			Bag<Long> ba = ver.getIds();
//			for(long b:ba)
//			{
//				if(ie.get(b)!=null)
//				{
//					Vertex<Long, String, Double> ver2 = ie.get(b);
//					double peso= haversine(Double.parseDouble(ver.getLatitud()), Double.parseDouble(ver.getLongitud()), Double.parseDouble(ver2.getLatitud()), Double.parseDouble(ver2.getLongitud()));
//					grafo3.addEdge(a, b, peso);
//				}
//			}
//		}
//		System.out.println("Vertices "+grafo3.V());
//		System.out.println("Arcos "+grafo3.E());
//		//en el bag estan los vertices del componente conexo mayor
//		System.out.println("Numeros de componentes: "+componentes);
//		
		
		RedBlackBST<Long, Long> r= new RedBlackBST<>();
		grafo3= new Graph<>();
		ArregloDinamico<Vertex<Long, String, Double>> mayores= new ArregloDinamico<>(20);
		Graph<Long,String,Double> grafo1= new Graph<>();
		for(int i=0;i<n;i++)
		{
			Vertex<Long, String, Double> v= heap.delMax();
			mayores.agregar(v);
			System.out.println(v.darInfoVertice());
			r.put(v.getId(), v.getId());
			grafo1.addVertex(v.getId(), v.getLatitud()+"|"+v.getLongitud(), v.getInfracciones(),v.getIds());
		}
		LinearProbing<Long, Vertex<Long,String,Double>> lin= grafo1.getV();
		LinearProbing<Long, Vertex<Long,String,Double>> linGrande= grafo.getV();
		for(int i=0;i<mayores.darTamano()-1;i++)
		{
			Vertex<Long, String, Double> v1 = mayores.darElem(i);
			int j=i+1;
			while(j<=mayores.darTamano()-1)
			{
				Vertex<Long, String, Double> v2 = mayores.darElem(j);
				BFS<Long, String, Double> d= new BFS<>(grafo, v1.getId());
				if(d.hasPathTo(v2.getId()))
				{
					Iterable<Long> e = d.pathTo(v2.getId());
					Iterator<Long> ite = e.iterator();
					while(ite.hasNext())
					{
						Long v = ite.next();
						if(r.contains(v)==false)
						{
							String la=linGrande.get(v).getLatitud();
							String lo= linGrande.get(v).getLongitud();
							ArregloDinamico<String> inf= linGrande.get(v).getInfracciones();
							grafo1.addVertex(v,la+"|"+lo , inf, linGrande.get(v).getIds());
						}
						r.put(v, v);
					}
				}
				j++;
			}		
		}
		LinearProbing<Long, Boolean> marked = new LinearProbing<Long, Boolean>(grafo1.V());
		Iterator<Long> a = lin.keys();
		while(a.hasNext())
		{
			Long r1 = a.next();
			marked.put(r1, false);
			Vertex<Long, String, Double> ve = lin.get(r1);
			Bag<Long> ad = ve.getIds();
			for(long q:ad)
			{
				if(lin.get(q)!=null)
				{
					grafo1.addEdge(r1, q, grafo.getInfoEdge(r1, q));
				}
			}
		}
//		System.out.println(grafo1.V());
//		System.out.println(grafo1.E());

		Iterator<Long> ite= marked.keys();
		int componentes = 0;
		MaxHeapCP<Bag<Long>> h= new MaxHeapCP<Bag<Long>>();
		while(ite.hasNext())
		{
			Long a1 = ite.next();
			if(marked.get(a1)==false)
			{
				componentes++;
				DFS<Long,String,Double> d= new DFS<Long, String, Double>(grafo1, a1, marked);
				marked= d.darMarcados();
				h.agregar(d.darBagDeVertices());
			}
		}
		int c=0;
		while(h.darNumElementos()>0)
		{
			if(c==0)
			{
				Bag<Long> max = h.delMax();
				System.out.println("Componente conexa numero "+(c+1));
				for(Long d: max)
				{
					System.out.println(d);
					String lat=linGrande.get(d).getLatitud();
					String lon=linGrande.get(d).getLongitud();
					ArregloDinamico<String> arr = linGrande.get(d).getInfracciones();
					Bag<Long> b = linGrande.get(d).getIds();
					grafo3.addVertex(d, lat+"|"+lon, arr, b);
				}
			}
			else
			{
				Bag<Long> m = h.delMax();
				System.out.println("Componente conexa numero "+(c+1));
				for(long id:m)
				{
					System.out.println(id);
				}

			}
			c++;

		}
		LinearProbing<Long, Vertex<Long, String, Double>> ie = grafo3.getV();
		Iterator<Long> i = ie.keys();
		Stack<Edge<Long,String,Double>> stackIterable = new Stack<Edge<Long,String,Double>>();
		while(i.hasNext())
		{
			Long a1 = i.next();
			Vertex<Long, String, Double> ver = ie.get(a1);
			Bag<Long> ba = ver.getIds();
			for(long b:ba)
			{
				if(ie.get(b)!=null)
				{
					Vertex<Long, String, Double> ver2 = ie.get(b);
					double peso= haversine(Double.parseDouble(ver.getLatitud()), Double.parseDouble(ver.getLongitud()), Double.parseDouble(ver2.getLatitud()), Double.parseDouble(ver2.getLongitud()));
					grafo3.addEdge(a1, b, peso);
					stackIterable.push(new Edge<Long, String, Double>(a1, b, peso));
				}
			}
		}
		System.out.println("tam stack" + stackIterable.size());
		System.out.println("Vertices "+grafo3.V());
		System.out.println("Arcos "+grafo3.E());
		System.out.println("Numeros de componentes: "+componentes);
		MapBoxDraw map3 = new MapBoxDraw("./docs/req3.html", mayores, grafo3,stackIterable);
		map3.drawVerticesGraph();
	}

	public  double haversine(double lat1, double lon1, double lat2, double lon2) {

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1B: Encontrar el camino m�s corto para un viaje entre dos ubicaciones geogr�ficas 
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	public void caminoLongitudMinimoaB1(long idVertice1, long idVertice2) {
		BFS b = new BFS<>(grafo, idVertice1);
		Iterable r = b.pathTo(idVertice2);
		int c=0;
		ArregloDinamico<Long> v= new ArregloDinamico<>(30);
		for (Object object : r) {
			System.out.println(grafo.getV().get((long)object).darInfoVertice());
			v.agregar((long)object);
			c++;
		}
		System.out.println("Cantidad total de vertices: "+c);
		double kms=0;
		for(int i=0;i<v.darTamano()-1;i++)
		{
			Vertex<Long, String, Double> v1 = grafo.getV().get(v.darElem(i));
			Vertex<Long, String, Double> v2 = grafo.getV().get(v.darElem(i+1));
			kms+=haversine(Double.parseDouble(v1.getLatitud()), Double.parseDouble(v1.getLongitud()), Double.parseDouble(v2.getLatitud()), Double.parseDouble(v2.getLongitud()));
		}
		System.out.println("Total de kms: "+kms);
		MapBoxDrawV map4 = new MapBoxDrawV("./docs/req4.html", r, grafo);
		map4.draw();
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 2B:  Definir una cuadricula regular de N columnas por M filas. que incluya las longitudes y latitudes dadas
	 * @param  lonMin: Longitud minima presente dentro de la cuadricula
	 * @param  lonMax: Longitud maxima presente dentro de la cuadricula
	 * @param  latMin: Latitud minima presente dentro de la cuadricula
	 * @param  latMax: Latitud maxima presente dentro de la cuadricula
	 * @param  columnas: Numero de columnas de la cuadricula
	 * @param  filas: Numero de filas de la cuadricula
	 */
	public void definirCuadriculaB2(double lonMin, double lonMax, double latMin, double latMax, int columnas,
			int filas) {
		// TODO Auto-generated method stub
		//abajoIzq=lat y long min
		//abajoDer= lat max, lon min
		//arribaIzq=lat min, lon max
		//arribaDer=lat y lon max

		double  difLat= Math.abs(latMax-latMin);
		double difLon= Math.abs(lonMax-lonMin);
		double avancesLon=difLon/(filas-1);
		double avancesLat=difLat/(columnas-1);
		//		double avancesLon=difLon/(filas);
		//		double avancesLat=difLat/(columnas);
		ArregloDinamico<String> puntos= new ArregloDinamico<>(200);
		double lat=latMin;
		while(lat<=latMax)
		{
			double lon=lonMin;
			while(lon<=lonMax)
			{
				puntos.agregar(lat+"|"+lon);
				lon+=avancesLon;
			}
			lat+=avancesLat;
		}
		LinearProbing<Long, Vertex<Long,String, Double>> lista= grafo.getV();
		ArregloDinamico<Vertex<Long, String, Double>> cercanos=new ArregloDinamico<>(20);
		LinearProbing<Double, Long> distanciaVertice=new LinearProbing<>(300);
		for(int i=0;i<puntos.darTamano();i++)
		{
			String p = puntos.darElem(i);
			int index=p.indexOf("|");
			String la=p.substring(0, index);
			int index2=index+1;
			String lo=  p.substring(index2);
			double laPunto=Double.parseDouble(la);
			double loPunto=Double.parseDouble(lo);
			Iterator<Long> it = lista.keys();
			MinHeapCP<Double> heaps=new MinHeapCP<>();
			while(it.hasNext())
			{
				Long is = it.next();
				Vertex<Long, String, Double> v = lista.get(is);
				double l1 = Double.parseDouble(v.getLatitud());
				double l2 = Double.parseDouble(v.getLongitud());
				if(l1<=latMax && l1>=latMin && l2<=lonMax && l2>=lonMin)
				{

					double n= haversine(laPunto, loPunto, l1, l2);
					heaps.agregar(n);
					distanciaVertice.put(n, v.getId());


				}
			}
			double c = heaps.delMax();
			Long id=distanciaVertice.get(c);
			Vertex<Long, String, Double> v = lista.get(id);
			if(cercanos.contains(v)==false)
				cercanos.agregar(v);
		}
		System.out.println("Vertices cercanos "+cercanos.darTamano());
		cuadricula=cercanos;

		for(int i=0;i<cercanos.darTamano();i++)
		{
			System.out.println(cercanos.darElem(i).darInfoVertice());
		}
		MapBoxDraw map5 = new MapBoxDraw("./docs/req5.html", cuadricula);
		map5.drawVertices();
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1C:  Calcular un �rbol de expansi�n m�nima (MST) con criterio distancia, utilizando el algoritmo de Kruskal.
	 */
	public void arbolMSTKruskalC1() {
		grafo3.crearTablas();
		KruskalMST k= new KruskalMST(grafo3);
		Iterable<Edge<Long, String, Double>> edgs = k.edges();
		Iterator<Edge<Long, String, Double>> it = edgs.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
		System.out.println("El peso total del arbol es de "+k.weight());
		MapBoxDraw map6 = new MapBoxDraw("./docs/req6.html", edgs,grafo3);
		map6.drawIterable();
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 2C: Calcular un �rbol de expansi�n m�nima (MST) con criterio distancia, utilizando el algoritmo de Prim. (REQ 2C)
	 */
	public void arbolMSTPrimC2() {
		// TODO Auto-generated method stub
		grafo3.crearTablas();
		PrimMST p=new PrimMST(grafo3);
		Iterable<Edge<Long,String,Double>> pa=p.edges();
		Iterator<Edge<Long, String, Double>> i = pa.iterator();
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
		System.out.println("El peso total del arbol es de "+p.weight());
		MapBoxDraw map7 = new MapBoxDraw("./docs/req7.html", pa,grafo3);
		map7.drawIterable();
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 3C: Calcular los caminos de costo m�nimo con criterio distancia que conecten los v�rtices resultado
	 * de la aproximaci�n de las ubicaciones de la cuadricula N x M encontrados en el punto 5.
	 */
	public void caminoCostoMinimoDijkstraC3() 
	{
		// TODO Auto-generated method stub
		grafoR8.crearTablas();
		Vertex<Long, String, Double> e = cuadricula.darElem(0);
		DijkstraSP d= new DijkstraSP(grafoR8, e.getId());

		for(int i=1;i<cuadricula.darTamano();i++)
		{
			Iterable<Edge<Long, String, Double>> ed = d.pathTo(grafoR8.getIndiceT().get(cuadricula.darElem(i).getId()));
			Iterator<Edge<Long, String, Double>> it = ed.iterator();
			MapBoxDraw map = new MapBoxDraw("./docs/req8."+i+".html", ed, grafoR8);
			map.drawIterable();
			while(it.hasNext())
			{
				Edge<Long, String, Double> n = it.next();
				System.out.println(n.toString());
			}
			System.out.println("----------------");
		}
//		MapBoxDraw map8 = new MapBoxDraw("./docs/req8.html", ed, grafoR8);
//		map8.drawIterable();
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 4C:Encontrar el camino m�s corto para un viaje entre dos ubicaciones geogr�ficas escogidas aleatoriamente al interior del grafo.
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	public void caminoMasCortoC4(long idVertice1, long idVertice2) {
		grafoR2y9.crearTablas();
		DijkstraDios dijkstra = new DijkstraDios(grafoR2y9, idVertice1);
		Iterable<Edge<Long,String,Double>> iterable = dijkstra.pathTo(grafoR2y9.getIndiceT().get(idVertice2));
		Iterator<Edge<Long,String,Double>> iterator= iterable.iterator();
		int totInfra = 0;
		double distEst = 0.0;
		LinearProbing<Long, Vertex<Long,String,Double>> lin = grafoR2y9.getV();
		while(iterator.hasNext()) {
			Edge<Long,String,Double> arco = iterator.next();
			Long idIni = arco.getStartVertexId();
			Long idFin = arco.getEndVertexId();
			Vertex<Long, String, Double> vIni = lin.get(idIni);
			Vertex<Long, String, Double> vFin = lin.get(idFin);
			distEst += haversine(Double.parseDouble(vIni.getLatitud()), Double.parseDouble(vIni.getLongitud()), Double.parseDouble(vFin.getLatitud()), Double.parseDouble(vFin.getLongitud()));
			totInfra += vIni.getCantidadInfracciones() + vFin.getCantidadInfracciones();
			System.out.println("Secuencia: ");
			System.out.println(vIni.getId());
			System.out.println(vFin.getId());
		}
		System.out.println("Total infracciones "+ totInfra);
		System.out.println("Distancia estmada en Km: " + distEst + "Km");
		MapBoxDraw map9 = new MapBoxDraw("./docs/req9.html", iterable,grafoR2y9);
		map9.drawIterable();
	}

}
