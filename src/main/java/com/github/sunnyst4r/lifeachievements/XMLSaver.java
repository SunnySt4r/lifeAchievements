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
    private final TreeView<Category> treeView;

    public XMLSaver(TreeView<Category> treeView){
        this.treeView = treeView;
    }

    public void save(){
        //save 1.xml from TreeView

        Document dom;
        //get root element from treeView
        TreeItem<Category> root = treeView.getRoot();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            dom = documentBuilder.newDocument();

            //create the root element and add some attributes
            Element rootElement = dom.createElement("Category");
            rootElement.setAttribute("name", root.getValue().getName());
            //iterate every root inner elements
            for(TreeItem<Category> item : root.getChildren()){
                addElements(dom, item, rootElement);
            }
            //add root element into document
            dom.appendChild(rootElement);

            try{
                //create properties for .xml file
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                //send file
                transformer.transform(
                        new DOMSource(dom),
                        new StreamResult(new FileOutputStream("src/xml/1.xml"))
                );
            }catch (TransformerException | IOException e){
                System.out.println(e.getMessage());
            }
        }catch (ParserConfigurationException pce){
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }

    }

    private void addElements(Document dom, TreeItem<Category> item, Element superElement){
        //add element from item into superElement

        Element element;
        //if element is Category we need to add all items to this element
        if(item.getValue() instanceof Achievement){
            //item is Achievement or Challenge

            //different attributes for Achievement and Challenge
            if(item.getValue() instanceof Challenge){
                element = dom.createElement("Challenge");
                element.setAttribute("distance",
                        String.valueOf(((Challenge)item.getValue()).getDistance())
                );
                element.setAttribute("currentStreak",
                        String.valueOf(((Challenge)item.getValue()).getCurrentStreak())
                );
                element.setAttribute("attempt", String.valueOf(((Challenge)item.getValue()).getAttempt()));
                element.setAttribute("record", String.valueOf(((Challenge)item.getValue()).getRecord()));
            }else{
                element = dom.createElement("Achievement");
            }
            //common attributes for Achievement and Challenge
            element.setAttribute("name", item.getValue().getName());
            element.setAttribute("creationDate",
                    String.valueOf(((Achievement)item.getValue()).getCreatingDate().getTime())
            );
            if(((Achievement)item.getValue()).getEndingDate() == null){
                element.setAttribute("endingDate", "0");
            }else{
                element.setAttribute("endingDate",
                        String.valueOf(((Achievement)item.getValue()).getEndingDate().getTime())
                );
            }
            if(((Achievement)item.getValue()).getFinish() == null){
                element.setAttribute("finish", "0");
            }else{
                element.setAttribute("finish",
                        String.valueOf(((Achievement)item.getValue()).getFinish().getTime())
                );
            }
            if((((Achievement) item.getValue()).isDone())) {
                element.setAttribute("done", "1");
            }else{
                element.setAttribute("done", "0");
            }
            element.setAttribute("description", ((Achievement) item.getValue()).getDescription());
        }else{
            //item is Category
            element = dom.createElement("Category");
            element.setAttribute("name", item.getValue().getName());
            for(TreeItem<Category> item1 : item.getChildren()){
                addElements(dom, item1, element);
            }
        }
        //add element and all sub elements into superElement
        superElement.appendChild(element);
    }
}
