package com.xh.ssh.web.task.common.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cglib.beans.BeanMap;

public class PageData extends HashMap implements Map {

	private static final long serialVersionUID = 1L;

	Map map = null;
	HttpServletRequest request;

	public PageData(HttpServletRequest request) {
		this.request = request;
		Map properties = request.getParameterMap();
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		this.map = returnMap;
	}

	public PageData() {
		this.map = new HashMap();
	}

	public PageData(Map map) {
		this.map = map;
	}

	/**
	 * <p>Title: 实体类转map</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param emtity
	 * @return
	 */
	public static <T> PageData entityToMap(T emtity) {
		PageData pd = new PageData();
		if (emtity != null) {
			BeanMap beanMap = BeanMap.create(emtity);
			for (Object key : beanMap.keySet()) {
				pd.put(key + "", (beanMap.get(key) == null) ? "" : beanMap.get(key));
			}
		}
		return pd;
	}

	/**
	 * <p>Title: 实体类转map(稍快)</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param emtity
	 * @param pd
	 * @return
	 */
	public static <T> PageData entityToMap(T emtity, PageData pd) {
		if (emtity != null) {
			BeanMap beanMap = BeanMap.create(emtity);
			for (Object key : beanMap.keySet()) {
				pd.put(key + "", (beanMap.get(key) == null) ? "" : beanMap.get(key));
			}
		}
		return pd;
	}

	/**
	 * <p>Title: map转实体类</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param pd
	 * @param emtity
	 * @return
	 */
	public static <T> T mapToEntity(T emtity) {
		PageData pd = new PageData();
		BeanMap beanMap = BeanMap.create(emtity);
		beanMap.putAll(pd);
		return emtity;
	}

	/**
	 * <p>Title: map转实体类</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param pd
	 * @param emtity
	 * @return
	 */
	public static <T> T mapToEntity(PageData pd, T emtity) {
		BeanMap beanMap = BeanMap.create(emtity);
		beanMap.putAll(pd);
		return emtity;
	}

	/**
	 * <p>Title: 将List T 转换为List Map </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param entityList
	 * @return
	 */
	public static <T> List<PageData> entityToMap(List<T> entityList) {
		List<PageData> list = new ArrayList<PageData>();
		if (entityList != null && entityList.size() > 0) {
			PageData pd = new PageData();
			for (int i = 0, size = entityList.size(); i < size; i++) {
				list.add(entityToMap(entityList.get(i), pd));
			}
		}
		return list;
	}

	/**
	 * <p>Title: 将List Map 转换为List T </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param maps
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> mapToEntity(List<PageData> maps, Class<T> clazz) throws InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<T>();
		if (maps != null && maps.size() > 0) {
			for (int i = 0, size = maps.size(); i < size; i++) {
				list.add(mapToEntity(maps.get(i), clazz.newInstance()));
			}
		}
		return list;
	}

	@Override
	public Object get(Object key) {
		Object obj = null;
		if (map.get(key) instanceof Object[]) {
			Object[] arr = (Object[]) map.get(key);
			obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
		} else {
			obj = map.get(key);
		}
		return obj;
	}

	public String getString(Object key) {
		return (String) get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map t) {
		map.putAll(t);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}

}
