import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;

class Ucz {
	FilenameFilter filter = new FilenameFilter() {		  
        public boolean accept(File f, String name) {
            return name.endsWith(".png");
        }
    };
	public double[][] WczytajiUcz(String folder){
		double [][] daneUczace = new double [128][64];
		String sciezka = null;
		int f = 0;
		for (int k = 0; k < 4; k++) {
			if (k==0) {
				sciezka = "src/" + folder + "/W"; 
			}
			if (k==1) {
				sciezka = "src/" + folder + "/M";
			}
			if (k==2) {
				sciezka = "src/" + folder + "/N";
			}
			if (k==3) {
				sciezka = "src/" + folder + "/pozostale";
			}
			File path = new File(sciezka);
			File [] files = path.listFiles(filter);
			for (int p = 0; p < files.length; p++) {
				BufferedImage image;
			      File openFile = files[p];
			      try (FileInputStream fis = new FileInputStream(openFile)) {
			         image = ImageIO.read(fis);
			      } catch (IOException e) {
			    	  System.out.println("Nie znaleziono plików");
			         return null;
			      }
			      if (image == null)
			         return null;
			      int i = 0; 			      
			      for (int x = 0; x < image.getWidth(); x++) {
			          for (int y = 0; y < image.getHeight(); y++) {
			             Color c = new Color(image.getRGB(x, y));
			             double kolor = c.getBlue();
			             kolor = Math.abs(kolor/255-1);
			             daneUczace[f][i]= kolor;
			             i++;
			          }
			       }
			      f++;
				}
			}
		
		
		
		return daneUczace;
		}
	
	
	public double[][] WczytajOczekiwane(String folder){
		double [][] daneOczekiwane = new double [128][4];
		String sciezka = null;
		
		int f = 0;
		for (int k = 0; k < 4; k++) {
			if (k==0) {
				sciezka = "src/" + folder + "/W"; 
			}
			if (k==1) {
				sciezka = "src/" + folder + "/M"; 
			}
			if (k==2) {
				sciezka = "src/" + folder + "/N"; 
			}
			if (k==3) {
				sciezka = "src/" + folder + "/pozostale"; 
			}
			File path = new File(sciezka);
			File [] files = path.listFiles(filter);
			for (int p = 0; p < files.length; p++) {
				daneOczekiwane[f][k] = 1;  
			    f++;
				}
			}
		return daneOczekiwane;
	}
}