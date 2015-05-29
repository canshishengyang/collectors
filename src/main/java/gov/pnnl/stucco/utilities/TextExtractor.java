package gov.pnnl.stucco.utilities;

import gov.pnnl.stucco.collectors.PostProcessingException;
import gov.pnnl.stucco.utilities.TextExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: add comments on class
 * @author d3e145
 *
 */
public class TextExtractor {
    private static final Logger logger = LoggerFactory.getLogger(TextExtractor.class);
    
    /**
     * Combines the metadata and extracted text content into a JSON document
     * The document uses the names as TIKA defines for the key value pairs in the metadata structure
     * and this method add the "content" key and the value from the content.
     * @param content - text extracted (or filtered) from the document
     * @param metadata - A map of extracted metadata from the document (author, title, pubdate) as found in the document
     * @return - as JSON document
     */
    private String createJSONObject(String content, Metadata metadata)
    {
        String jsonString = "";
        String JSONCONTENT = "content";
        
        try{
            JSONObject document = new JSONObject();
            
            document.put(JSONCONTENT, new String(content));
            String[] names = metadata.names();
            for (String name : names)
            {
                String[] value = metadata.getValues(name);
                document.put(name, value);
            }
            jsonString = document.toString(4);

        }catch(JSONException e){
            logger.error("Error putting content from TIKA into JSON object: " + e);
        }
        return jsonString;
    }
    
    /**
     * Parses an HTML document and filters all the HTML markup out and returns metadata that was found in the document
     * 
     * @param is - the input stream containing the document
     * @return - a JSONString representing the document and metadata
     * @throws PostProcessingException  if TIKA fails for whatever reason
     */
    public String parseHTML(InputStream is) throws PostProcessingException {
        String rtnValue = "";
        try {
            ContentHandler contenthandler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            Parser parser = new AutoDetectParser();
            parser.parse(is, contenthandler, metadata, new ParseContext());
            rtnValue = createJSONObject(contenthandler.toString(), metadata);     
        }
        catch (IOException | TikaException  | SAXException e) {
            throw new PostProcessingException(e);
        }
        
        return rtnValue;
    }
    
    /**
     * Main for testing
     * @param args
     */
    public static void main(String... args) {
        // TODO Auto-generated method stub
        File file = new File("test.html");
        logger.error("Running TextExtractor ");
        try {
            InputStream is = new FileInputStream(file);
            TextExtractor te = new TextExtractor();
            String rtnValue = te.parseHTML(is);
            System.out.println(rtnValue);
        } catch (Exception e) {
           logger.error("Error "+ e); 
        }

    }

}
