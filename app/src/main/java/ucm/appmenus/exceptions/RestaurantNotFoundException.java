package ucm.appmenus.exceptions;

public class RestaurantNotFoundException extends Exception{

    public RestaurantNotFoundException() {
        super();
    }

    public RestaurantNotFoundException(String msg) {
        super(msg);
    }

    public RestaurantNotFoundException(String msg, Throwable causa) {
        super(msg, causa);
    }

    public RestaurantNotFoundException(Throwable causa) {
        super(causa);
    }

    public RestaurantNotFoundException(String msg, Throwable causa, boolean enableSuppression, boolean writableStackTrace) {
        super(msg, causa, enableSuppression, writableStackTrace);
    }
}
