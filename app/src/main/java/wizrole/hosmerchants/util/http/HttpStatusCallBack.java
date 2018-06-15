package wizrole.hosmerchants.util.http;

/**
 * Created by liushengping on 2017/11/28/028.
 * 何人执笔？
 */

public interface HttpStatusCallBack<T> {

    void onSuccess(T result);

    void onError(String e);
}
