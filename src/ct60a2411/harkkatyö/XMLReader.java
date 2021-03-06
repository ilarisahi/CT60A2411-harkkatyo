package ct60a2411.harkkatyö;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML reader class
 * 
 * @author Petri Rämö
 * opiskelijanro: 0438578
 * 
 * @author Ilari Sahi
 * opiskelijanro: 0438594
 * 
 * 16.12.2016
 * 
 * This class contains all the reading from smartpost data from the website, which
 * is XML format. This class follows singleton design.
 */
public class XMLReader {
    
    private Document doc;
    public SmartPost SP;
    private SmartPostContainer smartPosts = SmartPostContainer.getInstance();
    static private XMLReader xmlr = null;
    
    private XMLReader() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        
        URL url = new URL("http://smartpost.ee/fi_apt.xml");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String content = "";
        String line;
        
        while ((line = br.readLine()) != null) {
            content +=line + "\n";
        }
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        doc = db.parse(new InputSource(new StringReader(content)));
        
        doc.getDocumentElement().normalize();
        
        NodeList nodes = doc.getElementsByTagName("place");
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Element e = (Element) node;
            
            // Creates new SmartPost from read information and adds it to the container
            SP = new SmartPost(getValue("code", e), getValue("city", e).toUpperCase(), 
                    getValue("address", e), getValue("availability", e), 
                    getValue("postoffice", e), getValue("lat", e), 
                    getValue("lng", e), i);
            
            smartPosts.addSmartPost(SP);
            smartPosts.addCity(getValue("city", e).toUpperCase());
        }
    }
    
    static public XMLReader getInstance() {
        if (xmlr == null) {
            try {
                xmlr = new XMLReader();
            } catch (IOException | ParserConfigurationException | SAXException ex) {
                Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return xmlr;        
    }
    
    private String getValue(String tag, Element e) {
        return e.getElementsByTagName(tag).item(0).getTextContent();
    }
}
