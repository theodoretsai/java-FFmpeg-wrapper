package testModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FragmentStyleEnum {

    IN_FRAME(0, "嵌入"),
    FULL_SCREEN(1, "全部"),

    OVERLAY(10,"覆盖"),
    CONCAT(11,"链接");


    private Integer code;
    private String desc;

}