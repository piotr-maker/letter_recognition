
public class Siec {
	Warstwa [] warstwy;
	int liczba_warstw;
	
	public Siec(){
		warstwy=null;
		this.liczba_warstw=0;
	}
	public Siec(int liczba_wejsc,int liczba_warstw,int [] lnww){
		this.liczba_warstw=liczba_warstw;
		warstwy=new Warstwa[liczba_warstw];
		for(int i=0;i<liczba_warstw;i++)
			warstwy[i]=new Warstwa((i==0)?liczba_wejsc:lnww[i-1],lnww[i]);
	}
	double [] oblicz_wyjscie(double [] wejscia){
		double [] wyjscie=null;
		for(int i=0;i<liczba_warstw;i++)
			wejscia = wyjscie = warstwy[i].oblicz_wyjscie(wejscia);
		return wyjscie;
	}

	public void propagacjaBledowWstecz(double [] sigma) {
		int idOstatniejWarstwy = liczba_warstw - 1;
		for(int i = idOstatniejWarstwy; i >= 0; i--) {
			for(int j = 0; j < warstwy[i].liczba_neuronow; j++) {
				if(i == idOstatniejWarstwy) {
					warstwy[i].neurony[j].sigma = sigma[j];
				} else {
					double lokalnaSigma = 0.0;
					for(int k = 0; k < warstwy[i+1].liczba_neuronow; k++) {
						lokalnaSigma += warstwy[i+1].neurony[k].wagi[j+1] * warstwy[i+1].neurony[k].sigma;
					}
					warstwy[i].neurony[j].sigma = lokalnaSigma;
				}
			}
		}
	}
	
	public void obliczKorekty(double eta) {
		for(int i = 0; i < liczba_warstw; i++) {
			for(int j = 0; j < warstwy[i].liczba_neuronow; j++) {
				warstwy[i].neurony[j].obliczKorekty(eta);
			}
		}
		
	}
	
	public void wprowadzKorekty() {
		for(int i = 0; i < liczba_warstw; i++) {
			for(int j = 0; j < warstwy[i].liczba_neuronow; j++) {
				warstwy[i].neurony[j].wprowadzKorekty();
			}
		}
	}
}
