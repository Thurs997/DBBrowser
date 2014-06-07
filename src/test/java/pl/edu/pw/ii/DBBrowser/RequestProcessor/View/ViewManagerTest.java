package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ViewManagerTest {

    @Test
    public void testViewScanning(){
        assertNotNull(ViewManager.getInstance().getViews().get("/"));
    }

}
