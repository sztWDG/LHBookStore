package com.example.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.exception.EnumValueIsNotDefineException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    ORDERED(0),
    CANCELLED(1),
    PAYED(2),
    DELIVERED(3),
    RECEIVED(4),
    FINISHED(5);


    @EnumValue
    private final int value;

    @JsonValue
    public int serialize() {
        return switch (this) {
            case ORDERED -> 0;
            case CANCELLED -> 1;
            case PAYED -> 2;
            case DELIVERED -> 3;
            case RECEIVED -> 4;
            case FINISHED -> 5;
        };
    }

    //前端到后端代码的反序列化
    @JsonCreator
    public OrderStatus deserialize(int value) {
        return switch (value) {
            case 0-> ORDERED;
            case 1-> CANCELLED;
            case 2-> PAYED;
            case 3-> DELIVERED;
            case 4-> RECEIVED;
            case 5-> FINISHED;
            default -> throw new EnumValueIsNotDefineException();
        };
    }



}
