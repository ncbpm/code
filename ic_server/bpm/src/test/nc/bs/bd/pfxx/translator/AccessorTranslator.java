package nc.bs.bd.pfxx.translator;

import nc.bs.pfxx.ITranslateContext;
import nc.bs.pfxx.ITranslatorStrategy;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.vo.bd.accessor.IBDData;
import nc.vo.pfxx.exception.PfxxException;
import nc.vo.pfxx.xxconfig.IBasicDataMatchRule;

public abstract class AccessorTranslator implements ITranslatorStrategy {

	@Override
	public String getStrategyDescription() {
		return null;
	}

	@Override
	public String translateExToNC(String srcValue, String metaDataID,
			ITranslateContext translateContext) throws PfxxException {
		if (translateContext.getTranslateRule() == IBasicDataMatchRule.RULE_PK) {
			return srcValue;
		}
		IGeneralAccessor accessor = GeneralAccessorFactory
				.getAccessor(metaDataID);
		IBDData doc = null;
		if (translateContext.getTranslateRule() == IBasicDataMatchRule.RULE_CODE) {
			doc = accessor.getDocByCode(getPk_org(translateContext), srcValue);
		} else if (translateContext.getTranslateRule() == IBasicDataMatchRule.RULE_NAME) {
			doc = accessor.getDocByNameWithMainLang(getPk_org(translateContext), srcValue);
		}
		if (doc != null)
			return doc.getPk();
		return null;
	}

	@Override
	public String translateNCToEx(String docPk, String metaDataID,
			ITranslateContext translateContext) throws PfxxException {
		if (translateContext.getTranslateRule() == IBasicDataMatchRule.RULE_PK) {
			return docPk;
		}
		IGeneralAccessor accessor = GeneralAccessorFactory
				.getAccessor(metaDataID);
		IBDData doc = accessor.getDocByPk(docPk);
		if (doc != null) {
			if (translateContext.getTranslateRule() == IBasicDataMatchRule.RULE_CODE) {
				return doc.getCode();
			}
			if (translateContext.getTranslateRule() == IBasicDataMatchRule.RULE_NAME) {
				return doc.getName().getText();
			}
		}
		return null;
	}

	/**
	 * 返回当前翻译器按哪个组织的可见性翻译数据.
	 */
	public abstract String getPk_org(ITranslateContext translateContext);
}
