package Final;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

public class RoutePainterOrigin implements Painter<JXMapViewer>
{
	private boolean antiAlias = true;
	private int color;
	private Track track;
	private ArrayList<Line2D> segment;

	public RoutePainterOrigin(Track track, int color)
	{
		this.track = track;
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		segment = new ArrayList<Line2D>();
		g = (Graphics2D) g.create();

		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);

		if (antiAlias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (color == 0)
			g.setColor(new Color(0, 200, 255));
		else if (color == 1)
			g.setColor(new Color(255, 0, 100));

		g.setStroke(new BasicStroke(20 / map.getZoom()));

		drawRoute(g, map);
		g.dispose();
	}

	private void drawRoute(Graphics2D g, JXMapViewer map)
	{
		//int l=track.size();
		//Point2D pt = map.getTileFactory().geoToPixel(track.get(l/2), map.getZoom());
		//g.fillOval((int)pt.getX()-10,(int) pt.getY()-10, 20, 20);
		
		int lastX = 0;
		int lastY = 0;

		boolean first = true;
		for (GeoPosition gp : track)
		{
			Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
			if (first)
			{
				first = false;
			} else
			{
				Line2D.Double line = new Line2D.Double(lastX, lastY, (int) pt.getX(), (int) pt.getY());
				segment.add(line);
				g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
			}
			lastX = (int) pt.getX();
			lastY = (int) pt.getY();
		}
	}
	
	public ArrayList<Line2D> getSegment()
	{
		return segment;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public int getColor()
	{
		return color;
	}
}
