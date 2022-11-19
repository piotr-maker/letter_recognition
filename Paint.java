import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class ButtonsPanel extends JPanel implements ActionListener{
	private Siec siec;
	private int [] tabNeuronow = {7, 5, 3};
	private JButton rozpoznaj;
	private JButton ucz;
	private JButton testuj;
	
	private enum Actions {
	    ZAPISZ8x8,
	    WYCZYSC,
	    UCZ,
	    ROZPOZNAJ,
	    TESTUJ
	  }
	public ButtonsPanel() {
		this.setPreferredSize(new Dimension(200, 200));
		this.setBackground(Color.lightGray);
		
		rozpoznaj=new JButton("Rozpoznaj");
		JButton wyczysc=new JButton("Wyczyść");
		JButton zapisz8x8=new JButton("Zapisz 8x8");
		testuj = new JButton("Testuj");
		ucz=new JButton("Ucz");
		
		zapisz8x8.addActionListener(this);
		zapisz8x8.setActionCommand(Actions.ZAPISZ8x8.name());
		wyczysc.addActionListener(this);
		wyczysc.setActionCommand(Actions.WYCZYSC.name());
		testuj.addActionListener(this);
		testuj.setActionCommand(Actions.TESTUJ.name());
		testuj.setEnabled(false);
		ucz.addActionListener(this);
		ucz.setActionCommand(Actions.UCZ.name());
		rozpoznaj.addActionListener(this);
		rozpoznaj.setEnabled(false);
		rozpoznaj.setActionCommand(Actions.ROZPOZNAJ.name());
		
		
		this.add(rozpoznaj);
		this.add(wyczysc);
		this.add(zapisz8x8);
		this.add(testuj);
		this.add(ucz);
		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
		
		
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == Actions.ZAPISZ8x8.name()) {
	       
			JFileChooser fileChooser = new JFileChooser();
		      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		      int result = fileChooser.showSaveDialog(this);
		      if (result != JFileChooser.APPROVE_OPTION)
		         return;
		      File saveFile = fileChooser.getSelectedFile();
		      if (!saveFile.getAbsolutePath().toLowerCase().endsWith(".png"))
		         saveFile = new File(saveFile.getAbsolutePath() + ".png");
		      BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
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
		        	 int kolor_value = 255 - (255*licznik_pikseli)/ (Paint.paintPanel.getSize().width/8*Paint.paintPanel.getSize().height/8);
		        	 Color col = new Color(kolor_value, kolor_value, kolor_value);
		            image.setRGB(x, y, col.getRGB());
		            
		         }
		      }
		      try {
		         ImageIO.write(image, "png", saveFile);
		      } catch (IOException exception) {
		         return;
		      }
			
	       
		}
		if (e.getActionCommand() == Actions.WYCZYSC.name()) {
			Paint.paintPanel.clear();
		}
		if (e.getActionCommand() == Actions.UCZ.name()) {
			ucz.setEnabled(false);
			Ucz uczObject = new Ucz();
			double [][] ciagUczacy = uczObject.WczytajiUcz("uczace");
			double [][] ciagOczekiwany = uczObject.WczytajOczekiwane("uczace");
			siec = new Siec(64, 3, tabNeuronow);
			ElementUczacy [] daneUczace = new ElementUczacy[ciagUczacy.length];
			for(int i = 0; i < ciagUczacy.length; i++) {
				daneUczace[i] = new ElementUczacy(ciagUczacy[i], ciagOczekiwany[i]);
			}
			AlgorytmWstecznejPropagacjiBledow algorytm = new AlgorytmWstecznejPropagacjiBledow(siec, daneUczace);
			algorytm.naucz(150);
			rozpoznaj.setEnabled(true);
			testuj.setEnabled(true);
		}
		if (e.getActionCommand() == Actions.ROZPOZNAJ.name()) {
			String komunikat;
			Rozpoznaj rozpoznaj = new Rozpoznaj();
			double [] doRozpoznania = rozpoznaj.RozpoznajLitere();
			double [] rezultat;
			rezultat = siec.oblicz_wyjscie(doRozpoznania);
			Rozpoznaj.Litera litera = rozpoznaj.klasyfikujLiterę(rezultat);
			switch(litera) {
			case LITERA_W:
				komunikat = "W";
				break;
			case LITERA_M:
				komunikat = "M";
				break;
			case LITERA_N:
				komunikat = "N";
				break;
			default:
				komunikat = "niezdefiniowana";
			}
			System.out.println("W: " + String.format("%.2f", rezultat[0]));
			System.out.println("M: " + String.format("%.2f", rezultat[1]));
			System.out.println("N: " + String.format("%.2f", rezultat[2]));
			JOptionPane.showMessageDialog(null, komunikat, "Rozpoznana litera", JOptionPane.INFORMATION_MESSAGE);
		}
		if(e.getActionCommand() == Actions.TESTUJ.name()) {
			Ucz uczObject = new Ucz();
			Rozpoznaj rozpoznaj = new Rozpoznaj();
			double [] rezultat;
			double [][] ciagTestowy = uczObject.WczytajiUcz("testy");
			double [][] ciagOczekiwany = uczObject.WczytajOczekiwane("testy");

			Rozpoznaj.Litera litera;
			int poprawne = 0;
			for(int i = 0; i < ciagTestowy.length; i++) {
				rezultat = siec.oblicz_wyjscie(ciagTestowy[i]);
				litera = rozpoznaj.klasyfikujLiterę(rezultat);
				for(int j = 0; j < rezultat.length; j++) {
					if((ciagOczekiwany[i][j] > 0.5) && (j == litera.ordinal())) {
						poprawne++;
					}
				}
			}
			String komunikat = String.format("%d", poprawne * 100 / ciagTestowy.length);
			System.out.println("Skuteczność: " + komunikat + "%");
			JOptionPane.showMessageDialog(null, komunikat + "%", "Skuteczność sieci", JOptionPane.INFORMATION_MESSAGE);
		}
		
		
	   }
	
}

class SimplePaintPanel extends JPanel {
   private final Set<Point> blackPixels = new HashSet<Point>();
   private final int brushSize;

   private int mouseButtonDown = 0;
   int drawingPanelx = 300;
   int drawingPanely = 300;

   public SimplePaintPanel() {
      this(5, new HashSet<Point>());
   }

   public SimplePaintPanel(Set<Point> blackPixels) {
      this(5, blackPixels);
   }

   public SimplePaintPanel(int brushSize, Set<Point> blackPixels) {
      this.setPreferredSize(new Dimension(drawingPanelx, drawingPanely));
      this.brushSize = brushSize;
      this.blackPixels.addAll(blackPixels);
      final SimplePaintPanel self = this;

      MouseAdapter mouseAdapter = new MouseAdapter() {
         @Override
         public void mouseDragged(MouseEvent ev) {
            if (mouseButtonDown == 1)
               addPixels(getPixelsAround(ev.getPoint()));
            else if (mouseButtonDown == 3)
               removePixels(getPixelsAround(ev.getPoint()));
         }

         @Override
         public void mousePressed(MouseEvent ev) {
            self.mouseButtonDown = ev.getButton();
         }
      };
      this.addMouseMotionListener(mouseAdapter);
      this.addMouseListener(mouseAdapter);

   }

   public void paint(Graphics g) {
      int w = drawingPanelx;	
      int h = drawingPanely;	
      g.setColor(Color.white);
      g.fillRect(0, 0, w, h);
      g.setColor(Color.black);
      for (Point point : blackPixels)
         g.drawRect(point.x, point.y, 1, 1);

   }

   public void clear() {
      this.blackPixels.clear();
      this.invalidate();
      this.repaint();
   }

   public void addPixels(Collection<? extends Point> blackPixels) {
      this.blackPixels.addAll(blackPixels);
      this.invalidate();
      this.repaint();
   }

   public void removePixels(Collection<? extends Point> blackPixels) {
      this.blackPixels.removeAll(blackPixels);
      this.invalidate();
      this.repaint();
   }

   public boolean isPixel(Point blackPixel) {
      return this.blackPixels.contains(blackPixel);
   }

   private Collection<? extends Point> getPixelsAround(Point point) {
      Set<Point> points = new HashSet<>();
      for (int x = point.x - brushSize; x < point.x + brushSize; x++)
         for (int y = point.y - brushSize; y < point.y + brushSize; y++)
            points.add(new Point(x, y));
      return points;
   }
}

public class Paint extends JFrame implements ActionListener {
   private final String ACTION_NEW = "New Image";
   private final String ACTION_LOAD = "Load Image";
   private final String ACTION_SAVE = "Save Image";
   
   JPanel container = new JPanel();
   public static SimplePaintPanel paintPanel = new SimplePaintPanel();
   private final ButtonsPanel buttonsPanel = new ButtonsPanel(); 

   public Paint() {
      super();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Neural Network");
      Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
      
      setPreferredSize(new Dimension(600,300));
      setLocation(size.width/4, size.height/4);
      setResizable(false);

      initMenu();
      
      container.setLayout(new BorderLayout() );
      container.add(paintPanel, BorderLayout.LINE_START);
      container.add(buttonsPanel);
      
      this.getContentPane().add(container);

      pack();
      setVisible(true);
   }

   private void initMenu() {
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("File");
      JMenuItem mnuNew = new JMenuItem(ACTION_NEW);
      JMenuItem mnuLoad = new JMenuItem(ACTION_LOAD);
      JMenuItem mnuSave = new JMenuItem(ACTION_SAVE);
      mnuNew.setActionCommand(ACTION_NEW);
      mnuLoad.setActionCommand(ACTION_LOAD);
      mnuSave.setActionCommand(ACTION_SAVE);
      mnuNew.addActionListener(this);
      mnuLoad.addActionListener(this);
      mnuSave.addActionListener(this);
      menu.add(mnuNew);
      menu.add(mnuLoad);
      menu.add(mnuSave);
      menuBar.add(menu);
      this.setJMenuBar(menuBar);
   }

   @Override
   public void actionPerformed(ActionEvent ev) {
      switch (ev.getActionCommand()) {
      case ACTION_NEW:
         paintPanel.clear();
         break;
      case ACTION_LOAD:
         doLoadImage();
         break;
      case ACTION_SAVE:
         doSaveImage();
         break;
      }
   }

   private void doSaveImage() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int result = fileChooser.showSaveDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
         return;
      File saveFile = fileChooser.getSelectedFile();
      if (!saveFile.getAbsolutePath().toLowerCase().endsWith(".png"))
         saveFile = new File(saveFile.getAbsolutePath() + ".png");
      BufferedImage image = new BufferedImage(paintPanel.getSize().width, paintPanel.getSize().height,
            BufferedImage.TYPE_INT_RGB);
      for (int x = 0; x < image.getWidth(); x++) {
         for (int y = 0; y < image.getHeight(); y++) {
            image.setRGB(x, y, Color.white.getRGB());
            if (paintPanel.isPixel(new Point(x, y))) {
               image.setRGB(x, y, Color.black.getRGB());
            }
         }
      }
      try {
         ImageIO.write(image, "png", saveFile);
      } catch (IOException e) {
         return;
      }
   }
   
   

   private void doLoadImage() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int result = fileChooser.showOpenDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
         return;
      BufferedImage image;
      File openFile = fileChooser.getSelectedFile();
      try (FileInputStream fis = new FileInputStream(openFile)) {
         image = ImageIO.read(fis);
      } catch (IOException e) {
         return;
      }
      if (image == null)
         return;
      paintPanel.clear();
      Set<Point> blackPixels = new HashSet<Point>();
      for (int x = 0; x < image.getWidth(); x++) {
         for (int y = 0; y < image.getHeight(); y++) {
            Color c = new Color(image.getRGB(x, y));
            if ((c.getBlue() < 128 || c.getRed() < 128 || c.getGreen() < 128) && c.getAlpha() == 255) {
               blackPixels.add(new Point(x, y));
            }
         }
      }
      paintPanel.addPixels(blackPixels);
   }
   
   private void doLoadData() {
	      JFileChooser fileChooser = new JFileChooser();
	      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	      int result = fileChooser.showOpenDialog(this);
	      if (result != JFileChooser.APPROVE_OPTION)
	         return;
	      BufferedImage image;
	      File openFile = fileChooser.getSelectedFile();
	      try (FileInputStream fis = new FileInputStream(openFile)) {
	         image = ImageIO.read(fis);
	      } catch (IOException e) {
	         return;
	      }
	      if (image == null)
	         return;
	      double wartosci[];
	      wartosci = new double[image.getWidth()*image.getHeight()];
	      for (int x = 0; x < image.getWidth(); x++) {
	         for (int y = 0; y < image.getHeight(); y++) {
	            Color c = new Color(image.getRGB(x, y));          
	            wartosci[x*image.getWidth()+y] = (255.0 - c.getBlue()) / 255;            
	         }
	      }
	      for (int i = 0; i < wartosci.length; i++ ) {
	    	  System.out.println(wartosci[i]);
	      }	      
	   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new Paint();
         }
      });
   }
}
