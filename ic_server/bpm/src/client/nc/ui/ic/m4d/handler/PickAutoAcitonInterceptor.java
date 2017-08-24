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
		// TODO �Զ����ɵķ������
		return true;
	}

	@Override
	public void afterDoActionSuccessed(Action action, ActionEvent e) {
		// TODO �Զ����ɵķ������
		try {
			getTool().setCostObjectValueAllRow();
		} catch (BusinessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
			ExceptionUtils.wrappException(e1);
		}
	}

	@Override
	public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex) {
		// TODO �Զ����ɵķ������
		return true;
	}

	public CostObjectDealTool getTool() {
		return tool;
	}

	public void setTool(CostObjectDealTool tool) {
		this.tool = tool;
	}

}
