package Final;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.WMSTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class DrawTest
{
	
	public static List<RoutePainterOrigin> painters;
	static List<GeoPosition> track;
	static JXMapViewer mapViewer;
	static RoutePainterOrigin routePainter;
	static GeoPosition pos1,pos2;

	public static void main(String[] args)
    {
		//-------the-map-tiles(images)
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        
        //-------initiate-the-map
        mapViewer = new JXMapViewer();
       // mapViewer.setTileFactory(new DefaultTileFactory(osmInfo));
        mapViewer.setTileFactory(tileFactory);
        
        //-------set-initial-map-location
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(new GeoPosition(33.9716,- 6.8498));

        //-------add-mouselistener-to-the-map-so-we-can-move-and-zoom
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        
        JPanel panel = new JPanel();
        
        //-------painters-that-contain-all-the-tracks-painters
		painters = new ArrayList<RoutePainterOrigin>();
		
        //-------tracks-where-we-store-every-track
		List<Track> tracks=MyXMLParser.getAllSegments();
		
        //-------values-where-we-store-the-value-of-each-track(0,1)
		List<Integer> values=getValues();
		int i=0;
		for(Track track:tracks)
		{
			int value=values.get(i);
			routePainter = new RoutePainterOrigin(track,value);
			painters.add(routePainter);
			i++;
		}
		
        //-------to-change-the-value-of-a-track-or-display-its-id
		mapViewer.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					int index=0;
					boolean first=true;
					for(RoutePainterOrigin routePainter:painters)
					{
						List<Line2D> segment=routePainter.getSegment();
						for (Line2D line : segment)
						{
							java.awt.Point p = e.getPoint();
							GeoPosition geo = mapViewer.convertPointToGeoPosition(p);
							Point2D pt = mapViewer.getTileFactory().geoToPixel(geo, mapViewer.getZoom());

							if (line.intersects(pt.getX()-10, pt.getY()-10, 20, 20) && first)
							{
								if(e.getButton() == MouseEvent.BUTTON1)
								{
									JOptionPane.showMessageDialog(mapViewer,"The index of the segment you clicked on is "+index);
								}
								if(e.getButton() == MouseEvent.BUTTON3)
								{
									changeValue(index);
									if(routePainter.getColor()==0)
										routePainter.setColor(1);
									else
										routePainter.setColor(0);
									mapViewer.repaint();
								}
								first=false;

							}
						}
						index++;
					}
				}
			}
		});
		
        //-------draw-the-painters-on-the-map
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
		
        //-------initiate-the-frame
        JFrame frame = new JFrame("JXMapviewer2");
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
	
    //-------get-the-track-value
	static int getValue(int Trackid)
	{
		int value=0;
		try
		{
			String line;
			File file = new File("segments.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			input.readLine();
			
			while((line=input.readLine())!=null)
			{
				if(MyXMLParser.isElement(line, "segment") && MyXMLParser.getAttribute(line, "id").equals(String.valueOf(Trackid)))
				{
					value=Integer.valueOf(MyXMLParser.getAttribute(line, "etat"));
				}
			}
			input.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return value;
	}
	
    //-------get-a-list-of-all-the-tracks-values
	static List<Integer> getValues()
	{
		List<Integer> values=new ArrayList<Integer>();
		try
		{
			String line;
			File file = new File("segments.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			input.readLine();
			
			while((line=input.readLine())!=null)
			{
				if(MyXMLParser.isElement(line, "segment"))
				{
					//System.out.println(line);
					values.add(Integer.valueOf(MyXMLParser.getAttribute(line, "etat")));
				}
			}
			input.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return values;
	}
	
    //-------to-change-the-value-of-a-track
	static void changeValue(int Trackid)
	{
		try
		{
			String line;
			List<String> lines=new ArrayList<String>();
			File file = new File("segments.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			while((line=input.readLine())!=null)
			{
				lines.add(line);
			}
			input.close();
			
			FileWriter output = new FileWriter("segments.xml",false);
			BufferedWriter bufferedFile=new BufferedWriter(output);
			for(String l:lines)
			{
				if(MyXMLParser.isElement(l, "segment") && MyXMLParser.getAttribute(l, "id").equals(String.valueOf(Trackid)))
				{
					if(MyXMLParser.getAttribute(l, "etat").equals("1"))
					{
						l=MyXMLParser.setAttribute(l, "etat", "0");
					}
					else
					{
						l=MyXMLParser.setAttribute(l, "etat", "1");
					}
				}
				bufferedFile.write(l);
				bufferedFile.newLine();
			}
			bufferedFile.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
}
