package Final;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jxmapviewer.viewer.GeoPosition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RouteSegmentation
{
	static double maxDistanceKm;
	public static void Segment(int maxDistance)
	{
		maxDistanceKm=maxDistance;
		try
		{		
			//-------Initiate-The-output-document-segments.xml
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			//-------write-first-element-on-the-document
			Element rootElement = doc.createElement("roads");
			doc.appendChild(rootElement);
			
			//-------the-list-of-tracks-we-would-write-in-the-document
			List<Track> tracks=MyXMLParser.getAllWays();
			
			int i=0; //----segment-id
			
			for(Track track:tracks)
			{
				double trackLenght=getTrackLenght(track);
				List<Track> subTracks=new ArrayList<Track>(); //the list where we store the sub tracks
				
				//-------check-if-track-is-longer-than-maxdistance-to-segment-it
				if(trackLenght>maxDistanceKm)
				{
					subTracks=segmentTrack(track);
				}
				else
				{
					subTracks.add(track);
				}
				boolean dd=false;
				//-------write-each-subtrack-as-a-segment-on-the-document
				for(Track subTrack:subTracks)
				{
					Element segment = doc.createElement("segment");
					Attr attr = doc.createAttribute("id");
					attr.setValue(String.valueOf(i));
					segment.setAttributeNode(attr);
					
					Attr attr2 = doc.createAttribute("etat");
					if(dd)
						attr2.setValue("0");
					else
						attr2.setValue("1");

					dd=!dd;
					segment.setAttributeNode(attr2);
					
					//-------add-to-each-segement-its-points-with-its-coordinates
					for(GeoPosition pos:subTrack)
					{
						Element point = doc.createElement("point");
						Attr attrLat = doc.createAttribute("lat");
						attrLat.setValue(String.valueOf(pos.getLatitude()));
						point.setAttributeNode(attrLat);
						Attr attrLon = doc.createAttribute("lon");
						attrLon.setValue(String.valueOf(pos.getLongitude()));
						point.setAttributeNode(attrLon);
						segment.appendChild(point);
					}
					rootElement.appendChild(segment);
					i++;
				}
			}
			
			//-------write-the-final-document
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("segments.xml"));
			transformer.transform(source, result);
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static double degreesToRadians(double degrees)
	{
		return degrees * Math.PI / 180;
	}
	
	//-------calculate-the-distance-from-pos1-to-pos2-in-km
	static double getDistance(GeoPosition pos1,GeoPosition pos2)
	{
		int earthRadiusKm = 6371;
		
		double lat1=pos1.getLatitude();
		double lon1=pos1.getLongitude();
		double lat2=pos2.getLatitude();
		double lon2=pos2.getLongitude();
		
		double dLat = degreesToRadians(lat2 - lat1);
		double dLon = degreesToRadians(lon2 - lon1);

		lat1 = degreesToRadians(lat1);
		lat2 = degreesToRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)+ Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return earthRadiusKm * c;
	}
	
	//-------calculate-the-track-lenght-in-km
	static double getTrackLenght(List<GeoPosition> track)
	{
		double distance=0;
		for(int i=0;i<track.size()-1;i++)
		{
			distance+=getDistance(track.get(i), track.get(i+1));
		}
		return distance;
	}
	
	//-------segment-the-track-and-return-it-as-list-of-sub-track
	static List<Track> segmentTrack(Track track)
	{
		//-------the-list-of-subtracks-to-return
		List<Track> subTracks=new ArrayList<Track>();
		
		double distanceSum=0;
		
		//-------we-add-the-first-position-to-the-first-subtrack
		Track subTrack=new Track();
		subTrack.add(track.get(0));
		
		//-------last-position-we-added-to-the-subtrack
		GeoPosition lastPos=track.get(0);

		for(int i=0;i<track.size()-1;i++)
		{
			double distance=getDistance(lastPos, track.get(i+1));
			distanceSum+=distance;
			
			while(distanceSum>maxDistanceKm)
			{
				double distanceToAdd=distance+maxDistanceKm-distanceSum;

				GeoPosition pos1=lastPos;
				GeoPosition pos2=track.get(i+1);
				
				//-------calculate-the-coordinate-of-the-midpoint
				double x=pos1.getLatitude()+(pos2.getLatitude()-pos1.getLatitude())*distanceToAdd/distance;
				double y=pos1.getLongitude()+(pos2.getLongitude()-pos1.getLongitude())*distanceToAdd/distance;
				GeoPosition midPoint=new GeoPosition(x,y);
				
				lastPos=midPoint;
				subTrack.add(midPoint);
				//-------we-finished-the-subtrack-at-this-point-so-we-add-it-to-the-list-of-subtracks
				subTracks.add(subTrack);
				
				//-------we-create-a-new-subtrack
				subTrack=new Track();
				//-------we-add-the-last-position
				subTrack.add(lastPos);
				
				distanceSum-=maxDistanceKm;
				distance-=distanceToAdd;
			}
			
			if(distanceSum<=maxDistanceKm)
			{
				subTrack.add(track.get(i+1));
				lastPos=track.get(i+1);
			}
		}
		
		//-------we-add-the-last-subtrack-to-the-list-of-subtracks
		subTracks.add(subTrack);
		
		return subTracks;
	}
}
