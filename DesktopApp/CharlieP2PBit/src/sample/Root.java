package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Root {

    private AnchorPane anchorPane;

    public Root() {
        anchorPane = new AnchorPane();
    }

    public Pane getRootPane() {
        return anchorPane;
    }
}
