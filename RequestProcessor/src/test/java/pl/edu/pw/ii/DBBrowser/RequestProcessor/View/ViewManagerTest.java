package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
public class ViewManagerTest {

    @Test
    public void testViewScanning(){
        assertNotNull(ViewManager.getInstance().getViews().get("/"));
    }

}
