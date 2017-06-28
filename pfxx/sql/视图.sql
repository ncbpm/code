--------------------------------------------------------
--  文件已创建 - 星期三-六月-21-2017   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for View VIEW_BPM_WAITEM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_WAITEM" ("PK_WA_CLASS", "CLNAME", "PK_ORG", "CYEAR", "CPERIOD", "ITEMKEY", "ITEMNAME", "CODE", "NAME") AS 
  SELECT citem.pk_wa_class,cl.name clname,citem.pk_org,citem.cyear,
  citem.cperiod,citem.itemkey,citem.name itemname,
  def.code,def.name name
  FROM wa_classitem citem
  left join wa_waclass cl on cl.pk_wa_class=citem.pk_wa_class
  left JOIN wa_item item ON citem.pk_wa_item = item.pk_wa_item
  left join bd_defdoc def on item.category_id  = def.pk_defdoc 
  ORDER BY citem.itemkey;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BHGBT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_BHGBT" ("PK_REJECTBILL", "PK_ORG", "ORGNAME", "VBILLCODE", "PK_STOCKORG", "STOCKORGNAME", "VREPORTBILLCODE", "VAPPLYBILLCODE", "DEPT_NAME", "CAPPLYTIME", "VCHKSTANDARDNAME", "PK_MATERIAL", "INV_CODE", "INV_NAME", "MATERIALSPEC", "MATERIALTYPE", "VBATCHCODE", "VCODE", "JLZNAME", "NCHECKASTNUM", "VCHANGERATE", "JLNAME", "NCHECKNUM", "VMEMO") AS 
  SELECT h.pk_rejectbill,   
  h.pk_org,org.name as orgname,--质检中心组织  
  h.vbillcode,--不合格品处理单号
  h.pk_stockorg,--库存组织主键
  orgs.name as stockorgname,--库存组织名称
  h.vreportbillcode ,  --质检报告号 
  h.vapplybillcode, -- 报检单号  
  od.name as dept_name,--报检部门信息 
  h.capplytime,   --报检时间   
  qc.vchkstandardname, --检验方案
  h.pk_material,
  inv.code as inv_code,  inv.name as inv_name,--物料  
  inv.materialspec,inv.materialtype,--规格、型号
  h.vbatchcode,--物料批次  
  sn.vcode,--序列号
  mea.name as jlZName,--主单位
  h.ncheckastnum, --检验数量
  h.vchangerate,--换算率
  meat.name as jlName,--单位
  h.nchecknum,--检验主数量 
  --单据状态默认:自由
  h.vmemo--备注
FROM qc_rejectbill h 
left join org_orgs org on org.pk_org=h.pk_org
left join org_orgs orgs on orgs.pk_org=h.pk_stockorg
LEFT JOIN org_dept od ON h.pk_applydept = od.pk_dept
left join qc_checkstandard qc on qc.pk_checkstandard =h.pk_chkstd --检验方案
left join sn_serialno sn on  sn.pk_serialno = h.pk_serialno --序列号
left join bd_measdoc mea on mea.pk_measdoc  = h.cunitid--计量单位cunitid
left join bd_measdoc meat on meat.pk_measdoc  = h.castunitid --单位
LEFT JOIN bd_material inv ON h. pk_material = inv.pk_material
where nvl(h.dr,0)=0 and h.fbillstatus=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BD_BOMVERSION
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_BD_BOMVERSION" ("HVERSION", "FBOMTYPECODE", "FBOMTYPE", "CBOMID", "CWLNAME", "CWLCODE", "CPK_WL", "CPK_WLFL", "CWLFLNAME", "CWLFLCODE", "CPK_UNIT", "CUNITNAME", "CUNITCODE", "CMATERIALSPEC", "CMATERIALTYPE") AS 
  select d.HVERSION,d.FBOMTYPE AS FBOMTYPECODE,(case d.FBOMTYPE when 1 then '生产BOM' when 2 then '包装BOM' when 3 then '配置BOM' end) as FBOMTYPE,d.CBOMID,a.name cWLname,a.code cWLcode,a.pk_material cPk_WL,a.pk_marbasclass cPk_WLFL,b.name cWLFLname,b.code cWLFLcode,a.pk_measdoc cPk_Unit,
c.name cUnitName,c.code cUnitCode,a.materialspec cMaterialspec,a.materialtype cMaterialtype
from bd_bom d left JOIN bd_material a on a.pk_material=d.hcmaterialid left join
bd_marbasclass b on a.pk_marbasclass=b.pk_marbasclass left join bd_measdoc c on a.pk_measdoc=c.pk_measdoc
where FBOMTYPE=1;
--------------------------------------------------------
--  DDL for View VIEW_BPM_RYLB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_RYLB" ("CODE", "NAME", "PK_PSNCL", "PARENT_ID") AS 
  select code, name, pk_psncl, parent_id from bd_psncl 
where 11 = 11 and ( enablestate = 2 ) and ( ( 1 = 1 ) ) order by code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BHGMXB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_BHGMXB" ("PK_REJECTBILL", "PK_REJECTBILL_B", "CROWNO", "JLNAME", "NASTNUM", "VCHANGERATE", "JLZNAME", "NNUM", "INV_CODE", "INV_NAME", "CQUALITYLVNAME", "BELIGIBLE", "VREJECTTYPENAME", "VCHECKITEMNAME", "VNOELIGNOTE", "PK_SUGGPROCESS", "CDEALFASHNAME", "FPROCESSJUDGE", "CLFSPD", "PSNNAME", "DEPTNAME", "VMEMOB") AS 
  SELECT b.pk_rejectbill,  
  b.pk_rejectbill_b,
  b.crowno,--行号
  meat.name as jlName,--单位
  b.nastnum, --数量
  b.vchangerate,--换算率
  mea.name as jlZName,--主单位
  b.nnum,--主数量
  inv.code as inv_code,--改判物料编码
  inv.name as inv_name,--物料名称
  scm.cqualitylvname, --质量等级 
  b.beligible,--是否合格品
  qc.vrejecttypename, --不合格类型pk_defecttype 
  item.vcheckitemname, --不合格项目
  b.vnoelignote ,--不合格说明
  b.pk_suggprocess,
  scmdel.cdealfashname, --建议处理方式 
  b.fprocessjudge,
  (case fprocessjudge when 1 then '入库'
    when 2 then '合格入库'
    when 3 then '报废入库'
    when 4 then '返工'
    when 5 then '不入库'
    when 6 then '合格'
    when 7 then '料废'
    when 8 then '工废'
    else '拒收' end ) as   clfspd,--处理方式判定:1=入库，2=合格入库，3=报废入库，4=返工，5=不入库，6=合格，7=料废，8=工废，9=拒收
  psn.name as psnname,--责任人
  dept.name as deptname,--责任部门
  b.vmemob --备注
FROM qc_rejectbill_b b
left join bd_measdoc mea on mea.pk_measdoc  = b.cunitid--计量单位cunitid
left join bd_measdoc meat on meat.pk_measdoc  = b.castunitid --单位
LEFT JOIN bd_material inv ON b.pk_chgmrl = inv.pk_material--物料
left join scm_qualitylevel_b scm on scm.pk_qualitylv_b = b.pk_qualitylv_b --质量等级
left join qc_rejecttype qc on qc.pk_rejecttype =b.pk_defecttype--不合格类型
left join qc_checkitem item on item.pk_checkitem = b.pk_noelgichkitem--不合格项目
left join scm_dealfashion scmdel on scmdel.pk_dealfashion = b.pk_suggprocess--建议处理方式
left join bd_psndoc psn on psn.pk_psndoc=b.pk_chargepsn --责任人
LEFT JOIN org_dept dept ON dept.pk_dept=b.pk_chargedept --责任部门
where nvl(b.dr,0)=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_ACCOA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_ACCOA" ("ORG_PK", "ORG_CODE", "ORG_NAME", "OASTM_CODERULE", "OASTM_PK", "OASTM_CODE", "OASTM_NAME", "KEMU_PK", "KEMU_NAME", "KEMU_CODE", "SSITEM_PK", "SSITEM_CODE", "SSITEM_NAME") AS 
  (

select 

--科目表以及对应的财务组织和科目体系
--分配到组织
org.pk_org as org_pk,
org.code as org_code,
org.name as org_name,

--科目体系
oastm.acccoderule as oastm_coderule ,--科目编码规则  
oastm.pk_accsystem as oastm_pk,
oastm.code as oastm_code,
oastm.name as oastm_name,

--会计科目明细
oab. pk_accasoa  as kemu_pk,
oab.name as  kemu_name, 
oabase.code as kemu_code,

--辅助核算
ssitem.pk_accassitem  as  ssitem_pk,
ssitem.code  as ssitem_code,
ssitem.name  as  ssitem_name

from 
--会计科目
bd_accasoa oab
left join bd_account oabase on oab.  pk_account = oabase. pk_account 
left join bd_accchart oah on oab.   pk_accchart  = oah.  pk_accchart 
left join org_orgs org on   oah.   pk_org  = org.pk_org
left join bd_accsystem oastm  on oah.  pk_accsystem  = oastm. pk_accsystem 
--辅助核算，逻辑上可以有多个
left join bd_accass oass on oab.pk_accasoa=oass.pk_accasoa
left join bd_accassitem ssitem on oass. pk_entity   = ssitem.pk_accassitem
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_ASSETSCODE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_ASSETSCODE" ("卡片主键", "单据类型", "利润中心", "利润中心版本", "财务组织", "资产编码", "资产名称", "币种", "CURR_NAME", "资产类别编码", "资产类别名称", "规格", "型号", "存放地点", "生产厂家", "供应商主键", "开始使用日期", "原币原值", "净值", "使用月限", "使用部门", "部门编码", "PK_USEDEPT") AS 
  Select
	A .Pk_Card Pk_Card,
	A .Pk_Transitype Pk_Transitype,
	A .pk_raorg pk_raorg,
	A .pk_raorg_v pk_raorg_v,
	A .pk_org pk_org,
	A .asset_code asset_code,
	A .asset_name asset_name,
	bd_currtype .pk_currtype pk_currtype,
  bd_currtype.NAME curr_name ,
	c.cate_code cate_code,
	c.cate_name cate_name,
	A .spec spec,
	A .card_model card_model,
	A .position position,
	A .producer producer,
	A .Provider Provider,
	SUBSTR (A .begin_date, 0, 10) startDate,
	b.originvalue originvalue,
	(
		b.localoriginvalue - b.accudep_cal
	) networth,
	B.Naturemonth Naturemonth,
  
	b.pk_usedept  PK_USEDEPT


FROM
	fa_card A
INNER JOIN fa_cardhistory b ON A .pk_card = b.pk_card
INNER JOIN fa_cardsub ON A .pk_card = fa_cardsub.pk_card
LEFT OUTER JOIN fa_category c ON b.pk_category = c.pk_category
--LEFT  JOIN org_dept D ON b.pk_usedept = D .pk_dept
left outer join bd_currtype bd_currtype ON bd_currtype.pk_currtype=a.pk_currency
WHERE
	(
		b.laststate_flag = 'Y'
		OR (
			b.asset_state IN (
				'reduce',
				'reduce_split',
				'reduce_combin',
				'redeploy_way',
				'redeploy_out'
			)
			AND b.laststate_flag = 'N'
		)
	)
AND A .dr = 0
AND b.dr = 0
AND fa_cardsub.dr = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_PORTDOC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_PORTDOC" ("INNERCODE", "NAME", "PK_DEFDOC") AS 
  select b.INNERCODE,b.NAME,b.PK_DEFDOC from bd_defdoc b left join bd_defdoclist a on a.pk_defdoclist=b.pk_defdoclist where a.code='A03' and (b.PID!='~' or b.pid!=null);
--------------------------------------------------------
--  DDL for View VIEW_JL_INV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_JL_INV" ("存货编码", "CINVADDCODE", "存货名称", "规格型号", "是否销售", "是否外购", "是否自制", "助记码", "是否计量", "计量单位名称", "入库超额上限") AS 
  (SELECT inv.code      AS 存货编码,
    ''                  AS cInvAddCode,
    inv. name           AS 存货名称,
    inv. materialspec   AS 规格型号,
    '是'                 AS 是否销售,
    '是'                 AS 是否外购,
    '是'                 AS 是否自制,
    inv.materialmnecode AS 助记码,
    NVL(inv.def1,'N')   AS 是否计量,
    meas.name           AS 计量单位名称,
    9999999             AS 入库超额上限
  FROM bd_material inv
  LEFT JOIN bd_measdoc meas
  ON inv.pk_measdoc = meas.pk_measdoc
  WHERE NVL(inv.dr,0)   =0
    --是否计量
  AND NVL(inv.def1,'N')='Y'
    --已经启用
  AND inv.enablestate =2
    --  AND (cInvCCode NOT BETWEEN '0401' AND '0413')
  );
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEAUTOCOST01
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_NC_ZUOYEAUTOCOST01" ("PK_GROUP", "PK_ORG", "CCOSTCENTERID", "CPERIOD", "WGNNUM", "XHNNUM", "ZYNNUM") AS 
  (
select pk_group,pk_org,ccostcenterid,cperiod,sum(wgnnum) as wgnnum,sum(xhnum) as xhnnum,sum(zynnum)  as zynnum  from (

select pk_group,pk_org,ccostcenterid,cperiod , sum(1) as  wgnnum, 0 as xhnum, 0 as zynnum  from  cm_product where nvl(dr,0)=0
group by pk_group,pk_org,ccostcenterid,cperiod
union all

select pk_group,pk_org,ccostcenterid,cperiod , 0 as  wgnnum, sum(1) as xhnum, 0 as zynnum from  cm_stuff where nvl(dr,0)=0
group by pk_group,pk_org,ccostcenterid,cperiod
union all

select pk_group,pk_org,ccostcenterid,cperiod , 0 as  wgnnum, 0 as xhnum, sum(1) as zynnum from  cm_actcost where nvl(dr,0)=0
group by pk_group,pk_org,ccostcenterid,cperiod
)
group by pk_group,pk_org,ccostcenterid,cperiod
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_TZLX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_TZLX" ("TRNSTYPECODE", "TRNSTYPENAME", "PK_TRNSTYPE") AS 
  SELECT trnstypecode,
  trnstypename,
  pk_trnstype
FROM hr_trnstype
WHERE ( 1        = 1
AND enablestate  = 2
AND trnsevent    = 3 )
AND ( ( ( pk_org = 'GLOBLE00000000000000'
OR pk_group      = '0001A51000000000078A' ) ) )
ORDER BY trnstypecode;
--------------------------------------------------------
--  DDL for View VIEW_BPM_SUPPLIERCLASS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_SUPPLIERCLASS" ("CODE", "CREATIONTIME", "CREATOR", "DATAORIGINFLAG", "DEF1", "DEF2", "DEF3", "DEF4", "DEF5", "DR", "ENABLESTATE", "INNERCODE", "MNECODE", "MODIFIEDTIME", "MODIFIER", "NAME", "NAME2", "NAME3", "NAME4", "NAME5", "NAME6", "PARENT_ID", "PK_GROUP", "PK_ORG", "PK_SUPPLIERCLASS", "SEQ", "TS") AS 
  (select "CODE","CREATIONTIME","CREATOR","DATAORIGINFLAG","DEF1","DEF2","DEF3","DEF4","DEF5","DR","ENABLESTATE","INNERCODE","MNECODE","MODIFIEDTIME","MODIFIER","NAME","NAME2","NAME3","NAME4","NAME5","NAME6","PARENT_ID","PK_GROUP","PK_ORG","PK_SUPPLIERCLASS","SEQ","TS" from bd_supplierclass  where  nvl(dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_LZLX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_LZLX" ("TRNSTYPECODE", "TRNSTYPENAME", "PK_TRNSTYPE") AS 
  SELECT trnstypecode,
  trnstypename,
  pk_trnstype
FROM hr_trnstype
WHERE ( 1        = 1
AND enablestate  = 2
AND trnsevent    = 4 )
AND ( ( ( pk_org = 'GLOBLE00000000000000'
OR pk_group      = '0001A51000000000078A' ) ) )
ORDER BY trnstypecode;
--------------------------------------------------------
--  DDL for View VIEW_BPM_XZFA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_XZFA" ("PK_WA_CLASS", "CODE", "NAME", "PK_PERIODSCHEME", "CYEAR", "CPERIOD", "MUTIPLEFLAG", "PK_ORG") AS 
  SELECT 
  wa_waclass.pk_wa_class,--薪资方案主键
  wa_waclass.code,--薪资方案编码
  wa_waclass.name,--薪资方案名称
  wa_waclass.pk_periodscheme,--薪资期间方案主键
  wa_waclass.cyear,--年份
  wa_waclass.cperiod,--月份期间
  wa_waclass.mutipleflag, --多次发放 
  wa_waclass.pk_org--组织
FROM wa_waclass
WHERE 11                        = 11
AND ( stopflag                  = 'N' )
AND wa_waclass.pk_periodscheme != '~'
AND showflag                    = 'Y' 
ORDER BY wa_waclass.pk_org,
  wa_waclass.code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_SUPPLIER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_SUPPLIER" ("PK_SUPPLIER", "VENNAME", "VENCODE", "VENSHORTNAME", "RESPPERSON", "PSNNAME", "PSNCODE", "RESPDEPT", "DEPCODE", "DEPNAME") AS 
  select a.pk_supplier,a.name as VenName,a.code as VenCode,a.shortname as Venshortname,
b.respperson,d.name as psnname,d.code as psncode,
b.respdept,d.code as depcode, d.name as depname
--c.pk_purchaseorg,c.code as sorgcode ,c.name as sorgname
from bd_supplier a
inner join bd_supstock b on a.pk_supplier=b.pk_supplier
--inner join org_purchaseorg c on a.pk_org=c.pk_purchaseorg
left join bd_psndoc d on b.respperson =d.pk_psndoc 
left join org_dept e on b.respdept=e.pk_dept;
--------------------------------------------------------
--  DDL for View VIEW_BPM_INCOTERM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_INCOTERM" ("CODE", "NAME", "PK_INCOTERM", "PK_ORG", "PK_GROUP") AS 
  (
  select c.code, c.name, c.pk_incoterm, c.pk_org, c.pk_group
  from bd_incoterm c );
--------------------------------------------------------
--  DDL for View VIEW_BPM_ARRIVEORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_ARRIVEORDER" ("VBILLCODE", "BBACKREFORDER", "BC_VVENDBATCHCODE", "BFAFLAG", "BLETGOSTATE", "BPRESENT", "BPRESENTSOURCE", "BTRANSASSET", "BTRIATRADEFLAG", "CASSCUSTID", "CASTUNITID", "CCURRENCYID", "CFFILEID", "CFIRSTBID", "CFIRSTID", "CFIRSTTYPECODE", "CORIGCURRENCYID", "CPASSBOLLROWNO", "CPRODUCTORID", "CPROJECTID", "CPROJECTTASKID", "CQUALITYLEVELID", "CRECECOUNTRYID", "CREPORTERID", "CROWNO", "CSENDCOUNTRYID", "CSOURCEARRIVEBID", "CSOURCEARRIVEID", "CSOURCEBID", "CSOURCEID", "CSOURCETYPECODE", "CTAXCOUNTRYID", "CUNITID", "DBILLDATE", "DINVALIDDATE", "DPLANRECEIVEDATE", "DPRODUCEDATE", "DR", "DREPORTDATE", "FBUYSELLFLAG", "FPRODUCTCLASS", "FTAXTYPEFLAG", "IVALIDDAY", "NACCUMBACKNUM", "NACCUMCHECKNUM", "NACCUMLETGOINNUM", "NACCUMLETGONUM", "NACCUMREPLNUM", "NACCUMSTORENUM", "NASTNUM", "NELIGNUM", "NEXCHANGERATE", "NMNY", "NNOTELIGNUM", "NNUM", "NORIGMNY", "NORIGPRICE", "NORIGTAXMNY", "NORIGTAXPRICE", "NPLANASTNUM", "NPLANNUM", "NPRESENTASTNUM", "NPRESENTNUM", "NPRICE", "NTAX", "NTAXMNY", "NTAXPRICE", "NTAXRATE", "NWASTASTNUM", "NWASTNUM", "PK_APFINANCEORG", "PK_APFINANCEORG_V", "PK_APLIABCENTER", "PK_APLIABCENTER_V", "PK_ARRIVEORDER", "PK_ARRIVEORDER_B", "PK_ARRLIABCENTER", "PK_ARRLIABCENTER_V", "PK_BATCHCODE", "PK_GROUP", "PK_MATERIAL", "PK_ORDER", "PK_ORDER_B", "PK_ORDER_BB1", "PK_ORG", "PK_ORG_V", "PK_PASSBILL", "PK_PASSBILL_B", "PK_PSFINANCEORG", "PK_PSFINANCEORG_V", "PK_RACK", "PK_RECEIVESTORE", "PK_REQSTOORG", "PK_REQSTOORG_V", "PK_REQSTORE", "PK_SRCMATERIAL", "TS", "VBACKREASONB", "VBATCHCODE", "VBDEF1", "VBDEF10", "VBDEF11", "VBDEF12", "VBDEF13", "VBDEF14", "VBDEF15", "VBDEF16", "VBDEF17", "VBDEF18", "VBDEF19", "VBDEF2", "VBDEF20", "VBDEF3", "VBDEF4", "VBDEF5", "VBDEF6", "VBDEF7", "VBDEF8", "VBDEF9", "VCHANGERATE", "VFIRSTCODE", "VFIRSTROWNO", "VFIRSTTRANTYPE", "VFREE1", "VFREE10", "VFREE2", "VFREE3", "VFREE4", "VFREE5", "VFREE6", "VFREE7", "VFREE8", "VFREE9", "VMEMOB", "VPASSBILLCODE", "VSOURCECODE", "VSOURCEROWNO", "VSOURCETRANTYPE") AS 
  (select r.vbillcode ,b."BBACKREFORDER",b."BC_VVENDBATCHCODE",b."BFAFLAG",b."BLETGOSTATE",b."BPRESENT",b."BPRESENTSOURCE",b."BTRANSASSET",b."BTRIATRADEFLAG",b."CASSCUSTID",b."CASTUNITID",b."CCURRENCYID",b."CFFILEID",b."CFIRSTBID",b."CFIRSTID",b."CFIRSTTYPECODE",b."CORIGCURRENCYID",b."CPASSBOLLROWNO",b."CPRODUCTORID",b."CPROJECTID",b."CPROJECTTASKID",b."CQUALITYLEVELID",b."CRECECOUNTRYID",b."CREPORTERID",b."CROWNO",b."CSENDCOUNTRYID",b."CSOURCEARRIVEBID",b."CSOURCEARRIVEID",b."CSOURCEBID",b."CSOURCEID",b."CSOURCETYPECODE",b."CTAXCOUNTRYID",b."CUNITID",b."DBILLDATE",b."DINVALIDDATE",b."DPLANRECEIVEDATE",b."DPRODUCEDATE",b."DR",b."DREPORTDATE",b."FBUYSELLFLAG",b."FPRODUCTCLASS",b."FTAXTYPEFLAG",b."IVALIDDAY",b."NACCUMBACKNUM",b."NACCUMCHECKNUM",b."NACCUMLETGOINNUM",b."NACCUMLETGONUM",b."NACCUMREPLNUM",b."NACCUMSTORENUM",b."NASTNUM",b."NELIGNUM",b."NEXCHANGERATE",b."NMNY",b."NNOTELIGNUM",b."NNUM",b."NORIGMNY",b."NORIGPRICE",b."NORIGTAXMNY",b."NORIGTAXPRICE",b."NPLANASTNUM",b."NPLANNUM",b."NPRESENTASTNUM",b."NPRESENTNUM",b."NPRICE",b."NTAX",b."NTAXMNY",b."NTAXPRICE",b."NTAXRATE",b."NWASTASTNUM",b."NWASTNUM",b."PK_APFINANCEORG",b."PK_APFINANCEORG_V",b."PK_APLIABCENTER",b."PK_APLIABCENTER_V",b."PK_ARRIVEORDER",b."PK_ARRIVEORDER_B",b."PK_ARRLIABCENTER",b."PK_ARRLIABCENTER_V",b."PK_BATCHCODE",b."PK_GROUP",b."PK_MATERIAL",b."PK_ORDER",b."PK_ORDER_B",b."PK_ORDER_BB1",b."PK_ORG",b."PK_ORG_V",b."PK_PASSBILL",b."PK_PASSBILL_B",b."PK_PSFINANCEORG",b."PK_PSFINANCEORG_V",b."PK_RACK",b."PK_RECEIVESTORE",b."PK_REQSTOORG",b."PK_REQSTOORG_V",b."PK_REQSTORE",b."PK_SRCMATERIAL",b."TS",b."VBACKREASONB",b."VBATCHCODE",b."VBDEF1",b."VBDEF10",b."VBDEF11",b."VBDEF12",b."VBDEF13",b."VBDEF14",b."VBDEF15",b."VBDEF16",b."VBDEF17",b."VBDEF18",b."VBDEF19",b."VBDEF2",b."VBDEF20",b."VBDEF3",b."VBDEF4",b."VBDEF5",b."VBDEF6",b."VBDEF7",b."VBDEF8",b."VBDEF9",b."VCHANGERATE",b."VFIRSTCODE",b."VFIRSTROWNO",b."VFIRSTTRANTYPE",b."VFREE1",b."VFREE10",b."VFREE2",b."VFREE3",b."VFREE4",b."VFREE5",b."VFREE6",b."VFREE7",b."VFREE8",b."VFREE9",b."VMEMOB",b."VPASSBILLCODE",b."VSOURCECODE",b."VSOURCEROWNO",b."VSOURCETRANTYPE" from  po_arriveorder r join po_arriveorder_b b  on r.pk_arriveorder =b.pk_arriveorder  where   nvl(r.fbillstatus ,0) = 3 and nvl(r.dr,0) = 0 and nvl(b.dr,0) = 0)
;
--------------------------------------------------------
--  DDL for View VIEW_BPM_REGISTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_REGISTE" ("ORG_PK", "ORG_CODE", "ORG_NAME", "BILLNO_PK", "FBMBILLNO", "QIANFA_DATE", "DAOQI_DATE", "SHOUDAO_DATE", "PAYBANK_PK", "PAYBANK_CODE", "PAYBANK_NAME") AS 
  (SELECT
   --组织
    org.pk_org AS org_pk,
    org.code   AS org_code,
    org.name   AS org_name,
    --NC票据号主键
    pk_register AS billno_pk,
    --票据号
    fbmbillno AS fbmbillno,
    --dvoucherdate  制证日期 ： as 签发日期、
    dvoucherdate AS qianfa_date,
    --到期日期
    enddate AS daoqi_date,
    --收到日期
    gatherdate AS shoudao_date,
    --承兑银行
    hidepaybank as paybank_pk,
    bank.code as paybank_code,
    bank.name as paybank_name
   
  FROM FBM_REGISTER fbm
  left  JOIN org_orgs org  ON fbm.pk_org = org.pk_org
  left join bd_bankdoc bank on fbm.hidepaybank= bank. pk_bankdoc 
  
  WHERE NVL(fbm.dr,0)     =0
  AND fbm.BASEINFOSTATUS IN('register','has_invoice')
  
    --如果判断已经使用？
  );
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYECHUYUN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_NC_ZUOYECHUYUN" ("PK_GROUP", "PK_ORG", "CDPTID", "CTRANTYPEID", "DBILLDATE", "TAUDITTIME", "NNUM") AS 
  (
---采购入库
select  pk_group,pk_org ,cdptid as  cdptid, ctrantypeid ,   dbilldate ,taudittime , abs(ntotalnum) as nnum from  ic_purchasein_h 
where nvl(dr,0)=0 
union all
---调拨入库
select  pk_group,pk_org ,cdptid as  cdptid, ctrantypeid , dbilldate , taudittime , abs(ntotalnum) as nnum from  ic_transin_h 
where nvl(dr,0)=0 
union all
---产成品入库
select  pk_group,pk_org ,cdptid as  cdptid, ctrantypeid , dbilldate , taudittime , abs(ntotalnum) as nnum from  ic_finprodin_h 
where nvl(dr,0)=0 
union all
---其他入库
select  pk_group,pk_org ,cdptid as  cdptid, ctrantypeid , dbilldate , taudittime , abs(ntotalnum) as nnum from  ic_generalin_h 
where nvl(dr,0)=0 
union all
--委托加工入库
select  pk_group,pk_org ,cdptid as  cdptid, ctrantypeid , dbilldate , taudittime , abs(ntotalnum) as nnum from  ic_subcontin_h 
where nvl(dr,0)=0 
union all
---生产报废入库
select  pk_group,pk_org ,cdptid as  cdptid, ctrantypeid , dbilldate , taudittime , abs(ntotalnum) as nnum from  ic_discardin_h 
where nvl(dr,0)=0 
union all
--销售出库
select  pk_group,pk_org ,   cdptid  as  cdptid ,  ctrantypeid ,  dbilldate , taudittime , abs(ntotalnum) as nnum  from  ic_saleout_h 
where nvl(dr,0)=0 
union all
--调拨出库
select  pk_group,pk_org ,   cdptid  as  cdptid ,  ctrantypeid ,  dbilldate , taudittime , abs(ntotalnum) as nnum  from  ic_transout_h 
where nvl(dr,0)=0

union all
--材料出库
select  pk_group,pk_org ,   cdptid  as  cdptid ,  ctrantypeid ,  dbilldate , taudittime , abs(ntotalnum) as nnum  from  ic_material_h 
where nvl(dr,0)=0 
union all
--其他出库
select  pk_group,pk_org ,   cdptid  as  cdptid ,  ctrantypeid ,  dbilldate , taudittime , abs(ntotalnum) as nnum  from  ic_generalout_h 
where nvl(dr,0)=0 
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_FIVEMETALSBALANCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_FIVEMETALSBALANCE" ("CARDNO", "PK_GROUP", "PK_ORG", "CPERIOD", "DEPARTNAME", "BALANCE") AS 
  (select h.vcardno cardNo,
       h.pk_group,
       h.pk_org,
       h.cperiod,
       case
         when h.vdepartment is null then
          h.vproject
         else
          h.vdepartment
       end departName,
       e.balance
  from ic_fivemetals_h h
  join (select sum(nmny * itype) balance, b.pk_fivemetals_h
          from ic_fivemetals_b b
         where nvl(b.dr, 0) = 0
         group by b.pk_fivemetals_h) e
    on h.pk_fivemetals_h = e.pk_fivemetals_h
 where nvl(h.dr, 0) = 0)
;
--------------------------------------------------------
--  DDL for View VIEW_BPM_CGTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_CGTYPE" ("PK_BILLTYPECODE", "BILLTYPENAME", "PK_BILLTYPEID") AS 
  select distinct pk_billtypecode, billtypename, pk_billtypeid from bd_billtype where parentbilltype = '21' and pk_group = '0001A51000000000078A' and nvl ( islock, 'N' ) = 'N' and PK_BILLTYPECODE!='21-Cxx-01';
--------------------------------------------------------
--  DDL for View VIEW_JL_CUSTOMER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_JL_CUSTOMER" ("客商编码", "客商名称", "客商简称", "助记码", "是否国外", "CVENDEFINE1", "CVENDEFINE2") AS 
  (
  SELECT code AS 客商编码,
    name       AS 客商名称,
    shortname  AS 客商简称,
    mnecode    AS 助记码,
    CASE pk_country
      WHEN '0001Z010000000079UJJ'
      THEN '是'
      ELSE '否'
    END AS 是否国外,
    '' cVenDefine1,
    '' cVenDefine2
  FROM bd_customer
  where nvl(dr,0)=0 
  --是否计量
  and nvl(def1,'N')='Y'
  );
--------------------------------------------------------
--  DDL for View VIEW_BPM_CUSTOMER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_CUSTOMER" ("CUSNAME", "CUSCODE", "SHORTNAME", "PK_CUSTOMER", "PK_COUNTRY", "COUNTRYNAME", "COUNTRYCODE") AS 
  ( SELECT a.name as CusName,a.code as CusCode,A.SHORTNAME,A.PK_CUSTOMER,A.PK_COUNTRY,b.NAME as countryName,b.CODE as countryCode 
FROM bd_customer a left join bd_countryzone b on a.pk_country = b.pk_country);
--------------------------------------------------------
--  DDL for View VIEW_BPM_CURRINFO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_CURRINFO" ("FNPK", "FNNAME", "SRCCURPK", "SRCCURCODE", "SRCCURNAME", "OPPCURPK", "OPPCURCODE", "OPPCURNAM", "ADJUSTPK", "MONTH", "YEAR", "ADJUSTRATE", "YEARMTH") AS 
  (
select
--汇率方案
bc.pk_exratescheme as fnpk,
scm.name as fnname,

--源币种
bc.pk_currtype  as srccurpk,
srccur.code as srccurcode,
srccur.name as srccurname,
--目的币种
bc.oppcurrtype  as oppcurpk,
oppcur.code as oppcurcode,
oppcur.name as oppcurnam,
--期间汇率
arate. pk_adjustrate  as adjustpk,
--会计月份：RF是自然月的会计期间
amonth.Accperiodmth as month,
substr(amonth.yearmth,0,4) as year,
--amonth.begindate as abegindate ,
--amonth.enddate  as aenddate  ,
arate.adjustrate  as adjustrate,
amonth.yearmth as yearmth
from  bd_exratescheme scm
inner  join bd_currinfo bc on  scm.pk_exratescheme = bc. pk_exratescheme
inner join bd_currtype  oppcur on bc.oppcurrtype = oppcur.  pk_currtype
inner join bd_currtype  srccur on bc.pk_currtype  = srccur.pk_currtype
inner join  bd_adjustrate arate on bc. pk_currinfo= arate. pk_currinfo
inner join  bd_accperiodmonth amonth on arate. pk_accperiodmonth  = amonth. pk_accperiodmonth
where nvl(bc.dr,0)=0 and nvl(arate.dr,0)=0

);
--------------------------------------------------------
--  DDL for View VIEW_BPM_BD_MATERIAL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_BD_MATERIAL" ("CWLNAME", "CWLCODE", "PRODAREA", "CPK_WL", "CPK_WLFL", "CWLFLNAME", "CWLFLCODE", "CPK_UNIT", "CUNITNAME", "CUNITCODE", "CMATERIALSPEC", "CMATERIALTYPE") AS 
  select a.name cWLname,a.code cWLcode,a.prodarea,a.pk_material cPk_WL,a.pk_marbasclass cPk_WLFL,b.name cWLFLname,b.code cWLFLcode,a.pk_measdoc cPk_Unit,
c.name cUnitName,c.code cUnitCode,a.materialspec cMaterialspec,a.materialtype cMaterialtype
from bd_material a left join
bd_marbasclass b on a.pk_marbasclass=b.pk_marbasclass left join bd_measdoc c on a.pk_measdoc=c.pk_measdoc;
--------------------------------------------------------
--  DDL for View VIEW_BPM_POORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_POORDER" ("VBILLCODE", "BARRIVECLOSE", "BBORROWPUR", "BINVOICECLOSE", "BLARGESS", "BPAYCLOSE", "BRECEIVEPLAN", "BSTOCKCLOSE", "BTRANSCLOSED", "BTRIATRADEFLAG", "CASSCUSTID", "CASTUNITID", "CCONTRACTID", "CCONTRACTROWID", "CCURRENCYID", "CDESTIAREAID", "CDESTICOUNTRYID", "CDEVADDRID", "CDEVAREAID", "CECBILLBID", "CECBILLID", "CECTYPECODE", "CFFILEID", "CFIRSTBID", "CFIRSTID", "CFIRSTTYPECODE", "CHANDLER", "CORIGAREAID", "CORIGCOUNTRYID", "CORIGCURRENCYID", "CPRAYBILLBID", "CPRAYBILLCODE", "CPRAYBILLHID", "CPRAYBILLROWNO", "CPRAYTYPECODE", "CPRICEAUDIT_BB1ID", "CPRICEAUDIT_BID", "CPRICEAUDITID", "CPRODUCTORID", "CPROJECTID", "CPROJECTTASKID", "CQPBASESCHEMEID", "CQTUNITID", "CQUALITYLEVELID", "CRECECOUNTRYID", "CROWNO", "CSENDCOUNTRYID", "CSOURCEBID", "CSOURCEID", "CSOURCETYPECODE", "CTAXCODEID", "CTAXCOUNTRYID", "CUNITID", "CVENDDEVADDRID", "CVENDDEVAREAID", "DBILLDATE", "DCORRECTDATE", "DPLANARRVDATE", "DR", "FBUYSELLFLAG", "FISACTIVE", "FTAXTYPEFLAG", "NACCCANCELINVMNY", "NACCUMARRVNUM", "NACCUMDEVNUM", "NACCUMINVOICEMNY", "NACCUMINVOICENUM", "NACCUMPICKUPNUM", "NACCUMRPNUM", "NACCUMSTORENUM", "NACCUMWASTNUM", "NASTNUM", "NBACKARRVNUM", "NBACKSTORENUM", "NCALCOSTMNY", "NCALTAXMNY", "NEXCHANGERATE", "NFEEMNY", "NGLOBALEXCHGRATE", "NGLOBALMNY", "NGLOBALTAXMNY", "NGROUPEXCHGRATE", "NGROUPMNY", "NGROUPTAXMNY", "NITEMDISCOUNTRATE", "NMNY", "NNETPRICE", "NNOSUBTAX", "NNOSUBTAXRATE", "NNUM", "NORIGMNY", "NORIGNETPRICE", "NORIGPRICE", "NORIGTAXMNY", "NORIGTAXNETPRICE", "NORIGTAXPRICE", "NPACKNUM", "NPRICE", "NQTNETPRICE", "NQTORIGNETPRICE", "NQTORIGPRICE", "NQTORIGTAXNETPRC", "NQTORIGTAXPRICE", "NQTPRICE", "NQTTAXNETPRICE", "NQTTAXPRICE", "NQTUNITNUM", "NSUPRSNUM", "NTAX", "NTAXMNY", "NTAXNETPRICE", "NTAXPRICE", "NTAXRATE", "NVOLUMN", "NWEIGHT", "PK_APFINANCEORG", "PK_APFINANCEORG_V", "PK_APLIABCENTER", "PK_APLIABCENTER_V", "PK_ARRLIABCENTER", "PK_ARRLIABCENTER_V", "PK_ARRVSTOORG", "PK_ARRVSTOORG_V", "PK_BATCHCODE", "PK_DISCOUNT", "PK_FLOWSTOCKORG", "PK_FLOWSTOCKORG_V", "PK_GROUP", "PK_MATERIAL", "PK_ORDER", "PK_ORDER_B", "PK_ORG", "PK_ORG_V", "PK_PSFINANCEORG", "PK_PSFINANCEORG_V", "PK_RECEIVEADDRESS", "PK_RECVSTORDOC", "PK_REQCORP", "PK_REQDEPT", "PK_REQDEPT_V", "PK_REQSTOORG", "PK_REQSTOORG_V", "PK_REQSTORDOC", "PK_SRCMATERIAL", "PK_SRCORDER_B", "PK_SUPPLIER", "TS", "VBATCHCODE", "VBDEF1", "VBDEF10", "VBDEF11", "VBDEF12", "VBDEF13", "VBDEF14", "VBDEF15", "VBDEF16", "VBDEF17", "VBDEF18", "VBDEF19", "VBDEF2", "VBDEF20", "VBDEF3", "VBDEF4", "VBDEF5", "VBDEF6", "VBDEF7", "VBDEF8", "VBDEF9", "VBMEMO", "VCHANGERATE", "VCONTRACTCODE", "VECBILLCODE", "VFIRSTCODE", "VFIRSTROWNO", "VFIRSTTRANTYPE", "VFREE1", "VFREE10", "VFREE2", "VFREE3", "VFREE4", "VFREE5", "VFREE6", "VFREE7", "VFREE8", "VFREE9", "VPRICEAUDITCODE", "VQTUNITRATE", "VSOURCECODE", "VSOURCEROWNO", "VSOURCETRANTYPE", "VVENDDEVADDR", "VVENDINVENTORYCODE", "VVENDINVENTORYNAME") AS 
  (select r.vbillcode, b."BARRIVECLOSE",b."BBORROWPUR",b."BINVOICECLOSE",b."BLARGESS",b."BPAYCLOSE",b."BRECEIVEPLAN",b."BSTOCKCLOSE",b."BTRANSCLOSED",b."BTRIATRADEFLAG",b."CASSCUSTID",b."CASTUNITID",b."CCONTRACTID",b."CCONTRACTROWID",b."CCURRENCYID",b."CDESTIAREAID",b."CDESTICOUNTRYID",b."CDEVADDRID",b."CDEVAREAID",b."CECBILLBID",b."CECBILLID",b."CECTYPECODE",b."CFFILEID",b."CFIRSTBID",b."CFIRSTID",b."CFIRSTTYPECODE",b."CHANDLER",b."CORIGAREAID",b."CORIGCOUNTRYID",b."CORIGCURRENCYID",b."CPRAYBILLBID",b."CPRAYBILLCODE",b."CPRAYBILLHID",b."CPRAYBILLROWNO",b."CPRAYTYPECODE",b."CPRICEAUDIT_BB1ID",b."CPRICEAUDIT_BID",b."CPRICEAUDITID",b."CPRODUCTORID",b."CPROJECTID",b."CPROJECTTASKID",b."CQPBASESCHEMEID",b."CQTUNITID",b."CQUALITYLEVELID",b."CRECECOUNTRYID",b."CROWNO",b."CSENDCOUNTRYID",b."CSOURCEBID",b."CSOURCEID",b."CSOURCETYPECODE",b."CTAXCODEID",b."CTAXCOUNTRYID",b."CUNITID",b."CVENDDEVADDRID",b."CVENDDEVAREAID",b."DBILLDATE",b."DCORRECTDATE",b."DPLANARRVDATE",b."DR",b."FBUYSELLFLAG",b."FISACTIVE",b."FTAXTYPEFLAG",b."NACCCANCELINVMNY",b."NACCUMARRVNUM",b."NACCUMDEVNUM",b."NACCUMINVOICEMNY",b."NACCUMINVOICENUM",b."NACCUMPICKUPNUM",b."NACCUMRPNUM",b."NACCUMSTORENUM",b."NACCUMWASTNUM",b."NASTNUM",b."NBACKARRVNUM",b."NBACKSTORENUM",b."NCALCOSTMNY",b."NCALTAXMNY",b."NEXCHANGERATE",b."NFEEMNY",b."NGLOBALEXCHGRATE",b."NGLOBALMNY",b."NGLOBALTAXMNY",b."NGROUPEXCHGRATE",b."NGROUPMNY",b."NGROUPTAXMNY",b."NITEMDISCOUNTRATE",b."NMNY",b."NNETPRICE",b."NNOSUBTAX",b."NNOSUBTAXRATE",b."NNUM",b."NORIGMNY",b."NORIGNETPRICE",b."NORIGPRICE",b."NORIGTAXMNY",b."NORIGTAXNETPRICE",b."NORIGTAXPRICE",b."NPACKNUM",b."NPRICE",b."NQTNETPRICE",b."NQTORIGNETPRICE",b."NQTORIGPRICE",b."NQTORIGTAXNETPRC",b."NQTORIGTAXPRICE",b."NQTPRICE",b."NQTTAXNETPRICE",b."NQTTAXPRICE",b."NQTUNITNUM",b."NSUPRSNUM",b."NTAX",b."NTAXMNY",b."NTAXNETPRICE",b."NTAXPRICE",b."NTAXRATE",b."NVOLUMN",b."NWEIGHT",b."PK_APFINANCEORG",b."PK_APFINANCEORG_V",b."PK_APLIABCENTER",b."PK_APLIABCENTER_V",b."PK_ARRLIABCENTER",b."PK_ARRLIABCENTER_V",b."PK_ARRVSTOORG",b."PK_ARRVSTOORG_V",b."PK_BATCHCODE",b."PK_DISCOUNT",b."PK_FLOWSTOCKORG",b."PK_FLOWSTOCKORG_V",b."PK_GROUP",b."PK_MATERIAL",b."PK_ORDER",b."PK_ORDER_B",b."PK_ORG",b."PK_ORG_V",b."PK_PSFINANCEORG",b."PK_PSFINANCEORG_V",b."PK_RECEIVEADDRESS",b."PK_RECVSTORDOC",b."PK_REQCORP",b."PK_REQDEPT",b."PK_REQDEPT_V",b."PK_REQSTOORG",b."PK_REQSTOORG_V",b."PK_REQSTORDOC",b."PK_SRCMATERIAL",b."PK_SRCORDER_B",b."PK_SUPPLIER",b."TS",b."VBATCHCODE",b."VBDEF1",b."VBDEF10",b."VBDEF11",b."VBDEF12",b."VBDEF13",b."VBDEF14",b."VBDEF15",b."VBDEF16",b."VBDEF17",b."VBDEF18",b."VBDEF19",b."VBDEF2",b."VBDEF20",b."VBDEF3",b."VBDEF4",b."VBDEF5",b."VBDEF6",b."VBDEF7",b."VBDEF8",b."VBDEF9",b."VBMEMO",b."VCHANGERATE",b."VCONTRACTCODE",b."VECBILLCODE",b."VFIRSTCODE",b."VFIRSTROWNO",b."VFIRSTTRANTYPE",b."VFREE1",b."VFREE10",b."VFREE2",b."VFREE3",b."VFREE4",b."VFREE5",b."VFREE6",b."VFREE7",b."VFREE8",b."VFREE9",b."VPRICEAUDITCODE",b."VQTUNITRATE",b."VSOURCECODE",b."VSOURCEROWNO",b."VSOURCETRANTYPE",b."VVENDDEVADDR",b."VVENDINVENTORYCODE",b."VVENDINVENTORYNAME" from po_order r join po_order_b b on r.pk_order = b.pk_order  where   nvl(r.forderstatus,0) = 3 and nvl(r.dr,0) = 0 and nvl(b.dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_WADATA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_WADATA" ("CYEAR", "CPERIOD", "PK_ORG", "PK_GROUP", "PK_PSNDOC", "PK_WA_CLASS", "PK_WA_DATA", "F_1", "F_2", "F_3", "F_4", "F_5", "F_6", "F_7", "F_8", "F_9", "F_10", "F_11", "F_12", "F_13", "F_14", "F_15", "F_16", "F_17", "F_18", "F_19", "F_20", "F_21", "F_22", "F_23", "F_24", "F_25", "F_26", "F_27", "F_28", "F_29", "F_30", "F_31", "F_32", "F_33", "F_34", "F_35", "F_36", "F_37", "F_38", "F_39", "F_40", "F_41", "F_42", "F_43", "F_44", "F_45", "F_46", "F_47", "F_48", "F_49", "F_50", "F_51", "F_52", "F_53", "F_54", "F_55", "F_56", "F_57", "F_58", "F_59", "F_60", "F_61", "F_62", "F_63", "F_64", "F_65", "F_66", "F_67", "F_68", "F_69", "F_70", "F_71", "F_72", "F_73", "F_74", "F_75", "F_76", "F_77", "F_78", "F_79", "F_80", "F_81", "F_82", "F_83", "F_84", "F_85", "F_86", "F_87", "F_88", "F_89", "F_90", "F_91", "F_92", "F_93", "F_94", "F_95", "F_96", "F_97", "F_98", "F_99", "F_100") AS 
  (select wa.cyear,
       wa.cperiod,
       wa.pk_org,
       wa.pk_group,
       wa.pk_psndoc,
       wa.pk_wa_class,
       pk_wa_data,
       wa.f_1,
       wa.f_2,
       wa.f_3,
       wa.f_4,
       wa.f_5,
       wa.f_6,
       wa.f_7,
       wa.f_8,
       wa.f_9,
       wa.f_10,
       wa.f_11,
       wa.f_12,
       wa.f_13,
       wa.f_14,
       wa.f_15,
       wa.f_16,
       wa.f_17,
       wa.f_18,
       wa.f_19,
       wa.f_20,
       wa.f_21,
       wa.f_22,
       wa.f_23,
       wa.f_24,
       wa.f_25,
       wa.f_26,
       wa.f_27,
       wa.f_28,
       wa.f_29,
       wa.f_30,
       wa.f_31,
       wa.f_32,
       wa.f_33,
       wa.f_34,
       wa.f_35,
       wa.f_36,
       wa.f_37,
       wa.f_38,
       wa.f_39,
       wa.f_40,
       wa.f_41,
       wa.f_42,
       wa.f_43,
       wa.f_44,
       wa.f_45,
       wa.f_46,
       wa.f_47,
       wa.f_48,
       wa.f_49,
       wa.f_50,
       wa.f_51,
       wa.f_52,
       wa.f_53,
       wa.f_54,
       wa.f_55,
       wa.f_56,
       wa.f_57,
       wa.f_58,
       wa.f_59,
       wa.f_60,
       wa.f_61,
       wa.f_62,
       wa.f_63,
       wa.f_64,
       wa.f_65,
       wa.f_66,
       wa.f_67,
       wa.f_68,
       wa.f_69,
       wa.f_70,
       wa.f_71,
       wa.f_72,
       wa.f_73,
       wa.f_74,
       wa.f_75,
       wa.f_76,
       wa.f_77,
       wa.f_78,
       wa.f_79,
       wa.f_80,
       wa.f_81,
       wa.f_82,
       wa.f_83,
       wa.f_84,
       wa.f_85,
       wa.f_86,
       wa.f_87,
       wa.f_88,
       wa.f_89,
       wa.f_90,
       wa.f_91,
       wa.f_92,
       wa.f_93,
       wa.f_94,
       wa.f_95,
       wa.f_96,
       wa.f_97,
       wa.f_98,
       wa.f_99,
       wa.f_100
  from wa_data wa
 where nvl(wa.dr, 0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PACKAGESTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_PACKAGESTYPE" ("PK_DEFDOC", "CODE", "NAME") AS 
  SELECT b.PK_DEFDOC,
    b.code,
    b.NAME
  FROM bd_defdoc b
  LEFT JOIN bd_defdoclist a
  ON a.pk_defdoclist  =b.pk_defdoclist
  WHERE b.enablestate = 2
  AND a.code          ='01';
--------------------------------------------------------
--  DDL for View VIEW_BPM_DELIVERY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_DELIVERY" ("VBILLCODE", "BADVFEEFLAG", "BBARSETTLEFLAG", "BCHECKFLAG", "BLARGESSFLAG", "BOUTENDFLAG", "BQUALITYFLAG", "BTRANSENDFLAG", "BTRIATRADEFLAG", "BUSECHECKFLAG", "CARORGID", "CARORGVID", "CASTUNITID", "CCHANNELTYPEID", "CCHAUFFEURID", "CCURRENCYID", "CCUSTMATERIALID", "CDELIVERYBID", "CDELIVERYID", "CDEPTID", "CDEPTVID", "CEMPLOYEEID", "CFIRSTBID", "CFIRSTID", "CFREECUSTID", "CINSTOCKORGID", "CINSTOCKORGVID", "CINSTORDOCID", "CINVOICECUSTID", "CMATERIALID", "CMATERIALVID", "CMFFILEID", "CORDERCUSTID", "CORIGAREAID", "CORIGCOUNTRYID", "CORIGCURRENCYID", "CPRICEFORMID", "CPRODLINEID", "CPRODUCTORID", "CPROFITCENTERID", "CPROFITCENTERVID", "CPROJECTID", "CQTUNITID", "CQUALITYLEVELID", "CRECECOUNTRYID", "CRECEIVEADDDOCID", "CRECEIVEADDRID", "CRECEIVEAREAID", "CRECEIVECUSTID", "CRECEIVEPERSONID", "CRETREASONID", "CROWNO", "CRPROFITCENTERID", "CRPROFITCENTERVID", "CSALEORGID", "CSALEORGVID", "CSENDADDDOCID", "CSENDADDRID", "CSENDAREAID", "CSENDCOUNTRYID", "CSENDPERSONID", "CSENDSTOCKORGID", "CSENDSTOCKORGVID", "CSENDSTORDOCID", "CSETTLEORGID", "CSETTLEORGVID", "CSPACEID", "CSPROFITCENTERID", "CSPROFITCENTERVID", "CSRCBID", "CSRCID", "CSUPERCARGOID", "CTAXCODEID", "CTAXCOUNTRYID", "CTRANSCUSTID", "CUNITID", "CVEHICLEID", "CVEHICLETYPEID", "CVENDORID", "DBILLDATE", "DR", "DRECEIVEDATE", "DSENDDATE", "FBUYSELLFLAG", "FROWNOTE", "FTAXTYPEFLAG", "NASTNUM", "NCALTAXMNY", "NDISCOUNT", "NDISCOUNTRATE", "NEXCHANGERATE", "NGLOBALEXCHGRATE", "NGLOBALMNY", "NGLOBALTAXMNY", "NGROUPEXCHGRATE", "NGROUPMNY", "NGROUPTAXMNY", "NITEMDISCOUNTRATE", "NMNY", "NNETPRICE", "NNUM", "NORIGDISCOUNT", "NORIGMNY", "NORIGNETPRICE", "NORIGPRICE", "NORIGTAXMNY", "NORIGTAXNETPRICE", "NORIGTAXPRICE", "NPIECE", "NPRICE", "NQTNETPRICE", "NQTORIGNETPRICE", "NQTORIGPRICE", "NQTORIGTAXNETPRC", "NQTORIGTAXPRICE", "NQTPRICE", "NQTTAXNETPRICE", "NQTTAXPRICE", "NQTUNITNUM", "NREQRSNUM", "NTAX", "NTAXMNY", "NTAXNETPRICE", "NTAXPRICE", "NTAXRATE", "NTOTALARNUM", "NTOTALELIGNUM", "NTOTALESTARNUM", "NTOTALNOTOUTNUM", "NTOTALOUTNUM", "NTOTALREPORTNUM", "NTOTALRUSHNUM", "NTOTALTRANSNUM", "NTOTALUNELIGNUM", "NTRANSLOSSNUM", "NVOLUME", "NWEIGHT", "PK_BATCHCODE", "PK_GROUP", "PK_ORG", "TS", "VBATCHCODE", "VBDEF1", "VBDEF10", "VBDEF11", "VBDEF12", "VBDEF13", "VBDEF14", "VBDEF15", "VBDEF16", "VBDEF17", "VBDEF18", "VBDEF19", "VBDEF2", "VBDEF20", "VBDEF3", "VBDEF4", "VBDEF5", "VBDEF6", "VBDEF7", "VBDEF8", "VBDEF9", "VCHANGERATE", "VFIRSTBILLDATE", "VFIRSTCODE", "VFIRSTROWNO", "VFIRSTTRANTYPE", "VFIRSTTYPE", "VFREE1", "VFREE10", "VFREE2", "VFREE3", "VFREE4", "VFREE5", "VFREE6", "VFREE7", "VFREE8", "VFREE9", "VQTUNITRATE", "VRECEIVETEL", "VRETURNMODE", "VSENDTEL", "VSRCCODE", "VSRCROWNO", "VSRCTRANTYPE", "VSRCTYPE") AS 
  (
 select y.vbillcode, b."BADVFEEFLAG",b."BBARSETTLEFLAG",b."BCHECKFLAG",b."BLARGESSFLAG",b."BOUTENDFLAG",b."BQUALITYFLAG",b."BTRANSENDFLAG",b."BTRIATRADEFLAG",b."BUSECHECKFLAG",b."CARORGID",b."CARORGVID",b."CASTUNITID",b."CCHANNELTYPEID",b."CCHAUFFEURID",b."CCURRENCYID",b."CCUSTMATERIALID",b."CDELIVERYBID",b."CDELIVERYID",b."CDEPTID",b."CDEPTVID",b."CEMPLOYEEID",b."CFIRSTBID",b."CFIRSTID",b."CFREECUSTID",b."CINSTOCKORGID",b."CINSTOCKORGVID",b."CINSTORDOCID",b."CINVOICECUSTID",b."CMATERIALID",b."CMATERIALVID",b."CMFFILEID",b."CORDERCUSTID",b."CORIGAREAID",b."CORIGCOUNTRYID",b."CORIGCURRENCYID",b."CPRICEFORMID",b."CPRODLINEID",b."CPRODUCTORID",b."CPROFITCENTERID",b."CPROFITCENTERVID",b."CPROJECTID",b."CQTUNITID",b."CQUALITYLEVELID",b."CRECECOUNTRYID",b."CRECEIVEADDDOCID",b."CRECEIVEADDRID",b."CRECEIVEAREAID",b."CRECEIVECUSTID",b."CRECEIVEPERSONID",b."CRETREASONID",b."CROWNO",b."CRPROFITCENTERID",b."CRPROFITCENTERVID",b."CSALEORGID",b."CSALEORGVID",b."CSENDADDDOCID",b."CSENDADDRID",b."CSENDAREAID",b."CSENDCOUNTRYID",b."CSENDPERSONID",b."CSENDSTOCKORGID",b."CSENDSTOCKORGVID",b."CSENDSTORDOCID",b."CSETTLEORGID",b."CSETTLEORGVID",b."CSPACEID",b."CSPROFITCENTERID",b."CSPROFITCENTERVID",b."CSRCBID",b."CSRCID",b."CSUPERCARGOID",b."CTAXCODEID",b."CTAXCOUNTRYID",b."CTRANSCUSTID",b."CUNITID",b."CVEHICLEID",b."CVEHICLETYPEID",b."CVENDORID",b."DBILLDATE",b."DR",b."DRECEIVEDATE",b."DSENDDATE",b."FBUYSELLFLAG",b."FROWNOTE",b."FTAXTYPEFLAG",b."NASTNUM",b."NCALTAXMNY",b."NDISCOUNT",b."NDISCOUNTRATE",b."NEXCHANGERATE",b."NGLOBALEXCHGRATE",b."NGLOBALMNY",b."NGLOBALTAXMNY",b."NGROUPEXCHGRATE",b."NGROUPMNY",b."NGROUPTAXMNY",b."NITEMDISCOUNTRATE",b."NMNY",b."NNETPRICE",b."NNUM",b."NORIGDISCOUNT",b."NORIGMNY",b."NORIGNETPRICE",b."NORIGPRICE",b."NORIGTAXMNY",b."NORIGTAXNETPRICE",b."NORIGTAXPRICE",b."NPIECE",b."NPRICE",b."NQTNETPRICE",b."NQTORIGNETPRICE",b."NQTORIGPRICE",b."NQTORIGTAXNETPRC",b."NQTORIGTAXPRICE",b."NQTPRICE",b."NQTTAXNETPRICE",b."NQTTAXPRICE",b."NQTUNITNUM",b."NREQRSNUM",b."NTAX",b."NTAXMNY",b."NTAXNETPRICE",b."NTAXPRICE",b."NTAXRATE",b."NTOTALARNUM",b."NTOTALELIGNUM",b."NTOTALESTARNUM",b."NTOTALNOTOUTNUM",b."NTOTALOUTNUM",b."NTOTALREPORTNUM",b."NTOTALRUSHNUM",b."NTOTALTRANSNUM",b."NTOTALUNELIGNUM",b."NTRANSLOSSNUM",b."NVOLUME",b."NWEIGHT",b."PK_BATCHCODE",b."PK_GROUP",b."PK_ORG",b."TS",b."VBATCHCODE",b."VBDEF1",b."VBDEF10",b."VBDEF11",b."VBDEF12",b."VBDEF13",b."VBDEF14",b."VBDEF15",b."VBDEF16",b."VBDEF17",b."VBDEF18",b."VBDEF19",b."VBDEF2",b."VBDEF20",b."VBDEF3",b."VBDEF4",b."VBDEF5",b."VBDEF6",b."VBDEF7",b."VBDEF8",b."VBDEF9",b."VCHANGERATE",b."VFIRSTBILLDATE",b."VFIRSTCODE",b."VFIRSTROWNO",b."VFIRSTTRANTYPE",b."VFIRSTTYPE",b."VFREE1",b."VFREE10",b."VFREE2",b."VFREE3",b."VFREE4",b."VFREE5",b."VFREE6",b."VFREE7",b."VFREE8",b."VFREE9",b."VQTUNITRATE",b."VRECEIVETEL",b."VRETURNMODE",b."VSENDTEL",b."VSRCCODE",b."VSRCROWNO",b."VSRCTRANTYPE",b."VSRCTYPE"
  from so_delivery y
  join so_delivery_b b
    on y.cdeliveryid = b.cdeliveryid
 where nvl(y.dr, 0) = 0
   and nvl(b.dr, 0) = 0
   and nvl(y.fstatusflag, 0) = 2);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PRAYBILL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_PRAYBILL" ("VBILLCODE", "BCANPURCHASEORGEDIT", "BISARRANGE", "BISGENSAORDER", "BPUBLISHTOEC", "BROWCLOSE", "CASSCUSTID", "CASTUNITID", "CFFILEID", "CFIRSTBID", "CFIRSTID", "CFIRSTTYPECODE", "CORDERTRANTYPECODE", "CPRODUCTORID", "CPROJECTID", "CPROJECTTASKID", "CROWNO", "CSOURCEBID", "CSOURCEID", "CSOURCETYPECODE", "CUNITID", "DBILLDATE", "DR", "DREQDATE", "DSUGGESTDATE", "NACCUMULATENUM", "NASTNUM", "NGENCT", "NNUM", "NPRICEAUDITBILL", "NQUOTEBILL", "NTAXMNY", "NTAXPRICE", "PK_BATCHCODE", "PK_CUSTOMER", "PK_EMPLOYEE", "PK_GROUP", "PK_MATERIAL", "PK_ORG", "PK_ORG_V", "PK_PRAYBILL", "PK_PRAYBILL_B", "PK_PRODUCT", "PK_PRODUCT_V", "PK_PURCHASEORG", "PK_PURCHASEORG_V", "PK_REQDEPT", "PK_REQDEPT_V", "PK_REQSTOORG", "PK_REQSTOORG_V", "PK_REQSTOR", "PK_SRCMATERIAL", "PK_SUGGESTSUPPLIER", "TS", "VBATCHCODE", "VBDEF1", "VBDEF10", "VBDEF11", "VBDEF12", "VBDEF13", "VBDEF14", "VBDEF15", "VBDEF16", "VBDEF17", "VBDEF18", "VBDEF19", "VBDEF2", "VBDEF20", "VBDEF3", "VBDEF4", "VBDEF5", "VBDEF6", "VBDEF7", "VBDEF8", "VBDEF9", "VBMEMO", "VCHANGERATE", "VFIRSTCODE", "VFIRSTROWNO", "VFIRSTTRANTYPE", "VFREE1", "VFREE10", "VFREE2", "VFREE3", "VFREE4", "VFREE5", "VFREE6", "VFREE7", "VFREE8", "VFREE9", "VSOURCECODE", "VSOURCEROWNO", "VSRCTRANTYPECODE") AS 
  (select r.vbillcode  ,b."BCANPURCHASEORGEDIT",b."BISARRANGE",b."BISGENSAORDER",b."BPUBLISHTOEC",b."BROWCLOSE",b."CASSCUSTID",b."CASTUNITID",b."CFFILEID",b."CFIRSTBID",b."CFIRSTID",b."CFIRSTTYPECODE",b."CORDERTRANTYPECODE",b."CPRODUCTORID",b."CPROJECTID",b."CPROJECTTASKID",b."CROWNO",b."CSOURCEBID",b."CSOURCEID",b."CSOURCETYPECODE",b."CUNITID",b."DBILLDATE",b."DR",b."DREQDATE",b."DSUGGESTDATE",b."NACCUMULATENUM",b."NASTNUM",b."NGENCT",b."NNUM",b."NPRICEAUDITBILL",b."NQUOTEBILL",b."NTAXMNY",b."NTAXPRICE",b."PK_BATCHCODE",b."PK_CUSTOMER",b."PK_EMPLOYEE",b."PK_GROUP",b."PK_MATERIAL",b."PK_ORG",b."PK_ORG_V",b."PK_PRAYBILL",b."PK_PRAYBILL_B",b."PK_PRODUCT",b."PK_PRODUCT_V",b."PK_PURCHASEORG",b."PK_PURCHASEORG_V",b."PK_REQDEPT",b."PK_REQDEPT_V",b."PK_REQSTOORG",b."PK_REQSTOORG_V",b."PK_REQSTOR",b."PK_SRCMATERIAL",b."PK_SUGGESTSUPPLIER",b."TS",b."VBATCHCODE",b."VBDEF1",b."VBDEF10",b."VBDEF11",b."VBDEF12",b."VBDEF13",b."VBDEF14",b."VBDEF15",b."VBDEF16",b."VBDEF17",b."VBDEF18",b."VBDEF19",b."VBDEF2",b."VBDEF20",b."VBDEF3",b."VBDEF4",b."VBDEF5",b."VBDEF6",b."VBDEF7",b."VBDEF8",b."VBDEF9",b."VBMEMO",b."VCHANGERATE",b."VFIRSTCODE",b."VFIRSTROWNO",b."VFIRSTTRANTYPE",b."VFREE1",b."VFREE10",b."VFREE2",b."VFREE3",b."VFREE4",b."VFREE5",b."VFREE6",b."VFREE7",b."VFREE8",b."VFREE9",b."VSOURCECODE",b."VSOURCEROWNO",b."VSRCTRANTYPECODE" from  po_praybill r join po_praybill_b b  on r.pk_praybill =b.pk_praybill  where   nvl(r.fbillstatus  ,0) = 3 and nvl(r.dr,0) = 0 and nvl(b.dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_MATERIAL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_MATERIAL" ("INVPK", "INVCODE", "INVNAME", "MEASPK", "MEASCODE", "MEASNAME", "SFACPK", "SFACCODE", "SFACNAME") AS 
  (

SELECT 
--物料信息
inv.pk_material  as invpk,
inv.code as invcode,
inv.name  as invname,
--主单位
meas.pk_measdoc as measpk,
meas.code as meascode, 
meas.name as measname,
--工厂信息
sfac.pk_factory  as sfacpk ,
sfac.code as sfaccode ,
sfac.name as sfacname
FROM 
bd_material  inv
inner join bd_materialprod invprod
on inv.pk_material =invprod.pk_material
inner join org_factory sfac
on invprod.pk_org = sfac.pk_factory
left  join bd_measdoc meas
on inv.pk_measdoc  = meas.pk_measdoc
where nvl(inv.dr,0)=0 and nvl(invprod.dr,0)=0


);
--------------------------------------------------------
--  DDL for View VIEW_BPM_CUST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_CUST" ("CUSTPK", "CUSTCODE", "CUSTNAME", "CUSTSHORTNAME", "PSNPK", "PSNCODE", "PSNNAME", "DEPPK", "DEPCODE", "DEPNAME", "SORGPK", "SORGCODE", "SORGNAME") AS 
  (
SELECT 
--客户信息
cust.pk_customer as custpk,
cust.code as custcode,
cust.name  as custname,
cust.shortname as custshortname,
--专管业务员信息
bcs.respperson as psnpk,
psn.code as psncode, 
psn.name as psnname,
--专管部门信息
bcs.respdept as deppk,
dept.code as depcode, 
dept.name as depname,
--销售组织信息
sorg.pk_salesorg as sorgpk ,
sorg.code as sorgcode ,
sorg.name as sorgname
FROM bd_customer  cust
left join bd_custsale bcs on cust.pk_customer = bcs.pk_customer
left join org_salesorg sorg on bcs.pk_org = sorg.pk_salesorg
left  join bd_psndoc psn on bcs.respperson = psn.pk_psndoc
left join org_dept dept on bcs.respdept = Dept.Pk_Dept
where nvl(cust.dr,0)=0 and nvl(bcs.dr,0)=0
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PURBILLTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_PURBILLTYPE" ("ACCOUNTCLASS", "BILLCODERULE", "BILLSTYLE", "BILLTYPENAME", "BILLTYPENAME2", "BILLTYPENAME3", "BILLTYPENAME4", "BILLTYPENAME5", "BILLTYPENAME6", "CANEXTENDTRANSACTION", "CHECKCLASSNAME", "CLASSNAME", "COMP", "COMPONENT", "DATAFINDERCLZ", "DEF1", "DEF2", "DEF3", "DR", "EMENDENUMCLASS", "FORWARDBILLTYPE", "ISACCOUNT", "ISAPPROVEBILL", "ISBIZFLOWBILL", "ISEDITABLEPROPERTY", "ISENABLEBUTTON", "ISENABLETRANSTYPEBCR", "ISLOCK", "ISROOT", "ISTRANSACTION", "NCBRCODE", "NODECODE", "PARENTBILLTYPE", "PK_BILLTYPECODE", "PK_BILLTYPEID", "PK_GROUP", "PK_ORG", "REFERCLASSNAME", "SYSTEMCODE", "TRANSTYPE_CLASS", "TS", "WEBNODECODE", "WHERESTRING") AS 
  (select "ACCOUNTCLASS","BILLCODERULE","BILLSTYLE","BILLTYPENAME","BILLTYPENAME2","BILLTYPENAME3","BILLTYPENAME4","BILLTYPENAME5","BILLTYPENAME6","CANEXTENDTRANSACTION","CHECKCLASSNAME","CLASSNAME","COMP","COMPONENT","DATAFINDERCLZ","DEF1","DEF2","DEF3","DR","EMENDENUMCLASS","FORWARDBILLTYPE","ISACCOUNT","ISAPPROVEBILL","ISBIZFLOWBILL","ISEDITABLEPROPERTY","ISENABLEBUTTON","ISENABLETRANSTYPEBCR","ISLOCK","ISROOT","ISTRANSACTION","NCBRCODE","NODECODE","PARENTBILLTYPE","PK_BILLTYPECODE","PK_BILLTYPEID","PK_GROUP","PK_ORG","REFERCLASSNAME","SYSTEMCODE","TRANSTYPE_CLASS","TS","WEBNODECODE","WHERESTRING" from bd_billtype  where systemcode in('PO','PURP','MPP') and nvl(dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_JZQJ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_JZQJ" ("PK_ORG", "ORGNAME", "PK_PERIODSTATE", "PK_PERIODSCHEME", "ACCOUNTMARK", "PK_WA_CLASS", "WACLASSCODE", "WACLASSCNAME", "PK_WA_PERIOD", "CYEAR", "CPERIOD", "XZQJ") AS 
  select a.pk_org , org.name orgname,a.pk_periodstate ,b.pk_periodscheme,a.accountmark,
a.pk_wa_class ,c.code waclasscode,c.name waclasscname,
a.pk_wa_period,b.cyear,b.cperiod, b.cyear||b.cperiod AS xzqj
from wa_periodstate a
inner join wa_period b on a.pk_wa_period=b.pk_wa_period
left join wa_waclass c on a.pk_wa_class=c.pk_wa_class
left join org_orgs org on org.pk_org=a.pk_org 
where a.enableflag = 'Y';
--------------------------------------------------------
--  DDL for View VIEW_BPM_SALETYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_SALETYPE" ("PK_BILLTYPECODE", "BILLTYPENAME", "PK_BILLTYPEID") AS 
  SELECT pk_billtypecode,
    billtypename,
    pk_billtypeid
  FROM bd_billtype
  WHERE ( istransaction   = 'Y'
  AND pk_group            = '0001A51000000000078A'
  AND NVL ( islock, 'N' ) = 'N'
  AND ( ( parentbilltype  = '30'
  AND 1                   = 1
  AND pk_group            = '0001A51000000000078A' ) ) )
  ORDER BY pk_billtypecode;
--------------------------------------------------------
--  DDL for View VIEW_BPM_ORDERTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_ORDERTYPE" ("CODE", "NAME", "MEMO", "MNECODE", "PK_DEFDOC") AS 
  SELECT code,
    name,
    memo,
    mnecode,
    pk_defdoc
  FROM bd_defdoc
  WHERE 11          = 11
  AND ( enablestate = 2 )
  AND ( ( pk_group  = '0001A51000000000078A' )
  AND pk_defdoclist = '1001A4100000000019GI' )
  Order By Code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_JOBLEVEL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_JOBLEVEL" ("CODE", "NAME", "SYSCODE", "SYSNAME", "PK_JOBLEVEL", "PK_JOBLEVELSYS") AS 
  SELECT om_joblevel.code,
  om_joblevel.name,
  om_joblevelsys.code syscode,
  om_joblevelsys.name sysname,
  pk_joblevel,
  om_joblevelsys.pk_joblevelsys
FROM om_joblevel
INNER JOIN om_joblevelsys
ON om_joblevel.pk_joblevelsys = om_joblevelsys.pk_joblevelsys
where om_joblevelsys.code='01'
ORDER BY pk_joblevel;
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYECHONGJIE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_NC_ZUOYECHONGJIE" ("PK_GROUP", "PK_ORG", "PERIODID", "CDPTID", "PK_LARGEITEM", "VACTIVITYNAME", "NNUM") AS 
  (
select 
mmpac_huanbao_h. pk_group,  mmpac_huanbao_h. pk_org ,
--会计期间
mmpac_huanbao_h.periodid as periodid,
 ---受益部门
mmpac_huanbao_b.cdeptid as  cdptid,
--劳务项目
mmpac_huanbao_b.pk_largeitem as pk_largeitem,
bd_activity.vactivityname  as vactivityname ,
--注意：作业档案有一项为 折算废水量，折算废水量取数的时候只合计夜班的数量！
case 
when bd_activity.vactivityname='折算废水量' then ntotaldaynum
when bd_activity.vactivityname='新鲜水量' then ntotalnum*0.794
else ntotalnum  end 
as nnum  from  mmpac_huanbao_b 
inner join mmpac_huanbao_h on mmpac_huanbao_b.pk_head = mmpac_huanbao_h.pk_head
inner join bd_activity on mmpac_huanbao_b.pk_largeitem= bd_activity.cactivityid 
where nvl( mmpac_huanbao_h. dr,0)=0
 and nvl( mmpac_huanbao_b. dr,0)=0 
 and bd_activity.vactivityname in ('新鲜水量','废水量','折算废水量')
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_XZXM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_XZXM" ("CODE", "NAME", "PK_WA_ITEM") AS 
  select code, name, pk_wa_item from wa_item 
where 11 = 11 and ( ( 1 = 1 ) and ( ( pk_org = 'GLOBLE00000000000000' or pk_org = '0001A51000000000078A' or pk_org = '0001A5100000000023GP' ) ) and ( ( isinhi = 'Y' or isinhi = 'y' ) and ( nvl ( iprivil, 0 ) = 0 ) ) ) order by code;
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEHUANBAO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_NC_ZUOYEHUANBAO" ("PK_GROUP", "PK_ORG", "PERIODID", "CDPTID", "PK_LARGEITEM", "VACTIVITYNAME", "NNUM") AS 
  (
---作业环保统计单
select mmpac_huanbao_h. pk_group,  mmpac_huanbao_h. pk_org ,
--会计期间
mmpac_huanbao_h.periodid as periodid,
 ---受益部门
mmpac_huanbao_b.cdeptid as  cdptid,
--劳务项目
mmpac_huanbao_b.pk_largeitem as pk_largeitem,
bd_activity.vactivityname  as vactivityname ,
--注意：作业档案有一项为 折算废水量，折算废水量取数的时候只合计夜班的数量！
case when 
bd_activity.vactivityname='折算废水量' then ntotaldaynum
else ntotalnum  end 
as nnum  from  mmpac_huanbao_b 
inner join mmpac_huanbao_h on mmpac_huanbao_b.pk_head = mmpac_huanbao_h.pk_head
inner join bd_activity on mmpac_huanbao_b.pk_largeitem= bd_activity.cactivityid 
where nvl( mmpac_huanbao_h. dr,0)=0
 and nvl( mmpac_huanbao_b. dr,0)=0 
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_RESPPERSON
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_RESPPERSON" ("PSNPK", "PSNCODE", "PSNNAME", "DEPPK", "DEPCODE", "DEPNAME") AS 
  select distinct 
--专管业务员信息
psnpk,
psncode, 
psnname,
--专管部门信息
DEPPK,
depcode, 
depname from VIEW_BPM_CUST;
--------------------------------------------------------
--  DDL for View VIEW_BPM_GWGZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_GWGZ" ("PK_WA_CRT", "PK_WA_GRD", "PK_WA_PRMLV", "MAX_VALUE", "MIN_VALUE", "PK_WA_SECLV", "CRITERIONVALUE", "GRDNAME", "PRMLVNAME", "PK_WA_GRADEVER", "SECLVNAME") AS 
  (
SELECT wc.pk_wa_crt,
  wc.pk_wa_grd,
  wc.pk_wa_prmlv,
  wc.max_value,
  wc.min_value,
  wc.pk_wa_seclv,
  wc.criterionvalue,
  wg.name grdName,
  wp.levelname prmlvName,
  ver.pk_wa_gradever,
  ws.levelname seclvName
FROM wa_seclv ws,
  wa_grade wg,
  wa_prmlv wp,  
  wa_criterion wc,
  wa_grade_ver ver
WHERE   1 = 1
AND wc.pk_wa_grd       = wg.pk_wa_grd
AND wc.pk_wa_prmlv     = wp.pk_wa_prmlv
AND wc.pk_wa_gradever  = ver.pk_wa_gradever
AND wc.pk_wa_seclv     = ws.pk_wa_seclv
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_HTGL_CHBM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_HTGL_CHBM" ("ROWNO", "DATASOURCE", "VBILLCODE", "PK_MATERIAL", "INVCODE", "INVNAME", "NNUM", "NPRICE", "PK_MEASDOC", "UTCODE", "UTNAME", "BZGGNAME", "PK_BZGG") AS 
  select ROWNUM as rowNo,t."DATASOURCE",t."VBILLCODE",t."PK_MATERIAL",t."INVCODE",t."INVNAME",t."NNUM",t."NPRICE",t."PK_MEASDOC",t."UTCODE",t."UTNAME",t."BZGGNAME",t."PK_BZGG" from (
select distinct '请购单' as DataSource,A.VBILLCODE,c.pk_material,c.code as Invcode,c.name as Invname,b.nnum,0 as nprice,d.pk_measdoc,d.code as UTcode,d.name as UTname,
e.NAME as BZGGname,e.PK_DEFDOC as PK_BZGG
from po_praybill_b b left join po_praybill a on a.pk_praybill =b.pk_praybill  
left join bd_material c on b.pk_material=c.pk_material 
left join bd_measdoc d on b.cunitid =d.pk_measdoc
left join VIEW_BPM_PACKAGESTYPE e on b.vfree1=e.PK_DEFDOC
) t;
--------------------------------------------------------
--  DDL for View VIEW_JL_SUPPLIER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_JL_SUPPLIER" ("客商编码", "客商名称", "客商简称", "助记码", "是否国外", "CVENDEFINE1", "CVENDEFINE2") AS 
  (
  SELECT code AS 客商编码,
    name       AS 客商名称,
    shortname  AS 客商简称,
    mnecode    AS 助记码,
    CASE pk_country
      WHEN '0001Z010000000079UJJ'
      THEN '是'
      ELSE '否'
    END AS 是否国外,
    '' cVenDefine1,
    '' cVenDefine2
  FROM bd_supplier
  where nvl(dr,0)=0 
  --是否计量
  and nvl(def1,'N')='Y'
  );
--------------------------------------------------------
--  DDL for View VIEW_JL_ARR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_JL_ARR" ("到货单子表ID", "到货单主表ID", "仓库编码", "仓库名称", "存货编码", "存货名称", "规格型号", "计量单位", "应到数量", "合格入库数量", "批号", "包装规格", "到货单号", "单据日期", "供应商编码", "供应商名称", "原币无税单价", "原币含税单价", "采购订单号", "采购订单子表ID", "采购类型", "税率", "联系电话", "部门编码", "业务员编码", "业务员名称", "备注") AS 
  ( SELECT arrb. pk_arriveorder_b AS 到货单子表ID,
      arrb. pk_arriveorder         AS 到货单主表ID,
      warehouse.code               AS 仓库编码,
      warehouse.name               AS 仓库名称,
      inventory.code               AS 存货编码,
      inventory.name               AS 存货名称,
      inventory.materialspec
      || inventory.materialtype AS 规格型号,
      ComputationUnit.name      AS 计量单位,
      arrb.nplannum             AS 应到数量,
      arrb.naccumstorenum       AS 合格入库数量,
      arrb.vbatchcode           AS 批号,
      bgzg.name                 AS 包装规格,
      arrh.vbillcode            AS 到货单号,
      arrh.dbilldate            AS 单据日期,
      vendor.code               AS 供应商编码,
      vendor.name               AS 供应商名称,
      arrb.norigprice           AS 原币无税单价,
      arrb.norigtaxprice        AS 原币含税单价,
      arrb.vsourcecode          AS 采购订单号,
      arrb.csourcebid           AS 采购订单子表ID,
      arrh.vtrantypecode        AS 采购类型,
      arrb.ntaxrate             AS 税率,
      --        po_arriveorder.cDefine10                         AS 联系电话,
      '189xxxxx' AS 联系电话,
      dept.code  AS 部门编码,
      psn.code   AS 业务员编码,
      psn.name   AS 业务员名称,
      arrh.vmemo AS 备注
    FROM po_arriveorder arrh
    LEFT OUTER JOIN po_arriveorder_b arrb
    ON arrb.pk_arriveorder = arrh.pk_arriveorder
    LEFT OUTER JOIN bd_stordoc warehouse
      --//  pk_receivestore  收货仓库   or  pk_reqstore  需求仓库
    ON warehouse. pk_stordoc = arrb.pk_receivestore
    LEFT OUTER JOIN bd_material inventory
    ON inventory. pk_material = arrb.pk_material
    LEFT OUTER JOIN bd_measdoc ComputationUnit
    ON inventory. pk_measdoc = ComputationUnit.pk_measdoc
    LEFT OUTER JOIN bd_supplier vendor
    ON vendor. pk_supplier = arrh. pk_supplier
      --包装规格
    LEFT JOIN bd_defdoc bgzg
    ON arrb.vfree1= bgzg.pk_defdoc
      --采购部门
    LEFT JOIN org_dept dept
    ON arrh. pk_dept = dept.pk_dept
      --业务员
    LEFT JOIN bd_psndoc psn
    ON arrh. pk_pupsndoc             = psn. pk_psndoc
    WHERE 
    arrh.fbillstatus             =3
    AND arrb.nnum -nvl(arrb.naccumstorenum , 0) >0
      -- 待定   warehouse.code NOT IN ('603', '604')
      --榜单号
    AND arrb.vbdef20='~'
      --已经报检不需要
--    AND arrb. pk_arriveorder_b NOT IN
--      (SELECT csourcebid
--      FROM qc_applybill_s
--      WHERE NVL(dr,0)     =0
--      AND csourcetypecode ='23'
--      )
--      
AND ( NVL ( arrb.naccumchecknum, 0 ) = 0 )


      );
--------------------------------------------------------
--  DDL for View VIEW_BPM_JOBRANK
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_JOBRANK" ("JOBRANKCODE", "JOBRANKNAME", "PK_JOBRANK", "JOBRANKORDER") AS 
  SELECT jobrankcode,
  jobrankname,
  pk_jobrank,
  jobrankorder
FROM om_jobrank;
--------------------------------------------------------
--  DDL for View VIEW_BPM_LZYY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_LZYY" ("CODE", "NAME", "MNECODE", "PK_DEFDOC", "PID") AS 
  SELECT code,
  name,
  mnecode,
  pk_defdoc,
  pid
FROM bd_defdoc
WHERE 11          = 11
AND ( enablestate = 2 )
AND ( ( 1         = 1 )
AND pk_defdoclist = '1001Z71000000000GPD1' )
ORDER BY code;
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEAUTOCOST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_NC_ZUOYEAUTOCOST" ("PK_GROUP", "PK_ORG", "PK_COSTCENTER", "CCNAME", "CPERIOD", "CCOSTOBJECTID", "PK_MEASDOC", "NNUM") AS 
  (
select  pk_group,pk_org,pk_costcenter,  ccname ,cperiod,
'1001A41000000000CU6E'  as ccostobjectid,--成本对象默认
'0001Z0100000000000XT' as pk_measdoc,--计量单位默认
100 as nnum --数量默认
from(
--查询每个会计期间
select  cs.pk_group,cs.pk_org,cs.pk_costcenter,  cs.ccname ,zuoye.cperiod,
case 
when cctype=3 and ccname in ('研发成本中心','销售成本中心','采购成本中心') then '0'
when cctype=2 and wgnnum >0 then 'N'
when cctype=2 and  wgnnum =0  and (xhnnum+zynnum)>0 then '1'
else 'N'
end
as bcost
from resa_costcenter cs
left join view_nc_zuoyeautocost01 zuoye
on cs.pk_group||cs.pk_org||cs.pk_costcenter = zuoye.pk_group||zuoye.pk_org||zuoye.ccostcenterid
where nvl(dr,0)=0 

and (
(cctype =3 and ccname in ('研发成本中心','销售成本中心','采购成本中心')) or
cctype=2
)
)temp
where bcost in('0','1')
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_CASHFLOW
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_CASHFLOW" ("ORG_PK", "ORG_CODE", "ORG_NAME", "CASH_PK", "CASH_CODE", "CASH_NAME") AS 
  (SELECT
    --组织信息
    org.pk_org AS org_pk,
    org.code   AS org_code,
    org.name   AS org_name,
    --现金流项目表
    cash.pk_cashflow as cash_pk,
    cash.code as cash_code,
    cash.name as cash_name
    
  FROM bd_cashflow cash
  INNER JOIN org_orgs org
  ON cash. pk_org     = org.pk_org
  WHERE NVL(cash.dr,0)=0
  --1=未启用，2=已启用，3=已停用， 
  AND cash.enablestate=2
  );
--------------------------------------------------------
--  DDL for View VIEW_BPM_BHGJYXX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_BHGJYXX" ("PK_REJECTBILL", "PK_CHECKBILL", "PK_CHECKBILL_B", "VBSAMPLECODE", "VCHECKITEMNAME", "NTOTALSAMPLE", "NACCEPTNUM", "NREJECTNUM", "NUNQUALIFIEDNUM", "FACCETPTYPE") AS 
  SELECT 
h.pk_rejectbill,
chkb.pk_checkbill,
    chkb.pk_checkbill_b,
    vbsamplecode,
    chkitem.vcheckitemname,
    ntotalsample,
    nacceptnum ,
    nrejectnum,
    nunqualifiednum,
    faccetptype
  FROM qc_checkbill_b chkb
  LEFT JOIN qc_checkbill chk on chk.pk_checkbill =chkb.pk_checkbill
  LEFT JOIN qc_applybill_s apl on apl.pk_applybill = chk.cfirstid
  LEFT JOIN  qc_rejectbill h on h.pk_applybill  = apl.pk_applybill
  LEFT JOIN  qc_rejectbill_b b ON b. pk_rejectbill = h. pk_rejectbill
  LEFT JOIN qc_checkitem chkitem
  ON chkb. pk_checkitem =chkitem.pk_checkitem
  where nvl(chkb.dr,0)=0 and chkb.buseless='N';
--------------------------------------------------------
--  DDL for View VIEW_BPM_PROJECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_PROJECT" ("PH_PK", "PH_CODE", "PH_NAME", "ORG_PK", "ORG_CODE", "ORG_NAME") AS 
  (
select 
--ph.pk_project项目主键
ph.pk_project as  ph_pk,
--ph.project_code 项目编码
ph.project_code as ph_code,
--ph.project_name 项目名称
ph.project_name  as ph_name,
--分配到组织
org.pk_org as org_pk,
org.code as org_code,
org.name as org_name
from bd_project ph
inner join bd_project_b pb
on ph.  pk_project = pb.  pk_project 
inner join org_orgs org on   pb.   pk_parti_org  = org.pk_org
where nvl(ph.dr,0)=0 and nvl(pb.dr,0)=0 
--  enablestate  启用状态  enablestate int  启用状态   1=未启用，2=已启用，3=已停用， 
and ph.enablestate = 2

);
--------------------------------------------------------
--  DDL for View VIEW_BPM_BOM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_BOM" ("PLANORGPK", "PLANORG_CODE", "PLANORG_NAME", "BOM_PK", "PK_MATERIAL", "INV_CODE", "INV_NAME", "INV_SPEC", "INV_TYPE", "BOM_TYPE", "BOM_TYPENAME", "BOM_HVERSION", "MEASPK", "MEASCODE", "MEASNAME", "BOM_FNUM") AS 
  (SELECT
    --生产组织，主键，编码，名称
    bom. pk_org AS planorgpk,
    org.code    AS planorg_code,
    org.name    AS planorg_name,
    --物料信息
    --cbomid bom主键
    bom.cbomid AS bom_pk,
    --物料主键
    inv.pk_material,
    --inv.code物料编码
    inv.code AS inv_code,
    --inv.name物料名称
    inv.name AS inv_name,
    --materialspec  规格
    inv.materialspec AS inv_spec ,
    --materialtype 型号
    inv.materialtype AS inv_type,
    -- BOM类型  1=生产BOM，2=包装BOM，3=配置BOM
    bom.fbomtype AS bom_type,
    CASE bom.fbomtype
      WHEN 1
      THEN '生产BOM'
      WHEN 2
      THEN '包装BOM'
      WHEN 3
      THEN '配置BOM'
    END AS bom_typename,
    --hversion 版本号
    bom.hversion AS bom_hversion,
    --BOM单位
    meas.pk_measdoc    AS measpk,
    meas.code          AS meascode,
    meas.name          AS measname,
    bom.hnassparentnum AS bom_fnum
  FROM bd_bom bom
  LEFT JOIN org_orgs org ON bom.pk_org = org.pk_org
  LEFT JOIN bd_material inv ON bom.hcmaterialid = inv.pk_material
  LEFT JOIN bd_measdoc meas ON bom.hcmeasureid = meas.pk_measdoc
  WHERE NVL(bom.dr,0)=0
  );
--------------------------------------------------------
--  DDL for View VIEW_BPM_card
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_card" ("资产编码", "资产名称", "资产类别编码", "资产类别名称", "规格", "型号", "设备主键", "存放地点", "生产厂家", "开始使用日期", "原币原值", "净值", "使用月限", "使用部门", "部门编码") AS 
  SELECT a.asset_code 资产编码,
    a.asset_name 资产名称 ,
    c.cate_code 资产类别编码,
    c.cate_name 资产类别名称,
    a.spec 规格,
    a.card_model 型号,
    a.pk_equip 设备主键,
    a.position 存放地点,
    a.producer 生产厂家,
    SUBSTR(a.begin_date,0,10) 开始使用日期,
    b.originvalue 原币原值,
    (b.localoriginvalue-b.accudep_cal) 净值,
    b.naturemonth 使用月限,
    d.name 使用部门,
    d.code 部门编码
  FROM fa_card a
  INNER JOIN fa_cardhistory b
  ON a.pk_card = b.pk_card
  INNER JOIN fa_cardsub
  ON a.pk_card = fa_cardsub.pk_card
  LEFT OUTER JOIN fa_category c
  ON b.pk_category=c.pk_category
  LEFT OUTER JOIN org_dept d
  ON B.Pk_Mandept          =D.Pk_Dept
  WHERE ( b.laststate_flag = 'Y'
  OR ( b.asset_state      IN ( 'reduce', 'reduce_split', 'reduce_combin', 'redeploy_way', 'redeploy_out' )
  And B.Laststate_Flag     = 'N' ) )
  And Nvl(A.Dr,0)          = 0
 AND Nvl(B.Dr,0）          = 0
 AND Nvl(Fa_Cardsub.Dr,0) = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_REJECTBILL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_REJECTBILL" ("PK_ORG", "CAPPLYTIME", "VBILLCODE", "VAPPLYBILLCODE", "VREPORTBILLCODE", "PK_DEPT", "DEPT_CODE", "DEPT_NAM", "INV_CODE", "INV_NAME", "VBATCHCODE", "NAPPLYNUM", "NNUM", "PK_REJECTBILL_B", "PK_REJECTBILL", "VCHECKITEMNAME", "BACCORDED", "VSTDVALUE1", "VCHKVALUE") AS 
  (
  SELECT

----  不合格品处理单信息+表头+表体一行明细
  --库存组织
  h.pk_org,
  --报检时间
  h.capplytime,
  --不合格品处理单号
  h.vbillcode,
  -- 报检单号
  h.vapplybillcode,
  --质检报告号
  h.vreportbillcode ,
 
  --报检部门主键
  od.pk_dept ,
  od.code as dept_code,
  od.name as dept_nam,
  --物料
  inv.code as inv_code,
  inv.name as inv_name,
  --物料批次
  h. vbatchcode,
   --报检主数量
  h.napplynum ,
  --不合格数量
  b.nnum,
  -- 不合格品处理单明细  
  b.pk_rejectbill_b,
   b.pk_rejectbill,
-------------检验单的信息-------
--检验项目
chkitem.vcheckitemname ,
--    达标 
chkb.baccorded,
--   标准值1  
chkb.vstdvalue1,
--   实际检验值  
chkb.vchkvalue

FROM qc_rejectbill_b b
LEFT JOIN qc_rejectbill h
ON b. pk_rejectbill = h. pk_rejectbill
LEFT JOIN org_dept od
ON h.pk_applydept = od.pk_dept
LEFT JOIN bd_material inv
ON h. pk_material = inv.pk_material
--报检单 
--left join qc_applybill_s apl on h.  pk_applybill  = apl.pk_applybill
--检验单
left join qc_checkbill chk on h.pk_applybill= chk.csourceid 
left join qc_checkbill_b chkb on  chk.  pk_checkbill  = chkb.pk_checkbill
--检验项目
left join  qc_checkitem chkitem on chkb. pk_checkitem  =chkitem.pk_checkitem 

where nvl(h.dr,0)=0 and nvl(b.dr,0)=0
and h.fbillstatus=0
  );
--------------------------------------------------------
--  DDL for View VIEW_BPM_REGISTE_BPM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_REGISTE_BPM" ("单据状态", "出票日期", "到期日期", "收票日期", "户名", "名称", "账号", "PK_REGISTER", "票据编号") AS 
  SELECT f.baseinfostatus 单据状态,
    f.invoicedate 出票日期 ,
    f.enddate 到期日期,
    f.gatherdate 收票日期,
    a.ACCNAME 户名,
    a.name 名称,
    a.accnum 账号,
    f.pk_register,
    f.fbmbillno 票据编号
  FROM fbm_register f
  LEFT OUTER JOIN bd_bankaccsub a
  ON f.pk_payacc        =a. pk_bankaccsub
  WHERE NVL(f.dr,0)     =0
  AND f.BASEINFOSTATUS IN('register','has_invoice');
--------------------------------------------------------
--  DDL for View VIEW_BPM_SECLV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VIEW_BPM_SECLV" ("PK_WA_CRT", "PK_WA_GRD", "PK_WA_PRMLV", "MAX_VALUE", "MIN_VALUE", "PK_WA_SECLV", "CRITERIONVALUE", "GRDNAME", "PRMLVNAME", "PK_WA_GRADEVER", "SECLVNAME") AS 
  (
SELECT wc.pk_wa_crt,
  wc.pk_wa_grd,
  wc.pk_wa_prmlv,
  wc.max_value,
  wc.min_value,
  wc.pk_wa_seclv,
  wc.criterionvalue,
  wg.name grdName,
  wp.levelname prmlvName,
  ver.pk_wa_gradever,
  ws.levelname seclvName
FROM wa_seclv ws,
  wa_grade wg,
  wa_prmlv wp,
  wa_criterion wc,
  wa_grade_ver ver
WHERE 1                = 1
AND wc.pk_wa_grd       = wg.pk_wa_grd
AND wc.pk_wa_prmlv     = wp.pk_wa_prmlv
AND wc.pk_wa_gradever  = ver.pk_wa_gradever
AND wc.pk_wa_seclv     = ws.pk_wa_seclv

);
