package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private Set<Esame> best;
	private double mediaBest;
	
	public Model()
	{
		EsameDAO dao = new EsameDAO();
		
		this.esami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int m) 
	{
		// ripristino soluzione migliore
		best = new HashSet<Esame>();
		mediaBest = Double.MIN_VALUE;
		
		Set<Esame> parziale = new HashSet<Esame>();
		
		// approccio stupido
		// cercaStupido(parziale, 0, m);
		
		// approccio furbo
		cercaFurbo(parziale, 0, m);
		
		return best;	
	}
	
	/*
	 * APPROCCIO FURBO
	 * Complessità = 2^N
	 */
	private void cercaFurbo(Set<Esame> parziale, int livello, int m) 
	{
		int sommaCrediti = this.sommaCrediti(parziale);
		
		// CASI TERMINALI
		
		// soluzione non valida
		if(sommaCrediti > m)
		{
			return;
		}
		// soluzione valida, è la migliore?
		if(sommaCrediti == m)
		{
			double mediaVoti = this.calcolaMedia(parziale);
			
			if(mediaVoti > mediaBest)
			{	// la soluzione è la migliore (ad ora)
				
				this.best = new HashSet<Esame>(parziale);
				mediaBest = mediaVoti;
			}
			
			return;
		}
		// qui, sommaCrediti < m
		if(livello == esami.size())
		{	// ma non ho più esami da aggiungere ...
			
			return;
		}
			

		// CASO NORMALE
		// non siamo ancora a m crediti ma ho ancora esami da inserire!
		
		// proviamo ad aggiungere l'esame	 	esami[livello]
		parziale.add(esami.get(livello));
		this.cercaFurbo(parziale, livello+1, m);
		
		// proviamo a NON aggiungere l'esame 	esami[livello]
		parziale.remove(esami.get(livello));
		this.cercaFurbo(parziale, livello+1, m);
		
	}

	/*
	 * APPROCCIO STUPIDO
	 * Complessità: N!
	 */
	private void cercaStupido(Set<Esame> parziale, int livello, int m) 
	{
		int sommaCrediti = this.sommaCrediti(parziale);
		
		// CASI TERMINALI
		
		// soluzione non valida
		if(sommaCrediti > m)
		{
			return;
		}
		// soluzione valida, è la migliore?
		if(sommaCrediti == m)
		{
			double mediaVoti = this.calcolaMedia(parziale);
			
			if(mediaVoti > mediaBest)
			{	// la soluzione è la migliore (ad ora)
				
				this.best = new HashSet<Esame>(parziale);
				mediaBest = mediaVoti;
			}
			
			return;
		}
		
		// qui, sommaCrediti < m
		if(livello == esami.size())
		{	// non ho esami da aggiungere ...
			
			return;
		}
			

		// CASO NORMALE
		// non siamo ancora a m crediti ma ho ancora esami da inserire!
		for(Esame e: esami)
		{
			// lo inserisco solo se esame non lo contiene già
			if(!parziale.contains(e))
			{
				parziale.add(e);
				cercaStupido(parziale, livello+1, m);
				parziale.remove(e);
				// backtracking --> aggiungo, chiamata al metodo ricorsivo, tolgo
			}
		}
		
	}
	
	public double calcolaMedia(Set<Esame> esami) 
	{
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) 
	{
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
