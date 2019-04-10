package Final;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

public class MyXMLParser
{
	//-------To-Check-if-the-line-is-an-element-of-type-node
	static boolean isElement(String line,String node)
	{
		line=line.replaceAll("\\s","");
		if(line.startsWith("<"+node)) return true;
		return false;
	}

	//-------return-the-attribute-in-line
	static String getAttribute(String line,String attribute)
	{
		String ret = "";
		int index = line.indexOf(attribute);
		ret=line.substring(index+attribute.length()+2);
		ret=ret.substring(0,ret.indexOf("\""));
		return ret;
	}
	
	static String setAttribute(String line,String attribute,String value)
	{
		String ret="";
		String oldAttr=getAttribute(line, attribute);
		int index = line.indexOf(attribute)+attribute.length();
		boolean first=true;
		for(int i=0;i<line.length();i++)
		{
			if(i>=index+2 && i<index+2+oldAttr.length())
			{
				if (first)
				{
					ret+=value;
					first=false;
				}
			}
			else
			{
				ret+=line.charAt(i);
			}
		}
		return ret;
	}
	
	static Track getWay(String id)
	{
		Track track=new Track();
		try 
		{
			File file = new File("finally.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			String line;
			while((line=input.readLine())!=null)
			{
				if(isElement(line,"way") && getAttribute(line, "id").equals(id))
				{
					line=input.readLine();
					while(isElement(line, "point"))
					{
						double lat=Double.valueOf(getAttribute(line, "lat"));
						double lon=Double.valueOf(getAttribute(line, "lon"));
						GeoPosition pos=new GeoPosition(lat,lon);
						track.add(pos);
						
						line=input.readLine();
					}
					break;
				}
			}
			input.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return track;
	}

	static List<String> getWaysId()
	{
		List<String> waysId=new ArrayList<String>();
		try 
		{
			File file = new File("finally.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			String line;
			while((line=input.readLine())!=null)
			{
				if(isElement(line,"way"))
				{
					waysId.add(getAttribute(line, "id"));
					line=input.readLine();
				}
			}
			input.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return waysId;
	}
	
	static List<Track> getAllWays()
	{
		List<Track> tracks=new ArrayList<Track>();
		try 
		{
			File file = new File("finally.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			String line;
			while((line=input.readLine())!=null)
			{
				if(isElement(line,"way"))
				{
					Track track=new Track();
					line=input.readLine();
					while(isElement(line, "point"))
					{
						double lat=Double.valueOf(getAttribute(line, "lat"));
						double lon=Double.valueOf(getAttribute(line, "lon"));
						GeoPosition pos=new GeoPosition(lat,lon);
						track.add(pos);
						line=input.readLine();
					}
					tracks.add(track);
				}
			}
			input.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return tracks;
	}
	
	static List<Track> getAllSegments()
	{
		List<Track> tracks=new ArrayList<Track>();
		try 
		{
			File file = new File("segments.xml");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			String line;
			while((line=input.readLine())!=null)
			{
				if(isElement(line,"segment"))
				{
					Track track=new Track();
					
					line=input.readLine();
					while(isElement(line, "point"))
					{
						double lat=Double.valueOf(getAttribute(line, "lat"));
						double lon=Double.valueOf(getAttribute(line, "lon"));
						GeoPosition pos=new GeoPosition(lat,lon);
						track.add(pos);
						line=input.readLine();
					}
					tracks.add(track);
				}
			}
			input.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return tracks;
	}
	
	static Track getRoute()
	{
		Track track=new Track();
		try 
		{
			File file = new File("route.txt");
			LineNumberReader input = new LineNumberReader(new FileReader(file));
			String line;
			while((line=input.readLine())!=null)
			{
				String[] s=line.split(",");
				GeoPosition pos=new GeoPosition(Double.valueOf(s[1]),Double.valueOf(s[0]));
				track.add(pos);
			}
			input.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return track;
	}

}
