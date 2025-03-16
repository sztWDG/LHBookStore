package com.example.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.exception.EnumValueIsNotDefineException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEvents {

    ORDER(0),
    CANCEL(1),
    PAY(2),
    DELIVER(3),
    CONFIRM(4),
    FINISH(5);

    @EnumValue
    private final int value;

    @JsonValue
    public int serialize() {
        return switch (this) {
            case ORDER -> 0;
            case CANCEL -> 1;
            case PAY -> 2;
            case DELIVER -> 3;
            case CONFIRM -> 4;
            case FINISH -> 5;
        };
    }

    //前端到后端代码的反序列化
    @JsonCreator
    public OrderEvents deserialize(int value) {
        return switch (value) {
            case 0-> ORDER;
            case 1-> CANCEL;
            case 2-> PAY;
            case 3-> DELIVER;
            case 4-> CONFIRM;
            case 5-> FINISH;
            default -> throw new EnumValueIsNotDefineException();
        };
    }
}
