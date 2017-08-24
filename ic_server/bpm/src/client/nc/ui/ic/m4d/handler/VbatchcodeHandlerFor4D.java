package nc.ui.ic.m4d.handler;

import nc.ui.ic.general.handler.VbatchcodeHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.ICBillType;

public class VbatchcodeHandlerFor4D extends VbatchcodeHandler {

	CostObjectDealTool tool = null;

	public void afterCardBodyEdit(CardBodyAfterEditEvent event) {
		super.afterCardBodyEdit(event);
		// TODO �Զ����ɵķ������
		if (this.getEditorModel().getICBillType()
				.equals(ICBillType.MaterialOut)) {
			try {
				getTool().setCostObjctValue(event.getRow());
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				ExceptionUtils.wrappException(e);
			}

		}
	}

	public CostObjectDealTool getTool() {
		return tool;
	}

	public void setTool(CostObjectDealTool tool) {
		this.tool = tool;
	}

}
