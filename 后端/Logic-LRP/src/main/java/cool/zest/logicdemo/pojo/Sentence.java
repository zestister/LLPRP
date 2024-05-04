package cool.zest.logicdemo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Sentence {

    private Integer id;//主键ID
    @NotEmpty
    private String sentence;//语句
    @NotEmpty
    private String sentenceType;//分类
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    private  String state;//状态

}
