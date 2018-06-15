package wizrole.hosmerchants.my.preserent.regist;

import wizrole.hosmerchants.my.model.regist.RegistBack;
import wizrole.hosmerchants.my.model.regist.RegistBacktInterface;
import wizrole.hosmerchants.my.model.regist.RegistHttp;

/**
 * Created by liushengping on 2017/12/11/011.
 * 何人执笔？
 */

public class RegistPreserent implements RegistBacktInterface{

    public RegistInerface registInerface;
    public RegistHttp registHttp;
    public RegistPreserent(RegistInerface registInerface){
        this.registInerface=registInerface;
        registHttp=new RegistHttp(this);
    }

    public void Http(String tel,String pass,String email){
        registHttp.regist(tel,pass,email);
    }
    @Override
    public void Succ(Object o) {
        registInerface.getDataSucc((RegistBack) o);
    }

    @Override
    public void Fail(String msg) {
        registInerface.getDataFail(msg);
    }
}
