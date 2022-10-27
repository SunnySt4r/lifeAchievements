package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Category;
import com.github.sunnyst4r.lifeachievements.Achievements.Challenge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class LifeAchievementsController implements Initializable {
    @FXML
    private TreeView<Category> treeView;
    private TreeCell<Category> treeCell;
    private TreeCell<Category> source;
    @FXML
    private AnchorPane achievementPanel;
    @FXML
    private AnchorPane categoryPanel;
    @FXML
    private Label nameAchievement;
    @FXML
    private Label nameCategory;
    @FXML
    private Label countAchievements;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        (new XMLOpener(treeView)).open("src/xml/1.xml");

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
        Dragboard dragboard = dragEvent.getDragboard();
        if (dragboard.hasString()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    private void onDragDropped(DragEvent dragEvent) {
        TreeCell<Category> targetNode = (TreeCell<Category>) dragEvent.getGestureTarget();
        if(!source.getTreeItem().equals(targetNode.getTreeItem())
                && targetNode.getTreeItem() != null
                && !(targetNode.getTreeItem().getValue() instanceof Achievement)){

            source.getTreeItem().getParent().getChildren().remove(source.getTreeItem());
            targetNode.getTreeItem().getChildren().add(source.getTreeItem());
        }
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    public void selectItem(){
        TreeItem<Category> item = treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            if(item.getValue() instanceof Achievement){
                categoryPanel.setOpacity(0);
                categoryPanel.setDisable(true);
                achievementPanel.setOpacity(1);
                achievementPanel.setDisable(false);
                if(item.getValue() instanceof Challenge){
                    nameAchievement.setText(item.getValue().getName());
                }else{
                    nameAchievement.setText(item.getValue().getName());
                }
            }else{
                achievementPanel.setOpacity(0);
                achievementPanel.setDisable(true);
                categoryPanel.setOpacity(1);
                categoryPanel.setDisable(false);
                int size = checkChildren(item);
                nameCategory.setText(item.getValue().toString());
                countAchievements.setText(String.valueOf(size));
            }
        }
    }

    private int checkChildren(TreeItem<Category> treeItem){
        if(treeItem.getValue() instanceof Achievement){
            return 1;
        }else{
            int size = 0;
            for(TreeItem<Category> item : treeItem.getChildren()){
                size += checkChildren(item);
            }
            return size;
        }
    }

    public void createNewAchievement() {
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
        (new XMLSaver(treeView)).save();
    }

    public void openXMLFile() {
        (new XMLOpener(treeView)).open("src/xml/1.xml");
    }
}