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
import java.util.Date;

public class XMLOpener {
    private final TreeView<Category> treeView;

    public XMLOpener(TreeView<Category> treeView){
        this.treeView = treeView;
    }

    public void open(String xml){
        //open file.xml with path (String xml)

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document dom = documentBuilder.parse(new File(xml));
            dom.getDocumentElement().normalize();
            //get root element from document
            Element rootElement = dom.getDocumentElement();
            //create root TreeItem with Category constructor
            TreeItem<Category> root = new TreeItem<>(new Category(rootElement.getAttribute("name")));

            NodeList nodeList = rootElement.getChildNodes();
            //iterate every elements in root
            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                //every second element is not Node.ELEMENT_NODE
                //because of this index = (i+1)/2
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    addTreeItem(element, root, String.valueOf(((i+1)/2)));
                }
            }
            //set root into treeView from gui
            treeView.setRoot(root);
            treeView.setShowRoot(false);
            root.setExpanded(true);

        }catch (ParserConfigurationException | SAXException | IOException | ParseException e){
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void addTreeItem(Element element, TreeItem<Category> superItem, String index) throws ParseException {
        //parse TreeItem from element and add it into superItem, and add Label with index like 1.2.3

        TreeItem<Category> item;
        //if element is category we need to add all items to this element
        if(element.getTagName().equals("Category")){
            Category category = new Category(element.getAttribute("name"));
            item = new TreeItem<>(category, new Label(index));

            //iterate all elements in Category, and add into Category
            NodeList nodeList = element.getChildNodes();
            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element subElement = (Element) node;
                    addTreeItem(subElement, item, index + "." + ((i+1)/2));
                }
            }
        }else{
            //element is Achievement or Challenge

            //create calendar for parse date
            Calendar c = Calendar.getInstance();

            //parse creation date
            c.setTimeInMillis(Long.parseLong(element.getAttribute("creationDate")));
            Date creationDate = c.getTime();
            //parse ending date
            Date endingDate = null;
            if(!element.getAttribute("endingDate").equals("0")){
                c.setTimeInMillis(Long.parseLong(element.getAttribute("endingDate")));
                endingDate = c.getTime();
            }
            //parse finish
            Date finish = null;
            if(!element.getAttribute("finish").equals("0")){
                c.setTimeInMillis(Long.parseLong(element.getAttribute("finish")));
                finish = c.getTime();
            }
            //parse description
            String description = element.getAttribute("description");
            //parse done
            boolean done = element.getAttribute("done").equals("1");

            if(element.getTagName().equals("Challenge")){
                //add new challenge from attributes
                Challenge challenge = new Challenge(
                        creationDate,
                        endingDate,
                        element.getAttribute("name"),
                        description,
                        Integer.parseInt(element.getAttribute("distance"))
                );
                challenge.setAttempt(Integer.parseInt(element.getAttribute("attempt")));
                challenge.setRecord(Integer.parseInt(element.getAttribute("record")));
                challenge.setCurrentStreak(Integer.parseInt(element.getAttribute("currentStreak")));
                challenge.setFinish(finish);
                challenge.setDone(done);
                item = new TreeItem<>(challenge, new Label(index));
            }else{
                //add new achievement from attributes
                Achievement achievement = new Achievement(
                        creationDate,
                        endingDate,
                        element.getAttribute("name"),
                        description
                );
                achievement.setFinish(finish);
                achievement.setDone(done);
                item = new TreeItem<>(achievement, new Label(index));
            }
        }
        //add (TreeItem) item into (TreeItem) superItem
        superItem.getChildren().addAll(item);
    }
}
