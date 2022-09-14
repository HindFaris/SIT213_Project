package emetteur;


import destinations.DestinationInterface;
import destinations.*;
import information.*;
import transmetteurs.*;
import signaux.*;

public class EmetteurAnalogique extends Transmetteur<Boolean, Float>{
	
	private String typeEmmeteur;
	private int nbEchantillons;
	private float min=0f;
	private float max=1f;
	
	public EmetteurAnalogique(String _typeEmetteur, int _nbEchantillons) {
		super();
		typeEmmeteur=_typeEmetteur;
		nbEchantillons=_nbEchantillons;
	}
	
	public  void recevoir(Information <Boolean> information) throws InformationNonConformeException{
		informationRecue = information;
		this.emettre();
	}

	/*
	 * émet l'information construite par le transmetteur
	 */
	public void emettre() throws InformationNonConformeException{
		
		if(typeEmmeteur.equalsIgnoreCase("RZ")) {
			Signal<Boolean,Float> signal = new SignalRZ<Boolean, Float>(informationRecue, nbEchantillons, min, max);
			informationEmise = signal.generer();
		}
		else if(typeEmmeteur.equalsIgnoreCase("NRZ")) {
			SignalNRZ signal = new SignalNRZ(informationRecue, nbEchantillons, min, max);
			informationEmise = signal.generer();
		}
		else if(typeEmmeteur.equalsIgnoreCase("NRZT")) {
			Signal<Boolean,Float> signal = new SignalNRZT<>(informationRecue, nbEchantillons, min, max);
			informationEmise = signal.generer();
		}
		else {
			throw new InformationNonConformeException("Vous devez spécifier RZ, NRZ ou NRZT en"
					+ " fonction du type de signal à émmettre");
		}
	
		for (DestinationInterface<Float> transmetteurConnectee : destinationsConnectees) {
			transmetteurConnectee.recevoir(informationEmise);
		}
		 
	}
}