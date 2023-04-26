package org.zsz.mybatiscodegen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsz
 * @version 1.0
 * @create 2023/4/26 17:05
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorProperty {

    private String name;
    private String value;
}
