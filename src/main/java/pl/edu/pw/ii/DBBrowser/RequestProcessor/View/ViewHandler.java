package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;

import java.lang.annotation.*;

/**
 * Created by Bartosz Andrzejczak on 5/31/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewHandler {
    String path();
}
