package cool.zest.logicdemo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cool.zest.logicdemo.mapper.AtomicMapper;
import cool.zest.logicdemo.mapper.SentenceMapper;
import cool.zest.logicdemo.pojo.Atomic;
import cool.zest.logicdemo.pojo.Re.ReSentence;
import cool.zest.logicdemo.pojo.Re.Retags;
import cool.zest.logicdemo.pojo.PageBean;
import cool.zest.logicdemo.pojo.Sentence;
import cool.zest.logicdemo.service.SentenceService;
import cool.zest.logicdemo.utils.ThreadLocalUtil;
import cool.zest.logicdemo.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SentenceServiceImpl implements SentenceService {

    @Autowired
    private SentenceMapper sentenceMapper;
    @Autowired
    private AtomicMapper atomicMapper;
    @Override
    public void add(String text, String type, ReSentence reSentence) {
        //设置属性
        Sentence sentence=new Sentence();
        sentence.setSentence(text);
        sentence.setSentenceType(type);
        sentence.setCreateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        sentence.setCreateUser(userId);
        if(reSentence.isSuccess()){
            sentence.setState("成功");
            //添加句子
            sentenceMapper.add(sentence);
            //添加word
            if ((type.equals( "CTL")&&ValidatorUtil.containsTemporalOperator(text))||type.equals("NLP")) {
                Atomic atomic = new Atomic();
                for (Retags np : reSentence.getResult()) {
                    atomic.setWord(np.getWord());
                    atomic.setTags(np.getTags().toString());
                    atomic.setSentenceId(sentence.getId());
                    atomic.setCreateUser(userId);
                    atomicMapper.add(atomic);
                }
            }

        }else {
            sentence.setState("失败");
            sentenceMapper.add(sentence);
        }
    }

    @Override
    public void deleteById(Integer id) {
        sentenceMapper.deleteAtomicById(id);
        sentenceMapper.deleteSentenceById(id);
    }

    @Override
    public Sentence findById(Integer id) {
        return sentenceMapper.findById(id);
    }

    @Override
    public List<Atomic> findBySId(Integer id) {
        return sentenceMapper.findBySId(id);
    }

    @Override
    public PageBean<Sentence> listall(Integer pageNum, Integer pageSize, String type, String state) {

        //1.创建PageBean对象
        PageBean<Sentence> ps=new PageBean<>();

        //2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum,pageSize);

        //3.调用mapper
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Sentence> as = sentenceMapper.listall(userId,type,state);
        //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Sentence> p = (Page<Sentence>) as;

        //把数据填充到PageBean对象中
        ps.setTotal(p.getTotal());
        ps.setItems(p.getResult());
        return ps;
    }


    @Override
    public List<Sentence> list() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return sentenceMapper.list(userId);
    }
}
