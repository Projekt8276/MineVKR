package com.helixd2s.minevkr

import net.minecraft.util.math.Matrix4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

object ReflectionUtil {
    @Throws(NoSuchFieldException::class)
    fun getField(clazz: Class<*>, fieldName: String?): Field {
        return try {
            clazz.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            val superClass = clazz.superclass
            superClass?.let { getField(it, fieldName) } ?: throw e
        }
    }

    fun makeAccessible(field: Field) {
        if (!Modifier.isPublic(field.modifiers) || !Modifier.isPublic(field.declaringClass.modifiers)) {
            field.isAccessible = true
        }
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun getFieldFValue(obj: Matrix4f, name: String?): Float {
        val field = getField(obj.javaClass, name)
        makeAccessible(field)
        return field.getFloat(obj)
    }
}
