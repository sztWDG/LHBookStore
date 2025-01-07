package com.example.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.exception.EnumValueIsNotDefineException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum BookStatusEnum {
    De_List(0),
    En_List(1);


    @EnumValue
    private final int value;

    @JsonValue
    public int serialize() {
        return switch (this) {
            case En_List -> 1;
            case De_List -> 0;
        };
    }

    @JsonCreator
    public BookStatusEnum deserialize(int value) {
        return switch (value) {
            case 1 -> En_List;
            case 0 -> De_List;
            default -> throw new EnumValueIsNotDefineException();
        };
    }


}
