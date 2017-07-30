package nc.impl.ic.fivemetals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import nc.jdbc.framework.processor.ResultSetProcessor;

/**
 * 
 * pkField : key的列名 valueField: 值的列名 返回Map型的结果集<pk,value>
 * 其中pk为列名pkField对应的值，Value为列名
 * 
 * @author heyy1
 * 
 */
@SuppressWarnings("serial")
public class KeyValueMapProcessor<K, V> implements ResultSetProcessor {

	private String pkField = null;
	private String valueFiled = null;

	// 默认去第1，2个座位key,value
	private int pkIndex = 1;

	private int valueIndex = 2;

	public KeyValueMapProcessor() {
		super();
	}

	public KeyValueMapProcessor(String pkFiled, String valueFiled) {
		this.pkField = pkFiled;
		this.valueFiled = valueFiled;
	}

	public KeyValueMapProcessor(int pkIndex, int valueIndex) {
		this.pkIndex = pkIndex;
		this.valueIndex = valueIndex;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object handleResultSet(ResultSet rs) throws SQLException {
		Map<K, V> retMap = new HashMap<K, V>();
		while (rs.next()) {

			K key = null;
			V value = null;

			if (pkField != null) {
				key = (K) rs.getObject(pkField);
				value = (V) rs.getObject(valueFiled);
			} else {
				key = (K) rs.getObject(pkIndex);
				value = (V) rs.getObject(valueIndex);
			}
			retMap.put(key, value);
		}

		return retMap;
	}

}
