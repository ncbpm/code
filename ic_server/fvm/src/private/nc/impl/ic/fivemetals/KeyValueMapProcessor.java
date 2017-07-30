package nc.impl.ic.fivemetals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import nc.jdbc.framework.processor.ResultSetProcessor;

/**
 * 
 * pkField : key������ valueField: ֵ������ ����Map�͵Ľ����<pk,value>
 * ����pkΪ����pkField��Ӧ��ֵ��ValueΪ����
 * 
 * @author heyy1
 * 
 */
@SuppressWarnings("serial")
public class KeyValueMapProcessor<K, V> implements ResultSetProcessor {

	private String pkField = null;
	private String valueFiled = null;

	// Ĭ��ȥ��1��2����λkey,value
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
