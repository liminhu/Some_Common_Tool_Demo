package design.pattern;

import java.io.Serializable;

/**
 * Created by hu on 3/2/18.
 */

public class Singleton implements Serializable {
    private static final long serrialVersionUID=1L;
    static Singleton sInstance =new Singleton();

    public Singleton() {
    }


    private Object readResolve() {
        return sInstance;
    }

}
