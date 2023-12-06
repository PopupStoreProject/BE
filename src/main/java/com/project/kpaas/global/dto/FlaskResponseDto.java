package com.project.kpaas.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class FlaskResponseDto {

    private List<Long> popupIds;
    private List<String> contents;

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(String title : this.contents){
            result.append(", ").append(title);
        }
        return result.toString();
    }
}
