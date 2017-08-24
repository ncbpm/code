package nc.ui.ic.m4d.handler;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.uif2.actions.ActionInterceptor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class PickAutoAcitonInterceptor implements ActionInterceptor {

	CostObjectDealTool tool;

	@Override
	public boolean beforeDoAction(Action action, ActionEvent e) {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public void afterDoActionSuccessed(Action action, ActionEvent e) {
		// TODO 自动生成的方法存根
		try {
			getTool().setCostObjectValueAllRow();
		} catch (BusinessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			ExceptionUtils.wrappException(e1);
		}
	}

	@Override
	public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex) {
		// TODO 自动生成的方法存根
		return true;
	}

	public CostObjectDealTool getTool() {
		return tool;
	}

	public void setTool(CostObjectDealTool tool) {
		this.tool = tool;
	}

}
