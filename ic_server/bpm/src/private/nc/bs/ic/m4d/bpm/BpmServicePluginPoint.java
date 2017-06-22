package nc.bs.ic.m4d.bpm;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.pubapp.res.NCModule;

public enum BpmServicePluginPoint implements IPluginPoint {

	/**
	 * 备料申请推式生成材料出库单
	 */
	pushSaveFor422X("nc.pubimpl.ic.m4d.m422x.action.PushSaveActionFor422X");

	private static final String COMPONENT_NAME = "m4d";

	private String point = null;

	private BpmServicePluginPoint(String point) {
		this.point = point;
	}

	@Override
	public String getComponent() {
		return BpmServicePluginPoint.COMPONENT_NAME;
	}

	@Override
	public String getModule() {
		return NCModule.IC.getSystemCode();
	}

	@Override
	public String getPoint() {
		return this.point;
	}
}