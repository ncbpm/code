/*
 * Created on 2004-10-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.bs.pfxx.process;

import org.w3c.dom.Document;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.util.ObjectCreator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pfxx.IXChangeContext;
import nc.bs.pfxx.plugin.IProcessRequest;
import nc.bs.uap.lock.PKLock;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.auxiliary.AuxiregisterVO;
import nc.vo.pfxx.exception.FileConfigException;
import nc.vo.pfxx.exception.ISendResult;
import nc.vo.pfxx.exception.PfxxException;
import nc.vo.pfxx.exception.PfxxPluginException;
import nc.vo.pfxx.pub.PfxxConstants;
import nc.vo.pfxx.pub.SwapException;
import nc.vo.pfxx.xxconfig.BusinessProcessorDefination;
import nc.vo.pfxx.xxconfig.LockLevel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ҵ�����ַ���
 * 
 * @author cch (cch@ufida.com.cn) 2006-3-16-9:50:49
 * 
 */
public class BusinessProcessorDispatcher {

	private static BusinessProcessorDispatcher businessProcessorDispatcher = new BusinessProcessorDispatcher();

	private BusinessProcessorDispatcher() {
	}

	public static BusinessProcessorDispatcher getInstance() {
		return businessProcessorDispatcher;
	}

	public void forward(Document doc, IXChangeContext context) throws PfxxException {
		BusinessProcessorDefination bp = context.getRegisterInfoCenter().getBusinessProcessor(context.getBillType());
		IProcessRequest processRequest = getProcessRequest(context, bp);
		String dsName = InvocationInfoProxy.getInstance().getUserDataSource();
		LockLevel locklevel = new LockLevel(bp.getLockLevel(), context.getBillType(), context.getOrgPk(),
				context.getDocID());
		try {
			// ����������+��ˮ������,�Կ���ͬһ�ŵ����ظ�����
			int i = 0;
			int max = 15;
			//2017-07-06 �����Ŀ,����������
//			for (; i < max && !PKLock.getInstance().acquireLock(locklevel.getLockKey(), "pfxxplugin", dsName); i++) {
//				Thread.sleep(100);
//			}
//			if (i == max) {
//				throw new PfxxPluginException(processRequest.getClass().getName(), locklevel.getExceptionDesc());
//			}
		} catch (Exception e) {
			throw new PfxxPluginException(processRequest.getClass().getName(), e.getMessage());
		}

		// ת��ҵ��������
		try {
			processRequest.ProcessRequest(doc, context);
		} 
		// �׳�ת���쳣����ʾû�ߵ�����ͳ����� sjt 14.8.27
		catch (SwapException se){
			throw se;
		}
		catch (BusinessException be) {
			throw new PfxxPluginException(processRequest.getClass().getName(), be);
		}
		catch (Exception e){
			Logger.error(NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-V51005")/* "���ò�������г���δ֪�쳣" */,e);
			throw new PfxxPluginException(e.getMessage(),NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-V51005")/* "���ò�������г���δ֪�쳣" */);
		}
		finally {
			// ����������+��ˮ�Ž���
			PKLock.getInstance().releaseLock(locklevel.getLockKey(), "pfxxplugin", dsName);
		}
	}

	/**
	 * ���ڵ��Դ�ӡ������Ϣ
	 * 
	 * @param vo
	 */
	public void logXsysregister(AggxsysregisterVO vo) {
		if (vo == null || vo.getParentVO() == null)
			return;

		String[] anames = vo.getParentVO().getAttributeNames();
		for (int i = 0; i < anames.length; i++) {
			Logger.info("XsysregisterVO��ֵΪ:");
			Logger.info(anames[i] + ":=" + vo.getParentVO().getAttributeValue(anames[i]));
		}

		CircularlyAccessibleValueObject[] avo = vo.getChildrenVO();
		int i = 0;
		for (CircularlyAccessibleValueObject circularlyAccessibleValueObject : avo) {
			AuxiregisterVO auxiVO = (AuxiregisterVO) circularlyAccessibleValueObject;
			Logger.info("������ϢAuxiregisterVO[" + i + "]��ֵΪ:");
			Logger.info(auxiVO.getAuxiregistername() + ":=" + auxiVO.getAuxiregistervalue());
			Logger.info(auxiVO.getAuxiregistername() + ":=" + auxiVO.getAuxiregisterexpvalue());
			i++;
		}
	}

	/**
	 * ȡ��ע���ҵ����
	 * 
	 * @param context
	 * @return
	 * @throws PfxxException
	 */
	private IProcessRequest getProcessRequest(IXChangeContext context, BusinessProcessorDefination bpDefination)
			throws PfxxPluginException {
		try {
			if (bpDefination == null) {
				throw new FileConfigException(PfxxConstants.BUSINESS_PROCESSORS_PATH,
						ISendResult.ERR_CONFIG_BILLINFO_NONE, nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"pfxx", "UPPpfxx-000500", null, new String[] { context.getBillType() /*
																									 * ,
																									 * context.getProcess
																									 * ()
																									 */})); /*
																											 * "�޷��ҵ���������Ϊ:"+
																											 * billType+
																											 * ", ��������Ϊ:"
																											 * +process+
																											 * "��ҵ����!"
																											 */
			}
		} catch (FileConfigException e) {
			Logger.error(e.getMessage(), e);
			throw new PfxxPluginException(
					NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-V50025")/* "�Ҳ��������" */, e);
		}

		IProcessRequest processRequest = null;

		String pluginClassName = bpDefination.getPluginClassName();
		String moduleName = bpDefination.getModuleName();
		Logger.info("ȡ��ҵ������" + pluginClassName + ",ģ����:" + moduleName);
		try {
			processRequest = (IProcessRequest) ObjectCreator.newInstance(moduleName, pluginClassName);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PfxxPluginException(pluginClassName, nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("pfxx",
					"UPPpfxx-000017")/* @res "�޷���ʼ��" */
					+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-V50099")/* @res "�����" */
					+ " ,class name: " + pluginClassName + " ,module name: " + moduleName);
		}
		return processRequest;
	}
}