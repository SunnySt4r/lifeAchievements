package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Challenge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class LifeAchievementsController implements Initializable {
    @FXML
    private TreeView<Achievement> treeView;
    private TreeCell<Achievement> treeCell;
    private TreeCell<Achievement> source;
    @FXML
    private Label name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Achievement root = new Achievement(Calendar.getInstance().getTime(), "Ачивки");
        Achievement category1 = new Achievement(Calendar.getInstance().getTime(), "1.");
        Achievement category2 = new Achievement(Calendar.getInstance().getTime(), "2.");
        Achievement category1_1 = new Achievement(Calendar.getInstance().getTime(), "1.1");
        Achievement achievement1 = new Achievement(Calendar.getInstance().getTime(), "сделать дела");
        Achievement achievement2 = new Achievement(Calendar.getInstance().getTime(), "написать программу");
        Challenge challenge = new Challenge(Calendar.getInstance().getTime(), "челендж 1", 100);

        TreeItem<Achievement> rootItem = new TreeItem<>(root);
        TreeItem<Achievement> categoryItem1 = new TreeItem<>(category1);
        TreeItem<Achievement> categoryItem2 = new TreeItem<>(category2);
        TreeItem<Achievement> categoryItem1_1 = new TreeItem<>(category1_1);
        TreeItem<Achievement> achiev1 = new TreeItem<>(achievement1);
        TreeItem<Achievement> achiev2 = new TreeItem<>(achievement2);
        TreeItem<Achievement> achiev3 = new TreeItem<>(challenge);

        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        rootItem.setExpanded(true);
        categoryItem1.setExpanded(true);
        categoryItem2.setExpanded(true);
        categoryItem1_1.setExpanded(true);
        rootItem.getChildren().addAll(categoryItem1, categoryItem2);
        categoryItem1.getChildren().addAll(categoryItem1_1, achiev1);
        categoryItem1_1.getChildren().addAll(achiev2, achiev3);

        treeView.setCellFactory(ach -> {
            //creating cell from default factory
            treeCell = TextFieldTreeCell.forTreeView((new TextFieldTreeCell<Achievement>()).getConverter()).call(ach);
            //setting handlers
            treeCell.setOnDragDetected(this::onDragDetected);
            treeCell.setOnDragOver(this::onDragOver);
            treeCell.setOnDragDropped(this::onDragDropped);
            return treeCell;
        });
    }

    private void onDragDetected(MouseEvent event) {
        source = (TreeCell<Achievement>) event.getSource();
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
        TreeCell<Achievement> targetNode = (TreeCell<Achievement>) dragEvent.getGestureTarget();
        if(!source.getTreeItem().equals(targetNode.getTreeItem()) && targetNode.getTreeItem() != null){
            source.getTreeItem().getParent().getChildren().remove(source.getTreeItem());
            targetNode.getTreeItem().getChildren().add(source.getTreeItem());
        }
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    public void selectItem(){
        TreeItem<Achievement> item = treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            if(item.isLeaf()){
                if(item.getValue() instanceof Challenge){
                    name.setText(((Challenge) item.getValue()).getDistance() + "");
                }else{
                    name.setText(item.getValue().toString());
                }
            }else{
                int size = checkChildren(item);
                name.setText("Категория: " + item.getValue() + " Количество ачивок: " + size);
            }
        }
    }

    private int checkChildren(TreeItem<Achievement> treeItem){
        if(treeItem.isLeaf()){
            return 1;
        }else{
            int size = 0;
            for(TreeItem<Achievement> item : treeItem.getChildren()){
                size += checkChildren(item);
            }
            return size;
        }
    }
}