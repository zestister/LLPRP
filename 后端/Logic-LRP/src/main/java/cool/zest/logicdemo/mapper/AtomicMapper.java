package cool.zest.logicdemo.mapper;

import cool.zest.logicdemo.pojo.Atomic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AtomicMapper {

    //新增
    @Insert("insert into atomic(word,tags,sentence_id,create_user) " +
            "values(#{word},#{tags},#{sentenceId},#{createUser})")
    void add(Atomic atomic);
}
