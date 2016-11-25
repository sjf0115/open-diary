package com.sjf.open.json.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.sjf.open.json.annotation.Excluded;


/**
 * Created by xiaosi on 16-11-17.
 */
public class UserExcludedStrategy implements ExclusionStrategy{

    // 需要排除的类型
    private final Class<?> typeToSkip;

    public UserExcludedStrategy() {
        this(null);
    }

    public UserExcludedStrategy(Class<?> typeToSkip) {
        this.typeToSkip = typeToSkip;
    }

    // 需要排除的属性
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        //如果属性带有Excluded注解则排除
        return fieldAttributes.getAnnotation(Excluded.class) != null;
    }

    // 需要排除的类
    public boolean shouldSkipClass(Class<?> aClass) {
        // 排除构造器指定的类类型
        return (aClass == typeToSkip);
    }
}
