import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class SimplePaintPanel extends JPanel {
	private static final long serialVersionUID = 1L;
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
