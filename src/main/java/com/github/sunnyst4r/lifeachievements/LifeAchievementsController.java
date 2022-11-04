package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.*;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class LifeAchievementsController implements Initializable {
    @FXML
    public TabPane informationTabPane;
    @FXML
    private TreeView<Category> treeView;
    private TreeCell<Category> treeCell;
    private TreeCell<Category> source;

    //get information tab
    @FXML
    private Tab achievementTab ;
    @FXML
    private Tab categoryTab;

    //get all creation tab
    @FXML
    private Tab creationCategoryTab;
    @FXML
    private Tab creationAchievementTab;
    @FXML
    private Tab creationChallengeTab;

    //get all components of creation category
    @FXML
    private TextField categoryName;

    //get all components of creation achievement
    @FXML
    private TextField achievementName;
    @FXML
    private CheckBox achievementHasDateCreation;
    @FXML
    private DatePicker achievementDateCreationPicker;
    @FXML
    private CheckBox achievementHasDateEnding;
    @FXML
    private DatePicker achievementDateEndingPicker;
    @FXML
    private TextArea achievementDescription;

    //get all components of creation challenge
    @FXML
    private TextField challengeName;
    @FXML
    private CheckBox challengeHasDateCreation;
    @FXML
    private DatePicker challengeDateCreationPicker;
    @FXML
    private CheckBox challengeHasDateEnding;
    @FXML
    private DatePicker challengeDateEndingPicker;
    @FXML
    private TextArea challengeDescription;
    @FXML
    private Spinner<Integer> challengeDistance;
    @FXML
    private Label achievementNameInfo;
    @FXML
    private Label categoryNameInfo;
    @FXML
    private Label countAchievements;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //add stylesheet to TreeView
        treeView.getStylesheets().add(LifeAchievementsApplication.class.getResource("style.css").toExternalForm());

        //load file that we saved before
        (new XMLOpener(treeView)).open("src/xml/1.xml");

        //create drag and drop function
        treeView.setCellFactory(ach -> {
            //creating cell from default factory
            treeCell = TextFieldTreeCell.forTreeView((new TextFieldTreeCell<Category>()).getConverter()).call(ach);
            //setting handlers
            treeCell.setOnDragDetected(this::onDragDetected);
            treeCell.setOnDragOver(this::onDragOver);
            treeCell.setOnDragEntered(this::onDragEntered);
            treeCell.setOnDragExited(this::onDragExited);
            treeCell.setOnDragDropped(this::onDragDropped);
            return treeCell;
        });

        /*
            GUI START CONFIGURATION
         */
        //clear all tabs from information
        informationTabPane.getTabs().clear();
        //rename all index
        renameLabel(treeView.getRoot(), "");
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

    private void onDragEntered(DragEvent dragEvent){
        //entered into one of elements and highlight this element

        //if target isn't null and isn't source we set one of two style
        TreeCell<Category> target = (TreeCell<Category>) dragEvent.getTarget();
        if(target != source && target.getTreeItem() != null){
            //if we can drop element into target we highlight green, else - red
            if(!(target.getTreeItem().getValue() instanceof Achievement)
                    && !checkIsChild(target.getTreeItem(), source.getTreeItem())){
                target.getStyleClass().add("tree-cell-on-drag-entered-true");
            }else {
                target.getStyleClass().add("tree-cell-on-drag-entered-false");
            }
        }
        dragEvent.consume();
    }

    private void onDragExited(DragEvent dragEvent){
        //exited from element and stop highlight this element

        //remove all two styles from the element
        TreeCell<Category> target = (TreeCell<Category>) dragEvent.getTarget();
        if(target != source){
            target.getStyleClass().remove("tree-cell-on-drag-entered-false");
            target.getStyleClass().remove("tree-cell-on-drag-entered-true");
        }
        dragEvent.consume();
    }

    private void onDragDropped(DragEvent dragEvent) {
        //drop TreeItem into other TreeItem

        TreeItem<Category> targetNode = ((TreeCell<Category>) dragEvent.getGestureTarget()).getTreeItem();
        //if target is null, we can drop into root TreeItem
        if(targetNode == null){
            targetNode = treeView.getRoot();
        }
        //check target TreeItem is available
        //not node that dragged && not null && not Achievement or Challenge && not subnode
        if(!source.getTreeItem().equals(targetNode)
                && !(targetNode.getValue() instanceof Achievement)
                && !checkIsChild(targetNode, source.getTreeItem())){

            //remove from one TreeItem and insert into other
            source.getTreeItem().getParent().getChildren().remove(source.getTreeItem());
            targetNode.getChildren().add(source.getTreeItem());

            //rename all Lable in TreeItem after dropping into other place
            renameLabel(treeView.getRoot(), "");
        }
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    public void selectItem(){
        //select one TreeItem in TreeView

        TreeItem<Category> item = treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            //change information tabs Achievement or Category
            if(item.getValue() instanceof Achievement){
                //replace if other type of tab or size=0
                if(informationTabPane.getTabs().size()==0
                        || !informationTabPane.getTabs().get(0).equals(achievementTab)){
                    informationTabPane.getTabs().clear();
                    informationTabPane.getTabs().add(achievementTab);
                }
                achievementNameInfo.setText(item.getValue().getName());
            }else{
                //replace if other type of tab or size=0
                if(informationTabPane.getTabs().size()==0
                        || !informationTabPane.getTabs().get(0).equals(categoryTab)){
                    informationTabPane.getTabs().clear();
                    informationTabPane.getTabs().add(categoryTab);
                }
                int size = countChildren(item);
                categoryNameInfo.setText(item.getValue().toString());
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
        //first call is renameLabel(root, "")
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
                item.getChildren().get(i).getGraphic().getStyleClass().add("tree-cell-category");
                renameLabel(item.getChildren().get(i), text);
            }
        }
    }

    public void saveXMLFile() {
        //save TreeView as xml file
        (new XMLSaver(treeView)).save();
    }

    public void openXMLFile() {
        //open xml file as TreeView
        (new XMLOpener(treeView)).open("src/xml/1.xml");
    }

    public void createTab(ActionEvent actionEvent) {
        Tab tab;
        if(((MenuItem) actionEvent.getSource()).getId().equals("0")){
            tab = creationCategoryTab;
        }else if(((MenuItem) actionEvent.getSource()).getId().equals("1")) {
            tab = creationAchievementTab;
        }else{
            tab = creationChallengeTab;
        }
        if(informationTabPane.getTabs().size()==0
                || !informationTabPane.getTabs().get(0).equals(tab)){
            informationTabPane.getTabs().clear();
            informationTabPane.getTabs().add(tab);
        }
    }

    public void createNewElement(ActionEvent actionEvent) {
        String current = ((Button) actionEvent.getSource()).getId();
        switch (current) {
            case "category":
                if (!categoryName.getText().equals("")) {
                    Category category = new Category(categoryName.getText());
                    TreeItem<Category> parent = treeView.getRoot();
                    if (treeView.getSelectionModel().getSelectedItem() != null
                            && !(treeView.getSelectionModel().getSelectedItem().getValue() instanceof Achievement)) {
                        parent = treeView.getSelectionModel().getSelectedItem();
                    }
                    TreeItem<Category> newItem = new TreeItem<>(category, new Label());
                    parent.getChildren().add(newItem);
                    categoryName.clear();
                    renameLabel(treeView.getRoot(), "");
                }else {
                    System.out.println("category's name is null");
                }
                break;
            case "achievement":
                Date creationDate = Calendar.getInstance().getTime();
                if(achievementHasDateCreation.isSelected()){
                    try{
                        creationDate = new SimpleDateFormat("yyyy-MM-dd")
                                .parse(achievementDateCreationPicker
                                        .getValue()
                                        .toString());
                    }catch (ParseException e){
                        System.out.println("Ошибка при переводе даты");
                    }
                }
                if(!achievementName.getText().equals("")){
                    Achievement achievement = new Achievement(creationDate, achievementName.getText());
                    TreeItem<Category> newItem = new TreeItem<>(achievement, new Label());
                    TreeItem<Category> parent = treeView.getRoot();
                    if (treeView.getSelectionModel().getSelectedItem() != null
                            && !(treeView.getSelectionModel().getSelectedItem().getValue() instanceof Achievement)) {
                        parent = treeView.getSelectionModel().getSelectedItem();
                    }
                    parent.getChildren().add(newItem);
                    achievementName.clear();
                    achievementDateCreationPicker.getEditor().clear();
                    achievementHasDateCreation.setSelected(false);
                    achievementDateCreationPicker.setDisable(true);
                    renameLabel(treeView.getRoot(), "");
                }else {
                    System.out.println("achievement's name is null");
                }
                break;
            case "challenge":
                System.out.println("challenge");
                break;
        }
    }

    public void enableDatePicker(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(achievementHasDateCreation)){
            achievementDateCreationPicker.getEditor().clear();
            achievementDateCreationPicker.setDisable(!achievementHasDateCreation.isSelected());
        }
    }
}