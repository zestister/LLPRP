package cool.zest.logicdemo.service;

import cool.zest.logicdemo.pojo.Atomic;
import cool.zest.logicdemo.pojo.Re.ReSentence;
import cool.zest.logicdemo.pojo.PageBean;
import cool.zest.logicdemo.pojo.Sentence;

import java.util.List;

public interface SentenceService {
    //列表查询
    List<Sentence> list();

    //添加记录
    void add(String text, String type, ReSentence reSentence);

    //通过id删除
    void deleteById(Integer id);

    //通过id查找
    Sentence findById(Integer id);

    //通过id查找
    List<Atomic> findBySId(Integer id);
    //条件分页
    PageBean<Sentence> listall(Integer pageNum, Integer pageSize, String type, String state);
}
