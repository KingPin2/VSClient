package main.classes;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kane- on 24.09.2017.
 */
public class IconButtonFXMLController extends Node {

    private Image image;
    @FXML
    private Button b;

    @FXML
    private ImageView icon;

    public IconButtonFXMLController(Image image)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/IconButton.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.image = image;
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {

        }
    }



    public void initialize(URL url, ResourceBundle rb)
    {
        icon.setImage(image);
    }

    @Override
    protected NGNode impl_createPeer() {
        return null;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return null;
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return null;
    }
}
