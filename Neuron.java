import java.util.Random;


public class Neuron {
	protected double wyjscie;
	protected double [] wejscia;
	protected double [] wagi;
	protected double sigma;
	protected int liczba_wejsc;
	protected double [] korekty;

	public Neuron(){
		liczba_wejsc=0;
		wagi=null;
	}
	public Neuron(int liczba_wejsc){
		this.liczba_wejsc=liczba_wejsc;
		wagi=new double[liczba_wejsc+1];
		korekty = new double [liczba_wejsc + 1];
		generuj();
	}
	private void generuj() {
		Random r=new Random();
		for(int i=0;i<=liczba_wejsc;i++)
			//wagi[i]=(r.nextDouble()-0.5)*2.0*10;//do ogladania
			wagi[i]=(r.nextDouble()-0.5)*2.0;//do projektu
	}
	public double oblicz_wyjscie(double [] wejscia){
		//double fi=wagi[0];
		double fi=0.0;
		this.wejscia = new double [wejscia.length];
		System.arraycopy(wejscia, 0, this.wejscia, 0, wejscia.length);
		
		for(int i=1;i<=liczba_wejsc;i++)
			
			fi+=wagi[i]*wejscia[i-1];
			
		double wynik=1.0/(1.0+Math.exp(-fi));// funkcja aktywacji sigma -unip
		//double wynik=(fi>0.0)?1.0:0.0;//skok jednostkowy
		//double wynik=fi; //f.a. liniowa
		wyjscie = wynik;
		return wynik;
	}

	public void obliczKorekty(double eta) {
		double pochodna = wyjscie * (1 - wyjscie);
		for(int i = 1; i <= liczba_wejsc; i++) {
			korekty[i] += eta * sigma * pochodna * wejscia[i-1];
		}
	}
	
	public void wprowadzKorekty() {
		for(int i = 1; i <= liczba_wejsc; i++) {
			wagi[i] += korekty[i];
			korekty[i] = 0.0;
		}
	}
}
