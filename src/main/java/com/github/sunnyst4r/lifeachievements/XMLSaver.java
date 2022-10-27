package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Category;
import com.github.sunnyst4r.lifeachievements.Achievements.Challenge;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLSaver {
    private TreeView<Category> treeView;

    public XMLSaver(TreeView<Category> treeView){
        this.treeView = treeView;
    }

    public void save(){
        Document dom;
        TreeItem<Category> root = treeView.getRoot();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try{
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            dom = documentBuilder.newDocument();

            //create the root element
            Element rootElement = dom.createElement("Category");
            rootElement.setAttribute("name", root.getValue().getName());
            for(TreeItem<Category> item : root.getChildren()){
                addElements(dom, item, rootElement);
            }
            dom.appendChild(rootElement);

            try{
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                //send file
                transformer.transform(new DOMSource(dom), new StreamResult(new FileOutputStream("src/xml/1.xml")));
            }catch (TransformerException | IOException e){
                System.out.println(e.getMessage());
            }
        }catch (ParserConfigurationException pce){
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }

    }

    private void addElements(Document dom, TreeItem<Category> item, Element superElement){
        Element element;
        if(item.getValue() instanceof Achievement){
            if(item.getValue() instanceof Challenge){
                element = dom.createElement("Challenge");
                element.setAttribute("distance", String.valueOf(((Challenge)item.getValue()).getDistance()));
            }else{
                element = dom.createElement("Achievement");
            }
            element.setAttribute("name", item.getValue().getName());
            element.setAttribute("creationDate", String.valueOf(((Achievement)item.getValue()).getCreatingDate().getTime()));
        }else{
            element = dom.createElement("Category");
            element.setAttribute("name", item.getValue().getName());
            for(TreeItem<Category> item1 : item.getChildren()){
                addElements(dom, item1, element);
            }
        }
        superElement.appendChild(element);
    }
}
