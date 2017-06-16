package nc.bs.ic.fivemetals.plugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.pubapp.res.NCModule;

public enum FivemetalsPluginPoint implements IPluginPoint {

	/**
	 * 新增保存
	 */
	INSERT,

	/**
	 * 修改保存
	 */
	UPDATE;

	@Override
	public String getComponent() {
		return "F230";
	}

	@Override
	public String getModule() {
		return NCModule.IC.getSystemCode();
	}

	@Override
	public String getPoint() {
		return this.name();
	}
}
