package pl.edu.pw.ii.DBBrowser.RequestProcessor.Transport;

/**
 * Created by kokoss on 5/31/14.
 */
public class HttpResponse {
    boolean complete;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public static HttpResponse incomplete() {
        HttpResponse incomplete = new HttpResponse();
        incomplete.setComplete(false);
        return incomplete;
    }
}
