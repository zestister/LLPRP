package cool.zest.logicdemo.mapper;

import cool.zest.logicdemo.pojo.Atomic;
import cool.zest.logicdemo.pojo.Sentence;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SentenceMapper {
    //新增
    @Insert("insert into sentence(sentence,sentence_type,create_user,create_time,state) " +
            "values(#{sentence},#{sentenceType},#{createUser},#{createTime},#{state})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Sentence allSentence);

    //查询所有
    @Select("select * from sentence where create_user = #{userId}")
    List<Sentence> list(Integer userId);

    //根据id删除
    @Delete("delete from atomic where sentence_id=#{id}")
    void deleteById(Integer id);
    @Delete("delete from atomic where sentence_id=#{id}")
    void deleteAtomicById(Integer id);

    @Delete("delete from sentence where id=#{id}")
    void deleteSentenceById(Integer id);


    //根据id查询
    @Select("select * from sentence where id = #{id}")
    Sentence findById(Integer id);

    @Select("select * from atomic where sentence_id = #{id}")
    List<Atomic> findBySId(Integer id);

    List<Sentence> listall(Integer userId, String type, String state);
}
