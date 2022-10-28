package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Category;
import com.github.sunnyst4r.lifeachievements.Achievements.Challenge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.*;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class LifeAchievementsController implements Initializable {
    @FXML
    public TabPane informationTabPane;
    @FXML
    private TreeView<Category> treeView;
    private TreeCell<Category> treeCell;
    private TreeCell<Category> source;
    @FXML
    private Tab achievementTab ;
    @FXML
    private Tab categoryTab;
    @FXML
    private Label nameAchievement;
    @FXML
    private Label nameCategory;
    @FXML
    private Label countAchievements;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //clear all tabs from information
        informationTabPane.getTabs().clear();

        //load file that we saved before
        (new XMLOpener(treeView)).open("src/xml/1.xml");

        //create drag and drop function
        treeView.setCellFactory(ach -> {
            //creating cell from default factory
            treeCell = TextFieldTreeCell.forTreeView((new TextFieldTreeCell<Category>()).getConverter()).call(ach);
            //setting handlers
            treeCell.setOnDragDetected(this::onDragDetected);
            treeCell.setOnDragOver(this::onDragOver);
            treeCell.setOnDragDropped(this::onDragDropped);
            return treeCell;
        });
    }

    private void onDragDetected(MouseEvent event) {
        //start dragging

        source = (TreeCell<Category>) event.getSource();
        if(source.getTreeItem() != null){
            Dragboard dragboard = source.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(source.getTreeItem().toString());
            dragboard.setContent(content);
        }
        event.consume();
    }

    private void onDragOver(DragEvent dragEvent) {
        //stop dragging

        Dragboard dragboard = dragEvent.getDragboard();
        if (dragboard.hasString()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    private void onDragDropped(DragEvent dragEvent) {
        //drop TreeItem into other TreeItem

        TreeCell<Category> targetNode = (TreeCell<Category>) dragEvent.getGestureTarget();
        //check target TreeItem is available
        //not node that dragged && not null && not Achievement or Challenge && not subnode
        if(!source.getTreeItem().equals(targetNode.getTreeItem())
                && targetNode.getTreeItem() != null
                && !(targetNode.getTreeItem().getValue() instanceof Achievement)
                && !checkIsChild(targetNode.getTreeItem(), source.getTreeItem())){

            //remove from one TreeItem and insert into other
            source.getTreeItem().getParent().getChildren().remove(source.getTreeItem());
            targetNode.getTreeItem().getChildren().add(source.getTreeItem());

            //rename all Lable in TreeItem after dropping into other place
            TreeItem<Category> root = treeView.getRoot();
            renameLabel(root, "");
        }
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    public void selectItem(){
        //select one TreeItem in TreeView

        TreeItem<Category> item = treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            //change information tabs
            if(item.getValue() instanceof Achievement){
                informationTabPane.getTabs().clear();
                informationTabPane.getTabs().add(achievementTab);
                if(item.getValue() instanceof Challenge){
                    nameAchievement.setText(item.getValue().getName());
                }else{
                    nameAchievement.setText(item.getValue().getName());
                }
            }else{
                informationTabPane.getTabs().clear();
                informationTabPane.getTabs().add(categoryTab);
                int size = countChildren(item);
                nameCategory.setText(item.getValue().toString());
                countAchievements.setText(String.valueOf(size));
            }
        }
    }

    private int countChildren(TreeItem<Category> treeItem){
        //count all Achievement or Challenge in one Category
        if(treeItem.getValue() instanceof Achievement){
            return 1;
        }else{
            int size = 0;
            for(TreeItem<Category> item : treeItem.getChildren()){
                size += countChildren(item);
            }
            return size;
        }
    }

    private boolean checkIsChild(TreeItem<Category> target, TreeItem<Category> source){
        //check is target TreeItem a child or subchild of source TreeItem
        if(!(source.getValue() instanceof Achievement)){
            for(TreeItem<Category> child : source.getChildren()){
                if(child.equals(target)){
                    return true;
                }else{
                    if(checkIsChild(target, child)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void renameLabel(TreeItem<Category> item, String index){
        //rename all Lable in order like in example (1.1.4 or 2.5.10.2)
        for(int i=0; i<item.getChildren().size(); i++){
            //check is first row children in root or not
            String text = index;
            if(index.equals("")){
                text += (i+1);
            }else{
                text += "." + (i+1);
            }
            //replace text in Label
            if(item.getChildren().get(i).getGraphic() instanceof Label){
                ((Label) item.getChildren().get(i).getGraphic()).setText(text);
            }
            //if Category, then rename all Label in it
            if(!(item.getChildren().get(i).getValue() instanceof Achievement)){
                renameLabel(item.getChildren().get(i), text);
            }
        }
    }

    public void createNewAchievement() {
        //create new Achievement in random place
        //TODO normal creation of Achievements and Category

        TreeItem<Category> target;
        if(Math.random() > 0.5){
            target = treeView.getRoot();
        }else{
            target = treeView.getRoot().getChildren()
                    .get((int) (Math.random()*treeView.getRoot().getChildren().size()));
        }
        String[] names = {"Покушать", "Позаниматься", "Пописать код", "Поделать домашку", "Полить цветы", "Сделать оригами", "Съесть мармеладки"};
        Achievement achievement = new Achievement(Calendar.getInstance().getTime(), names[(int) (Math.random()*7)]);
        TreeItem<Category> achievementItem = new TreeItem<>(achievement);
        target.getChildren().add(achievementItem);
    }

    public void saveXMLFile() {
        //save TreeView as xml file
        (new XMLSaver(treeView)).save();
    }

    public void openXMLFile() {
        //open xml file as TreeView
        (new XMLOpener(treeView)).open("src/xml/1.xml");
    }
}