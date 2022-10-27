package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Category;
import com.github.sunnyst4r.lifeachievements.Achievements.Challenge;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

public class XMLOpener {
    private TreeView<Category> treeView;

    public XMLOpener(TreeView<Category> treeView){
        this.treeView = treeView;
    }

    public void open(String xml){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document dom = documentBuilder.parse(new File(xml));
            dom.getDocumentElement().normalize();
            Element rootElement = dom.getDocumentElement();
            TreeItem<Category> root = new TreeItem<>(new Category(rootElement.getAttribute("name")));

            NodeList nodeList = rootElement.getChildNodes();
            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    addTreeItem(element, root, String.valueOf(((i+1)/2)));
                }
            }
            treeView.setRoot(root);
            treeView.setShowRoot(false);
            root.setExpanded(true);

        }catch (ParserConfigurationException | SAXException | IOException | ParseException e){
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void addTreeItem(Element element, TreeItem<Category> superItem, String index) throws ParseException {
        TreeItem<Category> item;
        if(element.getTagName().equals("Category")){
            Category category = new Category(element.getAttribute("name"));
            item = new TreeItem<>(category, new Label(index));

            NodeList nodeList = element.getChildNodes();
            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element subElement = (Element) node;
                    addTreeItem(subElement, item, index + "." + ((i+1)/2));
                }
            }
        }else{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(element.getAttribute("creationDate")));
            if(element.getTagName().equals("Challenge")){
                Challenge challenge = new Challenge(
                        c.getTime(),
                        element.getAttribute("name"),
                        Integer.parseInt(element.getAttribute("distance"))
                );
                item = new TreeItem<>(challenge, new Label(index));
            }else{
                Achievement achievement = new Achievement(
                        c.getTime(),
                        element.getAttribute("name")
                );
                item = new TreeItem<>(achievement, new Label(index));
            }
        }
        superItem.getChildren().addAll(item);
    }
}
