package nc.ui.mapub.allocfacotor.action;

import nc.ui.pubapp.uif2app.actions.batch.BatchAddLineAction;
import nc.vo.mapub.allocfacotor.Factorofinv;
/**
  batch addLine or insLine action autogen
*/
public class AllocfacotorAddLineActiona extends BatchAddLineAction {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setDefaultData(Object obj) {
		super.setDefaultData(obj);
		Factorofinv singleDocVO = (Factorofinv) obj;
		//singleDocVO.setPk_group(this.getModel().getContext().getPk_group());
		//singleDocVO.setPk_org(this.getModel().getContext().getPk_org());
		singleDocVO.setAttributeValue("pk_group", this.getModel().getContext().getPk_group());
		singleDocVO.setAttributeValue("pk_org", this.getModel().getContext().getPk_org());
	}

}