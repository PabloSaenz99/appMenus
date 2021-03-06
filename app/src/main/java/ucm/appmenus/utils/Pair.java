package ucm.appmenus.utils;

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
    public void setSegundo(T2 segundo) {
        this.segundo = segundo;
    }

    public T1 getPrimero() {
        return primero;
    }
    public T2 getSegundo() {
        return segundo;
    }

    public String toString(){
        return "[" + primero.toString() + " , " + segundo.toString() + "]";
    }
}
