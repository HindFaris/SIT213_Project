package sources;

import information.*;
import transmetteurs.Transmetteur;
import destinations.DestinationInterface;
import java.util.*;

/** 
 * Classe Abstraite d'un composant source d'informations dont les
 * elements sont de type T
 * @author prou
 */
public  abstract class Source <T> implements  SourceInterface <T> {

	/**
	 * la methode qui retourne toutes les destinations connectees
	 * @return recuper les destinations qui sont connectees a la source
	 */
	public LinkedList<DestinationInterface<T>> getDestinationsConnectees() {
		return destinationsConnectees;
	}

	/**
	 * la methode qui retourne toutes les transmetteurs connectees
	 * @return recuper les transmetteurs qui sont connectees a la source
	 */

	public LinkedList<Transmetteur<Boolean, Float>> getTransmetteursConnectees() {
		return transmetteursConnectees;
	}


	/** 
	 * la liste des composants destination connectes
	 */
	protected LinkedList <DestinationInterface <T>> destinationsConnectees;
	/** 
	 * la liste des transmetteurs destination connectes
	 */
	protected LinkedList <Transmetteur <Boolean,Float>> transmetteursConnectees;

	/** 
	 * l'information generee par la source
	 */
	protected Information <T>  informationGeneree;


	/** 
	 * l'information emise par la source
	 */
	protected Information <T>  informationEmise;

	/** 
	 * un constructeur factorisant les initialisations communes aux
	 * realisations de la classe abstraite Source
	 */
	public Source () {
		destinationsConnectees = new LinkedList <DestinationInterface <T>> ();
		informationGeneree = null;
		informationEmise = null;
	}

	/**
	 * retourne la derniere information emise par la source
	 * @return une information   
	 */
	public Information <T>  getInformationEmise() {
		return this.informationEmise;
	}
	@Override
	public void connecter (DestinationInterface <T> destination) {
		destinationsConnectees.add(destination); 
	}

	/**
	 * deconnecte une destination de la source
	 * @param destination  la destination a deconnecter
	 */
	public void deconnecter (DestinationInterface <T> destination) {
		destinationsConnectees.remove(destination); 
	}

	@Override
	public void emettre() throws InformationNonConformeException {

		for (DestinationInterface<T> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationGeneree);
		}

		this.informationEmise = informationGeneree;   	
	}

	/**
	 * permet de recuperer l'info
	 * @return l'information
	 */
	public Information<T> getInformationGeneree() {
		return informationGeneree;
	}
}
