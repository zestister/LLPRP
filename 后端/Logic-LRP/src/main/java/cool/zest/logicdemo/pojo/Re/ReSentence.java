package cool.zest.logicdemo.pojo.Re;

import lombok.Data;

import java.util.List;
@Data
public class ReSentence {

    private List<Retags> result;
    private boolean success;
    private String type;
    private String cost;

}

