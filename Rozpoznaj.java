import java.awt.Point;


class Rozpoznaj {
	public enum Litera {
		LITERA_W, LITERA_M, LITERA_N, NIEZDEFINIOWANA
	}
	
	public double [] RozpoznajLitere() {
		
		
		
		double [] literaDoRozpoznania = new double[64];
		int element = 0;
		for (int x = 0; x < 8; x++) {    	  
	         for (int y = 0; y < 8; y++) {   
	        	  
	        	 int licznik_pikseli = 0;
	        	 for (int i = 0; i < (int)Paint.paintPanel.getSize().width/8; i++) {        		 
	        		 for (int j = 0; j < (int)Paint.paintPanel.getSize().height/8; j++ ) {        			 
	        			 if (Paint.paintPanel.isPixel(new Point(x*(int)Paint.paintPanel.getSize().width/8+i, y*(int)Paint.paintPanel.getSize().height/8+j))) {
	        				 licznik_pikseli ++;        				 
	        			 }
	        		 }
	        	 }
	        	 double kolor_value = ((255*licznik_pikseli)/ (Paint.paintPanel.getSize().width/8*Paint.paintPanel.getSize().height/8))/255.0;
	        	 literaDoRozpoznania[element]= kolor_value;
	        	 element++;
	        	 
	            
	         }
	      }
		return literaDoRozpoznania;
		
	}
	
	public Rozpoznaj.Litera klasyfikujLiterę(double [] wyjscieSieci) {
		double treshold = 0.89;
		Rozpoznaj.Litera litera = Rozpoznaj.Litera.NIEZDEFINIOWANA;
		if(wyjscieSieci[Litera.LITERA_W.ordinal()] > treshold) {
			litera = Litera.LITERA_W;
		}else if(wyjscieSieci[Litera.LITERA_M.ordinal()] > treshold) {
			litera = Litera.LITERA_M;
		} else if(wyjscieSieci[Litera.LITERA_N.ordinal()] > treshold) {
			litera = Litera.LITERA_N;
		}
		return litera;	
	}
}