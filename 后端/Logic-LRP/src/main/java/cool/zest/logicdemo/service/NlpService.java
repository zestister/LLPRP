package cool.zest.logicdemo.service;

import com.aliyuncs.exceptions.ClientException;

public interface NlpService {
    String GetWsChGeneral(String text,  String outType) throws ClientException;
    String getCost() ;
    String getrequstId();

}

