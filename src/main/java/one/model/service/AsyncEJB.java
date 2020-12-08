package one.model.service;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.util.concurrent.Future;

@Stateless
@Asynchronous
public class AsyncEJB {
    public Future<Integer> addNumbers(int n1, int n2) {
        Integer result;

        result = n1 + n2;
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return new AsyncResult(result);
    }
}
