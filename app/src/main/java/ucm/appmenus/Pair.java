package ucm.appmenus;

public class Pair<T1, T2> {

    private T1 primero;
    private T2 segundo;

    public Pair(T1 primero, T2 segundo){
        this.primero = primero;
        this.segundo = segundo;
    }

    public void setPrimero(T1 primero) {
        this.primero = primero;
    }

    public T1 getPrimero() {
        return primero;
    }

    public void setSegundo(T2 segundo) {
        this.segundo = segundo;
    }

    public T2 getSegundo() {
        return segundo;
    }
}
