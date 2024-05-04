package cool.zest.logicdemo.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alinlp.model.v20200629.GetWsChGeneralRequest;
import com.aliyuncs.alinlp.model.v20200629.GetWsChGeneralResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import cool.zest.logicdemo.service.NlpService;
import org.springframework.stereotype.Service;

@Service
public class NlpServiceImpl implements NlpService {
//    @Autowired
//    private AtomicMapper atomicMapper;
    // 阿里云API访问凭证信息，加入KeyId，accessKeySecret
    
    
    private final String endpoint = "alinlp.cn-hangzhou.aliyuncs.com";
    private final String serviceCode = "alinlp";
    private final String tokenizerId = "GENERAL_CHN";
    GetWsChGeneralResponse response=new GetWsChGeneralResponse();
    String cost;

    @Override
    // 处理自然语言处理请求并调用阿里云API
    public String GetWsChGeneral(String text,  String outType) throws ClientException {
        // 使用阿里云SDK创建客户端
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        // 创建请求对象并设置参数
        GetWsChGeneralRequest request = new GetWsChGeneralRequest();
        request.setSysEndpoint(endpoint);
        request.setServiceCode(serviceCode);
        request.setText(text);
        if (tokenizerId != null) {
            request.setTokenizerId(tokenizerId);
        }
        if (outType != null) {
            request.setOutType(outType);
        }

        long start = System.currentTimeMillis();
        // 调用阿里云返回数据并获取响应
        response = client.getAcsResponse(request);
        cost= (System.currentTimeMillis() - start) / 1000.0 +"s";

        return response.getData();
    }
    @Override
    public String getCost() {
        return  cost;
    }
    @Override
    public String getrequstId() {
        return response.getRequestId();
    }


}
