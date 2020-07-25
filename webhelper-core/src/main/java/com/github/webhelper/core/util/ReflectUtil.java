package com.github.webhelper.core.util;

import com.github.webhelper.core.model.NamedVar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReflectUtil {
  private static final Set<String> EXCLUDED_PROPS = new HashSet<>(Arrays.asList("getClass"));

  @SuppressWarnings("unchecked")
  public static <T> T invoke(Object obj, String methodName, Object... args)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = getMethod(obj.getClass(), methodName, args);
    return (T) method.invoke(obj);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getProperty(Object obj, String propertyName)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method;
    try {
      method = getMethod(obj.getClass(), "get" + capitalize(propertyName), null);
    } catch (NoSuchMethodException e) {
      method = getMethod(obj.getClass(), "is" + capitalize(propertyName), null);
    }
    return (T) method.invoke(obj);
  }

  public static Map<String, Type> getProperties(Class<?> clazz) {
    Map<String, Type> properties = new LinkedHashMap<>();
    for (Method method : clazz.getMethods()) {
      if (method.getParameterCount() == 0 && method.getName().matches("^(get|is)[A-Z]\\w*")
          && !EXCLUDED_PROPS.contains(method.getName())) {
        Type type = method.getGenericReturnType();
        String varName = lowerFirst(method.getName().replaceFirst("^(get|is)", ""));
        System.out.println("prop: " + varName + " : " + type);
        properties.put(varName, type);
      }
    }
    return properties;
  }

  public static Method getMethod(Class<?> clazz, String methodName, Object[] args)
      throws NoSuchMethodException {
    while (clazz != null && clazz != Object.class) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (method.getName().equals(methodName) && argsMatch(method.getParameterTypes(), args)) {
          return method;
        }
      }
      clazz = clazz.getSuperclass();
    }
    throw new NoSuchMethodException("method " + methodName + " not found in " + clazz);
  }

  public static boolean argsMatch(Class<?>[] types, Object[] args) {
    if (types.length != (args != null ? args.length : 0)) {
      return false;
    }
    for (int i = 0; i < types.length; i++) {
      if(args[i] != null && !types[i].isInstance(args[i])) {
        return false;
      }
    }
    return true;
  }

  public static String capitalize(String str) {
    if (str != null && str.length() > 0) {
      return str.substring(0, 1).toUpperCase() + str.substring(1);
    } else {
      return str;
    }
  }

  public static String lowerFirst(String str) {
    if (str != null && str.length() > 0) {
      return str.substring(0, 1).toLowerCase() + str.substring(1);
    } else {
      return str;
    }
  }
}
