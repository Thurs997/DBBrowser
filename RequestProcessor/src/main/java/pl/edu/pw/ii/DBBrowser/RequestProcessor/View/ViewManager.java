package pl.edu.pw.ii.DBBrowser.RequestProcessor.View;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport.HttpRequest;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.logging.Logger;


/**
 * Created by kokoss on 5/31/14.
 */
public class ViewManager {

    public static final String VIEWS_PACKAGE = "pl.edu.pw.ii.DBBrowser.View";
    Map<String, Class<View>> views;
    Logger logger;

    public Map<String, Class<View>> getViews() {
        return views;
    }

    public ViewManager(){
        logger = Logger.getLogger(ViewManager.class.getName());
        scanForViews();
    }

    private void scanForViews() {
        logger.info("Scanning for available views in package "+VIEWS_PACKAGE);
        views = new HashMap<String, Class<View>>();
        Set<Class<?>> annotatedClasses = getClassesAnnotatedWithViewHandler();
        for(Class<?> annotatedClass : annotatedClasses){
            analyzeClass(annotatedClass);
        }
    }

    private Set<Class<?>> getClassesAnnotatedWithViewHandler() {
        Reflections reflections = new Reflections(VIEWS_PACKAGE, new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(ViewHandler.class);
    }

    private void analyzeClass(Class<?> annotatedClass) {
        logger.info("Analyzing class "+annotatedClass.getCanonicalName());
        if(!implementsView(annotatedClass)) return;
        String name = getViewHandlerAnnotationName(annotatedClass);
        if (isDuplicate(annotatedClass, name))
            return;
        views.put(name, (Class<View>) annotatedClass);
        logger.info("View added: Path="+name+", Class="+annotatedClass.getCanonicalName());
    }

    private boolean implementsView(Class<?> annotatedClass) {
        Class<?>[] interfaces = annotatedClass.getInterfaces();
        for(Class<?> interfaceClass : interfaces)
            if(interfaceClass.equals(View.class))
                return true;
        logger.info("Class "+annotatedClass.getCanonicalName()+" doesn't implement" +
                "interface "+View.class.getCanonicalName());
        return false;
    }

    private String getViewHandlerAnnotationName(Class<?> annotatedClass) {
        Annotation[] annotations = annotatedClass.getAnnotations();
        for(Annotation annotation : annotations) {
            try {
                ViewHandler viewHandler = (ViewHandler) annotation;
                if (viewHandler.path() != null && !viewHandler.path().equals(""))
                    return viewHandler.path();
                break;
            } catch (ClassCastException e) {
                continue;
            }
        }
        logger.info("Class "+annotatedClass.getCanonicalName()+" cannot have empty path" +
                "property in annotation "+ViewHandler.class.getCanonicalName());
        return null;
    }

    private boolean isDuplicate(Class<?> annotatedClass, String name) {
        if(views.get(name) != null){
            logger.info("View path conflict! Classes "+views.get(name).getCanonicalName()+
                    " and "+annotatedClass.getCanonicalName()+" both have path set to \""+name+"\"");
            return true;
        }
        return false;
    }

    public boolean isView(HttpRequest request) {
        return (views.get(request.getPath()) != null);
    }

    public String getView(HttpRequest request) {
        try {
            View view = views.get(request.getPath()).newInstance();
            return view.getView(request);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }
}
