package nc.bpm.fa.pfxx;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.impl.pfxx.plugins.AssetPfxxImpl;
import nc.itf.fa.prv.INewAsset;
import nc.vo.fa.newasset.AggNewAssetVO;
import nc.vo.fa.newasset.NewAssetHeadVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

public class AddAssetPlugin extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		return new AssetPfxxImpl().processBill(vo, swapContext, aggvo);
	}

}
