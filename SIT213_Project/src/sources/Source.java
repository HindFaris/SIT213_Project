package sources;

import information.*;
import transmetteurs.Transmetteur;
import destinations.DestinationInterface;
import java.util.*;

/** 
 * Classe Abstraite d'un composant source d'informations dont les
 * éléments sont de type T
 * @author prou
 */
public  abstract class Source <T> implements  SourceInterface <T> {

	public LinkedList<DestinationInterface<T>> getDestinationsConnectees() {
		return destinationsConnectees;
	}

	public void setDestinationsConnectees(LinkedList<DestinationInterface<T>> destinationsConnectees) {
		this.destinationsConnectees = destinationsConnectees;
	}

	public LinkedList<Transmetteur<Boolean, Float>> getTransmetteursConnectees() {
		return transmetteursConnectees;
	}

	public void setTransmetteursConnectees(LinkedList<Transmetteur<Boolean, Float>> transmetteursConnectees) {
		this.transmetteursConnectees = transmetteursConnectees;
	}

	public void setInformationGeneree(Information<T> informationGeneree) {
		this.informationGeneree = informationGeneree;
	}

	public void setInformationEmise(Information<T> informationEmise) {
		this.informationEmise = informationEmise;
	}

	/** 
	 * la liste des composants destination connectés
	 */
	protected LinkedList <DestinationInterface <T>> destinationsConnectees;
	
	protected LinkedList <Transmetteur <Boolean,Float>> transmetteursConnectees;

	/** 
	 * l'information générée par la source
	 */
	protected Information <T>  informationGeneree;


	/** 
	 * l'information émise par la source
	 */
	protected Information <T>  informationEmise;

	/** 
	 * un constructeur factorisant les initialisations communes aux
	 * réalisations de la classe abstraite Source
	 */
	public Source () {
		destinationsConnectees = new LinkedList <DestinationInterface <T>> ();
		informationGeneree = null;
		informationEmise = null;
	}

	/**
	 * retourne la dernière information émise par la source
	 * @return une information   
	 */
	public Information <T>  getInformationEmise() {
		return this.informationEmise;
	}

	/**
	 * connecte une destination à la source
	 * @param destination  la destination à connecter
	 */
	public void connecter (DestinationInterface <T> destination) {
		destinationsConnectees.add(destination); 
	}

	/**
	 * déconnecte une destination de la source
	 * @param destination  la destination à déconnecter
	 */
	public void deconnecter (DestinationInterface <T> destination) {
		destinationsConnectees.remove(destination); 
	}

	/**
	 * émet l'information générée
	 */
	public   void emettre() throws InformationNonConformeException {

		for (DestinationInterface<T> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationGeneree);
		}
		
		this.informationEmise = informationGeneree;   	
		
	}
	
	public Information<T> getInformationGeneree() {
		return informationGeneree;
	}
}
