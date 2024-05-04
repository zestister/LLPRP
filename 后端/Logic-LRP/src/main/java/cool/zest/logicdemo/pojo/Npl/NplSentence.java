package cool.zest.logicdemo.pojo.Npl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cool.zest.logicdemo.pojo.Npl.Npltags;
import lombok.Data;

import java.util.List;
@Data
public class NplSentence {

    private List<Npltags> result;
    private boolean success;
    private String cost;
}

