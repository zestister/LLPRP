package cool.zest.logicdemo.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.zest.logicdemo.pojo.Atomic;
import cool.zest.logicdemo.pojo.Re.ReSentence;
import cool.zest.logicdemo.pojo.Re.Retags;
import cool.zest.logicdemo.pojo.PageBean;
import cool.zest.logicdemo.pojo.Result;
import cool.zest.logicdemo.pojo.Sentence;
import cool.zest.logicdemo.service.NlpService;
import cool.zest.logicdemo.service.SentenceService;
import cool.zest.logicdemo.utils.CTLOSUtil;
import cool.zest.logicdemo.utils.MuOSUtil;
import cool.zest.logicdemo.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/llrp")
public class SentenceController {

    // 注入NlpService
    @Autowired
    private NlpService nlpService;
    @Autowired
    private SentenceService sentenceService;

    // 处理POST请求，接收text和outType参数
    @PostMapping
    public Result<?> GetSearch(@RequestParam String text,
                               @RequestParam String type,
                               @RequestParam(required = false) String outType) throws ClientException {
        // 记录开始时间
        long startTime = System.nanoTime();
        ReSentence reSentence = new ReSentence();
        List<Sentence> ss = sentenceService.list();
        //查看数据库中是否有相同的查询
        boolean isExist = false;
        for (Sentence s : ss) {
            if (s.getSentence().equals(text))
            {
                // 判断是否存在相等的情况
                isExist = true;
                break;
            }
        }
        switch (type) {
            case "NLP" -> {
                // 调用NlpService的GetWsChGeneral方法处理自然语言处理请求
                String data = nlpService.GetWsChGeneral(text, outType);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    reSentence = objectMapper.readValue(data, ReSentence.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return Result.error("解析数据失败,错误操作代码：" + nlpService.getrequstId());
                }
            }
            case "CTL" -> {
                if (!ValidatorUtil.isValidFormula(text)) {
                    return Result.error("输入字符串含非法字符，请检查输入格式，或查看符号表");
                }
                //调用系统
                String data = CTLOSUtil.CTLOS(text);
                ObjectMapper objectMapper = new ObjectMapper();
                //格式化
                data = ValidatorUtil.parseData(data);

                    if (!ValidatorUtil.containsTemporalOperator(text)) {
                        reSentence.setSuccess(true);
                    } else {
                        try {
                            reSentence = objectMapper.readValue(data, ReSentence.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return Result.error("解析数据失败,错误操作代码：1111");
                        }
                    }

            }

            case "Mu-" -> {
                if (ValidatorUtil.isValidFormula(text)) {
                //调用系统
                    reSentence.setSuccess(MuOSUtil.MuOS(text));
                } else
                    return Result.error("输入字符串含非法字符，请检查输入格式，或查看符号表");
            }
            case "Auto" ->{
                if (!ValidatorUtil.isValidFormula(text)) {
                    return Result.error("输入字符串含非法字符，请检查输入格式，或查看符号表");
                }
                //首先进行CTL识别
                String data = CTLOSUtil.CTLOS(text);
                ObjectMapper objectMapper = new ObjectMapper();
                data = ValidatorUtil.parseData(data);
                type = "CTL";
                //再进行Mu-演算识别
                boolean isMuOS = MuOSUtil.MuOS(text);
                boolean hasTemporalOperator = ValidatorUtil.containsTemporalOperator(text);
                try {
                    reSentence = objectMapper.readValue(data, ReSentence.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return Result.error("解析数据失败,错误操作代码：1111");
                }
                if (reSentence.isSuccess()){
                    if(!hasTemporalOperator){
                        type = "CTL&Mu-";
                        reSentence.setResult(null);
                        reSentence.setSuccess(true);
                    }
                }else if (isMuOS) {
                    type = "Mu-";
                    reSentence.setResult(null);
                    reSentence.setSuccess(true);
                } else {
                    type = "CTL&Mu-";
                    reSentence.setSuccess(false);
                }
            }
            default -> {
                return Result.error("非法类型输入");
            }
        }
        if (!isExist) {
            sentenceService.add(text, type, reSentence);
        }
        // 记录结束时间
        long endTime = System.nanoTime();
        // 计算消耗时间（纳秒）
        long durationInNano = endTime - startTime;
        // 将纳秒转换为秒
        double durationInSec = (double) durationInNano / 1e9;
        reSentence.setCost(String.format("%.3f", durationInSec));
        reSentence.setType(type);
        return Result.success(reSentence);
    }

    @GetMapping("/history")
    public Result<PageBean<Sentence>> HistoryList(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String state
    ) {
        PageBean<Sentence> ss = sentenceService.listall(pageNum,pageSize,type,state);
        return Result.success(ss);
    }
    @GetMapping("/detail")
    public Result<List<Atomic>> detail(Integer id){
        List<Atomic> a= sentenceService.findBySId(id);
        return Result.success(a);
    }

    @DeleteMapping
    public Result delete(Integer id){
        sentenceService.deleteById(id);
        return Result.success();
    }
}
