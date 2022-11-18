
public class ElementUczacy {
	private double [] dane;
	private double [] pozadana;
	
	public ElementUczacy(double [] dane, double [] pozadana) {
		this.dane = dane;
		this.pozadana = pozadana;
	}
	
	public double [] pobierzDane() {
		return dane;
	}
	
	public double [] pobierzWartoscPozadana() {
		return pozadana;
	}
}
