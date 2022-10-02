package com.example.myapp.util;

import java.lang.reflect.Field;

public class StrongParentSubclass {

    /**
     * @Author zx
     * @Description 反射循环赋值
     * @Date 2019/12/18 18:32
     * @Param [parent, subclass]
     **/
    public static Object parentSubclass(Object parent, Object subclass) {
        Field[] parents = parent.getClass().getDeclaredFields();//获取所有属性
        Field[] children = subclass.getClass().getSuperclass().getDeclaredFields();//获取父类所有属性
        try {
            for (Field fieldParent : parents) {
                fieldParent.setAccessible(true);
                String nameParent = fieldParent.getName(); //获取属性的名字

                if ("serialVersionUID".equals(nameParent)) {
                    // 跳过序列化ID
                    continue;
                }

                Object valueParent = fieldParent.get(parent);//获取属性值
                for (Field fieldChild : children) {
                    fieldChild.setAccessible(true);
                    String nameChild = fieldChild.getName(); //获取属性的名字
                    Object valueChild = fieldChild.get(subclass);
                    if (nameChild.equals(nameParent)) {
                        fieldChild.set(subclass, valueParent);
                    }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return subclass;
    }


    /**
     * @Author zx
     * @Author ygw
     * @Description 通过json
     * @Description 修改了引用类
     * @Date 2019/12/19 9:21
     * @Param [parent, subclass]
     **/
    public static Object jsonParentSubclass(Object parent, Object subclass) {
        String jsonStr = JsonUtil.object2Json(parent);
        return JsonUtil.json2Object(jsonStr, subclass.getClass());
    }
}
