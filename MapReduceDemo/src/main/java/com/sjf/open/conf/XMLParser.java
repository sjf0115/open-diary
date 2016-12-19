package com.sjf.open.conf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by xiaosi on 16-12-13.
 */
public class XMLParser {


    public static void parser(String path){

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        // ignore all comments inside the xml file
        docBuilderFactory.setIgnoringComments(true);
        // allow includes in the xml file
        docBuilderFactory.setNamespaceAware(true);

        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(path);
            Element root = doc.getDocumentElement();
            NodeList props = root.getChildNodes();
            for(int i =0 ;i < props.getLength();i++){
                Node prop = props.item(i);


                System.out.println(prop.getNodeName() + "---" + prop.getNodeValue());
                //System.out.println(prop.getNodeType());
                //System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String path = XMLParser.class.getResource("/core-site.xml").getPath().toString();
        parser(path);
    }

}
