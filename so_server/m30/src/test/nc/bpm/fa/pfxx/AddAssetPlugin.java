package nc.bpm.fa.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.fa.service.INewAssetService;
import nc.vo.am.proxy.AMProxy;
import nc.vo.fa.newasset.AggNewAssetVO;
import nc.vo.fa.newasset.NewAssetHeadVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.uif2.LoginContext;

public class AddAssetPlugin extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		/*
		INewAsset service = AMProxy.lookup(INewAsset.class);
		AggNewAssetVO temp = service.queryBillOfVOByPK("1001A41000000000DO00");
		NewAssetHeadVO tempHead = (NewAssetHeadVO)(temp.getParent());
		try{
			AggNewAssetVO[] temps = new AggNewAssetVO[1];
			temps[0] = temp;
			XmlOutTool.votoXmlFile("bpm_add_asset", temps, tempHead.getPk_org(), tempHead.getBill_code());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
		*/
		
		AggNewAssetVO billVO = (AggNewAssetVO)vo;
		//创建LoginContext
		LoginContext loginContext = new LoginContext();
		NewAssetHeadVO headVO = (NewAssetHeadVO)(billVO.getParent());
		loginContext.setPk_group(headVO.getPk_group());
		loginContext.setPk_org(headVO.getPk_org());
		return AMProxy.lookup(INewAssetService.class).saveFromWeb(loginContext, billVO);
		
		
	}

}
