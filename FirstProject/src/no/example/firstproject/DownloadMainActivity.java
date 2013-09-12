package no.example.firstproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class DownloadMainActivity extends Activity {

	final static String IMAGE_URL = "http://www.vasetskiheiser.no/wp-content/uploads/2013/06/logo-valdres.png";
	final static String TEXT_URL = "http://google.no";
	final static String RSSFEED_URL =  "http://www.nrk.no/ho/toppsaker.rss";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_main);
        
        
      //This is where the image is loaded
        //Sending any URL pointing to an image will work as the bitmap 
        // class reads common formats
        final Bitmap bitmap = downloadImage(IMAGE_URL);
        
        //Then display the image to a view
        final ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(bitmap);
        
        //If we wanted to do a straight text download we can do it with a similar function
        final TextView txt = (TextView) findViewById(R.id.text);
   		String str = downloadText(TEXT_URL);
        if (str.length() > 15) {
        	str = str.substring(0, 15);
        }
        txt.setText(str); 
    
        
        //example of using an RSS download
        String[] RSSTitles;
        RSSTitles = downloadRSS(RSSFEED_URL);
        
        final ListView itemlist = (ListView) findViewById(R.id.itemlist);  //you need this is the xml layout for your program
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RSSTitles); // more needed in the resources
        itemlist.setAdapter(adapter);  // assign the titles to the list
    }
    
    
    
    /**
     * @author Simon McCallum
     *
     * This function downloads the image at the URL
     * location passed and then returns the bitmap
     * @param  URL     an absolute URL giving the base location and name of the image
     * @return bitmap  the image at the specified URL
     *
     */
    private Bitmap downloadImage(String URL) {
   
        Bitmap bitmap = null;
        InputStream in = null;        
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;                
    }

    
    
    /**
     * @author Simon McCallum
     *
     * This function downloads the string of text at the URL
     * location passed and then returns content at a Java String
     * @param  URL           a String containing an absolute URL giving the base location and name of the text
     * @return returnString  the String constructed using a buffered reader
     *
     */
    private String downloadText(String URL) {
    	
    	int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e1) {
            e1.printStackTrace();
            return "";
        }
        
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        
        String returnString = "";
        char[] inputBuffer = new char[BUFFER_SIZE];          
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {                    
                //---convert the chars to a String---
                String readString = 
                    String.copyValueOf(inputBuffer, 0, charRead);                    
                returnString += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }    
        return returnString;
    }
   
    
    /**
     * @author Simon McCallum
     *
     * This function downloads and parses an rss feed from a specific
     * location passed in the URL and then
     * @param  URL          a String containing the absolute URL giving the 
     * 						base location and name of the rss feed
     * @return RSSTitles    an Array of Strings which are the titles from the RSS feed
     */ 
    private String[] downloadRSS(String URL) {
        InputStream in = null;
        String[] RSSTitles = new String[0];
        try {
            in = OpenHttpConnection(URL);
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            
            try {
                db = dbf.newDocumentBuilder();
                doc = db.parse(in);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }        
            
            doc.getDocumentElement().normalize(); 
            
            //---retrieve all the <item> nodes---
            NodeList itemNodes = doc.getElementsByTagName("item"); 
            
            String strTitle = "";
            RSSTitles = new String[itemNodes.getLength()];
            for (int i = 0; i < itemNodes.getLength(); i++) { 
                Node itemNode = itemNodes.item(i); 
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) { 
                    //---convert the Node into an Element---
                    Element itemElement = (Element) itemNode;
                    
                    //---get all the <title> element under the <item> 
                    // element---
                    NodeList titleNodes = (itemElement).getElementsByTagName("title");
                    
                    //---convert a Node into an Element---
                    Element titleElement = (Element) titleNodes.item(0);
                    
                    //---get all the child nodes under the <title> element---
                    NodeList textNodes = 
                        ((Node) titleElement).getChildNodes();
                    
                    //---retrieve the text of the <title> element---
                    strTitle = ((Node) textNodes.item(0)).getNodeValue();
                    RSSTitles[i] = strTitle;
                } 
            }
        } catch (IOException e1) {
            e1.printStackTrace();            
        }
        return RSSTitles;
    }
    
    /**
     * @author Simon McCallum
     *
     * Handles some of the complexity of opening up a HTTP connection
     * @param  URL   a String containing the absolute URL giving the base location and name of the content
     * @return in    an inputStream which will be the stream of text from the server
     *
     */    
    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
               
        final URL url = new URL(urlString); 
        final URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection)) {                     
            throw new IOException("Not an HTTP connection");
        }
        
        try {
            final HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        } catch (Exception ex) {
        	ex.printStackTrace();            
        }
        return in;     
    }
    
}
    

        
        
