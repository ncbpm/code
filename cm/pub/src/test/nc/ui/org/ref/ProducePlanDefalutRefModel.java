/**
 * 
 */
package nc.ui.org.ref;


import nc.itf.org.IOrgResourceCodeConst;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.OrgVO;
import nc.vo.pub.IBBDPubConst;

/**
 * �ƻ���֯����
 * @author wangybo
 *
 */
public class ProducePlanDefalutRefModel extends OrgBaseListDefaultRefModel {

	public ProducePlanDefalutRefModel() {
		super();
		reset();
	}
	
	@Override
	public String[] getFilterPks() {
		// TODO �Զ����ɵķ������
		return null;
	}
	
	public void reset() {
		setRefNodeName("�ƻ���֯");/*-=notranslate=-*/
		
		setFieldCode(new String[] { OrgVO.CODE, OrgVO.NAME});
		setFieldName(new String[] { 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279") /* @res "����" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /* @res "����" */
					});
		setHiddenFieldCode(new String[] {OrgVO.PK_ORG});
		setPkFieldCode(OrgVO.PK_ORG);
		setRefCodeField(OrgVO.CODE);
		setRefNameField(OrgVO.NAME);
		setTableName(getTableName());
		
		setOrderPart(OrgVO.CODE);
		
//		setResourceID(IOrgResourceCodeConst.ORG);
		
		//�����ù�����������
		setAddEnableStateWherePart(false);
		
//		setFilterRefNodeName(new String[] {"����"});/*-=notranslate=-*//
		
		resetFieldName();
	}
	
	@Override
	protected String getEnvWherePart() {
		return "(" + IBBDPubConst.PK_GROUP_FIELD + " = '" + getPk_group() + "')";
	}
	
	@Override
	public String getTableName() {
		String tablename = "(select pk_factory pk_org,code, name, name2,name3, name4, name5, name6, pk_group, enablestate from org_factory "
						 + "union "
						 + "select pk_plancenter pk_org, code, name, name2,name3, name4, name5, name6, pk_group, enablestate from org_plancenter) orgtemp";
		return tablename;
	}
	

}
