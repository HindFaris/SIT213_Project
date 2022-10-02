package transmetteurs;

import java.util.LinkedList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;
import signaux.Bruit;

public class TransmetteurAnalogiqueMultiTrajetsBruite extends Transmetteur<Float, Float> {

	private int nbEchantillons;
	private float SNRParBit;
	private Integer seed = null;
	private LinkedList<Float> alphas = new LinkedList<Float>(); //attenuation du second trajet entre 0 et 1
	private LinkedList<Integer> taus = new LinkedList<Integer>(); //retard du signal en nombre d'echantillons

	public TransmetteurAnalogiqueMultiTrajetsBruite(int nbEchantillons, float SNRParBit, Integer seed, LinkedList<Float> alphas, LinkedList<Integer> taus ) {
		super();
		this.nbEchantillons = nbEchantillons;
		this.SNRParBit = SNRParBit;
		this.seed = seed;
		this.alphas = alphas;
		this.taus = taus;
		informationEmise = new Information<Float>();
	}

	/**
	 * recevoir l'information float
	 */
	public void recevoir(Information <Float> information) throws InformationNonConformeException{
		informationRecue = information;
	}

	/**
	 * emettre l'information float a toutes les destinations connectees
	 */

	public void emettre() throws InformationNonConformeException {
		Information<Float> informationAjoutee = new Information<Float>();
		int tailleInformation = informationRecue.nbElements();
		int tauMax = 0;
		
		for (int index = 0; index < taus.size(); index++) {
			if (tauMax < Integer.valueOf(taus.get(index))) {
				tauMax= Integer.valueOf(taus.get(index));
			}
		}
		
		Bruit bruit = null;
		try {
			bruit = new Bruit(this.ecartType(), tailleInformation+tauMax, seed);
		} catch (Exception e) {
			System.out.println("Err : Impossible de creer le bruit dans Transmetteur MultiTrajet Bruite");
		}
		
		for (int index = 0; index < tailleInformation; index++) {
			informationAjoutee.add(0.0f);
		}
		for (int index = 0; index < tauMax; index++) {
			informationAjoutee.add(0.0f);
			informationRecue.add(0.0f);
		}
		
		//trajet indirect (alpha*s(t-tau))
		Information<Float> information;
		LinkedList<Float> infoRecue = null;

		for (int index = 0; index < taus.size(); index++) {
			information = new Information<Float>();
			
			try {
				infoRecue = informationRecue.cloneInformation();
			} catch (Exception e1) {
				System.out.println("ERR : Impossible de cloner l'informationRecue dans Transmetteur MultiTrajet Bruite");
			}
			
			int tau = taus.get(index);
			float alpha = alphas.get(index);

			while (tau>0){
				information.add(0.0f);
				tau--;
			}

			for (int i = 0; i < tailleInformation; i++) {
				information.add(infoRecue.get(0)*alpha);	
				infoRecue.remove(0);			
			}
			for (int i = 0; i < information.nbElements(); i++) {
				float var = informationAjoutee.iemeElement(0)+information.iemeElement(0);
				informationAjoutee.remove(0);
				information.remove(0);
				informationAjoutee.add(var);
			}
		}

		LinkedList<Float> informationRecueCopie = new LinkedList<Float>();
		try {
			informationRecueCopie = informationRecue.cloneInformation();
		} catch (Exception e1) {
			System.out.println("ERR : Impossible de cloner l'informationRecue dans Transmetteur MultiTrajet Parfait");
		}
		//signal emis par le transmetteur
		for(int indice = 0 ; indice < (tailleInformation+tauMax); indice++) {
			informationEmise.add(informationRecueCopie.get(0)+ informationAjoutee.iemeElement(0)+ bruit.iemeElement(0));
			informationRecue.remove(0);
			informationAjoutee.remove(0);
			bruit.remove(0);
		}

		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationEmise);
		}
		this.informationEmise = informationRecue;
	}

	public float ecartType() throws Exception{
		float ecartType = (float)Math.sqrt(this.puissance()*nbEchantillons/(2*Math.pow(10, SNRParBit/10)));
		return ecartType;
	}

	public float puissance(){
		float puissance = 0;
		LinkedList<Float> copieInformationRecue = new LinkedList<Float>();

		try {
			copieInformationRecue = informationRecue.cloneInformation();
		} catch (Exception e) {

		}
		for (int index = 0; index<informationRecue.nbElements(); index++) {
			puissance += Math.pow(copieInformationRecue.get(0), 2);
			copieInformationRecue.remove(0);
		}
		puissance = (puissance/(float)(informationRecue.nbElements()));
		return puissance;
	}

	public int getNbEchantillons() {
		return nbEchantillons;
	}

	public float getSNRParBit() {
		return SNRParBit;
	}

	public Integer getSeed() {
		return seed;
	}
	
	public LinkedList<Float> getAlphas() {
		return alphas;
	}

	public LinkedList<Integer> getTaus() {
		return taus;
	}
}
