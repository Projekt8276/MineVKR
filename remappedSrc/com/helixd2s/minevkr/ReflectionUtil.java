package com.helixd2s.minevkr;

import net.minecraft.util.math.Matrix4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtil {
    public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()))
        {
            field.setAccessible(true);
        }
    }
    public static float getFieldFValue(Matrix4f obj, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = ReflectionUtil.getField(obj.getClass(), name);
        ReflectionUtil.makeAccessible(field);
        return field.getFloat(obj);
    }
}
