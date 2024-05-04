package cool.zest.logicdemo.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Atomic {
    private Integer id;//主键ID
    @NotEmpty
    private String word;//词语，TYPE，原子语句
    private String tags;//标签，单词
    @NotNull
    private Integer sentenceId;//分类id
    private Integer createUser;//创建人ID

}
