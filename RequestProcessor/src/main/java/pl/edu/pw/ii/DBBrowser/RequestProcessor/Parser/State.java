package pl.edu.pw.ii.DBBrowser.RequestProcessor.Parser;

/**
 * Created by lucas on 02.06.14.
 */
public enum State {
    INITIAL {
        private boolean terminal = false;
        public boolean isTerminal() {return terminal;}
        public State nextState(char ch) {
            switch(ch) {
                default: return INITIAL;
            }
        }
    };

    public abstract boolean isTerminal();
    public abstract State nextState(char ch);
}
