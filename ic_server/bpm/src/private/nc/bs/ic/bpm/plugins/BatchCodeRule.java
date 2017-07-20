package nc.bs.ic.bpm.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.batch.IBatchRefQuery;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.batchcode.BatchDlgParam;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICNewBatchFields;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.define.IBizObject;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.special.define.ICSpecialBodyEntity;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

/**
 * 批次号档案匹配规则
 * @author liyf
 *
 */
public class BatchCodeRule {

	/**
	 * 根据组织，仓库，存货和批次号获取批次号+现存量信息
	 * @param pk_org
	 * @param cwarehouseid
	 * @param cmaterialvid
	 * @param vbatchcode
	 * @return
	 * @throws BusinessException
	 */
	public BatchRefViewVO getRefVO(String pk_org, String cwarehouseid,String cmaterialvid, String vbatchcode) throws BusinessException {
		if (StringUtil.isSEmptyOrNull(vbatchcode)) {
			return null;
		}
		String realVbatchcode = vbatchcode;
		if (vbatchcode.endsWith(".")) {
			realVbatchcode = vbatchcode.substring(0, vbatchcode.length() - 1);
		}
		BatchRefViewVO[] resultVOs = null;

		// 处理批次号参照框未被初始化的情况
		BatchDlgParam newParam = new BatchDlgParam();
		newParam.setPk_calbody(pk_org);
		newParam.setCwarehouseid(cwarehouseid);
		newParam.setCmaterialvid(cmaterialvid);
		newParam.setVbatchcode(realVbatchcode);
		newParam.setIsQueryZeroLot(UFBoolean.FALSE);
		resultVOs = NCLocator.getInstance().lookup(IBatchRefQuery.class)
				.queryBatchNum(newParam);
		// 过滤掉被封存的批次档案
		resultVOs = BatchRefViewVO.filterBsealBatchVO(resultVOs);
		if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
			return null;
		}
		return this.matchBatchcode(resultVOs, cmaterialvid, realVbatchcode);
	

	}

	private BatchRefViewVO matchBatchcode(BatchRefViewVO[] results,
			String cmaterialvid, String realbatchcode) {
		for (BatchRefViewVO viewvo : results) {
			if (!StringUtil.isStringEqual((String) viewvo
					.getAttributeValue(ICPubMetaNameConst.CMATERIALVID),
					cmaterialvid)
					|| !StringUtil.isStringEqual((String) viewvo
							.getAttributeValue(ICPubMetaNameConst.VBATCHCODE),
							realbatchcode)) {
				continue;
			}
			return viewvo;
		}
		return null;
	}
	
	
	/**
	 * 将批次信息同步至库存单据表体
	 * 
	 * @since 6.0
	 */
	public  void synBatch(BatchRefViewVO batchcode, IBizObject body) {
		// 不同步物料oid和物料vid及pk_group
		ICNewBatchFields batchandBill = new ICNewBatchFields();
		BatchSynchronizer syn = new BatchSynchronizer(batchandBill);
		syn.synBatchReftoBill(batchcode, body);

		// 同步物料辅助属性到单据表体
		this.synDimInfo(batchcode, body);
		// add by wangceb
//		this.synNnumInfo(batchcode, body);
	}

	/**
	 * 方法功能描述：同步物料辅助属性到单据表体
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param refVO
	 * @param bodyVO
	 *    
	 */
	private void synDimInfo(BatchRefViewVO refVO, IBizObject bodyVO) {
		this.synBizData((CircularlyAccessibleValueObject) refVO
				.getVO(OnhandDimVO.class), bodyVO, this.getSrctoTargetDimMap());
	}

	 /**
	   * 方法功能描述：同步数量货位到单据表体
	   * <p>
	   * <b>参数说明</b>
	   * 
	   * @param refVO
	   * @param bodyVO
	   *          <p>
	
	   */
	  private void synNnumInfo(BatchRefViewVO refVO, IBizObject bodyVO) {
		  String[] fields = OnhandDimVO.getDimContentFields();
		  
		  for (String field : fields) {
			  if (refVO.getAttributeValue(field) == null || StringUtil.isNullStringOrNull(refVO.getAttributeValue(field).toString())) {
				  continue;
			  }
			  bodyVO.setAttributeValue(field, refVO.getAttributeValue(field));
		  }
		  // 主数量
		  UFDouble nnum = (UFDouble)refVO.getAttributeValue(ICPubMetaNameConst.NNUM);
		  if(NCBaseTypeUtils.isNullOrZero(nnum)){
		    return;
		  }
		  // 辅数量
	    UFDouble nassistnum = null;
	    String vchangerate = (String)refVO.getAttributeValue(ICPubMetaNameConst.VCHANGERATE);
	    if (!StringUtil.isNullStringOrNull(vchangerate)) {
	      nassistnum = (UFDouble)refVO.getAttributeValue(ICPubMetaNameConst.NASSISTNUM);
	      bodyVO.setAttributeValue(ICPubMetaNameConst.VCHANGERATE, vchangerate);
	    }
	    else {
	      // 不返回换算率时，根据单据换算率重算
	      nassistnum =
	          HslParseUtil
	              .hslDivUFDouble((String) bodyVO
	                  .getAttributeValue(ICPubMetaNameConst.VCHANGERATE), nnum);
	    }
	    bodyVO.setAttributeValue(ICPubMetaNameConst.NNUM, nnum);
	    bodyVO.setAttributeValue(ICPubMetaNameConst.NASSISTNUM, nassistnum);
	  }
	  
	/**
	 * 方法功能描述：存量维度与表体字段匹配
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @return <p>
	 */
	private Map<String, String> getSrctoTargetDimMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(OnhandDimVO.CSTATEID, ICPubMetaNameConst.CSTATEID);
		map.put(OnhandDimVO.CVENDORID, ICPubMetaNameConst.CVENDORID);
		map.put(OnhandDimVO.CPROJECTID, ICPubMetaNameConst.CPROJECTID);
		map.put(OnhandDimVO.CPRODUCTORID, ICPubMetaNameConst.CPRODUCTORID);
		map.put(OnhandDimVO.CASSCUSTID, ICPubMetaNameConst.CASSCUSTID);
		map.put(OnhandDimVO.VFREE1, ICPubMetaNameConst.VFREE1);
		map.put(OnhandDimVO.VFREE2, ICPubMetaNameConst.VFREE2);
		map.put(OnhandDimVO.VFREE3, ICPubMetaNameConst.VFREE3);
		map.put(OnhandDimVO.VFREE4, ICPubMetaNameConst.VFREE4);
		map.put(OnhandDimVO.VFREE5, ICPubMetaNameConst.VFREE5);
		map.put(OnhandDimVO.VFREE6, ICPubMetaNameConst.VFREE6);
		map.put(OnhandDimVO.VFREE7, ICPubMetaNameConst.VFREE7);
		map.put(OnhandDimVO.VFREE8, ICPubMetaNameConst.VFREE8);
		map.put(OnhandDimVO.VFREE9, ICPubMetaNameConst.VFREE9);
		map.put(OnhandDimVO.VFREE10, ICPubMetaNameConst.VFREE10);
		// 批次号带寄存供应商
		map.put(OnhandDimVO.CVMIVENDERID, ICPubMetaNameConst.CVMIVENDERID);
		// 批次号带货主客户
		map.put(OnhandDimVO.CINVCUSTID, ICPubMetaNameConst.CTPLCUSTOMERID);
		return map;
	}

	private void synBizData(CircularlyAccessibleValueObject srcObject,
			IBizObject targetObject, Map<String, String> srctoTargetFields) {
		if (srcObject == null || targetObject == null
				|| srctoTargetFields == null) {
			return;
		}
		for (Map.Entry<String, String> srctoTarget : srctoTargetFields
				.entrySet()) {
			Object srcValue = srcObject.getAttributeValue(srctoTarget.getKey());
			// 辅助属性为空时，不清掉原来有的辅助属性（合成元客户倾向的效果）
			if (srcValue == null) {
				continue;
			}
			targetObject.setAttributeValue(srctoTarget.getValue(), srcValue);
		}
	}

	/**
	 * 获取批次号档案
	 * 
	 * @param vos
	 * @return Map<String(cmaterialvid+vbatchcode), BatchcodeVO批次档案>
	 */
	private Map<String, BatchcodeVO> getBatchcodeVO(ICSpecialBodyEntity[] vos) {
		List<String> cmaterialvidList = new ArrayList<String>();
		List<String> vbatchcodeList = new ArrayList<String>();
		Set<String> materialbatch = new HashSet<String>();
		for (ICSpecialBodyEntity body : vos) {
			if (body.getCmaterialvid() != null && body.getVbatchcode() != null) {
				if (materialbatch.contains(body.getCmaterialvid()
						+ body.getVbatchcode())) {
					continue;
				}
				cmaterialvidList.add(body.getCmaterialvid());
				vbatchcodeList.add(body.getVbatchcode());
				materialbatch
						.add(body.getCmaterialvid() + body.getVbatchcode());
			}
		}
		if (materialbatch.size() == 0) {
			return new HashMap<String, BatchcodeVO>();
		}
		IBatchcodePubService batchservice = NCLocator.getInstance().lookup(
				IBatchcodePubService.class);
		BatchcodeVO[] batchvos = null;
		try {
			batchvos = batchservice.queryBatchVOs(
					cmaterialvidList.toArray(new String[0]),
					vbatchcodeList.toArray(new String[0]));
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

		if (batchvos == null || batchvos.length == 0) {
			return new HashMap<String, BatchcodeVO>();
		}
		Map<String, BatchcodeVO> batchmap = new HashMap<String, BatchcodeVO>();
		for (BatchcodeVO batchvo : batchvos) {
			batchmap.put(batchvo.getCmaterialvid() + batchvo.getVbatchcode(),
					batchvo);
		}
		return batchmap;
	}
}
