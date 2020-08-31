package Element;

/**Classe fornecida pelo professor.
 * 
 * @author Luiz Eduardo Virgilio da Silva.
 */
public abstract class Element {

    protected boolean isTransposable;

    public Element() {
        this.isTransposable = true;
    }

    public final boolean IsTransposable() {
        return this.isTransposable;
    }

    public final void setTransposable(boolean isTransposable) {
        this.isTransposable = isTransposable;
    }

}
