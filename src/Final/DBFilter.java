package Final;
import java.io.File;
import java.io.FileReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.LineNumberReader;
import java.util.ArrayList;

import java.util.Collections; 

public class DBFilter
{
	static ArrayList<Long> refs;
	public static void Filter(boolean all)
	{
		try
		{
			//-------ReadFile
			File file = new File("emplacement.xml");
			LineNumberReader emplacement = new LineNumberReader(new FileReader(file));
			emplacement.readLine();
			
			//-------Initiate-The-output-document
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			//-------write-first-element-on-the-document
			Element roads = doc.createElement("roads");
			doc.appendChild(roads);
			
			//-------store-every-node's-refrence-and-coordinates-in-a-list
			refs=new ArrayList<Long>();
			ArrayList<double[]> coords=new ArrayList<double[]>();

			String line= emplacement.readLine();
			while (line != null)
			{
				//-------cycle-throught-the-nodes-and-add-them-to-a-list
				if(MyXMLParser.isElement(line,"node"))
				{
					long id=Long.valueOf(MyXMLParser.getAttribute(line, "id"));
					double lat=Double.valueOf(MyXMLParser.getAttribute(line, "lat"));
					double lon=Double.valueOf(MyXMLParser.getAttribute(line, "lon"));
					double[] coord={lat,lon};
					
					coords.add(coord);
					refs.add(id);
				}
				//-------cycle-throught-the-ways
				else if(MyXMLParser.isElement(line,"way"))
				{
					Element way = doc.createElement("way");
					
					String roadId=MyXMLParser.getAttribute(line, "id");
					
					Attr id = doc.createAttribute("id");
					id.setValue(roadId);
					way.setAttributeNode(id);

					line= emplacement.readLine();
					
					//-------store-the-way-with-its-nodes(points)-in-memory
					while (MyXMLParser.isElement(line,"nd"))
					{
						Element point = doc.createElement("point"); //creat point of nd 

						int index = Collections.binarySearch(refs, Long.valueOf(MyXMLParser.getAttribute(line,"ref"))); 
						double[] coord=coords.get(index);
						
						Attr attrLat = doc.createAttribute("lat");
						attrLat.setValue(String.valueOf(coord[0]));
						point.setAttributeNode(attrLat);
						
						Attr attrLon = doc.createAttribute("lon");  //create
						attrLon.setValue(String.valueOf(coord[1]));	//set value
						point.setAttributeNode(attrLon);			//add attr to point
						
						way.appendChild(point);	//add point to way
						
						line= emplacement.readLine();
					}
					
					//-------check-if-the-way-is-a-highway
					//-------if-so-we-write-them-to-the-document
					while (MyXMLParser.isElement(line,"tag"))
					{
						String v = MyXMLParser.getAttribute(line,"v");
						
						boolean isHighway = v.equals("motorway") || v.equals("trunk") || v.equals("primary") || v.equals("secondary");
						if(all) isHighway = v.equals("motorway") || v.equals("trunk") || v.equals("primary") || v.equals("secondary") || v.equals("tertiary") || v.equals("unclassified") || v.equals("residential");

						if(MyXMLParser.getAttribute(line,"k").equals("highway") && isHighway)
						{
							roads.appendChild(way);
							break;
						}
						line= emplacement.readLine();
					}
				}
				line= emplacement.readLine();
			}
			
			//-------write-the-final-document
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("finally.xml"));
			transformer.transform(source, result);

			emplacement.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}