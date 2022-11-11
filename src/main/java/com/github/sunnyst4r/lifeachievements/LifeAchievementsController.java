package com.github.sunnyst4r.lifeachievements;

import com.github.sunnyst4r.lifeachievements.Achievements.Achievement;
import com.github.sunnyst4r.lifeachievements.Achievements.Category;
import com.github.sunnyst4r.lifeachievements.Achievements.Challenge;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class LifeAchievementsController implements Initializable {
    @FXML
    public TabPane informationTabPane;
    @FXML
    private TreeView<Category> treeView;
    private TreeCell<Category> source;

    //get information tab
    @FXML
    private Tab achievementTab;
    @FXML
    private Tab challengeTab;
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
    final int MAX_CHALLENGE_DISTANCE = 10000;
    final int MIN_CHALLENGE_DISTANCE = 2;
    final int START_VALUE = 10;
    private final SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory
            .IntegerSpinnerValueFactory(MIN_CHALLENGE_DISTANCE, MAX_CHALLENGE_DISTANCE, START_VALUE);

    //get all components of category info
    @FXML
    private TextField categoryNameInfo;
    @FXML
    private Label countAchievements;
    @FXML
    private ProgressBar progressCategory;

    //get all components of achievement info
    @FXML
    private TextField achievementNameInfo;
    @FXML
    private CheckBox isDoneAchievement;
    @FXML
    private ProgressBar progressAchievement;
    @FXML
    private Label creationDateAchievementInfo;
    @FXML
    private Label endingDateAchievementInfo;
    @FXML
    private Label finishDateAchievementInfo;
    @FXML
    private TextArea achievementDescriptionInfo;

    //get all components of challenge info
    @FXML
    private ProgressBar progressChallenge;
    @FXML
    private TextField challengeNameInfo;
    @FXML
    private Label creationDateChallengeInfo;
    @FXML
    private Label endingDateChallengeInfo;
    @FXML
    private Label finishDateChallengeInfo;
    @FXML
    private TextArea challengeDescriptionInfo;
    @FXML
    private Label progressChallengeLabel;
    @FXML
    private Button minusButton;
    @FXML
    private Button plusButton;
    @FXML
    private Label lastRecord;
    @FXML
    private Button failButton;

    //add pseudo class for tree-cell
    private final PseudoClass CATEGORY = PseudoClass.getPseudoClass("category");
    private final PseudoClass DONE = PseudoClass.getPseudoClass("done");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //add stylesheet to TreeView
        treeView.getStylesheets().add(
                Objects.requireNonNull(
                        LifeAchievementsApplication.class.getResource("style.css"))
                        .toExternalForm()
        );

        //load file that we saved before
        (new XMLOpener(treeView)).open("src/xml/2.xml");

        //create new cell factory for pseudo class and drag & drop
        treeView.setCellFactory(tv -> {
            //creating cell with pseudo class
            TreeCell<Category> treeCell = new TreeCell<>(){
                @Override
                public void updateItem(Category item, boolean empty){
                    //default updater cell
                    super.updateItem(item, empty);
                    if(empty){
                        //if cell is empty, we delete all information inside it
                        setText("");
                        setGraphic(null);
                        //set pseudo class category to false
                        pseudoClassStateChanged(CATEGORY, false);
                        pseudoClassStateChanged(DONE, false);
                    }else{
                        //set Label into graphic of cell
                        setGraphic(getTreeItem().getGraphic());
                        //set text of cell
                        setText(item.getName());
                        //if cell have an item instanceof category, we set pseudo class active
                        pseudoClassStateChanged(CATEGORY, !(item instanceof Achievement));
                        //set if isDone
                        if(item instanceof Achievement achievement){
                            pseudoClassStateChanged(DONE, achievement.isDone());
                        }else{
                            pseudoClassStateChanged(DONE, false);
                        }
                    }
                }
            };
            //setting handlers for drag & drop
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
        //add spinner value factory into spinner
        challengeDistance.setValueFactory(spinnerValueFactory);
        //rename all index
        renameLabel(treeView.getRoot(), "");
    }

    private void onDragDetected(MouseEvent event) {
        //start dragging

        //select current item
        selectItem();

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
            target.pseudoClassStateChanged(CATEGORY, false);
            target.pseudoClassStateChanged(DONE, false);
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

            if(target.getTreeItem().getValue() != null){
                target.pseudoClassStateChanged(CATEGORY, !(target.getTreeItem().getValue() instanceof Achievement));
                if(target.getTreeItem().getValue() instanceof Achievement achievement){
                    target.pseudoClassStateChanged(DONE, achievement.isDone());
                }
            }
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

            //rename all Label in TreeItem after dropping into other place
            renameLabel(treeView.getRoot(), "");
        }
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    public void selectItem(){
        //select one TreeItem in TreeView

        TreeItem<Category> item = treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            //change information tabs Achievement, Challenge or Category
            createTab(new ActionEvent());
            if(item.getValue() instanceof Challenge challenge){
                challengeNameInfo.setText(challenge.getName());
                //set progress bar
                progressChallenge.setProgress(
                        challenge.getCurrentStreak() / (float) challenge.getDistance()
                );
                //set creation date
                creationDateChallengeInfo.setText(String.valueOf(
                        LocalDate.ofInstant(challenge.getCreatingDate().toInstant(), ZoneId.systemDefault())
                ));
                //set ending date
                if(challenge.getEndingDate() != null){
                    endingDateChallengeInfo.setText(String.valueOf(
                            LocalDate.ofInstant(challenge.getEndingDate().toInstant(), ZoneId.systemDefault())
                    ));
                }else{
                    endingDateChallengeInfo.setText("(нет)");
                }
                //set finish date
                if(challenge.getFinish() != null){
                    finishDateChallengeInfo.setText(String.valueOf(
                            LocalDate.ofInstant(challenge.getFinish().toInstant(), ZoneId.systemDefault())
                    ));
                }else{
                    finishDateChallengeInfo.setText("(нет)");
                }
                //set description
                challengeDescriptionInfo.setText(challenge.getDescription());
                //set current streak or progress
                progressChallengeLabel.setText(challenge.getCurrentStreak() + " / " + challenge.getDistance());
                progressChallengeLabel.setAlignment(Pos.BASELINE_CENTER);
                //set record
                lastRecord.setText(String.valueOf(challenge.getRecord()));

            }else if(item.getValue() instanceof Achievement achievement){
                achievementNameInfo.setText(achievement.getName());
                isDoneAchievement.setSelected(achievement.isDone());
                progressAchievement.setProgress(achievement.isDone()? 1:0);
                //set creation date
                creationDateAchievementInfo.setText(String.valueOf(
                        LocalDate.ofInstant(achievement.getCreatingDate().toInstant(), ZoneId.systemDefault())
                ));
                achievementDescriptionInfo.setText(achievement.getDescription());
                //set ending date
                if(achievement.getEndingDate() != null){
                    endingDateAchievementInfo.setText(String.valueOf(
                            LocalDate.ofInstant(achievement.getEndingDate().toInstant(), ZoneId.systemDefault())
                    ));
                }else{
                    endingDateAchievementInfo.setText("(нет)");
                }
                //set finish date
                if(achievement.getFinish() != null){
                    finishDateAchievementInfo.setText(String.valueOf(
                            LocalDate.ofInstant(achievement.getFinish().toInstant(), ZoneId.systemDefault())
                    ));
                }else{
                    finishDateAchievementInfo.setText("(нет)");
                }
            }else{
                int size = countChildren(item, false);
                categoryNameInfo.setText(item.getValue().toString());
                countAchievements.setText(String.valueOf(size));
                progressCategory.setProgress(
                        countChildren(item, true) / (float) countChildren(item, false)
                );
            }
        }
    }

    private int countChildren(TreeItem<Category> treeItem, boolean onlyDone){
        //count all Achievement or Challenge in one Category
        if(treeItem.getValue() instanceof Achievement){
            return onlyDone? (((Achievement) treeItem.getValue()).isDone()? 1:0):1;
        }else{
            int size = 0;
            for(TreeItem<Category> item : treeItem.getChildren()){
                size += countChildren(item, onlyDone);
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
        //rename all Label in order like in example (1.1.4 or 2.5.10.2)
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

    public void saveXMLFile() {
        //save TreeView as xml file
        (new XMLSaver(treeView)).save("src/xml/2.xml");
    }

    public void openXMLFile() {
        //open xml file as TreeView
        (new XMLOpener(treeView)).open("src/xml/2.xml");
    }

    public void createTab(ActionEvent actionEvent) {
        //create tab of creation on the right side of scene
        Tab tab = new Tab();
        //add information tabs
        if(treeView.getSelectionModel().getSelectedItem() != null){
            TreeItem<Category> item = treeView.getSelectionModel().getSelectedItem();
            if(item.getValue() instanceof Challenge){
                tab = challengeTab;
            }else if(item.getValue() instanceof Achievement){
                tab = achievementTab;
            }else{
                tab = categoryTab;
            }
        }
        //add creation tabs
        if(actionEvent.getSource() instanceof MenuItem){
            if(((MenuItem) actionEvent.getSource()).getId().equals("0")){
                tab = creationCategoryTab;
            }else if(((MenuItem) actionEvent.getSource()).getId().equals("1")) {
                tab = creationAchievementTab;
            }else if(((MenuItem) actionEvent.getSource()).getId().equals("2")){
                tab = creationChallengeTab;
            }
        }
        //if side TabPane is clear or have another tab of creation, we clear and add new tab
        if(informationTabPane.getTabs().size()==0
                || !informationTabPane.getTabs().get(0).equals(tab)){
            informationTabPane.getTabs().clear();
            informationTabPane.getTabs().add(tab);
        }
    }

    public void createNewElement(ActionEvent actionEvent) {
        //creation new Category, Achievement or Challenge inside parent TreeItem

        String current = ((Button) actionEvent.getSource()).getId();
        //first need params
        String name;
        Date creationDate = Calendar.getInstance().getTime();
        int distance;
        //not necessary params
        Date endDate= null;
        String description;
        //new item
        Category item = null;
        //parent TreeItem
            //root item by default
        TreeItem<Category> parent = treeView.getRoot();
        if (treeView.getSelectionModel().getSelectedItem() != null) {
            //if selected item is achievement or challenge,then parent is his parent
            if(treeView.getSelectionModel().getSelectedItem().getValue() instanceof Achievement){
                parent = treeView.getSelectionModel().getSelectedItem().getParent();
            }else{
                //if selected item is category, parent is him
                parent = treeView.getSelectionModel().getSelectedItem();
            }
        }
        //where is a button places?
        switch (current) {
            //if category we need: name
            case "category" -> {
                name = categoryName.getText();
                //check necessary params not null
                if (name.equals("")) {
                    System.out.println("category's name is null");
                } else {
                    item = new Category(name);
                }
            }
            //if achievement we need: name, creationDate
            case "achievement" -> {
                name = achievementName.getText();
                //check datePickers not null
                if (achievementHasDateCreation.isSelected()){
                    creationDate = Date.from(achievementDateCreationPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                }
                if (achievementHasDateEnding.isSelected()) {
                    endDate = Date.from(achievementDateEndingPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                }
                description = achievementDescription.getText();
                //check necessary params not null
                if (name.equals("")) {
                    System.out.println("achievement's name is null");
                } else {
                    item = new Achievement(creationDate, endDate, name, description);
                }
            }
            //if challenge we need: name, creationDate, distance
            case "challenge" -> {
                name = challengeName.getText();
                //check datePickers not null
                if (challengeHasDateCreation.isSelected()){
                    creationDate = Date.from(challengeDateCreationPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                }
                if (challengeHasDateEnding.isSelected()) {
                    endDate = Date.from(challengeDateEndingPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                }
                description = challengeDescription.getText();
                distance = challengeDistance.getValue();
                //check necessary params not null
                if (name.equals("")) {
                    System.out.println("challenge's name is null");
                } else if (distance == 0) {
                    System.out.println("challenge's distance equals 0");
                } else {
                    item = new Challenge(creationDate, endDate, name, description, distance);
                }
            }
        }

        if(item!=null){
            //if item is created,we put it inside parent
            TreeItem<Category> newItem = new TreeItem<>(item, new Label());
            parent.getChildren().add(newItem);
            //reset all fields
            clearAndDisableAll();
        }
    }

    public void enableDatePicker(ActionEvent actionEvent) {
        //unlock datePicker if they check box is selected

        DatePicker currentDatePicker = new DatePicker();
        //find a right one DatePicker
        if(actionEvent.getSource().equals(achievementHasDateCreation)){
            currentDatePicker = achievementDateCreationPicker;
        }else if(actionEvent.getSource().equals(achievementHasDateEnding)){
            currentDatePicker = achievementDateEndingPicker;
        }else if(actionEvent.getSource().equals(challengeHasDateCreation)){
            currentDatePicker = challengeDateCreationPicker;
        }else if(actionEvent.getSource().equals(challengeHasDateEnding)){
            currentDatePicker = challengeDateEndingPicker;
        }
        //set default date inside DatePicker
        currentDatePicker.setValue(LocalDate.ofInstant(
                Calendar.getInstance().toInstant(),
                ZoneId.systemDefault()
        ));
        //set default visible text
        currentDatePicker.getEditor().setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        //unlock DatePicker
        currentDatePicker.setDisable(!((CheckBox) actionEvent.getSource()).isSelected());
    }

    private void clearAndDisableAll(){
        //function for reset all field on scene

        //clear all field on category creation tab
        categoryName.clear();
        //clear all field on achievement creation tab
        achievementName.clear();
            //creation date
        achievementHasDateCreation.setSelected(false);
        achievementDateCreationPicker.setDisable(true);
        achievementDateCreationPicker.getEditor().setText(
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        achievementDateCreationPicker.setValue(LocalDate.now());
            //ending date
        achievementHasDateEnding.setSelected(false);
        achievementDateEndingPicker.setDisable(true);
        achievementDateEndingPicker.setValue(null);
        achievementDateEndingPicker.getEditor().setText("(нет)");
        //clear all field on challenge creation tab
        challengeName.clear();
            //creation date
        challengeHasDateCreation.setSelected(false);
        challengeDateCreationPicker.setDisable(true);
        challengeDateCreationPicker.getEditor().setText(
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        challengeDateCreationPicker.setValue(LocalDate.now());
            //ending date
        challengeHasDateEnding.setSelected(false);
        challengeDateEndingPicker.setDisable(true);
        challengeDateEndingPicker.setValue(null);
        challengeDateEndingPicker.getEditor().setText("(нет)");
            //distance
        challengeDistance.getValueFactory().setValue(START_VALUE);
        //rename all label
        renameLabel(treeView.getRoot(), "");
    }

    public void changeChallengeProgress(ActionEvent actionEvent) {
        Challenge challenge = (Challenge) treeView.getSelectionModel().getSelectedItem().getValue();
        if(actionEvent.getSource().equals(minusButton)){
            if(challenge.getCurrentStreak() > 0){
                challenge.setCurrentStreak(challenge.getCurrentStreak() - 1);
                if(challenge.getDistance() - challenge.getCurrentStreak() == 1){
                    challenge.setFinish(null);
                    challenge.setDone(false);
                }
            }
        }else if(actionEvent.getSource().equals(plusButton)){
            if(challenge.getCurrentStreak()<challenge.getDistance()){
                challenge.setCurrentStreak(challenge.getCurrentStreak() + 1);
                if(challenge.getCurrentStreak() == challenge.getDistance()){
                    challenge.iAmDone();
                }
            }
        }
        selectItem();
    }

    public void doneAchievement(ActionEvent actionEvent) {
        Achievement achievement = (Achievement) treeView.getSelectionModel().getSelectedItem().getValue();
        if(actionEvent.getSource().equals(isDoneAchievement)){
            if(isDoneAchievement.isSelected()){
                achievement.iAmDone();
            }else{
                achievement.setDone(false);
                achievement.setFinish(null);
            }
        }
        selectItem();
    }
}