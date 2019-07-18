package com.xh.ssh.web.task.common.tool;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title: 类型转换</b>
 * <p>Description: </p>
 * <p>
 * 将Map对象通过反射机制转换成Bean对象<br>
 * 将Bean对象转换成Map对象<br>
 * 将Object类型的值，转换成bean对象属性里对应的类型值<br>
 * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)<br>
 * 
 * </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年8月28日
 */
@SuppressWarnings("all")
public class BeanUtils {

	/**
	 * <b>Title: 将Map对象通过反射机制转换成Bean对象</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param map 存放数据的map对象
	 * @param clazz 待转换的class
	 * @return
	 */
	public static <T> T parseFromMapToClass(Map<String, Object> map, Class<T> clazz) {
		T obj = null;
		try {
			obj = clazz.newInstance(); // 创建 JavaBean 对象
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			String name = null;
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				name = propertyDescriptor.getName();
				// 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
				Field field = BeanUtils.getClassField(clazz, name);
				if (map.containsKey(name)) {
					Class fieldTypeClass = field.getType();

					// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
					Object value = map.get(name);
					value = ("".equals(value)) ? null : value;
					value = (value != null) ? BeanUtils.toValType(value, fieldTypeClass) : null;
					try {
						propertyDescriptor.getWriteMethod().invoke(obj, value);
					} catch (InvocationTargetException e) {
						System.out.println("字段映射失败");
					}
				}
			}
		} catch (IllegalAccessException e) {
			System.out.println("实例化 JavaBean 失败");
		} catch (IntrospectionException e) {
			System.out.println("分析类属性失败");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println("映射错误");
		} catch (InstantiationException e) {
			System.out.println("实例化 JavaBean 失败");
		}
		return (T) obj;
	}

	/**
	 * <b>Title: 将Bean对象转换成Map对象</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param clazz
	 * @param flag 判断是否显示空值，true 显示， false 不显示
	 * @return
	 */
	public static Map<Object, Object> parseFromBeanToMap(Object obj, boolean flag) {
		if (obj == null) {
			return null;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				if (property.getName().equals("class")) {
					continue;
				}
				// 得到property对应的getter方法
				Method getMethod = property.getReadMethod();
				if (!flag) {
					if (getMethod.invoke(obj) == null || getMethod.invoke(obj) == "") {
						continue;
					}
					map.put(property.getName(), getMethod.invoke(obj));
				} else {
					map.put(property.getName(), getMethod.invoke(obj));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * <b>Title: 将Object类型的值，转换成bean对象属性里对应的类型值</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param value Object对象值
	 * @param fieldTypeClass 属性的类型
	 * @return
	 */
	public static <T> Object toValType(Object value, Class<T> fieldTypeClass) {
		if (value.equals("null") || value == null || value == "") {
			return null;
		}
		Object retVal = null;
		if (Byte.class.getName().equals(fieldTypeClass.getName()) || byte.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Byte.parseByte(value.toString());
		} else if (Short.class.getName().equals(fieldTypeClass.getName()) || short.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Short.parseShort(value.toString());
		} else if (Integer.class.getName().equals(fieldTypeClass.getName()) || int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if (Long.class.getName().equals(fieldTypeClass.getName()) || long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if (Double.class.getName().equals(fieldTypeClass.getName()) || double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else if (Float.class.getName().equals(fieldTypeClass.getName()) || float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if (Boolean.class.getName().equals(fieldTypeClass.getName()) || boolean.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Boolean.parseBoolean(value.toString());
		} else if (Date.class.getName().equals(fieldTypeClass.getName())) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				retVal = sdf.parse(value.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			retVal = value;
		}
		return retVal;
	}

	/**
	 * <b>Title: 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param clazz 指定的class
	 * @param fieldName 字段名称
	 * @return
	 */
	public static <T> Field getClassField(Class<T> clazz, String fieldName) {
		if (Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		// => 此处是递归查找父类不要删除
		// Class superClass = clazz.getSuperclass();
		// if (superClass != null) {// 简单的递归一下
		// return Dom4jTool.getClassField(superClass, fieldName);
		// }
		return null;
	}
}
