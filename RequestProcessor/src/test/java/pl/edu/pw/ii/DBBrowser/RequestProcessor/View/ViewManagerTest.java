package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kokoss on 5/31/14.
 */
public class ViewManagerTest {

    @Test
    public void testViewScanning(){
        ViewManager viewManager = new ViewManager();
        assertNotNull(viewManager.getViews().get("/"));
    }

}
