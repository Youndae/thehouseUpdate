package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ColorVO {
    private String colorCode;
    private String colorType;
}
