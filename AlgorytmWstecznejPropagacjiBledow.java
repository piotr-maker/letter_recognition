import java.util.Arrays;

public class AlgorytmWstecznejPropagacjiBledow {
	private Siec siec;
	private ElementUczacy [] ciagUczacy;
	
	public AlgorytmWstecznejPropagacjiBledow(Siec siec, ElementUczacy [] ciagUczacy) {
		this.siec = siec;
		this.ciagUczacy = ciagUczacy;
	}
	
	public void naucz(int liczbaEpok) {
		double eta = 0.1;
		double sigma = 0.0;
		for(int i = 0; i < liczbaEpok; i++) {
			sigma = uruchomEpoke(eta);
			siec.wprowadzKorekty();
			System.out.println("Błąd: " + Double.toString(sigma));
		}
	}
	
	public double uruchomEpoke(double eta) {
		double [] sigma;
		double bladSrKwadratowy = 0.0;
		for(int i = 0; i < ciagUczacy.length; i++) {
			double [] y = siec.oblicz_wyjscie(ciagUczacy[i].pobierzDane());
			sigma = new double [y.length];
			for(int j = 0; j < sigma.length; j++) {
				sigma[j] = ciagUczacy[i].pobierzWartoscPozadana()[j] - y[j];
			}
			siec.propagacjaBledowWstecz(sigma);
			siec.obliczKorekty(eta);
			bladSrKwadratowy += Math.pow(Arrays.stream(sigma).sum() / sigma.length, 2.0);
		}
		return bladSrKwadratowy / ciagUczacy.length;
	}
}
