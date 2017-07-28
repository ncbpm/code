--------------------------------------------------------
--  文件已创建 - 星期四-七月-27-2017   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for View CURRTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW CURRTYPE (CODE, CREATIONTIME, CREATOR, CURRDIGIT, CURRTYPESIGN, DATAORIGINFLAG, DR, ISDEFAULT, MODIFIEDTIME, MODIFIER, NAME, NAME2, NAME3, NAME4, NAME5, NAME6, PK_CURRTYPE, PK_GROUP, PK_ORG, ROUNDTYPE, TS, UNITCURRDIGIT, UNITROUNDTYPE) AS 
  (
       select CODE,CREATIONTIME,CREATOR,CURRDIGIT,CURRTYPESIGN,DATAORIGINFLAG,DR,ISDEFAULT,MODIFIEDTIME,MODIFIER,NAME,NAME2,NAME3,NAME4,NAME5,NAME6,PK_CURRTYPE,PK_GROUP,PK_ORG,ROUNDTYPE,TS,UNITCURRDIGIT,UNITROUNDTYPE from bd_currtype
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_ACCBOOK
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_ACCBOOK (ORGCODE, ORGNAME, PK_ORG, CODE, NAME, PK_ACCOUNTINGBOOK, PK_RELORG) AS 
  SELECT
	org.code AS orgcode,
	ORG .name as orgname,
	org .pk_org ,
	book.code,
	book. NAME,
	book.pk_accountingbook,
	book.pk_relorg
FROM
	org_accountingbook book
LEFT JOIN org_orgs org ON org.pk_org = book.pk_relorg
order by BOOK.CODE;
--------------------------------------------------------
--  DDL for View VIEW_BPM_ACCOA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_ACCOA (ORG_PK, ORG_CODE, ORG_NAME, OASTM_CODERULE, OASTM_PK, OASTM_CODE, OASTM_NAME, KEMU_PK, KEMU_NAME, KEMU_CODE, SSITEM_PK, SSITEM_CODE, SSITEM_NAME) AS 
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
--  DDL for View VIEW_BPM_AREA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_AREA (CODE, NAME, PK_AREACL, PK_FATHERAREA, MNECODE) AS 
  SELECT code,
  name,
  pk_areacl,
  pk_fatherarea,
  mnecode
FROM bd_areacl
WHERE 11          = 11
AND ( enablestate = 2 )
AND ( ( pk_group  = '0001A110000000000DDM' ) )
ORDER BY code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_ARRIVEORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_ARRIVEORDER (VBILLCODE, BBACKREFORDER, BC_VVENDBATCHCODE, BFAFLAG, BLETGOSTATE, BPRESENT, BPRESENTSOURCE, BTRANSASSET, BTRIATRADEFLAG, CASSCUSTID, CASTUNITID, CCURRENCYID, CFFILEID, CFIRSTBID, CFIRSTID, CFIRSTTYPECODE, CORIGCURRENCYID, CPASSBOLLROWNO, CPRODUCTORID, CPROJECTID, CPROJECTTASKID, CQUALITYLEVELID, CRECECOUNTRYID, CREPORTERID, CROWNO, CSENDCOUNTRYID, CSOURCEARRIVEBID, CSOURCEARRIVEID, CSOURCEBID, CSOURCEID, CSOURCETYPECODE, CTAXCOUNTRYID, CUNITID, DBILLDATE, DINVALIDDATE, DPLANRECEIVEDATE, DPRODUCEDATE, DR, DREPORTDATE, FBUYSELLFLAG, FPRODUCTCLASS, FTAXTYPEFLAG, IVALIDDAY, NACCUMBACKNUM, NACCUMCHECKNUM, NACCUMLETGOINNUM, NACCUMLETGONUM, NACCUMREPLNUM, NACCUMSTORENUM, NASTNUM, NELIGNUM, NEXCHANGERATE, NMNY, NNOTELIGNUM, NNUM, NORIGMNY, NORIGPRICE, NORIGTAXMNY, NORIGTAXPRICE, NPLANASTNUM, NPLANNUM, NPRESENTASTNUM, NPRESENTNUM, NPRICE, NTAX, NTAXMNY, NTAXPRICE, NTAXRATE, NWASTASTNUM, NWASTNUM, PK_APFINANCEORG, PK_APFINANCEORG_V, PK_APLIABCENTER, PK_APLIABCENTER_V, PK_ARRIVEORDER, PK_ARRIVEORDER_B, PK_ARRLIABCENTER, PK_ARRLIABCENTER_V, PK_BATCHCODE, PK_GROUP, PK_MATERIAL, PK_ORDER, PK_ORDER_B, PK_ORDER_BB1, PK_ORG, PK_ORG_V, PK_PASSBILL, PK_PASSBILL_B, PK_PSFINANCEORG, PK_PSFINANCEORG_V, PK_RACK, PK_RECEIVESTORE, PK_REQSTOORG, PK_REQSTOORG_V, PK_REQSTORE, PK_SRCMATERIAL, TS, VBACKREASONB, VBATCHCODE, VBDEF1, VBDEF10, VBDEF11, VBDEF12, VBDEF13, VBDEF14, VBDEF15, VBDEF16, VBDEF17, VBDEF18, VBDEF19, VBDEF2, VBDEF20, VBDEF3, VBDEF4, VBDEF5, VBDEF6, VBDEF7, VBDEF8, VBDEF9, VCHANGERATE, VFIRSTCODE, VFIRSTROWNO, VFIRSTTRANTYPE, VFREE1, VFREE10, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VMEMOB, VPASSBILLCODE, VSOURCECODE, VSOURCEROWNO, VSOURCETRANTYPE) AS 
  (select r.vbillcode ,b.BBACKREFORDER,b.BC_VVENDBATCHCODE,b.BFAFLAG,b.BLETGOSTATE,b.BPRESENT,b.BPRESENTSOURCE,b.BTRANSASSET,b.BTRIATRADEFLAG,b.CASSCUSTID,b.CASTUNITID,b.CCURRENCYID,b.CFFILEID,b.CFIRSTBID,b.CFIRSTID,b.CFIRSTTYPECODE,b.CORIGCURRENCYID,b.CPASSBOLLROWNO,b.CPRODUCTORID,b.CPROJECTID,b.CPROJECTTASKID,b.CQUALITYLEVELID,b.CRECECOUNTRYID,b.CREPORTERID,b.CROWNO,b.CSENDCOUNTRYID,b.CSOURCEARRIVEBID,b.CSOURCEARRIVEID,b.CSOURCEBID,b.CSOURCEID,b.CSOURCETYPECODE,b.CTAXCOUNTRYID,b.CUNITID,b.DBILLDATE,b.DINVALIDDATE,b.DPLANRECEIVEDATE,b.DPRODUCEDATE,b.DR,b.DREPORTDATE,b.FBUYSELLFLAG,b.FPRODUCTCLASS,b.FTAXTYPEFLAG,b.IVALIDDAY,b.NACCUMBACKNUM,b.NACCUMCHECKNUM,b.NACCUMLETGOINNUM,b.NACCUMLETGONUM,b.NACCUMREPLNUM,b.NACCUMSTORENUM,b.NASTNUM,b.NELIGNUM,b.NEXCHANGERATE,b.NMNY,b.NNOTELIGNUM,b.NNUM,b.NORIGMNY,b.NORIGPRICE,b.NORIGTAXMNY,b.NORIGTAXPRICE,b.NPLANASTNUM,b.NPLANNUM,b.NPRESENTASTNUM,b.NPRESENTNUM,b.NPRICE,b.NTAX,b.NTAXMNY,b.NTAXPRICE,b.NTAXRATE,b.NWASTASTNUM,b.NWASTNUM,b.PK_APFINANCEORG,b.PK_APFINANCEORG_V,b.PK_APLIABCENTER,b.PK_APLIABCENTER_V,b.PK_ARRIVEORDER,b.PK_ARRIVEORDER_B,b.PK_ARRLIABCENTER,b.PK_ARRLIABCENTER_V,b.PK_BATCHCODE,b.PK_GROUP,b.PK_MATERIAL,b.PK_ORDER,b.PK_ORDER_B,b.PK_ORDER_BB1,b.PK_ORG,b.PK_ORG_V,b.PK_PASSBILL,b.PK_PASSBILL_B,b.PK_PSFINANCEORG,b.PK_PSFINANCEORG_V,b.PK_RACK,b.PK_RECEIVESTORE,b.PK_REQSTOORG,b.PK_REQSTOORG_V,b.PK_REQSTORE,b.PK_SRCMATERIAL,b.TS,b.VBACKREASONB,b.VBATCHCODE,b.VBDEF1,b.VBDEF10,b.VBDEF11,b.VBDEF12,b.VBDEF13,b.VBDEF14,b.VBDEF15,b.VBDEF16,b.VBDEF17,b.VBDEF18,b.VBDEF19,b.VBDEF2,b.VBDEF20,b.VBDEF3,b.VBDEF4,b.VBDEF5,b.VBDEF6,b.VBDEF7,b.VBDEF8,b.VBDEF9,b.VCHANGERATE,b.VFIRSTCODE,b.VFIRSTROWNO,b.VFIRSTTRANTYPE,b.VFREE1,b.VFREE10,b.VFREE2,b.VFREE3,b.VFREE4,b.VFREE5,b.VFREE6,b.VFREE7,b.VFREE8,b.VFREE9,b.VMEMOB,b.VPASSBILLCODE,b.VSOURCECODE,b.VSOURCEROWNO,b.VSOURCETRANTYPE from  po_arriveorder r join po_arriveorder_b b  on r.pk_arriveorder =b.pk_arriveorder  where   nvl(r.fbillstatus ,0) = 3 and nvl(r.dr,0) = 0 and nvl(b.dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_ASSETSCODE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_ASSETSCODE (PK_CARD, PK_USINGSTATUS, STATUS_NAME, PK_TRANSITYPE, PK_RAORG, PK_RAORG_V, PK_ORG, ASSET_CODE, ASSET_NAME, ASSET_STATE, PK_CURRTYPE, CURR_NAME, PK_CATEGORY, CATE_CODE, CATE_NAME, SPEC, CARD_MODEL, POSITION, PRODUCER, PROVIDER, STARTDATE, ORIGINVALUE, NETWORTH, NATUREMONTH, PK_USEDEPT, DEPT_NAME, DEPT_CODE) AS 
  Select
	A .Pk_Card Pk_Card,--资产卡片主键
  b.pk_usingstatus,--使用状况主键
  status.STATUS_NAME,--使用状况名称
	A .Pk_Transitype Pk_Transitype,--交易类型 
	A .pk_raorg pk_raorg,--利润中心
	A .pk_raorg_v pk_raorg_v,--利润中心版本
	A .pk_org pk_org,--财务组织
	A .asset_code asset_code,--资产编码
	A .asset_name asset_name,--资产名称
b.asset_state,
	bd_currtype .pk_currtype pk_currtype,
  bd_currtype.NAME curr_name ,
  c. pk_category  pk_category ,--类别主键
	c.cate_code cate_code,--类别编码
	c.cate_name cate_name,--类别名称 
	A .spec spec,--规格
	A .card_model card_model,--型号
	A .position position,--存放地点
	A .producer producer,--生产厂商
	A .Provider Provider,--供应商
	SUBSTR (A .begin_date, 0, 10) startDate,--开始使用日期 
	b.originvalue originvalue,--本币原值 
	(
		b.localoriginvalue - b.accudep_cal
	) networth,
	B.Naturemonth Naturemonth,--naturemonth
  
	b.pk_usedept  PK_USEDEPT,--使用部门主键
  dept.name  dept_name,--部门名称
  dept.code  dept_code--部门编码
FROM
	fa_card A
left JOIN fa_cardhistory b ON A .pk_card = b.pk_card
left JOIN fa_cardsub ON A .pk_card = fa_cardsub.pk_card
LEFT OUTER JOIN fa_category c ON b.pk_category = c.pk_category
left join fa_deptscale  deptscale on deptscale.link_key=b.pk_usedept
left join org_dept dept on dept.pk_dept= deptscale.pk_dept
left outer join bd_currtype bd_currtype ON bd_currtype.pk_currtype=a.pk_currency
left join fa_usingstatus status on status.PK_USINGSTATUS=b.PK_USINGSTATUS
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
AND nvl( A .dr,0) = 0
AND nvl( b.dr ,0)= 0
AND nvl( fa_cardsub.dr,0) = 0
and nvl(bd_currtype.dr,0)=0
and nvl( status.dr,0)=0
and nvl( dept.dr,0)=0
and nvl(deptscale.dr,0)=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BANKACCSUB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BANKACCSUB (PK_BANKACCSUB, TS, DR, FROZENDATE, DEF10, ISCONCERTED, ACCNAME, DATAORIGINFLAG, ACCNUM, CREATOR, PK_CURRTYPE, DEFROZENDATE, NAME, OVERDRAFTTYPE, DEF7, DEF8, DEF5, DEF6, DEF9, CODE, PK_BANKACCBAS, FRONZENMNY, NAME5, CONCERTEDMNY, CREATIONTIME, MODIFIER, NAME6, NAME3, NAME4, ISTRADE, ACCTYPE, MODIFIEDTIME, FRONZENSTATE, DEF4, PAYAREA, OVERDRAFTMNY, DEF3, NAME2, DEF2, DEF1) AS 
  SELECT bd_bankaccsub.pk_bankaccsub pk_bankaccsub,
  bd_bankaccsub.ts ts,
  bd_bankaccsub.dr dr,
  bd_bankaccsub.frozendate frozendate,
  bd_bankaccsub.def10 def10,
  bd_bankaccsub.isconcerted isconcerted,
  bd_bankaccsub.accname accname,
  bd_bankaccsub.dataoriginflag dataoriginflag,
  bd_bankaccsub.accnum accnum,
  bd_bankaccsub.creator creator,
  bd_bankaccsub.pk_currtype pk_currtype,
  bd_bankaccsub.defrozendate defrozendate,
  bd_bankaccsub. NAME NAME,
  bd_bankaccsub.overdrafttype overdrafttype,
  bd_bankaccsub.def7 def7,
  bd_bankaccsub.def8 def8,
  bd_bankaccsub.def5 def5,
  bd_bankaccsub.def6 def6,
  bd_bankaccsub.def9 def9,
  bd_bankaccsub.code code,
  bd_bankaccsub.pk_bankaccbas pk_bankaccbas,
  bd_bankaccsub.fronzenmny fronzenmny,
  bd_bankaccsub.name5 name5,
  bd_bankaccsub.concertedmny concertedmny,
  bd_bankaccsub.creationtime creationtime,
  bd_bankaccsub.modifier modifier,
  bd_bankaccsub.name6 name6,
  bd_bankaccsub.name3 name3,
  bd_bankaccsub.name4 name4,
  bd_bankaccsub.istrade istrade,
  bd_bankaccsub.acctype acctype,
  bd_bankaccsub.modifiedtime modifiedtime,
  bd_bankaccsub.fronzenstate fronzenstate,
  bd_bankaccsub.def4 def4,
  bd_bankaccsub.payarea payarea,
  bd_bankaccsub.overdraftmny overdraftmny,
  bd_bankaccsub.def3 def3,
  bd_bankaccsub.name2 name2,
  bd_bankaccsub.def2 def2,
  bd_bankaccsub.def1 def1
FROM bd_bankaccsub bd_bankaccsub
Where Bd_Bankaccsub.Pk_Bankaccbas In
  (SELECT pk_bankaccbas
  FROM bd_bankaccbas
  WHERE (accclass    = 2)
  And ( Enablestate In (2, 1)
  AND pk_org         = '0001A110000000000DDM' )
 );
 

   COMMENT ON TABLE VIEW_BPM_BANKACCSUB  IS '--凭证表单中查询银行中使用-对应于NC-银行账户-集团';
--------------------------------------------------------
--  DDL for View VIEW_BPM_BD_BOMVERSION
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BD_BOMVERSION (HVERSION, FBOMTYPECODE, FBOMTYPE, CBOMID, CWLNAME, CWLCODE, CPK_WL, CPK_WLFL, CWLFLNAME, CWLFLCODE, CPK_UNIT, CUNITNAME, CUNITCODE, CMATERIALSPEC, CMATERIALTYPE) AS 
  select d.HVERSION,d.FBOMTYPE AS FBOMTYPECODE,(case d.FBOMTYPE when 1 then '生产BOM' when 2 then '包装BOM' when 3 then '配置BOM' end) as FBOMTYPE,d.CBOMID,a.name cWLname,a.code cWLcode,a.pk_material cPk_WL,a.pk_marbasclass cPk_WLFL,b.name cWLFLname,b.code cWLFLcode,a.pk_measdoc cPk_Unit,
c.name cUnitName,c.code cUnitCode,a.materialspec cMaterialspec,a.materialtype cMaterialtype
from bd_bom d left JOIN bd_material a on a.pk_material=d.hcmaterialid left join
bd_marbasclass b on a.pk_marbasclass=b.pk_marbasclass left join bd_measdoc c on a.pk_measdoc=c.pk_measdoc
where FBOMTYPE=1;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BD_MATERIAL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BD_MATERIAL (CWLNAME, CWLCODE, PRODAREA, CPK_WL, CPK_WLFL, CWLFLNAME, CWLFLCODE, CPK_UNIT, CUNITNAME, CUNITCODE, CMATERIALSPEC, CMATERIALTYPE) AS 
  select a.name cWLname,a.code cWLcode,a.prodarea,a.pk_material cPk_WL,a.pk_marbasclass cPk_WLFL,b.name cWLFLname,b.code cWLFLcode,a.pk_measdoc cPk_Unit,
c.name cUnitName,c.code cUnitCode,a.materialspec cMaterialspec,a.materialtype cMaterialtype
from bd_material a left join
bd_marbasclass b on a.pk_marbasclass=b.pk_marbasclass left join bd_measdoc c on a.pk_measdoc=c.pk_measdoc;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BD_SUPPLIER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BD_SUPPLIER (PK_SUPPLIER, SUPCODE, SUPNAME, SHORTNAME, TAXPAYERID, ZIPCODE, PK_LINKMAN, PHONE, CELL, FAX, PK_CURRTYPE, CURCODE, CURNAME, PK_CUSTBANK, PK_BANKACCBAS, ACCNUM, ACCCLASS, PK_BANKDOC, BANKDOCCODE, BANKDOCNAME, PK_BANKTYPE, BANKTYPECODE, BANKTYPENAME, SUPPROP, PK_COUNTRY, COUNTRYNAME, CORPADDRESS) AS 
  select supp.pk_supplier,supp.code as supcode,supp.name as supname,supp.shortname,taxpayerid,zipcode,
--联系人
supman.pk_linkman,psnlin.phone,psnlin.cell,psnlin.fax,
--币种汇率
supp.pk_currtype,cur.code as curcode,cur.name as curname,
--银行账户信息
bank.pk_custbank,bankbas.pk_bankaccbas,bankbas.accnum,--账户账号
bankbas.accclass,--账户分类 0=个人，1=客户，2=公司，3=供应商
bankdoc.pk_bankdoc,bankdoc.code as bankdoccode,bankdoc.name as bankdocname,--开户银行编码、名称
banktype.pk_banktype,banktype.code as banktypecode,banktype.name as banktypename,--银行类别编码、名称
(case supp.supprop when 0 then '外部单位' when 1 then '内部单位' end ) as supprop,
supp.pk_country,country.name as countryname,--国家地区
supp.corpaddress--企业地址
from bd_supplier supp 
left join bd_suplinkman supman on supman.pk_supplier=supp.pk_supplier
left join bd_linkman psnlin on psnlin.pk_linkman=supman.pk_linkman
left join bd_custbank bank on bank.pk_cust=supp.pk_supplier
left join bd_bankaccbas bankbas on bankbas.pk_bankaccbas=bank.pk_bankaccbas 
left join bd_bankdoc bankdoc on bankdoc.pk_bankdoc=bankbas.pk_bankdoc
left join bd_banktype banktype on banktype.pk_banktype=bankbas.pk_banktype
left join bd_currtype cur on cur.pk_currtype=supp.pk_currtype
left join BD_COUNTRYZONE country on supp.pk_country=country.pk_country;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BD_SUPPLIER_T
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BD_SUPPLIER_T (PK_ORG, ORGCODE, ORGNAME, PK_SUPPLIER, RESPPERSON, PSNNAME, RESPDEPT, PK_DEPTVID, DEPTCODE, DEPTNAME) AS 
  select sups.pk_org,org.code as orgCode,org.name as orgName,sups.pk_supplier,
sups.respperson,psn.name as psnname,sups.respdept,dept.pk_vid as pk_deptvid,
dept.code as deptcode,dept.name as deptname
from bd_supstock sups
left join org_orgs org on org.pk_org=sups.pk_org
left join org_dept dept on dept.pk_dept=sups.respdept
left join bd_psndoc psn on psn.pk_psndoc=sups.respperson;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BHGBT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BHGBT (PK_REJECTBILL, PK_ORG, ORGNAME, VBILLCODE, DBILLDATE, PK_STOCKORG, STOCKORGNAME, VREPORTBILLCODE, VAPPLYBILLCODE, DEPT_NAME, CAPPLYTIME, VCHKSTANDARDNAME, PK_MATERIAL, INV_CODE, INV_NAME, MATERIALSPEC, MATERIALTYPE, VBATCHCODE, VCODE, JLZNAME, NCHECKASTNUM, VCHANGERATE, JLNAME, NCHECKNUM, VMEMO, BILLTYPENAME) AS 
  SELECT h.pk_rejectbill,   
  h.pk_org,org.name as orgname,--质检中心组织  
  h.vbillcode,--不合格品处理单号
  h.dbilldate,--单据日期
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
  h.vmemo,--备注
  ty.billtypename
FROM qc_rejectbill h 
left join org_orgs org on org.pk_org=h.pk_org
left join org_orgs orgs on orgs.pk_org=h.pk_stockorg
LEFT JOIN org_dept od ON h.pk_applydept = od.pk_dept
left join qc_checkstandard qc on qc.pk_checkstandard =h.pk_chkstd --检验方案
left join sn_serialno sn on  sn.pk_serialno = h.pk_serialno --序列号
left join bd_measdoc mea on mea.pk_measdoc  = h.cunitid--计量单位cunitid
left join bd_measdoc meat on meat.pk_measdoc  = h.castunitid --单位
LEFT JOIN bd_material inv ON h. pk_material = inv.pk_material
left join qc_reportbill qcreport on qcreport.vrejectcode = h.vbillcode
left join bd_billtype ty on ty.pk_billtypeid = qcreport.capplytranid--质检报告中的报告类型
where nvl(h.dr,0)=0 and h.fbillstatus=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_BHGJYXX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BHGJYXX (PK_REJECTBILL, PK_CHECKBILL, PK_CHECKBILL_B, VCHECKITEMNAME, VCHKVALUE, VSTDVALUE1, BKEYITEM, BDEFAULTITEM, BMUSTREACH, BACCORDED, NAME, TCHECKTIME, VMEMOB) AS 
  SELECT 
    h.pk_rejectbill,
    chkb.pk_checkbill,
    chkb.pk_checkbill_b,
    chkitem.vcheckitemname,
    vchkvalue,vstdvalue1,
    (case when chkb.bkeyitem='N' then '0' else '1' end) as bkeyitem,
    (case when chkb.bdefaultitem='N' then '0' else '1' end) as bdefaultitem,
    (case when chkb.bmustreach='N' then '0' else '1' end) as bmustreach,
    (case when chkb.baccorded='N' then '0' else '1' end) as baccorded,
    psn.name,chkb.tchecktime,chkb.vmemob
  FROM qc_checkbill_b chkb
  left join bd_psndoc psn on psn.pk_psndoc = chkb.pk_chkpsn
  LEFT JOIN qc_checkbill chk on chk.pk_checkbill =chkb.pk_checkbill
  LEFT JOIN qc_applybill_s apl on apl.pk_applybill = chk.cfirstid
  LEFT JOIN  qc_rejectbill h on h.pk_applybill  = apl.pk_applybill
  LEFT JOIN  qc_rejectbill_b b ON b. pk_rejectbill = h. pk_rejectbill
  LEFT JOIN qc_checkitem chkitem
  ON chkb. pk_checkitem =chkitem.pk_checkitem
  where nvl(chkb.dr,0)=0 and chkb.buseless='N';
--------------------------------------------------------
--  DDL for View VIEW_BPM_BHGMXB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BHGMXB (PK_REJECTBILL, PK_REJECTBILL_B, CROWNO, JLNAME, NASTNUM, VCHANGERATE, JLZNAME, NNUM, INV_CODE, INV_NAME, CQUALITYLVNAME, BELIGIBLE, VREJECTTYPENAME, VCHECKITEMNAME, VNOELIGNOTE, PK_SUGGPROCESS, CDEALFASHNAME, FPROCESSJUDGE, CLFSPD, PSNNAME, DEPTNAME, VMEMOB) AS 
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
--  DDL for View VIEW_BPM_BLSQ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BLSQ (VBILLCODE, CPICKMID, VFIRSTMOCODE, VSALEBILLCODE, VBATCHCODE, VBOMVERSIONNUMBER, VPBOMVERSIONNUMBER, CMATERIALVID, CPRODUCCODE, CPRODUCNAME) AS 
  SELECT
a.vbillcode,--备料计划单号
b.CODE as cProducCode,--产品编码
b.NAME as cProducName,--产品名称
a.vbatchcode,--生产批次
a.cpickmid  ,--备料计划单主键
a.vfirstmocode ,--源头生产订单号
a.vsalebillcode ,-- 销售订单号
a.vbomversionnumber	,--生产BOM版本
a.vpbomversionnumber,--包装BOM版本
a.cmaterialvid--	产品主键
FROM mm_pickm a--备料计划
LEFT OUTER JOIN bd_material  b --物料基本信息（多版本）
ON a.cmaterialvid =b.pk_material 
where a.dr='0';
--------------------------------------------------------
--  DDL for View VIEW_BPM_BLSQ_B
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BLSQ_B (CPICKMID, CPICKM_BID, CBMATERIALID, CBMATERIALVID, CBUNITID, CBASTUNITID, VBCHANGERATE, BCONTROLL, NQUOTNUM, NSHOULDASTNUM, NSHOULDNUM, CDELIVERORGID, CDELIVERORGVID, CODE, NAME, NQUOTASTNUM, NUNITUSEASTNUM, DOLDOUTBOUNDNUM, NPLANOUTASTNUM, NPLANOUTNUM, NACCOUTASTNUM, NACCOUTNUM, NBSETPARTSNUM, VROWNO) AS 
  SELECT
a.cpickmid  ,--备料计划单主键
c.cpickm_bid ,--备料计划明细
c.cbmaterialid,--材料最新版本
c.cbmaterialvid, --材料编码
c.cbunitid ,--主单位 
c.cbastunitid ,--单位 
c.vbchangerate ,--换算率
c.bcontroll ,--控制
c.nquotnum ,--主定额用量
c.nshouldastnum ,--累计待发数量 
c.nshouldnum ,
c.cdeliverorgid,--发料组织最新版本
--c.cdeliverorgvid,--发料组织 
d.name as cdeliverorgvid,

b.CODE,--物料名称
b.NAME,--  物料编码
c.nquotastnum,--定额用量
c.nunituseastnum ,--单位用量 
c.nplanoutastnum as dOldOutboundNum,--原计划出库量
c.nplanoutastnum,--计划出库数量
c.nplanoutnum,--计划出库主数量
c.naccoutastnum , --累计出库数量 
c.naccoutnum ,--累计出库主数量
c.nbsetpartsnum,
c.vrowno
FROM  mm_pickm_b  c --备料计划明细
left join mm_pickm a ON a.cpickmid=c.CPICKMID
LEFT OUTER JOIN bd_material b--物料基本信息（多版本）
on C.CBMATERIALVID =b.pk_material 
LEFT OUTER JOIN org_stockorg_v d
on c.cdeliverorgvid=d.pk_vid
where c.dr='0';
--------------------------------------------------------
--  DDL for View VIEW_BPM_BLSQ_M
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BLSQ_M (VBILLCODE, CPRODUCCODE, CPRODUCNAME, VBATCHCODE, CPICKMID, VFIRSTMOCODE, VSALEBILLCODE, VBOMVERSIONNUMBER, VPBOMVERSIONNUMBER, CMATERIALVID) AS 
  SELECT
a.vbillcode,--备料计划单号
b.CODE as cProducCode,--产品编码
b.NAME as cProducName,--产品名称
a.vbatchcode,--生产批次
a.cpickmid  ,--备料计划单主键
a.vfirstmocode ,--源头生产订单号
a.vsalebillcode ,-- 销售订单号
a.vbomversionnumber	,--生产BOM版本
a.vpbomversionnumber,--包装BOM版本
a.cmaterialvid--	产品主键
FROM mm_pickm a--备料计划
LEFT OUTER JOIN bd_material  b --物料基本信息（多版本）
ON a.cmaterialvid =b.pk_material 
where a.dr='0';
--------------------------------------------------------
--  DDL for View VIEW_BPM_BOM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_BOM (PK_ORG, PLANORGPK, PLANORG_CODE, PLANORG_NAME, BOM_PK, PK_MATERIAL, INV_CODE, INV_NAME, INV_SPEC, INV_TYPE, BOM_TYPE, BOM_TYPENAME, BOM_HVERSION, MEASPK, MEASCODE, MEASNAME, BOM_FNUM) AS 
  SELECT
    inv.pk_org,
    --生产组织，主键，编码，名称
    bom. pk_org AS planorgpk,
    org.code    AS planorg_code,
    org.name    AS planorg_name,
    -- bom主键
    bom.cbomid AS bom_pk,
    --物料主键 物料编码 物料名称
    inv.pk_material,
    inv.code AS inv_code,
    inv.name AS inv_name,
     --规格   型号
    inv.materialspec AS inv_spec ,
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
    -- 版本号
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
  WHERE NVL(bom.dr,0)=0 and NVL (inv.dr, 0) = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_card
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_card (资产编码, 资产名称, 资产类别编码, 资产类别名称, 规格, 型号, 设备主键, 存放地点, 生产厂家, 开始使用日期, 原币原值, 净值, 使用月限, 使用部门, 部门编码) AS 
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
--  DDL for View VIEW_BPM_CASHFLOW
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CASHFLOW (ORG_PK, ORG_CODE, ORG_NAME, CASH_PK, CASH_CODE, CASH_NAME) AS 
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
--  DDL for View VIEW_BPM_CGFKJH
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CGFKJH (BPREFLAG, CCONTRACTID, CCURRENCYID, CROWNO, DBEGINDATE, DENDDATE, FEFFDATETYPE, IACCOUNTTERMNO, IITERMDAYS, ISDEPOSIT, NACCUMPAYAPPMNY, NACCUMPAYAPPORGMNY, NACCUMPAYMNY, NACCUMPAYORGMNY, NEXCHANGERATE, NMNY, NORIGMNY, NRATE, NTOTALORIGMNY, PK_FINANCEORG, PK_FINANCEORG_V, PK_GROUP, PK_ORDER, PK_ORDER_PAYPLAN, PK_PAYMENTCH, PK_PAYTERM, TS, PK_ORG, PK_ORG_V, VBILLCODE, DBILLDATE, PK_SUPPLIER, SUPPCODE, SUPPNAME, CORIGCURRENCYID, FKXYCODE, FKXYNAME) AS 
  select a.bpreflag, a.ccontractid, a.ccurrencyid, a.crowno, a.dbegindate, a.denddate, 
a.feffdatetype, a.iaccounttermno, a.iitermdays, a.isdeposit, a.naccumpayappmny, a.naccumpayapporgmny, a.naccumpaymny,
a.naccumpayorgmny, a.nexchangerate, a.nmny, a.norigmny, a.nrate, a.ntotalorigmny, a.pk_financeorg, a.pk_financeorg_v,
a.pk_group, a.pk_order, a.pk_order_payplan, a.pk_paymentch, b.pk_payterm, a.ts,
--采购组织            订单编号                        订单日期        供应商主键       供应商编码           供应商名称          币种主键     付款协议编码    付款协议名称    
b.PK_ORG,b.PK_ORG_V,b.VBILLCODE,SUBStr(b.DBILLDATE,0,10) DBILLDATE,b.pk_supplier,c.CODE SuppCode,c.NAME SuppName,b.corigcurrencyid,d.CODE fkxyCode,d.NAME fkxyName
from po_order_payplan a join po_order b on a.pk_order=b.pk_order
join bd_supplier c on b.pk_supplier=c.pk_supplier
join bd_payment d on d.pk_payment = b.pk_payterm
where a.dr = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_CGFKJH_M
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CGFKJH_M (PK_PAYTERM, NTOTALORIGMNY, CCURRENCYID, VBILLCODE, DBILLDATE, PK_SUPPLIER, SUPPNAME, CORIGCURRENCYID, FKXYNAME, PK_ORG) AS 
  select distinct b.pk_payterm,A.NTOTALORIGMNY,a.ccurrencyid,
--订单编号                        订单日期        供应商主键            供应商名称          币种主键       付款协议名称   所属组织 
b.VBILLCODE,SUBStr(b.DBILLDATE,0,10) DBILLDATE,b.pk_supplier,c.NAME SuppName,b.corigcurrencyid,d.NAME fkxyName,b.PK_ORG
from po_order_payplan a join po_order b on a.pk_order=b.pk_order
join bd_supplier c on b.pk_supplier=c.pk_supplier
join bd_payment d on d.pk_payment = b.pk_payterm
where a.dr = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_CGFKJH_T
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CGFKJH_T (BPREFLAG, CCONTRACTID, CCURRENCYID, CROWNO, DBEGINDATE, DENDDATE, FEFFDATETYPE, IACCOUNTTERMNO, IITERMDAYS, ISDEPOSIT, NACCUMPAYAPPMNY, NACCUMPAYAPPORGMNY, NACCUMPAYMNY, NACCUMPAYORGMNY, NEXCHANGERATE, NMNY, NORIGMNY, NRATE, NTOTALORIGMNY, PK_FINANCEORG, PK_FINANCEORG_V, PK_GROUP, PK_ORDER, PK_ORDER_PAYPLAN, PK_PAYMENTCH, PK_PAYTERM, TS, PK_ORG, PK_ORG_V, VBILLCODE, DBILLDATE, PK_SUPPLIER, SUPPCODE, SUPPNAME, CORIGCURRENCYID, FKXYCODE, FKXYNAME) AS 
  select a.bpreflag, a.ccontractid, a.ccurrencyid, a.crowno, a.dbegindate, a.denddate, 
a.feffdatetype, a.iaccounttermno, a.iitermdays, a.isdeposit, a.naccumpayappmny, a.naccumpayapporgmny, a.naccumpaymny,
a.naccumpayorgmny, a.nexchangerate, a.nmny, a.norigmny, a.nrate, a.ntotalorigmny, a.pk_financeorg, a.pk_financeorg_v,
a.pk_group, a.pk_order, a.pk_order_payplan, a.pk_paymentch, b.pk_payterm, a.ts,
--采购组织            订单编号                        订单日期        供应商主键       供应商编码           供应商名称          币种主键     付款协议编码    付款协议名称    
b.PK_ORG,b.PK_ORG_V,b.VBILLCODE,SUBStr(b.DBILLDATE,0,10) DBILLDATE,b.pk_supplier,c.CODE SuppCode,c.NAME SuppName,b.corigcurrencyid,d.CODE fkxyCode,d.NAME fkxyName
from po_order_payplan a join po_order b on a.pk_order=b.pk_order
join bd_supplier c on b.pk_supplier=c.pk_supplier
join bd_payment d on d.pk_payment = b.pk_payterm
where a.dr = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_CGGLCPGX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CGGLCPGX (PK_ORG, PK_MATERIAL, CODE, NAME, MATERIALSPEC, MATERIALTYPE) AS 
  select mp.pk_org,mp.pk_material,m.code,m.name,m.materialspec,m.materialtype  from bd_materialpu mp 
left join bd_material m on m.pk_material=mp.pk_material
left join bd_marbasclass cla on m.pk_marbasclass=cla.pk_marbasclass 
where nvl(mp.dr,0)=0 and (cla.code like '06%' or cla.code like '07%');
--------------------------------------------------------
--  DDL for View VIEW_BPM_CGHT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CGHT (PK_ORG, PK_MARBASCLASS, CLSCODE, CLSNAME, PK_MATERIAL, CODE, NAME, MATERIALSPEC, MATERIALTYPE, PK_MEASDOC, MEANAME, CMATERIALVID, PK_STORDOC, STORENAME, NONHANDNUM) AS 
  select maorg.pk_org,cls.pk_marbasclass,cls.code as clscode,cls.name as clsname,ma.pk_material,ma.code,ma.name,
ma.materialspec ,ma.materialtype,ma.pk_measdoc,mea.name as meaname,
'' as cmaterialvid,'' as pk_stordoc,'' as storename,'0' as nonhandnum
from bd_material ma 
left join bd_marorg maorg on maorg.pk_material = ma.pk_material
left join bd_marbasclass cls on ma.pk_marbasclass=cls.pk_marbasclass
left join bd_measdoc mea on ma.pk_measdoc=mea.pk_measdoc
where cls.code not like '04%' and cls.code not like '05%' and cls.code not like '99%';
--------------------------------------------------------
--  DDL for View VIEW_BPM_CGTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CGTYPE (TYPE, PK_BILLTYPECODE, BILLTYPENAME, PK_BILLTYPEID) AS 
  select distinct 'DX' as type, pk_billtypecode, billtypename, pk_billtypeid from bd_billtype 
where parentbilltype = '21' and pk_group = '0001A110000000000DDM' and nvl ( islock, 'N' ) = 'N'
and BILLTYPENAME like '%代销%'
union all
select distinct 'X' as type, pk_billtypecode, billtypename, pk_billtypeid from bd_billtype 
where parentbilltype = '21' and pk_group = '0001A110000000000DDM' and nvl ( islock, 'N' ) = 'N'
and BILLTYPENAME not like '%代销%';
--------------------------------------------------------
--  DDL for View VIEW_BPM_CLIENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CLIENT (TYPE, CUSTPK, CUSTCODE, CUSTNAME, CUSTSHORTNAME, PSNPK, PSNCODE, PSNNAME, DEPPK, DEPCODE, DEPNAME, PK_DETPVID, SORGPK, SORGCODE, SORGNAME, PK_ORGVID, PK_COUNTRY, COUNTRY_NAME) AS 
  (
	SELECT
		'ckdd' AS TYPE,
		--客户信息
		cust.pk_customer AS custpk,
		cust.code AS custcode,
		cust. NAME AS custname,
		cust.shortname AS custshortname,
		--专管业务员信息
		bcs.respperson AS psnpk,
		psn.code AS psncode,
		psn. NAME AS psnname,
		--专管部门信息
		bcs.respdept AS deppk,
		dept.code AS depcode,
		Dept. NAME AS Depname,
		dept.pk_vid AS pk_detpVid,
		--销售组织信息
		sorg.pk_salesorg AS sorgpk,
		sorg.code AS sorgcode,
		sorg. NAME AS sorgname,
		sorg.pk_vid as pk_orgVid,
		--国家地区
		country.pk_country,
		country. NAME country_name
	FROM
		bd_customer cust
	LEFT JOIN bd_custsale bcs ON cust.pk_customer = bcs.pk_customer
	LEFT JOIN org_salesorg sorg ON bcs.pk_org = sorg.pk_salesorg
	LEFT JOIN bd_psndoc psn ON bcs.respperson = psn.pk_psndoc
	LEFT JOIN org_dept dept ON bcs.respdept = Dept.Pk_Dept
	LEFT JOIN bd_countryzone country ON country.pk_country = cust.pk_country
	WHERE
		CUST.pk_custclass IN (
			SELECT
				pk_custclass
			FROM
				BD_CUSTCLASS
			WHERE
				NAME IN ('境外公司')
		)
	AND NVL (cust.dr, 0) = 0
	AND NVL (bcs.dr, 0) = 0
	AND NVL (sorg.dr, 0) = 0
	AND NVL (psn.dr, 0) = 0
	AND NVL (dept.dr, 0) = 0
	UNION ALL
		SELECT
			--客户信息
			'ckdd' AS TYPE,
			cust.pk_customer AS custpk,
			cust.code AS custcode,
			cust. NAME AS custname,
			cust.shortname AS custshortname,
			--专管业务员信息
			bcs.respperson AS psnpk,
			psn.code AS psncode,
			psn. NAME AS psnname,
			--专管部门信息
			bcs.respdept AS deppk,
			dept.code AS depcode,
			Dept. NAME AS Depname,
			dept.pk_vid as pk_detpVid,
			--销售组织信息
			sorg.pk_salesorg AS sorgpk,
			sorg.code AS sorgcode,
			sorg. NAME AS sorgname,
			sorg.pk_vid as pk_orgVid,
			--国家地区
			country.pk_country,
			country. NAME country_name
		FROM
			bd_customer cust
		LEFT JOIN bd_custsale bcs ON cust.pk_customer = bcs.pk_customer
		LEFT JOIN org_salesorg sorg ON bcs.pk_org = sorg.pk_salesorg
		LEFT JOIN bd_psndoc psn ON bcs.respperson = psn.pk_psndoc
		LEFT JOIN org_dept dept ON bcs.respdept = Dept.Pk_Dept
		LEFT JOIN bd_countryzone country ON country.pk_country = cust.pk_country
		WHERE
			CUST.pk_custclass IN (
				SELECT
					PK_CUSTCLASS
				FROM
					BD_CUSTCLASS
				WHERE
					parent_id IN (
						SELECT
							PK_CUSTCLASS
						FROM
							BD_CUSTCLASS
						WHERE
							NAME = '国外客户'
					)
			)
		AND NVL (cust.dr, 0) = 0
		AND NVL (bcs.dr, 0) = 0
		AND NVL (sorg.dr, 0) = 0
		AND NVL (psn.dr, 0) = 0
		AND NVL (dept.dr, 0) = 0
		UNION ALL
			SELECT
				'nxdd' as type,
				--客户信息
				cust.pk_customer AS custpk,
				cust.code AS custcode,
				cust. NAME AS custname,
				cust.shortname AS custshortname,
				--专管业务员信息
				bcs.respperson AS psnpk,
				psn.code AS psncode,
				psn. NAME AS psnname,
				--专管部门信息
				bcs.respdept AS deppk,
				dept.code AS depcode,
				Dept. NAME AS Depname,
				dept.pk_vid as pk_detpVid,
				--销售组织信息
				sorg.pk_salesorg AS sorgpk,
				sorg.code AS sorgcode,
				sorg. NAME AS sorgname,
				sorg.pk_vid as pk_orgVid,
				--国家地区
				country.pk_country,
				country. NAME country_name
			FROM
				bd_customer cust
			LEFT JOIN bd_custsale bcs ON cust.pk_customer = bcs.pk_customer
			LEFT JOIN org_salesorg sorg ON bcs.pk_org = sorg.pk_salesorg
			LEFT JOIN bd_psndoc psn ON bcs.respperson = psn.pk_psndoc
			LEFT JOIN org_dept dept ON bcs.respdept = Dept.Pk_Dept
			LEFT JOIN bd_countryzone country ON country.pk_country = cust.pk_country
			WHERE
				CUST.pk_custclass IN (
					SELECT
						pk_custclass
					FROM
						BD_CUSTCLASS
					WHERE
						NAME IN ('国内公司')
				)
			AND NVL (cust.dr, 0) = 0
			AND NVL (bcs.dr, 0) = 0
			AND NVL (sorg.dr, 0) = 0
			AND NVL (psn.dr, 0) = 0
			AND NVL (dept.dr, 0) = 0
			UNION ALL
				SELECT
					--客户信息
					'nxdd' as type,
					cust.pk_customer AS custpk,
					cust.code AS custcode,
					cust. NAME AS custname,
					cust.shortname AS custshortname,
					--专管业务员信息
					bcs.respperson AS psnpk,
					psn.code AS psncode,
					psn. NAME AS psnname,
					--专管部门信息
					bcs.respdept AS deppk,
					dept.code AS depcode,
					Dept. NAME AS Depname,
					dept.pk_vid as pk_detpVid,
					--销售组织信息
					sorg.pk_salesorg AS sorgpk,
					sorg.code AS sorgcode,
					sorg. NAME AS sorgname,
					sorg.pk_vid as pk_orgVid,
					--国家地区
					country.pk_country,
					country. NAME country_name
				FROM
					bd_customer cust
				LEFT JOIN bd_custsale bcs ON cust.pk_customer = bcs.pk_customer
				LEFT JOIN org_salesorg sorg ON bcs.pk_org = sorg.pk_salesorg
				LEFT JOIN bd_psndoc psn ON bcs.respperson = psn.pk_psndoc
				LEFT JOIN org_dept dept ON bcs.respdept = Dept.Pk_Dept
				LEFT JOIN bd_countryzone country ON country.pk_country = cust.pk_country
				WHERE
					CUST.pk_custclass IN (
						SELECT
							PK_CUSTCLASS
						FROM
							BD_CUSTCLASS
						WHERE
							parent_id IN (
								SELECT
									PK_CUSTCLASS
								FROM
									BD_CUSTCLASS
								WHERE
									NAME = '国内客户'
							)
					)
				AND NVL (cust.dr, 0) = 0
				AND NVL (bcs.dr, 0) = 0
				AND NVL (sorg.dr, 0) = 0
				AND NVL (psn.dr, 0) = 0
				AND NVL (dept.dr, 0) = 0
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_CURRINFO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CURRINFO (FNPK, FNNAME, SRCCURPK, SRCCURCODE, SRCCURNAME, OPPCURPK, OPPCURCODE, OPPCURNAM, RATE, RATEDATE, YEAR, MONTH) AS 
  (
   SELECT
    --汇率方案
    bc.pk_exratescheme AS fnpk,
    scm.name           AS fnname,
    --源币种
    bc.pk_currtype AS srccurpk,
    srccur.code    AS srccurcode,
    srccur.name    AS srccurname,
    --目的币种
    bc.oppcurrtype AS oppcurpk,
    oppcur.code    AS oppcurcode,
    oppcur.name    AS oppcurnam,
    rate.rate ,--日汇率中间价
    rate.ratedate,--汇率日期
    SUBSTR(rate.ratedate,0,4) AS YEAR,
    SUBSTR(rate.ratedate,6,2) AS MONTH
  FROM bd_exratescheme scm
  INNER JOIN bd_currinfo bc  ON scm.pk_exratescheme = bc. pk_exratescheme
  INNER JOIN bd_currtype oppcur  ON bc.oppcurrtype = oppcur. pk_currtype
  INNER JOIN bd_currtype srccur  ON bc.pk_currtype = srccur.pk_currtype
  inner join bd_currrate rate on rate.PK_CURRINFO=bc.PK_CURRINFO
  WHERE NVL(bc.dr,0)          =0
  AND NVL(rate.dr,0)         =0


);
--------------------------------------------------------
--  DDL for View VIEW_BPM_CUST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CUST (CUSTPK, CUSTCODE, CUSTNAME, CUSTSHORTNAME, PSNPK, PSNCODE, PSNNAME, DEPPK, DEPCODE, DEPNAME, PK_VID, SORGPK, SORGCODE, SORGNAME) AS 
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
Dept.Name As Depname,
dept.pk_vid pk_vid,
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
--  DDL for View VIEW_BPM_CUSTOMER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CUSTOMER (CUSNAME, CUSCODE, SHORTNAME, PK_CUSTOMER, PK_COUNTRY, COUNTRYNAME, COUNTRYCODE) AS 
  ( SELECT a.name as CusName,a.code as CusCode,A.SHORTNAME,A.PK_CUSTOMER,A.PK_COUNTRY,b.NAME as countryName,b.CODE as countryCode 
FROM bd_customer a left join bd_countryzone b on a.pk_country = b.pk_country);
--------------------------------------------------------
--  DDL for View VIEW_BPM_CWZZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_CWZZ (PK_ACCOUNTINGBOOK, BOOK_CODE, BOOK_NAME, ORG_NAME, ORG_CODE, PK_ORG, ORG_VID) AS 
  SELECT
	BOOK.pk_accountingbook,
	book.code as book_code,
  book.NAME AS book_name ,
  ORG.NAME AS org_name,
	org.code AS org_code,
	ORG.pk_org,
	ORG.PK_VID org_vid
FROM
	org_orgs org
LEFT JOIN org_accountingbook book ON book.pk_relorg = org.pk_org
WHERE
	ORG.orgtype5 = 'Y'
AND NVL (ORG.DR, 0) = 0
AND NVL (BOOK.dr, 0) = 0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_DELIVERY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_DELIVERY (VBILLCODE, BADVFEEFLAG, BBARSETTLEFLAG, BCHECKFLAG, BLARGESSFLAG, BOUTENDFLAG, BQUALITYFLAG, BTRANSENDFLAG, BTRIATRADEFLAG, BUSECHECKFLAG, CARORGID, CARORGVID, CASTUNITID, CCHANNELTYPEID, CCHAUFFEURID, CCURRENCYID, CCUSTMATERIALID, CDELIVERYBID, CDELIVERYID, CDEPTID, CDEPTVID, CEMPLOYEEID, CFIRSTBID, CFIRSTID, CFREECUSTID, CINSTOCKORGID, CINSTOCKORGVID, CINSTORDOCID, CINVOICECUSTID, CMATERIALID, CMATERIALVID, CMFFILEID, CORDERCUSTID, CORIGAREAID, CORIGCOUNTRYID, CORIGCURRENCYID, CPRICEFORMID, CPRODLINEID, CPRODUCTORID, CPROFITCENTERID, CPROFITCENTERVID, CPROJECTID, CQTUNITID, CQUALITYLEVELID, CRECECOUNTRYID, CRECEIVEADDDOCID, CRECEIVEADDRID, CRECEIVEAREAID, CRECEIVECUSTID, CRECEIVEPERSONID, CRETREASONID, CROWNO, CRPROFITCENTERID, CRPROFITCENTERVID, CSALEORGID, CSALEORGVID, CSENDADDDOCID, CSENDADDRID, CSENDAREAID, CSENDCOUNTRYID, CSENDPERSONID, CSENDSTOCKORGID, CSENDSTOCKORGVID, CSENDSTORDOCID, CSETTLEORGID, CSETTLEORGVID, CSPACEID, CSPROFITCENTERID, CSPROFITCENTERVID, CSRCBID, CSRCID, CSUPERCARGOID, CTAXCODEID, CTAXCOUNTRYID, CTRANSCUSTID, CUNITID, CVEHICLEID, CVEHICLETYPEID, CVENDORID, DBILLDATE, DR, DRECEIVEDATE, DSENDDATE, FBUYSELLFLAG, FROWNOTE, FTAXTYPEFLAG, NASTNUM, NCALTAXMNY, NDISCOUNT, NDISCOUNTRATE, NEXCHANGERATE, NGLOBALEXCHGRATE, NGLOBALMNY, NGLOBALTAXMNY, NGROUPEXCHGRATE, NGROUPMNY, NGROUPTAXMNY, NITEMDISCOUNTRATE, NMNY, NNETPRICE, NNUM, NORIGDISCOUNT, NORIGMNY, NORIGNETPRICE, NORIGPRICE, NORIGTAXMNY, NORIGTAXNETPRICE, NORIGTAXPRICE, NPIECE, NPRICE, NQTNETPRICE, NQTORIGNETPRICE, NQTORIGPRICE, NQTORIGTAXNETPRC, NQTORIGTAXPRICE, NQTPRICE, NQTTAXNETPRICE, NQTTAXPRICE, NQTUNITNUM, NREQRSNUM, NTAX, NTAXMNY, NTAXNETPRICE, NTAXPRICE, NTAXRATE, NTOTALARNUM, NTOTALELIGNUM, NTOTALESTARNUM, NTOTALNOTOUTNUM, NTOTALOUTNUM, NTOTALREPORTNUM, NTOTALRUSHNUM, NTOTALTRANSNUM, NTOTALUNELIGNUM, NTRANSLOSSNUM, NVOLUME, NWEIGHT, PK_BATCHCODE, PK_GROUP, PK_ORG, TS, VBATCHCODE, VBDEF1, VBDEF10, VBDEF11, VBDEF12, VBDEF13, VBDEF14, VBDEF15, VBDEF16, VBDEF17, VBDEF18, VBDEF19, VBDEF2, VBDEF20, VBDEF3, VBDEF4, VBDEF5, VBDEF6, VBDEF7, VBDEF8, VBDEF9, VCHANGERATE, VFIRSTBILLDATE, VFIRSTCODE, VFIRSTROWNO, VFIRSTTRANTYPE, VFIRSTTYPE, VFREE1, VFREE10, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VQTUNITRATE, VRECEIVETEL, VRETURNMODE, VSENDTEL, VSRCCODE, VSRCROWNO, VSRCTRANTYPE, VSRCTYPE) AS 
  (
 select y.vbillcode, b.BADVFEEFLAG,b.BBARSETTLEFLAG,b.BCHECKFLAG,b.BLARGESSFLAG,b.BOUTENDFLAG,b.BQUALITYFLAG,b.BTRANSENDFLAG,b.BTRIATRADEFLAG,b.BUSECHECKFLAG,b.CARORGID,b.CARORGVID,b.CASTUNITID,b.CCHANNELTYPEID,b.CCHAUFFEURID,b.CCURRENCYID,b.CCUSTMATERIALID,b.CDELIVERYBID,b.CDELIVERYID,b.CDEPTID,b.CDEPTVID,b.CEMPLOYEEID,b.CFIRSTBID,b.CFIRSTID,b.CFREECUSTID,b.CINSTOCKORGID,b.CINSTOCKORGVID,b.CINSTORDOCID,b.CINVOICECUSTID,b.CMATERIALID,b.CMATERIALVID,b.CMFFILEID,b.CORDERCUSTID,b.CORIGAREAID,b.CORIGCOUNTRYID,b.CORIGCURRENCYID,b.CPRICEFORMID,b.CPRODLINEID,b.CPRODUCTORID,b.CPROFITCENTERID,b.CPROFITCENTERVID,b.CPROJECTID,b.CQTUNITID,b.CQUALITYLEVELID,b.CRECECOUNTRYID,b.CRECEIVEADDDOCID,b.CRECEIVEADDRID,b.CRECEIVEAREAID,b.CRECEIVECUSTID,b.CRECEIVEPERSONID,b.CRETREASONID,b.CROWNO,b.CRPROFITCENTERID,b.CRPROFITCENTERVID,b.CSALEORGID,b.CSALEORGVID,b.CSENDADDDOCID,b.CSENDADDRID,b.CSENDAREAID,b.CSENDCOUNTRYID,b.CSENDPERSONID,b.CSENDSTOCKORGID,b.CSENDSTOCKORGVID,b.CSENDSTORDOCID,b.CSETTLEORGID,b.CSETTLEORGVID,b.CSPACEID,b.CSPROFITCENTERID,b.CSPROFITCENTERVID,b.CSRCBID,b.CSRCID,b.CSUPERCARGOID,b.CTAXCODEID,b.CTAXCOUNTRYID,b.CTRANSCUSTID,b.CUNITID,b.CVEHICLEID,b.CVEHICLETYPEID,b.CVENDORID,b.DBILLDATE,b.DR,b.DRECEIVEDATE,b.DSENDDATE,b.FBUYSELLFLAG,b.FROWNOTE,b.FTAXTYPEFLAG,b.NASTNUM,b.NCALTAXMNY,b.NDISCOUNT,b.NDISCOUNTRATE,b.NEXCHANGERATE,b.NGLOBALEXCHGRATE,b.NGLOBALMNY,b.NGLOBALTAXMNY,b.NGROUPEXCHGRATE,b.NGROUPMNY,b.NGROUPTAXMNY,b.NITEMDISCOUNTRATE,b.NMNY,b.NNETPRICE,b.NNUM,b.NORIGDISCOUNT,b.NORIGMNY,b.NORIGNETPRICE,b.NORIGPRICE,b.NORIGTAXMNY,b.NORIGTAXNETPRICE,b.NORIGTAXPRICE,b.NPIECE,b.NPRICE,b.NQTNETPRICE,b.NQTORIGNETPRICE,b.NQTORIGPRICE,b.NQTORIGTAXNETPRC,b.NQTORIGTAXPRICE,b.NQTPRICE,b.NQTTAXNETPRICE,b.NQTTAXPRICE,b.NQTUNITNUM,b.NREQRSNUM,b.NTAX,b.NTAXMNY,b.NTAXNETPRICE,b.NTAXPRICE,b.NTAXRATE,b.NTOTALARNUM,b.NTOTALELIGNUM,b.NTOTALESTARNUM,b.NTOTALNOTOUTNUM,b.NTOTALOUTNUM,b.NTOTALREPORTNUM,b.NTOTALRUSHNUM,b.NTOTALTRANSNUM,b.NTOTALUNELIGNUM,b.NTRANSLOSSNUM,b.NVOLUME,b.NWEIGHT,b.PK_BATCHCODE,b.PK_GROUP,b.PK_ORG,b.TS,b.VBATCHCODE,b.VBDEF1,b.VBDEF10,b.VBDEF11,b.VBDEF12,b.VBDEF13,b.VBDEF14,b.VBDEF15,b.VBDEF16,b.VBDEF17,b.VBDEF18,b.VBDEF19,b.VBDEF2,b.VBDEF20,b.VBDEF3,b.VBDEF4,b.VBDEF5,b.VBDEF6,b.VBDEF7,b.VBDEF8,b.VBDEF9,b.VCHANGERATE,b.VFIRSTBILLDATE,b.VFIRSTCODE,b.VFIRSTROWNO,b.VFIRSTTRANTYPE,b.VFIRSTTYPE,b.VFREE1,b.VFREE10,b.VFREE2,b.VFREE3,b.VFREE4,b.VFREE5,b.VFREE6,b.VFREE7,b.VFREE8,b.VFREE9,b.VQTUNITRATE,b.VRECEIVETEL,b.VRETURNMODE,b.VSENDTEL,b.VSRCCODE,b.VSRCROWNO,b.VSRCTRANTYPE,b.VSRCTYPE
  from so_delivery y
  join so_delivery_b b
    on y.cdeliveryid = b.cdeliveryid
 where nvl(y.dr, 0) = 0
   and nvl(b.dr, 0) = 0
   and nvl(y.fstatusflag, 0) = 2);
--------------------------------------------------------
--  DDL for View VIEW_BPM_FACARD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_FACARD (CARD_CODE, ASSET_CODE, ASSET_NAME, POSITION, ORG_DEPTNAME, PK_USEDEPT, BD_PSNDOCNAME, BAR_CODE, TYPE7, TYPE8, TYPE9, TYPE10, TYPE11, TYPE12, TYPE13, TYPE14, TYPE15, TYPE16, PK_CARD, ROWNUM_) AS 
  (
SELECT CARD_CODE,ASSET_CODE,ASSET_NAME,POSITION,ORG_DEPTNAME,PK_USEDEPT,BD_PSNDOCNAME,BAR_CODE,TYPE7,TYPE8,TYPE9,TYPE10,TYPE11,TYPE12,TYPE13,TYPE14,TYPE15,TYPE16,PK_CARD,ROWNUM_
FROM
  (SELECT row_.*,
    rownum rownum_
  FROM
    (SELECT fa_card.card_code,
      fa_card.asset_code,
      fa_card.asset_name,
      fa_card.position,
      org_dept.name org_deptname,
      fa_cardhistory.pk_usedept,
      bd_psndoc.name bd_psndocname,
      fa_card.bar_code,
      NULL type7,
      NULL type8,
      NULL type9,
      NULL type10,
      NULL type11,
      NULL type12,
      NULL type13,
      NULL type14,
      NULL type15,
      NULL type16,
      fa_cardhistory.pk_card
    FROM fa_card
    INNER JOIN fa_cardhistory
    ON fa_card.pk_card = fa_cardhistory.pk_card
    LEFT OUTER JOIN org_dept
    ON fa_cardhistory.pk_mandept = org_dept.pk_dept
    LEFT OUTER JOIN pam_addreducestyle
    ON fa_card.pk_addreducestyle = pam_addreducestyle.pk_addreducestyle
    LEFT OUTER JOIN fa_usingstatus
    ON fa_cardhistory.pk_usingstatus = fa_usingstatus.pk_usingstatus
    LEFT OUTER JOIN bd_project
    ON fa_cardhistory.pk_jobmngfil = bd_project.pk_project
    LEFT OUTER JOIN bd_psndoc
    ON fa_card.pk_assetuser = bd_psndoc.pk_psndoc
    LEFT OUTER JOIN bd_supplier
    ON fa_card.provider              = bd_supplier.pk_supplier
    WHERE ( fa_card.dr               = 0
    AND fa_cardhistory.dr            = 0
    AND fa_cardhistory.business_flag = 'Y'
    AND EXISTS
      (SELECT pk_card
      FROM fa_cardhistory fa_cardhistory2
      WHERE dr                           = 0
      AND fa_cardhistory2.laststate_flag = 'Y'
      AND fa_cardhistory2.asset_state    = 'exist'
      AND fa_cardhistory.pk_card         = fa_cardhistory2.pk_card
      )
    AND ( fa_cardhistory.laststate_flag = 'Y'
    OR ( fa_cardhistory.asset_state    IN ( 'reduce', 'reduce_split', 'reduce_combin', 'redeploy_way', 'redeploy_out' )
    AND fa_cardhistory.laststate_flag   = 'N' ) )
    AND NVL ( fa_card.pk_equip, '~' )   = '~' )
    ORDER BY fa_card.card_code
    ) row_
  WHERE rownum <= 500
  )
WHERE rownum_ > 0
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_FIVEMEATALS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_FIVEMEATALS (PK_ORG, CCARDNO, CARDSTATUS, VPROJECT, PH_CODE, PH_NAME, VDEPARTMENT, CDEPCODE, CDEPNAME, VREMARK, YUE, CUSERMONTH) AS 
  SELECT MIN(h.pk_org) as pk_org,
  h.vcardno AS ccardno,
  MIN(
  CASE
    WHEN vbillstatus=1
    THEN'启用'
    ELSE '注销'
  END) AS CardStatus,
  MIN(h.vproject) as vproject,
  MIN(pro.ph_code) ph_code,
  MIN(pro.ph_name) ph_name,
  MIN(h.vdepartment) vdepartment,
  MIN(org.code||'_'||dept.code )  AS cdepcode,
  MIN(dept.name )AS cdepname,
  MIN(h.vremark) vremark,
  e.balance AS yue,
  MIN(e.cperiod ) AS cusermonth
FROM ic_fivemetals_h h
LEFT JOIN
  (SELECT SUM(nmny * itype) balance,
    b.cperiod,
    b.pk_fivemetals_h
  FROM ic_fivemetals_b b
  WHERE NVL(b.dr, 0) = 0
  GROUP BY b.pk_fivemetals_h,
    b.cperiod
  ) e
ON h.pk_fivemetals_h=e.pk_fivemetals_h
LEFT JOIN org_orgs org
ON org.pk_org=h.pk_org
LEFT JOIN org_dept dept
ON dept.pk_dept=h.vdepartment
LEFT JOIN view_bpm_project pro
ON pro.ph_pk        =h.vproject
WHERE h. vbillstatus=1
GROUP BY h.vcardno,e.balance;
--------------------------------------------------------
--  DDL for View VIEW_BPM_FIVEMETALSBALANC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_FIVEMETALSBALANC (CARDNO, PK_GROUP, PK_ORG, CPERIOD, VDEPARTMENT, VPROJECT, BALANCE, VBILLSTATUS) AS 
  (select h.vcardno cardNo,
       h.pk_group,
       h.pk_org,
       e.cperiod,
       h.vdepartment,
       h.vproject,
       e.balance,
       h.vbillstatus
  from ic_fivemetals_h h
  join (select sum(nmny * itype) balance, b.cperiod,b.pk_fivemetals_h
          from ic_fivemetals_b b
         where nvl(b.dr, 0) = 0
         group by b.pk_fivemetals_h, b.cperiod) e
    on h.pk_fivemetals_h = e.pk_fivemetals_h
 where nvl(h.dr, 0) = 0
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_FIVEMETALSBALANCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_FIVEMETALSBALANCE (CARDNO, PK_GROUP, PK_ORG, CPERIOD, VDEPARTMENT, VPROJECT, BALANCE, VBILLSTATUS) AS 
  (select h.vcardno cardNo,--卡号
       h.pk_group,--集团
       h.pk_org,--组织
       e.cperiod,--月份
       h.vdepartment,--部门
       h.vproject,--项目
       e.balance,--余额
       h.vbillstatus---状态  1-可用  2-停用
  from ic_fivemetals_h h
  join (select sum(nmny * itype) balance, b.cperiod,b.pk_fivemetals_h
          from ic_fivemetals_b b
         where nvl(b.dr, 0) = 0
         group by b.pk_fivemetals_h, b.cperiod) e
    on h.pk_fivemetals_h = e.pk_fivemetals_h
 where nvl(h.dr, 0) = 0
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_FKXY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_FKXY (PK_PAYMENT, PK_PAYMENTCH, ISDEPOSIT, ACCRATE, ACCOUNTDAY, PK_PAYPERIOD, PERCODE, PERNAME, PK_BALATYPE, TYPECODE, TYPENAME, CHECKDATA, EFFECTDATEADDDATE, EFFECTMONTH, PK_RATE, PAYMENTDAY, SHOWORDER, PREPAYMENT, EFFECTADDMONTH) AS 
  SELECT  pay.pk_payment,pay.pk_paymentch,pay.isdeposit,pay.accrate,pay.accountday,
pay.pk_payperiod,per.code as percode,per.name as pername,
pay.pk_balatype,ty.code as typecode,ty.name as typename,
pay.checkdata,pay.effectdateadddate,pay.effectmonth,pay.pk_rate,pay.paymentday,
pay.showorder,pay.prepayment,pay.effectaddmonth
FROM bd_paymentch pay
left join bd_payperiod per on per.pk_payperiod=pay.pk_payperiod
left join bd_balatype ty on ty.pk_balatype=pay.pk_balatype
where nvl(pay.dr,0)=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_GWGZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_GWGZ (PK_WA_CRT, PK_WA_GRD, PK_WA_PRMLV, MAX_VALUE, MIN_VALUE, PK_WA_SECLV, CRITERIONVALUE, GRDNAME, PRMLVNAME, PK_WA_GRADEVER, SECLVNAME) AS 
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

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_HTGL_CHBM (ROWNO, DATASOURCE, VBILLCODE, PK_MATERIAL, INVCODE, INVNAME, NNUM, NPRICE, PK_MEASDOC, UTCODE, UTNAME, BZGGNAME, PK_BZGG) AS 
  select ROWNUM as rowNo,t.DATASOURCE,t.VBILLCODE,t.PK_MATERIAL,t.INVCODE,t.INVNAME,t.NNUM,t.NPRICE,t.PK_MEASDOC,t.UTCODE,t.UTNAME,t.BZGGNAME,t.PK_BZGG from (
select distinct '请购单' as DataSource,A.VBILLCODE,c.pk_material,c.code as Invcode,c.name as Invname,b.nnum,0 as nprice,d.pk_measdoc,d.code as UTcode,d.name as UTname,
e.NAME as BZGGname,e.PK_DEFDOC as PK_BZGG
from po_praybill_b b left join po_praybill a on a.pk_praybill =b.pk_praybill  
left join bd_material c on b.pk_material=c.pk_material 
left join bd_measdoc d on b.cunitid =d.pk_measdoc
left join VIEW_BPM_PACKAGESTYPE e on b.vfree1=e.PK_DEFDOC
) t;
--------------------------------------------------------
--  DDL for View VIEW_BPM_HTGL_QGD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_HTGL_QGD (ROWNO, DATASOURCE, VBILLCODE, PK_ORG, CTRANTYPEID, VTRANTYPECODE, CROWNO, PK_MATERIAL, INVCODE, INVNAME, HEADID, BODYID, NNUM, CONTRACTNUMSUM, NPRICE, PK_MEASDOC, UTCODE, UTNAME, BZGGNAME, PK_BZGG) AS 
  select ROWNUM as rowNo,t.DATASOURCE,t.VBILLCODE,t.PK_ORG,t.CTRANTYPEID,t.VTRANTYPECODE,t.CROWNO,t.PK_MATERIAL,t.INVCODE,t.INVNAME,t.HEADID,t.BODYID,t.NNUM,t.CONTRACTNUMSUM,t.NPRICE,t.PK_MEASDOC,t.UTCODE,t.UTNAME,t.BZGGNAME,t.PK_BZGG
  from (
--数据来源，单据编号
select distinct '请购单' as DataSource,A.VBILLCODE,a.pk_org,
--交易类型主键，订单类型（交易类型编码）,从表行号
a.ctrantypeid,a.vtrantypecode,b.crowno,
--物料主键，物料编码，物料名称
c.pk_material,c.code as Invcode,c.name as Invname,
--数量，累计合同数量，单价，单位主键，单位编码，单位名称
b.pk_praybill as HeadID,b.pk_praybill_b as BodyID,b.nnum,
(case b.vbdef1
when '' then '0'
when '~' then '0'
ELSE b.vbdef1
end) as contractNumSum,0.000000 as nprice,d.pk_measdoc,d.code as UTcode,d.name as UTname,
--包装规格名称，包装规格主键
e.NAME as BZGGname,e.PK_DEFDOC as PK_BZGG
from po_praybill_b b left join po_praybill a on a.pk_praybill =b.pk_praybill  
left join bd_material c on b.pk_material=c.pk_material 
left join bd_measdoc d on b.cunitid =d.pk_measdoc
left join VIEW_BPM_PACKAGESTYPE e on b.vfree1=e.PK_DEFDOC
where nvl(b.dr,0)=0) t 
where nnum>contractNumSum;
--------------------------------------------------------
--  DDL for View VIEW_BPM_INCOTERM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_INCOTERM (CODE, NAME, PK_INCOTERM, PK_ORG, PK_GROUP) AS 
  (
  select c.code, c.name, c.pk_incoterm, c.pk_org, c.pk_group
  from bd_incoterm c );
--------------------------------------------------------
--  DDL for View VIEW_BPM_JHFA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_JHFA (CPSID, VPSCODE, VPSNAME, PK_PST, CODE, NAME) AS 
  select mm.cpsid,mm.vpscode,mm.vpsname,pl.pk_pst,pl.code,pl.name  from mm_ps mm 
left join bd_planstrategy pl on mm.fplanstrategy=pl.pk_pst;
--------------------------------------------------------
--  DDL for View VIEW_BPM_JOBLEVEL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_JOBLEVEL (CODE, NAME, PK_JOBLEVEL) AS 
  select b.code ,b.name ,b.PK_DEFDOC as pk_joblevel from bd_defdoc b 
  left join bd_defdoclist a on a.pk_defdoclist=b.pk_defdoclist 
  where a.code='HR32' order by b.code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_JOBRANK
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_JOBRANK (JOBRANKCODE, JOBRANKNAME, PK_JOBRANK) AS 
  select b.code as jobrankcode,b.name as jobrankname,b.PK_DEFDOC as pk_jobrank from bd_defdoc b 
  left join bd_defdoclist a on a.pk_defdoclist=b.pk_defdoclist 
  where a.code='HR29' order by b.code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_JYCLFS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_JYCLFS (CDEALFASHCODE, CDEALFASHNAME, VMEMO, PK_DEALFASHION) AS 
  select cdealfashcode, cdealfashname, vmemo, pk_dealfashion
  from scm_dealfashion
 where 11 = 11
   and ((pk_group = '0001A110000000000DDM' and nvl(dr,0) = 0))
 order by cdealfashcode;
--------------------------------------------------------
--  DDL for View VIEW_BPM_JZQJ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_JZQJ (PK_ORG, ORGNAME, PK_PERIODSTATE, PK_PERIODSCHEME, ACCOUNTMARK, PK_WA_CLASS, WACLASSCODE, WACLASSCNAME, PK_WA_PERIOD, CYEAR, CPERIOD, XZQJ) AS 
  select a.pk_org , org.name orgname,a.pk_periodstate ,b.pk_periodscheme,a.accountmark,
a.pk_wa_class ,c.code waclasscode,c.name waclasscname,
a.pk_wa_period,b.cyear,b.cperiod, b.cyear||b.cperiod AS xzqj
from wa_periodstate a
inner join wa_period b on a.pk_wa_period=b.pk_wa_period
left join wa_waclass c on a.pk_wa_class=c.pk_wa_class
left join org_orgs org on org.pk_org=a.pk_org 
where a.enableflag = 'Y';
--------------------------------------------------------
--  DDL for View VIEW_BPM_LZLX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_LZLX (TRNSTYPECODE, TRNSTYPENAME, PK_TRNSTYPE) AS 
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
--  DDL for View VIEW_BPM_LZYY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_LZYY (CODE, NAME, MNECODE, PK_DEFDOC, PID) AS 
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
--  DDL for View VIEW_BPM_MATERIAL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_MATERIAL (INVTYPE, INVTYPECODE, ELECTRONICSALE, PK_ORG, INVPK, INVCODE, INVNAME, MEASPK, MEASCODE, MEASNAME, SCDDNAME, PK_SCDD, TAXRATE, PK_XSORG, XSORGCODE, XSORGNAME, MATERORG, MATERORGCODE, MARTYPE) AS 
  SELECT DISTINCT
  (case mara.code
  when '01' then 'YL'
  WHEN '02' THEN 'ZJ'
  WHEN '06' THEN 'BCP'
  WHEN '07' THEN 'CP'
  END) as invType,--物料类型简称（原料、助剂、半成品、成品、副产品）
  mara.code invTypeCode,--物料类型编码
  inv.electronicsale,--是否电子销售
	inv.pk_org,
	--物料信息
	inv.pk_material AS invpk,
	inv.code AS invcode,
	inv. NAME AS invname,
	--主单位
	meas.pk_measdoc AS measpk,
	meas.code AS meascode,
	meas. NAME AS measname,
	--生产地点
	doc. NAME scddName,
	doc.pk_defdoc pk_scdd,
	--物料税率
	c.taxrate,
	--物料销售组织主键
  e.pk_salesorg as pk_xsOrg,  
  e.code xsOrgCode,
  e.name xsOrgName,
	--物料库存信息
  mater.pk_org as materOrg,
    ma.code as materOrgCode,
	(
		CASE mater.martype
		WHEN 'DR' THEN
			'分销补货'
		WHEN 'FR' THEN
			'工厂补货'
		WHEN 'MR' THEN
			'制造件'
		WHEN 'PR' THEN
			'采购件'
		WHEN 'OT' THEN
			'委外件'
		WHEN 'ET' THEN
			'其他'
		ELSE
			mater.martype
		END
	) AS martype
	--MATER.pk_materialstock
FROM
	bd_material inv
left JOIN bd_materialprod invprod ON inv.pk_material = invprod.pk_material
left JOIN bd_defdoc doc ON inv.prodarea = doc.pk_defdoc
LEFT JOIN bd_measdoc meas ON inv.pk_measdoc = meas.pk_measdoc
LEFT JOIN bd_taxcode b ON inv.pk_mattaxes = b.mattaxes
LEFT JOIN bd_taxrate c ON b.pk_taxcode = c.pk_taxcode
LEFT JOIN bd_materialsale D ON inv.pk_material = D .pk_material
left join org_salesorg e on d.pk_org =e.pk_salesorg
left JOIN bd_materialstock mater ON mater.pk_material = inv.pk_material
left join org_salesorg ma on ma.pk_salesorg =mater.pk_org
left join bd_marasstframe mara on inv.PK_MARASSTFRAME=mara.PK_MARASSTFRAME
left join bd_marbasclass class on inv.pk_marbasclass=class.pk_marbasclass 
WHERE	NVL (inv.dr, 0) = 0 AND NVL (invprod.dr, 0) = 0
AND NVL (MATER.dr, 0) = 0
AND NVL (doc.dr, 0) = 0
and mara.code in ('01','02','06','07') 
union all
SELECT DISTINCT
  (case class.code
  when '08' then 'FuChanPin'
  END) as invType,--物料类型简称
  class.code invTypeCode,--物料类型编码
  inv.electronicsale,--是否电子销售
	inv.pk_org,
	--物料信息
	inv.pk_material AS invpk,
	inv.code AS invcode,
	inv. NAME AS invname,
	--主单位
	meas.pk_measdoc AS measpk,
	meas.code AS meascode,
	meas. NAME AS measname,
	--生产地点
	doc. NAME scddName,
	doc.pk_defdoc pk_scdd,
	--物料税率
	c.taxrate,
	--物料销售组织主键
  e.pk_salesorg as pk_xsOrg,  
  e.code xsOrgCode,
  e.name xsOrgName,
	--物料库存信息
  mater.pk_org as materOrg,
  ma.code as materOrgCode,
	(
		CASE mater.martype
		WHEN 'DR' THEN
			'分销补货'
		WHEN 'FR' THEN
			'工厂补货'
		WHEN 'MR' THEN
			'制造件'
		WHEN 'PR' THEN
			'采购件'
		WHEN 'OT' THEN
			'委外件'
		WHEN 'ET' THEN
			'其他'
		ELSE
			mater.martype
		END
	) AS martype
	--MATER.pk_materialstock
FROM
	bd_material inv
left JOIN bd_materialprod invprod ON inv.pk_material = invprod.pk_material
left JOIN bd_defdoc doc ON inv.prodarea = doc.pk_defdoc
LEFT JOIN bd_measdoc meas ON inv.pk_measdoc = meas.pk_measdoc
LEFT JOIN bd_taxcode b ON inv.pk_mattaxes = b.mattaxes
LEFT JOIN bd_taxrate c ON b.pk_taxcode = c.pk_taxcode
LEFT JOIN bd_materialsale D ON inv.pk_material = D .pk_material
left join org_salesorg e on d.pk_org =e.pk_salesorg
left JOIN bd_materialstock mater ON mater.pk_material = inv.pk_material
left join org_salesorg ma on ma.pk_salesorg =mater.pk_org
left join bd_marasstframe mara on inv.PK_MARASSTFRAME=mara.PK_MARASSTFRAME
left join bd_marbasclass class on inv.pk_marbasclass=class.pk_marbasclass 
WHERE	NVL (inv.dr, 0) = 0 AND NVL (invprod.dr, 0) = 0
AND NVL (MATER.dr, 0) = 0
AND NVL (doc.dr, 0) = 0
and  class.code='08';
--------------------------------------------------------
--  DDL for View VIEW_BPM_MATERIAL_BCP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_MATERIAL_BCP (PK_ORG, INVPK, INVCODE, INVNAME, MEASPK, MEASCODE, MEASNAME, SCDDNAME, PK_SCDD, TAXRATE, PK_XSORG, XSORGCODE, XSORGNAME, MARTYPE) AS 
  SELECT DISTINCT
	inv.pk_org,
	--物料信息
	inv.pk_material AS invpk,
	inv.code AS invcode,
	inv. NAME AS invname,
	--主单位
	meas.pk_measdoc AS measpk,
	meas.code AS meascode,
	meas. NAME AS measname,
	--生产地点
	doc. NAME scddName,
	doc.pk_defdoc pk_scdd,
	--物料税率
	c.taxrate,
	--物料销售组织主键
  e.pk_salesorg as pk_xsOrg,  
  e.code xsOrgCode,
  e.name xsOrgName,
	--物料库存信息
	(
		CASE mater.martype
		WHEN 'DR' THEN
			'分销补货'
		WHEN 'FR' THEN
			'工厂补货'
		WHEN 'MR' THEN
			'制造件'
		WHEN 'PR' THEN
			'采购件'
		WHEN 'OT' THEN
			'委外件'
		WHEN 'ET' THEN
			'其他'
		ELSE
			mater.martype
		END
	) AS martype
	--MATER.pk_materialstock
FROM
	bd_material inv
left JOIN bd_materialprod invprod ON inv.pk_material = invprod.pk_material
left JOIN bd_defdoc doc ON inv.prodarea = doc.pk_defdoc
LEFT JOIN bd_measdoc meas ON inv.pk_measdoc = meas.pk_measdoc
LEFT JOIN bd_taxcode b ON inv.pk_mattaxes = b.mattaxes
LEFT JOIN bd_taxrate c ON b.pk_taxcode = c.pk_taxcode
LEFT JOIN bd_materialsale D ON inv.pk_material = D .pk_material
left join org_salesorg e on d.pk_org =e.pk_salesorg
left JOIN bd_materialstock mater ON mater.pk_material = inv.pk_material
WHERE	NVL (inv.dr, 0) = 0 AND NVL (invprod.dr, 0) = 0
AND inv.PK_MARASSTFRAME IN (
	SELECT PK_MARASSTFRAME FROM bd_marasstframe where code='06'
)
AND NVL (MATER.dr, 0) = 0
AND NVL (doc.dr, 0) = 0
ORDER BY invcode;
--------------------------------------------------------
--  DDL for View VIEW_BPM_MTCHDA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_MTCHDA (PK_ORG, PK_MARBASCLASS, CLSCODE, CLSNAME, PK_MATERIAL, CODE, NAME, MATERIALSPEC, MATERIALTYPE, PK_MEASDOC, MEANAME, CMATERIALVID, PK_STORDOC, STORENAME, NONHANDNUM) AS 
  select ma.pk_org,cls.pk_marbasclass,cls.code as clscode,cls.name as clsname,ma.pk_material,ma.code,ma.name,
ma.materialspec ,ma.materialtype,ma.pk_measdoc,mea.name as meaname,
'' as cmaterialvid,'' as pk_stordoc,'' as storename,'0' as nonhandnum
from bd_material ma 
left join bd_marbasclass cls on ma.pk_marbasclass=cls.pk_marbasclass
left join bd_measdoc mea on ma.pk_measdoc=mea.pk_measdoc
where cls.code like '03%';
--------------------------------------------------------
--  DDL for View VIEW_BPM_ORDERTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_ORDERTYPE (NAME, CODE, PK_DEFDOC) AS 
  select b.name,b.code,b.PK_DEFDOC from bd_defdoc b left join bd_defdoclist a on a.pk_defdoclist=b.pk_defdoclist where a.code='DDLB';
--------------------------------------------------------
--  DDL for View VIEW_BPM_PACKAGESTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PACKAGESTYPE (PK_MATERIAL, NAME, CODE, PK_DEFDOC) AS 
  select b.cmaterialid pk_material,c.name,c.code,c.pk_defdoc from cm_allocfac a 
join cm_allocfac_b b on a.callocfacid =b.callocfacid
join bd_defdoc c on b.VBFREE1=c.pk_defdoc --自定义档案
join bd_material_v d on d.pk_source=b.cmaterialid --物料基本信息表
 where a.vcode='BZGG' and c.enablestate=2
 order by c.code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_PAYABLEBILL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PAYABLEBILL (TYPE, BILLNO, PK_FIORG, PK_SUPPLIER, SUP_NAME, SUP_CODE, LOCAL_MONEY_CR, CURR_NAME, CURR_CODE, RATE, PSN_NAME, PK_PSNDOC, PSN_CODE, DEPT_NAME, DEPT_CODE, PK_DEPT, SUMMARYNAME) AS 
  SELECT 'DXLFK' AS TYPE,
    bill.billno,
    bill.pk_fiorg ,
    ----------供应商
    SUP.pk_supplier,
    SUP. NAME AS sup_name,
    SUP.code  AS sup_code,
    -----------------
    ITEM.local_money_cr,
    --组织本币金额
    --币种
    curr. NAME AS curr_name,
    CURr.code  AS curr_code,
    item.rate,
    --业务员及部门
    psn. NAME AS psn_name,
    psn.pk_psndoc,
    psn.code   AS psn_code,
    dept. NAME AS dept_name,
    dept.code  AS dept_code,
    dept.pk_dept,
    --摘要
    summ.summaryname
  FROM ap_payablebill bill
  LEFT JOIN ap_payableitem item
  ON BILL.PK_PAYABLEBILL = ITEM.PK_PAYABLEBILL
  LEFT JOIN bd_supplier sup
  ON sup.pk_supplier = item.supplier
  LEFT JOIN bd_currtype curr
  ON CURR.pk_currtype = item.pk_currtype
  LEFT JOIN bd_psndoc psn
  ON psn.pk_psndoc = item.pk_psndoc
  LEFT JOIN org_dept dept
  ON item.pk_deptid = dept.pk_dept
  LEFT JOIN fipub_summary summ
  ON summ.pk_summary      = item.scomment
  WHERE ITEM.src_billtype = '50'
  AND NVL(bill.dr,0)      =0
  AND NVL(sup.dr,0)       =0
  AND NVL(curr.dr,0)      =0
  AND NVL(psn.dr,0)       =0
  And Nvl(Dept.Dr,0)      =0
  AND nvl(summ.dr,0)=0
  UNION ALL
  SELECT 'QCLFK' AS TYPE,
    bill.billno,
    bill.pk_fiorg ,
    ----------供应商
    SUP.pk_supplier,
    SUP. NAME AS sup_name,
    SUP.code  AS sup_code,
    -----------------
    ITEM.local_money_cr,
    --组织本币金额
    --币种
    curr. NAME AS curr_name,
    CURr.code  AS curr_code,
    item.rate,
    --业务员及部门
    psn. NAME AS psn_name,
    psn.pk_psndoc,
    psn.code   AS psn_code,
    dept. NAME AS dept_name,
    dept.code  AS dept_code,
    dept.pk_dept,
    --摘要
    summ.summaryname
  FROM ap_payablebill bill
  LEFT JOIN ap_payableitem item
  ON BILL.PK_PAYABLEBILL = ITEM.PK_PAYABLEBILL
  LEFT JOIN bd_supplier sup
  ON sup.pk_supplier = item.supplier
  LEFT JOIN bd_currtype curr
  ON CURR.pk_currtype = item.pk_currtype
  LEFT JOIN bd_psndoc psn
  ON psn.pk_psndoc = item.pk_psndoc
  LEFT JOIN org_dept dept
  ON item.pk_deptid = dept.pk_dept
  LEFT JOIN fipub_summary summ
  ON summ.pk_summary = item.scomment
  WHERE Bill.Isinit  = 'Y'
  AND NVL(bill.dr,0)      =0
  AND NVL(sup.dr,0)       =0
  AND NVL(curr.dr,0)      =0
  AND NVL(psn.dr,0)       =0
  And Nvl(Dept.Dr,0)      =0
  AND nvl(summ.dr,0)=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_PAYMENTPLAN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PAYMENTPLAN (CROWNO, VBILLCODE, SUPPLIER_NAME, SUPPLIER_CODE, PK_SUPPLIER, IACCOUNTTERMNO, PAYPERIOD_NAME, FEFFDATETYPE, DBEGINDATE, IITERMDAYS, DENDDATE, BPREFLAG, NRATE, NORIGMNY, NACCUMPAYAPPORGMNY, NACCUMPAYORGMNY, PK_ORDER_PAYPLAN, PK_ORDER, PK_PAYMENTCH, PK_PAYTERM, ORG_NAME, PK_FINANCEORG, ORG_CODE) AS 
  SELECT P .Crowno Crowno,
    --序号,
    Po_Order.Vbillcode Vbillcode,
    --订单编号,
    Bd_Supplier. NAME Supplier_Name,
    --供应商,
    Bd_Supplier.Code Supplier_Code,
    --供应商编码,
    BD_SUPPLIER. pk_supplier ,
    --供应商主键
    P .Iaccounttermno Iaccounttermno,
    --账期号,
    Bd_Payperiod. NAME Payperiod_Name,
    --起算依据,
    p.feffdatetype ,
    --起算依据主键
    P .Dbegindate Dbegindate,
    --起算日期,
    P .Iitermdays Iitermdays,
    --账期天数,
    P .Denddate Denddate,
    --期账到期日,
    P .Bpreflag Bpreflag,
    --预付款,
    P .Nrate Nrate,
    --比例,
    P .Norigmny Norigmny,
    --原币金额,
    P .Naccumpayapporgmny Naccumpayapporgmny,
    --累计付款申请金额,
    P .Naccumpayorgmny Naccumpayorgmny,
    --累计付款金额,
    P .Pk_Order_Payplan Pk_Order_Payplan,
    --付款计划主键
    po_order.pk_order ,
    --采购订单主键
    p.pk_paymentch,
    --付款协议账期主键
    p.pk_payterm ,
    --付款协议
    Org_Financeorg. NAME Org_Name,
    --组织
    Org_Financeorg.Pk_Financeorg Pk_Financeorg,
    org_financeorg.code org_code
  FROM po_order_payplan P
  LEFT OUTER JOIN po_order po_order
  ON P .pk_order = po_order.pk_order
  LEFT OUTER JOIN bd_payperiod bd_payperiod
  ON P .feffdatetype = bd_payperiod.pk_payperiod
  LEFT OUTER JOIN org_financeorg org_financeorg
  ON P .pk_financeorg = org_financeorg.pk_financeorg
  LEFT OUTER JOIN bd_supplier bd_supplier
  ON po_order.pk_supplier = bd_supplier.pk_supplier
  WHERE P.Dbegindate     IS NOT NULL;
--------------------------------------------------------
--  DDL for View VIEW_BPM_POORDER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_POORDER (VBILLCODE, BARRIVECLOSE, BBORROWPUR, BINVOICECLOSE, BLARGESS, BPAYCLOSE, BRECEIVEPLAN, BSTOCKCLOSE, BTRANSCLOSED, BTRIATRADEFLAG, CASSCUSTID, CASTUNITID, CCONTRACTID, CCONTRACTROWID, CCURRENCYID, CDESTIAREAID, CDESTICOUNTRYID, CDEVADDRID, CDEVAREAID, CECBILLBID, CECBILLID, CECTYPECODE, CFFILEID, CFIRSTBID, CFIRSTID, CFIRSTTYPECODE, CHANDLER, CORIGAREAID, CORIGCOUNTRYID, CORIGCURRENCYID, CPRAYBILLBID, CPRAYBILLCODE, CPRAYBILLHID, CPRAYBILLROWNO, CPRAYTYPECODE, CPRICEAUDIT_BB1ID, CPRICEAUDIT_BID, CPRICEAUDITID, CPRODUCTORID, CPROJECTID, CPROJECTTASKID, CQPBASESCHEMEID, CQTUNITID, CQUALITYLEVELID, CRECECOUNTRYID, CROWNO, CSENDCOUNTRYID, CSOURCEBID, CSOURCEID, CSOURCETYPECODE, CTAXCODEID, CTAXCOUNTRYID, CUNITID, CVENDDEVADDRID, CVENDDEVAREAID, DBILLDATE, DCORRECTDATE, DPLANARRVDATE, DR, FBUYSELLFLAG, FISACTIVE, FTAXTYPEFLAG, NACCCANCELINVMNY, NACCUMARRVNUM, NACCUMDEVNUM, NACCUMINVOICEMNY, NACCUMINVOICENUM, NACCUMPICKUPNUM, NACCUMRPNUM, NACCUMSTORENUM, NACCUMWASTNUM, NASTNUM, NBACKARRVNUM, NBACKSTORENUM, NCALCOSTMNY, NCALTAXMNY, NEXCHANGERATE, NFEEMNY, NGLOBALEXCHGRATE, NGLOBALMNY, NGLOBALTAXMNY, NGROUPEXCHGRATE, NGROUPMNY, NGROUPTAXMNY, NITEMDISCOUNTRATE, NMNY, NNETPRICE, NNOSUBTAX, NNOSUBTAXRATE, NNUM, NORIGMNY, NORIGNETPRICE, NORIGPRICE, NORIGTAXMNY, NORIGTAXNETPRICE, NORIGTAXPRICE, NPACKNUM, NPRICE, NQTNETPRICE, NQTORIGNETPRICE, NQTORIGPRICE, NQTORIGTAXNETPRC, NQTORIGTAXPRICE, NQTPRICE, NQTTAXNETPRICE, NQTTAXPRICE, NQTUNITNUM, NSUPRSNUM, NTAX, NTAXMNY, NTAXNETPRICE, NTAXPRICE, NTAXRATE, NVOLUMN, NWEIGHT, PK_APFINANCEORG, PK_APFINANCEORG_V, PK_APLIABCENTER, PK_APLIABCENTER_V, PK_ARRLIABCENTER, PK_ARRLIABCENTER_V, PK_ARRVSTOORG, PK_ARRVSTOORG_V, PK_BATCHCODE, PK_DISCOUNT, PK_FLOWSTOCKORG, PK_FLOWSTOCKORG_V, PK_GROUP, PK_MATERIAL, PK_ORDER, PK_ORDER_B, PK_ORG, PK_ORG_V, PK_PSFINANCEORG, PK_PSFINANCEORG_V, PK_RECEIVEADDRESS, PK_RECVSTORDOC, PK_REQCORP, PK_REQDEPT, PK_REQDEPT_V, PK_REQSTOORG, PK_REQSTOORG_V, PK_REQSTORDOC, PK_SRCMATERIAL, PK_SRCORDER_B, PK_SUPPLIER, TS, VBATCHCODE, VBDEF1, VBDEF10, VBDEF11, VBDEF12, VBDEF13, VBDEF14, VBDEF15, VBDEF16, VBDEF17, VBDEF18, VBDEF19, VBDEF2, VBDEF20, VBDEF3, VBDEF4, VBDEF5, VBDEF6, VBDEF7, VBDEF8, VBDEF9, VBMEMO, VCHANGERATE, VCONTRACTCODE, VECBILLCODE, VFIRSTCODE, VFIRSTROWNO, VFIRSTTRANTYPE, VFREE1, VFREE10, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VPRICEAUDITCODE, VQTUNITRATE, VSOURCECODE, VSOURCEROWNO, VSOURCETRANTYPE, VVENDDEVADDR, VVENDINVENTORYCODE, VVENDINVENTORYNAME) AS 
  (select r.vbillcode, b.BARRIVECLOSE,b.BBORROWPUR,b.BINVOICECLOSE,b.BLARGESS,b.BPAYCLOSE,b.BRECEIVEPLAN,b.BSTOCKCLOSE,b.BTRANSCLOSED,b.BTRIATRADEFLAG,b.CASSCUSTID,b.CASTUNITID,b.CCONTRACTID,b.CCONTRACTROWID,b.CCURRENCYID,b.CDESTIAREAID,b.CDESTICOUNTRYID,b.CDEVADDRID,b.CDEVAREAID,b.CECBILLBID,b.CECBILLID,b.CECTYPECODE,b.CFFILEID,b.CFIRSTBID,b.CFIRSTID,b.CFIRSTTYPECODE,b.CHANDLER,b.CORIGAREAID,b.CORIGCOUNTRYID,b.CORIGCURRENCYID,b.CPRAYBILLBID,b.CPRAYBILLCODE,b.CPRAYBILLHID,b.CPRAYBILLROWNO,b.CPRAYTYPECODE,b.CPRICEAUDIT_BB1ID,b.CPRICEAUDIT_BID,b.CPRICEAUDITID,b.CPRODUCTORID,b.CPROJECTID,b.CPROJECTTASKID,b.CQPBASESCHEMEID,b.CQTUNITID,b.CQUALITYLEVELID,b.CRECECOUNTRYID,b.CROWNO,b.CSENDCOUNTRYID,b.CSOURCEBID,b.CSOURCEID,b.CSOURCETYPECODE,b.CTAXCODEID,b.CTAXCOUNTRYID,b.CUNITID,b.CVENDDEVADDRID,b.CVENDDEVAREAID,b.DBILLDATE,b.DCORRECTDATE,b.DPLANARRVDATE,b.DR,b.FBUYSELLFLAG,b.FISACTIVE,b.FTAXTYPEFLAG,b.NACCCANCELINVMNY,b.NACCUMARRVNUM,b.NACCUMDEVNUM,b.NACCUMINVOICEMNY,b.NACCUMINVOICENUM,b.NACCUMPICKUPNUM,b.NACCUMRPNUM,b.NACCUMSTORENUM,b.NACCUMWASTNUM,b.NASTNUM,b.NBACKARRVNUM,b.NBACKSTORENUM,b.NCALCOSTMNY,b.NCALTAXMNY,b.NEXCHANGERATE,b.NFEEMNY,b.NGLOBALEXCHGRATE,b.NGLOBALMNY,b.NGLOBALTAXMNY,b.NGROUPEXCHGRATE,b.NGROUPMNY,b.NGROUPTAXMNY,b.NITEMDISCOUNTRATE,b.NMNY,b.NNETPRICE,b.NNOSUBTAX,b.NNOSUBTAXRATE,b.NNUM,b.NORIGMNY,b.NORIGNETPRICE,b.NORIGPRICE,b.NORIGTAXMNY,b.NORIGTAXNETPRICE,b.NORIGTAXPRICE,b.NPACKNUM,b.NPRICE,b.NQTNETPRICE,b.NQTORIGNETPRICE,b.NQTORIGPRICE,b.NQTORIGTAXNETPRC,b.NQTORIGTAXPRICE,b.NQTPRICE,b.NQTTAXNETPRICE,b.NQTTAXPRICE,b.NQTUNITNUM,b.NSUPRSNUM,b.NTAX,b.NTAXMNY,b.NTAXNETPRICE,b.NTAXPRICE,b.NTAXRATE,b.NVOLUMN,b.NWEIGHT,b.PK_APFINANCEORG,b.PK_APFINANCEORG_V,b.PK_APLIABCENTER,b.PK_APLIABCENTER_V,b.PK_ARRLIABCENTER,b.PK_ARRLIABCENTER_V,b.PK_ARRVSTOORG,b.PK_ARRVSTOORG_V,b.PK_BATCHCODE,b.PK_DISCOUNT,b.PK_FLOWSTOCKORG,b.PK_FLOWSTOCKORG_V,b.PK_GROUP,b.PK_MATERIAL,b.PK_ORDER,b.PK_ORDER_B,b.PK_ORG,b.PK_ORG_V,b.PK_PSFINANCEORG,b.PK_PSFINANCEORG_V,b.PK_RECEIVEADDRESS,b.PK_RECVSTORDOC,b.PK_REQCORP,b.PK_REQDEPT,b.PK_REQDEPT_V,b.PK_REQSTOORG,b.PK_REQSTOORG_V,b.PK_REQSTORDOC,b.PK_SRCMATERIAL,b.PK_SRCORDER_B,b.PK_SUPPLIER,b.TS,b.VBATCHCODE,b.VBDEF1,b.VBDEF10,b.VBDEF11,b.VBDEF12,b.VBDEF13,b.VBDEF14,b.VBDEF15,b.VBDEF16,b.VBDEF17,b.VBDEF18,b.VBDEF19,b.VBDEF2,b.VBDEF20,b.VBDEF3,b.VBDEF4,b.VBDEF5,b.VBDEF6,b.VBDEF7,b.VBDEF8,b.VBDEF9,b.VBMEMO,b.VCHANGERATE,b.VCONTRACTCODE,b.VECBILLCODE,b.VFIRSTCODE,b.VFIRSTROWNO,b.VFIRSTTRANTYPE,b.VFREE1,b.VFREE10,b.VFREE2,b.VFREE3,b.VFREE4,b.VFREE5,b.VFREE6,b.VFREE7,b.VFREE8,b.VFREE9,b.VPRICEAUDITCODE,b.VQTUNITRATE,b.VSOURCECODE,b.VSOURCEROWNO,b.VSOURCETRANTYPE,b.VVENDDEVADDR,b.VVENDINVENTORYCODE,b.VVENDINVENTORYNAME from po_order r join po_order_b b on r.pk_order = b.pk_order  where   nvl(r.forderstatus,0) = 3 and nvl(r.dr,0) = 0 and nvl(b.dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PORTDOC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PORTDOC (PID, INNERCODE, NAME, PK_DEFDOC) AS 
  select b.PID,b.INNERCODE,b.NAME,b.PK_DEFDOC from bd_defdoc b left join bd_defdoclist a on a.pk_defdoclist=b.pk_defdoclist where a.code='Harbor' and (b.PID!='~' or b.pid!=null);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PRAYBILL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PRAYBILL (VBILLCODE, BCANPURCHASEORGEDIT, BISARRANGE, BISGENSAORDER, BPUBLISHTOEC, BROWCLOSE, CASSCUSTID, CASTUNITID, CFFILEID, CFIRSTBID, CFIRSTID, CFIRSTTYPECODE, CORDERTRANTYPECODE, CPRODUCTORID, CPROJECTID, CPROJECTTASKID, CROWNO, CSOURCEBID, CSOURCEID, CSOURCETYPECODE, CUNITID, DBILLDATE, DR, DREQDATE, DSUGGESTDATE, NACCUMULATENUM, NASTNUM, NGENCT, NNUM, NPRICEAUDITBILL, NQUOTEBILL, NTAXMNY, NTAXPRICE, PK_BATCHCODE, PK_CUSTOMER, PK_EMPLOYEE, PK_GROUP, PK_MATERIAL, PK_ORG, PK_ORG_V, PK_PRAYBILL, PK_PRAYBILL_B, PK_PRODUCT, PK_PRODUCT_V, PK_PURCHASEORG, PK_PURCHASEORG_V, PK_REQDEPT, PK_REQDEPT_V, PK_REQSTOORG, PK_REQSTOORG_V, PK_REQSTOR, PK_SRCMATERIAL, PK_SUGGESTSUPPLIER, TS, VBATCHCODE, VBDEF1, VBDEF10, VBDEF11, VBDEF12, VBDEF13, VBDEF14, VBDEF15, VBDEF16, VBDEF17, VBDEF18, VBDEF19, VBDEF2, VBDEF20, VBDEF3, VBDEF4, VBDEF5, VBDEF6, VBDEF7, VBDEF8, VBDEF9, VBMEMO, VCHANGERATE, VFIRSTCODE, VFIRSTROWNO, VFIRSTTRANTYPE, VFREE1, VFREE10, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VSOURCECODE, VSOURCEROWNO, VSRCTRANTYPECODE) AS 
  (select r.vbillcode  ,b.BCANPURCHASEORGEDIT,b.BISARRANGE,b.BISGENSAORDER,b.BPUBLISHTOEC,b.BROWCLOSE,b.CASSCUSTID,b.CASTUNITID,b.CFFILEID,b.CFIRSTBID,b.CFIRSTID,b.CFIRSTTYPECODE,b.CORDERTRANTYPECODE,b.CPRODUCTORID,b.CPROJECTID,b.CPROJECTTASKID,b.CROWNO,b.CSOURCEBID,b.CSOURCEID,b.CSOURCETYPECODE,b.CUNITID,b.DBILLDATE,b.DR,b.DREQDATE,b.DSUGGESTDATE,b.NACCUMULATENUM,b.NASTNUM,b.NGENCT,b.NNUM,b.NPRICEAUDITBILL,b.NQUOTEBILL,b.NTAXMNY,b.NTAXPRICE,b.PK_BATCHCODE,b.PK_CUSTOMER,b.PK_EMPLOYEE,b.PK_GROUP,b.PK_MATERIAL,b.PK_ORG,b.PK_ORG_V,b.PK_PRAYBILL,b.PK_PRAYBILL_B,b.PK_PRODUCT,b.PK_PRODUCT_V,b.PK_PURCHASEORG,b.PK_PURCHASEORG_V,b.PK_REQDEPT,b.PK_REQDEPT_V,b.PK_REQSTOORG,b.PK_REQSTOORG_V,b.PK_REQSTOR,b.PK_SRCMATERIAL,b.PK_SUGGESTSUPPLIER,b.TS,b.VBATCHCODE,b.VBDEF1,b.VBDEF10,b.VBDEF11,b.VBDEF12,b.VBDEF13,b.VBDEF14,b.VBDEF15,b.VBDEF16,b.VBDEF17,b.VBDEF18,b.VBDEF19,b.VBDEF2,b.VBDEF20,b.VBDEF3,b.VBDEF4,b.VBDEF5,b.VBDEF6,b.VBDEF7,b.VBDEF8,b.VBDEF9,b.VBMEMO,b.VCHANGERATE,b.VFIRSTCODE,b.VFIRSTROWNO,b.VFIRSTTRANTYPE,b.VFREE1,b.VFREE10,b.VFREE2,b.VFREE3,b.VFREE4,b.VFREE5,b.VFREE6,b.VFREE7,b.VFREE8,b.VFREE9,b.VSOURCECODE,b.VSOURCEROWNO,b.VSRCTRANTYPECODE from  po_praybill r join po_praybill_b b  on r.pk_praybill =b.pk_praybill  where   nvl(r.fbillstatus  ,0) = 3 and nvl(r.dr,0) = 0 and nvl(b.dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PROJECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PROJECT (PH_PK, PH_CODE, PH_NAME, ORG_PK, ORG_CODE, ORG_NAME) AS 
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
--  DDL for View VIEW_BPM_PSN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PSN (ORG_PK, ORG_CODE, ORG_NAME, PSN_PK, PSN_CODE, PSN_NAME, CT_BEGINDATE, CT_ENDDATE, PSNCL_PK, PSNCL_CODE, PSNCL_NAME, PJOB_ORG2_PK, PJOB_ORG2_CODE, PJOB_ORG2_NAME, PJOB_PK_DEPT, PJOB_DEPT_CODE, PJOB_DEPT_NAME, PJOB_ISMAINJOB, PJOB_INDUTYDATE, JOB_PK, JOB_CODE, JOB_NAME, JOBLEVEL_PK, JOBLEVEL_CODE, JOBLEVEL_NAME, POST_PK, POST_CODE, POST_NAME, TRIAL_FLAG, TRIAL_TYPE, PK_PSNJOB, PK_PSNORG) AS 
  (

SELECT
  --所在组织
  org.pk_org AS org_pk,
  org.code   AS org_code,
  org.name   AS org_name,
  --人员主键
  psn.pk_psndoc AS psn_pk,
  --基本信息
  psn.code AS psn_code,
  psn.name AS psn_name,
  
  --合同信息
  ctrt.begindate as ct_begindate,
  ctrt.enddate  as ct_enddate,
  --人员类别
  psncl.pk_psncl  as psncl_pk,
    psncl.code  as psncl_code,
  psncl.name   as psncl_name ,

  --任职组织
  org2.pk_org AS pjob_org2_pk,
  org2.code   AS pjob_org2_code,
  org2.name   AS pjob_org2_name,
 
  -- 任职 pk_dept 部门
  pjob.pk_dept AS pjob_pk_dept,
  dept.code    AS pjob_dept_code,
  dept.name    AS pjob_dept_name,
   --主职
  pjob.ismainjob AS pjob_ismainjob ,
  --入职日期
  pjob.indutydate as pjob_indutydate,
  
  --职务
  pjob.pk_job as job_pk,
  ojob.jobcode as job_code,
  ojob.jobname  as job_name,
  --职级
  joblevel.pk_joblevel as joblevel_pk,
  joblevel.code as joblevel_code,
    joblevel.name as joblevel_name,

  --岗位信息
  pjob. pk_post AS post_pk,
  post.postcode AS post_code,
  post.postname AS post_name,
  
  --试用情况
  hjob.trial_flag as trial_flag,
  hjob. trial_type as  trial_type,
  
  hjob.PK_PSNJOB,
  hjob.PK_PSNORG
  
FROM bd_psndoc psn
LEFT JOIN org_orgs org
ON psn.pk_org = org.pk_org

-- hi_psndoc_ctrt 合同信息
left join hi_psndoc_ctrt ctrt on psn.pk_psndoc = ctrt.pk_psndoc

LEFT JOIN bd_psnjob pjob
ON psn.pk_psndoc = pjob.pk_psndoc

LEFT JOIN hi_psnjob hjob
ON psn.pk_psndoc = hjob.pk_psndoc

LEFT JOIN bd_psncl psncl
ON pjob.pk_psncl = psncl.pk_psncl 

LEFT JOIN org_orgs org2
ON pjob.pk_org = org2.pk_org


LEFT JOIN org_dept dept
ON pjob.pk_dept = dept.pk_dept

LEFT JOIN om_job ojob
ON pjob.pk_job = ojob.pk_job

left join om_levelrelation levlra on ojob.pk_job= levlra.pk_job

left join om_joblevel joblevel on levlra.pk_joblevel = joblevel.pk_joblevel

LEFT JOIN om_post post
ON pjob. pk_post = post.pk_post

where nvl(psn.dr,0)=0 and nvl(pjob.dr,0)=0 
);
--------------------------------------------------------
--  DDL for View VIEW_BPM_PSNLZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PSNLZ (ORG_PK, ORG_CODE, ORG_NAME, PSN_PK, PSN_CODE, PSN_NAME, CT_BEGINDATE, CT_ENDDATE, PSNCL_PK, PSNCL_CODE, PSNCL_NAME, PJOB_ORG2_PK, PJOB_ORG2_CODE, PJOB_ORG2_NAME, PJOB_PK_DEPT, PJOB_DEPT_CODE, PJOB_DEPT_NAME, PJOB_ISMAINJOB, PJOB_INDUTYDATE, ENDDATE, JOB_PK, JOB_CODE, JOB_NAME, JOBLEVEL_PK, JOBLEVEL_CODE, JOBLEVEL_NAME, POST_PK, POST_CODE, POST_NAME, TRIAL_FLAG, TRIAL_TYPE, PK_PSNJOB, PK_PSNORG) AS 
  (SELECT
    --所在组织
    org.pk_org AS org_pk,
    org.code   AS org_code,
    org.name   AS org_name,
    --人员主键
    psn.pk_psndoc AS psn_pk,
    --基本信息
    psn.code AS psn_code,
    psn.name AS psn_name,
    --合同信息
    ctrt.begindate AS ct_begindate,
    ctrt.enddate   AS ct_enddate,
    --人员类别
    psncl.pk_psncl AS psncl_pk,
    psncl.code     AS psncl_code,
    psncl.name     AS psncl_name ,
    --任职组织
    org2.pk_org AS pjob_org2_pk,
    org2.code   AS pjob_org2_code,
    org2.name   AS pjob_org2_name,
    -- 任职 pk_dept 部门
    pjob.pk_dept AS pjob_pk_dept,
    dept.code    AS pjob_dept_code,
    dept.name    AS pjob_dept_name,
    --主职
    pjob.ismainjob AS pjob_ismainjob ,
    --入职日期
    pjob.begindate AS pjob_indutydate,
    pjob.enddate,
    --职务
    pjob.pk_job  AS job_pk,
    ojob.jobcode AS job_code,
    ojob.jobname AS job_name,
    --职级
    joblevel.pk_joblevel AS joblevel_pk,
    joblevel.code        AS joblevel_code,
    joblevel.name        AS joblevel_name,
    --岗位信息
    pjob. pk_post AS post_pk,
    post.postcode AS post_code,
    post.postname AS post_name,
    pjob.trial_flag,
    pjob.trial_type,
    pjob.pk_psnjob,
    pjob.pk_psnorg
  FROM bd_psndoc psn
  LEFT JOIN org_orgs org
  ON psn.pk_org = org.pk_org
    -- hi_psndoc_ctrt 合同信息
  LEFT JOIN hi_psndoc_ctrt ctrt
  ON psn.pk_psndoc = ctrt.pk_psndoc
  LEFT JOIN hi_psnjob pjob
  ON psn.pk_psndoc = pjob.pk_psndoc
  LEFT JOIN bd_psncl psncl
  ON pjob.pk_psncl = psncl.pk_psncl
  LEFT JOIN org_orgs org2
  ON pjob.pk_org = org2.pk_org
  LEFT JOIN org_dept dept
  ON pjob.pk_dept = dept.pk_dept
  LEFT JOIN om_job ojob
  ON pjob.pk_job = ojob.pk_job
  LEFT JOIN om_levelrelation levlra
  ON ojob.pk_job= levlra.pk_job
  LEFT JOIN om_joblevel joblevel
  ON levlra.pk_joblevel = joblevel.pk_joblevel
  LEFT JOIN om_post post
  ON pjob. pk_post   = post.pk_post
  WHERE NVL(psn.dr,0)=0
  AND NVL(pjob.dr,0) =0
  AND ismainjob      ='Y'
  AND pjob.enddate  IS NULL
  and ctrt.lastflag='Y'
  );
--------------------------------------------------------
--  DDL for View VIEW_BPM_PSNTZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PSNTZ (ORG_PK, ORG_CODE, ORG_NAME, PSN_PK, PK_DEGREE, EDUCATION, LASTEDUCATION, PSN_CODE, PSN_NAME, ENABLESTATE, CT_BEGINDATE, CT_ENDDATE, PSNCL_PK, PSNCL_CODE, PSNCL_NAME, PJOB_ORG2_PK, PJOB_ORG2_CODE, PJOB_ORG2_NAME, PJOB_PK_DEPT, PJOB_DEPT_CODE, PJOB_DEPT_NAME, PJOB_ISMAINJOB, PJOB_INDUTYDATE, ENDDATE, JOB_PK, JOB_CODE, JOB_NAME, JOBLEVEL_PK, JOBLEVEL_CODE, JOBLEVEL_NAME, POST_PK, POST_CODE, POST_NAME, PK_PSNJOB, PK_PSNORG, PK_POSTSERIES, TRIAL_FLAG, SERIES, PK_JOBRANK, JOBRANKCODE, JOBRANKNAME) AS 
  SELECT
     DISTINCT
    --所在组织
    org.pk_org AS org_pk,
    org.code   AS org_code,
    org.name   AS org_name,
    --人员主键
    psn.pk_psndoc AS psn_pk,
     --学历
    psnedu.PK_DEGREE ,psnedu.education,psnedu.LASTEDUCATION,
    --基本信息
    psn.code AS psn_code,
    psn.name AS psn_name,
    psn.enablestate,
    --合同信息
    ctrt.begindate AS ct_begindate,
    ctrt.enddate   AS ct_enddate,
    --人员类别
    psncl.pk_psncl AS psncl_pk,
    psncl.code     AS psncl_code,
    psncl.name     AS psncl_name ,
    --任职组织
    org2.pk_org AS pjob_org2_pk,
    org2.code   AS pjob_org2_code,
    org2.name   AS pjob_org2_name,
    -- 任职 pk_dept 部门
    pjob.pk_dept AS pjob_pk_dept,
    dept.code    AS pjob_dept_code,
    dept.name    AS pjob_dept_name,
    --主职
    pjob.ismainjob AS pjob_ismainjob ,
    --入职日期
    pjob.begindate AS pjob_indutydate,
    pjob.enddate,
    --职务
    pjob.pk_job  AS job_pk,
    ojob.jobcode AS job_code,
    ojob.jobname AS job_name,
    --职级
    joblevel.pk_joblevel AS joblevel_pk,
    joblevel.code        AS joblevel_code,
    joblevel.name        AS joblevel_name,
    --岗位信息
    pjob. pk_post AS post_pk,
    post.postcode AS post_code,
    post.postname AS post_name,
    pjob.pk_psnjob,
    pjob.pk_psnorg,
    pjob.pk_postseries,
    pjob.trial_flag,
    pjob.series,
    --职等
    jrank.pk_jobrank,
    jrank.jobrankcode ,
    jrank.jobrankname
  FROM bd_psndoc psn
  LEFT JOIN org_orgs org
  ON psn.pk_org = org.pk_org
    -- hi_psndoc_ctrt 合同信息
  LEFT JOIN hi_psndoc_ctrt ctrt
  ON psn.pk_psndoc = ctrt.pk_psndoc
  LEFT JOIN hi_psnjob pjob
  ON psn.pk_psndoc = pjob.pk_psndoc
  LEFT JOIN bd_psncl psncl
  ON pjob.pk_psncl = psncl.pk_psncl
  LEFT JOIN org_orgs org2
  ON pjob.pk_org = org2.pk_org
  LEFT JOIN org_dept dept
  ON pjob.pk_dept = dept.pk_dept
  LEFT JOIN om_job ojob
  ON pjob.pk_job = ojob.pk_job
  LEFT JOIN om_levelrelation levlra
  ON ojob.pk_job= levlra.pk_job
  LEFT JOIN om_joblevel joblevel
  ON levlra.pk_joblevel = joblevel.pk_joblevel
  LEFT JOIN om_post post
  ON pjob. pk_post = post.pk_post
  LEFT JOIN om_jobrank jrank
  ON levlra.jobrank   =jrank.pk_jobrank
  left join hi_psndoc_edu psnedu on psn.PK_DEGREE=psnedu.PK_DEGREE
  WHERE NVL(psn.dr,0) =0
  AND NVL(pjob.dr,0)  =0
  AND pjob.enddate   IS NULL
  AND psn.enablestate!=3;
--------------------------------------------------------
--  DDL for View VIEW_BPM_PSNXL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PSNXL (CODE, NAME, MNECODE, PK_DEFDOC, PID) AS 
  SELECT DISTINCT code,
  name,
  mnecode,
  pk_defdoc,
  pid
FROM bd_defdoc
WHERE 11          = 11
AND ( enablestate = 2 )
and PK_DEFDOCLIST='1001Z71000000000GP4V';
--------------------------------------------------------
--  DDL for View VIEW_BPM_PSNZZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PSNZZ (ORG_PK, ORG_CODE, ORG_NAME, PSN_PK, PDEGREE, PEDUCATION, PLASTEDUCATION, PSN_CODE, PSN_NAME, PSN_WORKDATE, PSN_DEGREE, CT_BEGINDATE, CT_ENDDATE, PSNCL_PK, PSNCL_CODE, PSNCL_NAME, PJOB_ORG2_PK, PJOB_ORG2_CODE, PJOB_ORG2_NAME, PJOB_PK_DEPT, PJOB_DEPT_CODE, PJOB_DEPT_NAME, PJOB_ISMAINJOB, PJOB_INDUTYDATE, BEGINDATE, ENDDATE, JOB_PK, JOB_CODE, JOB_NAME, JOBLEVEL_PK, JOBLEVEL_CODE, JOBLEVEL_NAME, POST_PK, POST_CODE, POST_NAME, TRIAL_FLAG, TRIAL_TYPE, PK_PSNJOB, PK_PSNORG) AS 
  SELECT
    
   --所在组织
  org.pk_org AS org_pk,org.code   AS org_code,org.name   AS org_name,
  --人员主键
  psn.pk_psndoc AS psn_pk,
   --学历
  psnedu.PK_DEGREE as pdegree,psnedu.education as peducation,psnedu.LASTEDUCATION as plasteducation,
  --基本信息
  psn.code AS psn_code,psn.name AS psn_name, psn.JOINWORKDATE AS psn_workdate, psn.PK_DEGREE as psn_DEGREE,
  --合同信息
  ctrt.begindate as ct_begindate,ctrt.enddate  as ct_enddate,
  --人员类别
  psncl.pk_psncl  as psncl_pk,psncl.code  as psncl_code,psncl.name   as psncl_name ,
  --任职组织
  org2.pk_org AS pjob_org2_pk,org2.code   AS pjob_org2_code,org2.name   AS pjob_org2_name, 
  -- 任职 pk_dept 部门
  pjob.pk_dept AS pjob_pk_dept,dept.code    AS pjob_dept_code,dept.name    AS pjob_dept_name,
  --主职
  pjob.ismainjob AS pjob_ismainjob ,
  --入职日期
  pjob.begindate as pjob_indutydate,  
  pjob.begindate as begindate,--入职开始
  pjob.enddate as enddate,--入职结束
  --职务
  pjob.pk_job as job_pk,ojob.jobcode as job_code,ojob.jobname  as job_name,
  --职级
  joblevel.pk_joblevel as joblevel_pk,joblevel.code as joblevel_code,joblevel.name as joblevel_name,
  --岗位信息
  pjob. pk_post AS post_pk,post.postcode AS post_code,post.postname AS post_name,
  pjob.trial_flag,pjob.trial_type,pjob.pk_psnjob,pjob.pk_psnorg

FROM bd_psndoc psn
left JOIN org_orgs org ON psn.pk_org = org.pk_org
left join hi_psndoc_ctrt ctrt on psn.pk_psndoc = ctrt.pk_psndoc-- hi_psndoc_ctrt 合同信息
LEFT JOIN hi_psnjob pjob ON psn.pk_psndoc = pjob.pk_psndoc
LEFT JOIN bd_psncl psncl ON pjob.pk_psncl = psncl.pk_psncl 
LEFT JOIN org_orgs org2 ON pjob.pk_org = org2.pk_org
LEFT JOIN org_dept dept ON pjob.pk_dept = dept.pk_dept
LEFT JOIN om_job ojob ON pjob.pk_job = ojob.pk_job
left join om_levelrelation levlra on ojob.pk_job= levlra.pk_job
left join om_joblevel joblevel on levlra.pk_joblevel = joblevel.pk_joblevel
LEFT JOIN om_post post ON pjob. pk_post = post.pk_post
left join hi_psndoc_edu psnedu on psn.PK_DEGREE=psnedu.PK_DEGREE
where nvl(psn.dr,0)=0 and nvl(pjob.dr,0)=0 
and ismainjob='Y' 
and pjob.trial_flag='Y';
--------------------------------------------------------
--  DDL for View VIEW_BPM_PURBILLTYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_PURBILLTYPE (ACCOUNTCLASS, BILLCODERULE, BILLSTYLE, BILLTYPENAME, BILLTYPENAME2, BILLTYPENAME3, BILLTYPENAME4, BILLTYPENAME5, BILLTYPENAME6, CANEXTENDTRANSACTION, CHECKCLASSNAME, CLASSNAME, COMP, COMPONENT, DATAFINDERCLZ, DEF1, DEF2, DEF3, DR, EMENDENUMCLASS, FORWARDBILLTYPE, ISACCOUNT, ISAPPROVEBILL, ISBIZFLOWBILL, ISEDITABLEPROPERTY, ISENABLEBUTTON, ISENABLETRANSTYPEBCR, ISLOCK, ISROOT, ISTRANSACTION, NCBRCODE, NODECODE, PARENTBILLTYPE, PK_BILLTYPECODE, PK_BILLTYPEID, PK_GROUP, PK_ORG, REFERCLASSNAME, SYSTEMCODE, TRANSTYPE_CLASS, TS, WEBNODECODE, WHERESTRING) AS 
  (select ACCOUNTCLASS,BILLCODERULE,BILLSTYLE,BILLTYPENAME,BILLTYPENAME2,BILLTYPENAME3,BILLTYPENAME4,BILLTYPENAME5,BILLTYPENAME6,CANEXTENDTRANSACTION,CHECKCLASSNAME,CLASSNAME,COMP,COMPONENT,DATAFINDERCLZ,DEF1,DEF2,DEF3,DR,EMENDENUMCLASS,FORWARDBILLTYPE,ISACCOUNT,ISAPPROVEBILL,ISBIZFLOWBILL,ISEDITABLEPROPERTY,ISENABLEBUTTON,ISENABLETRANSTYPEBCR,ISLOCK,ISROOT,ISTRANSACTION,NCBRCODE,NODECODE,PARENTBILLTYPE,PK_BILLTYPECODE,PK_BILLTYPEID,PK_GROUP,PK_ORG,REFERCLASSNAME,SYSTEMCODE,TRANSTYPE_CLASS,TS,WEBNODECODE,WHERESTRING from bd_billtype  where systemcode in('PO','PURP','MPP') and nvl(dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_REGISTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_REGISTE (ORG_PK, ORG_CODE, ORG_NAME, BILLNO_PK, FBMBILLNO, QIANFA_DATE, DAOQI_DATE, SHOUDAO_DATE, PAYBANK_PK, PAYBANK_CODE, PAYBANK_NAME) AS 
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
--  DDL for View VIEW_BPM_REGISTE_BPM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_REGISTE_BPM (单据状态, 出票日期, 到期日期, 收票日期, 户名, 名称, 账号, PK_REGISTER, 票据编号) AS 
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
--  DDL for View VIEW_BPM_REGISTER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_REGISTER (BASEINFOSTATUS, INVOICEDATE, ENDDATE, GATHERDATE, PK_REGISTER, FBMBILLNO, ACCNAME, NAME, ACCNUM, PK_BANKACCSUB) AS 
  SELECT F.Baseinfostatus,-- 单据状态,
  F.Invoicedate,        -- 出票日期 ,
  F.Enddate ,           --到期日期,
  F.Gatherdate ,        --收票日期,
  F.Pk_Register,
  F.Fbmbillno,-- 票据编号
  --银行账户
  A.Accname , --户名,
  A.Name ,    --名称,
  A.Accnum ,  --账号,
  a. pk_bankaccsub
FROM fbm_register f
LEFT OUTER JOIN bd_bankaccsub a
On F.Pk_Payacc       =A. Pk_Bankaccsub
Where Baseinfostatus In ('REGISTER','HAS_INVOICE')
And Nvl(A.Dr,0)=0
and nvl(f.dr,0)=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_REJECTBILL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_REJECTBILL (PK_ORG, CAPPLYTIME, VBILLCODE, VAPPLYBILLCODE, VREPORTBILLCODE, PK_DEPT, DEPT_CODE, DEPT_NAM, INV_CODE, INV_NAME, VBATCHCODE, NAPPLYNUM, NNUM, PK_REJECTBILL_B, PK_REJECTBILL, VCHECKITEMNAME, BACCORDED, VSTDVALUE1, VCHKVALUE) AS 
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
--  DDL for View VIEW_BPM_RESPPERSON
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_RESPPERSON (PSNPK, PSNCODE, PSNNAME, DEPPK, DEPCODE, DEPNAME) AS 
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
--  DDL for View VIEW_BPM_RYLB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_RYLB (CODE, NAME, PK_PSNCL, PARENT_ID) AS 
  select code, name, pk_psncl, parent_id from bd_psncl 
where 11 = 11 and ( enablestate = 2 ) and ( ( 1 = 1 ) ) order by code;
--------------------------------------------------------
--  DDL for View VIEW_BPM_SALETYPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_SALETYPE (TYPE, BILLTYPENAME, PK_BILLTYPECODE, PK_BILLTYPEID) AS 
  SELECT
	'ck' AS TYPE,
	billtypename,
	pk_billtypecode,
	pk_billtypeid
FROM
	bd_billtype
WHERE
	istransaction = 'Y'
AND NVL (islock, 'N') = 'N'
AND parentbilltype = '30'
AND PK_GROUP = '0001A110000000000DDM'
AND pk_billtypecode NOT IN (
	'30-01',
	'30-04'
)
Union All
	SELECT
		'nx' AS TYPE,
		billtypename,
		pk_billtypecode,
		pk_billtypeid
	FROM
		bd_billtype
	WHERE
		istransaction = 'Y'
	AND NVL (islock, 'N') = 'N'
	AND parentbilltype = '30'
	AND PK_GROUP = '0001A110000000000DDM'
	AND pk_billtypecode IN (
		'30-01',
		'30-04'
	)
Order By Pk_Billtypecode;
--------------------------------------------------------
--  DDL for View VIEW_BPM_SECLV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_SECLV (PK_WA_CRT, PK_WA_GRD, PK_WA_PRMLV, MAX_VALUE, MIN_VALUE, PK_WA_SECLV, CRITERIONVALUE, GRDNAME, PRMLVNAME, PK_WA_GRADEVER, SECLVNAME) AS 
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
--------------------------------------------------------
--  DDL for View VIEW_BPM_STORE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_STORE (PK_ORG, PK_MATERIAL, PK_STORDOC, STORCODE, STORNAME) AS 
  select stor.pk_org,matstor.pk_material,stor.pk_stordoc,stor.code as storcode,stor.name as storname
from bd_stordoc stor
left join bd_materialwarh matstor on stor.pk_stordoc=matstor.pk_stordoc;
--------------------------------------------------------
--  DDL for View VIEW_BPM_SUPPLIER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_SUPPLIER (PK_SUPPLIER, VENNAME, VENCODE, VENSHORTNAME, RESPPERSON, PSNNAME, PSNCODE, RESPDEPT, DEPCODE, DEPNAME) AS 
  select distinct a.pk_supplier,a.name as VenName,a.code as VenCode,a.shortname as Venshortname,
b.respperson,d.name as psnname,d.code as psncode,
b.respdept,e.code as depcode, e.name as depname
from bd_supplier a
right join bd_supstock b on a.pk_supplier=b.pk_supplier
left join bd_psndoc d on b.respperson =d.pk_psndoc 
left join org_dept e on b.respdept=e.pk_dept;
--------------------------------------------------------
--  DDL for View VIEW_BPM_SUPPLIERCLASS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_SUPPLIERCLASS (CODE, CREATIONTIME, CREATOR, DATAORIGINFLAG, DEF1, DEF2, DEF3, DEF4, DEF5, DR, ENABLESTATE, INNERCODE, MNECODE, MODIFIEDTIME, MODIFIER, NAME, NAME2, NAME3, NAME4, NAME5, NAME6, PARENT_ID, PK_GROUP, PK_ORG, PK_SUPPLIERCLASS, SEQ, TS) AS 
  (select CODE,CREATIONTIME,CREATOR,DATAORIGINFLAG,DEF1,DEF2,DEF3,DEF4,DEF5,DR,ENABLESTATE,INNERCODE,MNECODE,MODIFIEDTIME,MODIFIER,NAME,NAME2,NAME3,NAME4,NAME5,NAME6,PARENT_ID,PK_GROUP,PK_ORG,PK_SUPPLIERCLASS,SEQ,TS from bd_supplierclass  where  nvl(dr,0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_TZLX
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_TZLX (TRNSTYPECODE, TRNSTYPENAME, PK_TRNSTYPE) AS 
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
--  DDL for View VIEW_BPM_WADATA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_WADATA (CYEAR, CPERIOD, PK_ORG, PK_GROUP, PK_PSNDOC, PK_WA_CLASS, PK_WA_DATA, F_1, F_2, F_3, F_4, F_5, F_6, F_7, F_8, F_9, F_10, F_11, F_12, F_13, F_14, F_15, F_16, F_17, F_18, F_19, F_20, F_21, F_22, F_23, F_24, F_25, F_26, F_27, F_28, F_29, F_30, F_31, F_32, F_33, F_34, F_35, F_36, F_37, F_38, F_39, F_40, F_41, F_42, F_43, F_44, F_45, F_46, F_47, F_48, F_49, F_50, F_51, F_52, F_53, F_54, F_55, F_56, F_57, F_58, F_59, F_60, F_61, F_62, F_63, F_64, F_65, F_66, F_67, F_68, F_69, F_70, F_71, F_72, F_73, F_74, F_75, F_76, F_77, F_78, F_79, F_80, F_81, F_82, F_83, F_84, F_85, F_86, F_87, F_88, F_89, F_90, F_91, F_92, F_93, F_94, F_95, F_96, F_97, F_98, F_99, F_100, C_1, C_2, C_3, C_4, C_5, C_6, C_7, C_8, C_9, C_10, C_11, C_12, C_13, C_14, C_15, C_16, C_17, C_18, C_19, C_20, D_1, D_2, D_3, D_4, D_5, D_6, D_7, D_8, D_9, D_10) AS 
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
       wa.f_100,
wa.c_1,
wa.c_2,
wa.c_3,wa.c_4,wa.c_5,
wa.c_6,wa.c_7,wa.c_8,
wa.c_9,
wa.c_10,
wa.c_11,
wa.c_12,
wa.c_13,wa.c_14,wa.c_15,wa.c_16,wa.c_17,wa.c_18,wa.c_19,wa.c_20,wa.d_1,wa.d_2,wa.d_3,wa.d_4,wa.d_5,wa.d_6,wa.d_7,wa.d_8,wa.d_9,wa.d_10
  from wa_data wa
 where nvl(wa.dr, 0) = 0);
--------------------------------------------------------
--  DDL for View VIEW_BPM_WADATA_XZFF
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_WADATA_XZFF (CYEAR, CPERIOD, PK_ORG, PK_GROUP, PK_PSNDOC, PK_WA_CLASS, PK_WA_DATA, F_1, F_2, F_3, F_4, F_5, F_6, F_7, F_8, F_9, F_10, F_11, F_12, F_13, F_14, F_15, F_16, F_17, F_18, F_19, F_20, F_21, F_22, F_23, F_24, F_25, F_26, F_27, F_28, F_29, F_30, F_31, F_32, F_33, F_34, F_35, F_36, F_37, F_38, F_39, F_40, F_41, F_42, F_43, F_44, F_45, F_46, F_47, F_48, F_49, F_50, F_51, F_52, F_53, F_54, F_55, F_56, F_57, F_58, F_59, F_60, F_61, F_62, F_63, F_64, F_65, F_66, F_67, F_68, F_69, F_70, F_71, F_72, F_73, F_74, F_75, F_76, F_77, F_78, F_79, F_80, F_81, F_82, F_83, F_84, F_85, F_86, F_87, F_88, F_89, F_90, F_91, F_92, F_93, F_94, F_95, F_96, F_97, F_98, F_99, F_100, C_1, C_2, C_3, C_4, C_5, C_6, C_7, C_8, C_9, C_10, C_11, C_12, C_13, C_14, C_15, C_16, C_17, C_18, C_19, C_20, D_1, D_2, D_3, D_4, D_5, D_6, D_7, D_8, D_9, D_10, CODE, NAME, PSNTYPE, PK_DEPT) AS 
  select wadata.CYEAR,wadata.CPERIOD,wadata.PK_ORG,wadata.PK_GROUP,wadata.PK_PSNDOC,wadata.PK_WA_CLASS,wadata.PK_WA_DATA,wadata.F_1,wadata.F_2,wadata.F_3,wadata.F_4,wadata.F_5,wadata.F_6,wadata.F_7,wadata.F_8,wadata.F_9,wadata.F_10,wadata.F_11,wadata.F_12,wadata.F_13,wadata.F_14,wadata.F_15,wadata.F_16,wadata.F_17,wadata.F_18,wadata.F_19,wadata.F_20,wadata.F_21,wadata.F_22,wadata.F_23,wadata.F_24,wadata.F_25,wadata.F_26,wadata.F_27,wadata.F_28,wadata.F_29,wadata.F_30,wadata.F_31,wadata.F_32,wadata.F_33,wadata.F_34,wadata.F_35,wadata.F_36,wadata.F_37,wadata.F_38,wadata.F_39,wadata.F_40,wadata.F_41,wadata.F_42,wadata.F_43,wadata.F_44,wadata.F_45,wadata.F_46,wadata.F_47,wadata.F_48,wadata.F_49,wadata.F_50,wadata.F_51,wadata.F_52,wadata.F_53,wadata.F_54,wadata.F_55,wadata.F_56,wadata.F_57,wadata.F_58,wadata.F_59,wadata.F_60,wadata.F_61,wadata.F_62,wadata.F_63,wadata.F_64,wadata.F_65,wadata.F_66,wadata.F_67,wadata.F_68,wadata.F_69,wadata.F_70,wadata.F_71,wadata.F_72,wadata.F_73,wadata.F_74,wadata.F_75,wadata.F_76,wadata.F_77,wadata.F_78,wadata.F_79,wadata.F_80,wadata.F_81,wadata.F_82,wadata.F_83,wadata.F_84,wadata.F_85,wadata.F_86,wadata.F_87,wadata.F_88,wadata.F_89,wadata.F_90,wadata.F_91,wadata.F_92,wadata.F_93,wadata.F_94,wadata.F_95,wadata.F_96,wadata.F_97,wadata.F_98,wadata.F_99,wadata.F_100,wadata.C_1,wadata.C_2,wadata.C_3,wadata.C_4,wadata.C_5,wadata.C_6,wadata.C_7,wadata.C_8,wadata.C_9,wadata.C_10,wadata.C_11,wadata.C_12,wadata.C_13,wadata.C_14,wadata.C_15,wadata.C_16,wadata.C_17,wadata.C_18,wadata.C_19,wadata.C_20,wadata.D_1,wadata.D_2,wadata.D_3,wadata.D_4,wadata.D_5,wadata.D_6,wadata.D_7,wadata.D_8,wadata.D_9,wadata.D_10,psndoc.code,psndoc.NAME,psnjob.psntype,psnjob.pk_dept from VIEW_BPM_wadata 
wadata left join bd_psndoc psndoc on wadata.PK_PSNDOC = psndoc.PK_PSNDOC
left join hi_psnjob psnjob on wadata.PK_PSNDOC = psnjob.PK_PSNDOC
where psnjob.enddate is null;
--------------------------------------------------------
--  DDL for View VIEW_BPM_WAITEM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_WAITEM (PK_WA_CLASS, CLNAME, PK_ORG, CYEAR, CPERIOD, ITEMKEY, ITEMNAME, CODE, NAME) AS 
  SELECT citem.pk_wa_class,cl.name clname,citem.pk_org,citem.cyear,
  citem.cperiod,citem.itemkey,citem.name itemname,
  def.code,def.name name
  FROM wa_classitem citem
  left join wa_waclass cl on cl.pk_wa_class=citem.pk_wa_class
  left JOIN wa_item item ON citem.pk_wa_item = item.pk_wa_item
  left join bd_defdoc def on item.category_id  = def.pk_defdoc 
  ORDER BY citem.itemkey;
--------------------------------------------------------
--  DDL for View VIEW_BPM_XCL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_XCL (PK_ORG, NONHANDNUM, PK_BATCHCODE, VBATCHCODE, PK_STORDOC, STORENAME, CMATERIALVID, PK_MATERIAL, CODE, MATERIALNAME, CASTUNITID, MEASNAME) AS 
  select ich.pk_org,icnum.nonhandnum-ck.nassistnum as nonhandnum,ich.pk_batchcode,ich.vbatchcode,stor.pk_stordoc,stor.name as storename, 
ich.cmaterialvid,mat.pk_material,mat.code,mat.name as materialname,ich.castunitid,mess.name as measname
from ic_onhandnum icnum 
left join ic_onhanddim ich on icnum.pk_onhanddim=ich.pk_onhanddim
left join bd_stordoc stor on stor.pk_stordoc=ich.cwarehouseid
left join bd_material mat on mat.pk_material=ich.cmaterialvid
left join bd_measdoc mess on mess.pk_measdoc=ich.castunitid 
left join ic_generalout_b ck on ck.cmaterialvid = mat.pk_material and ich.vbatchcode=ck.vbatchcode
where nvl(icnum.dr,0)=0 and icnum.nonhandnum<>0 and nvl(ck.dr,0)=0;
--------------------------------------------------------
--  DDL for View VIEW_BPM_XZFA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_XZFA (PK_WA_CLASS, CODE, NAME, PK_PERIODSCHEME, CYEAR, CPERIOD, MUTIPLEFLAG, PK_ORG) AS 
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
--  DDL for View VIEW_BPM_XZQJ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_XZQJ (PK_ORG, ORGNAME, PK_PERIODSTATE, PK_PERIODSCHEME, ACCOUNTMARK, PK_WA_CLASS, WACLASSCODE, WACLASSCNAME, PK_WA_PERIOD, CYEAR, CPERIOD, XZQJ) AS 
  select a.pk_org , org.name orgname,a.pk_periodstate ,b.pk_periodscheme,a.accountmark,
a.pk_wa_class ,c.code waclasscode,c.name waclasscname,
a.pk_wa_period,b.cyear,b.cperiod, b.cyear||b.cperiod AS xzqj
from wa_periodstate a
inner join wa_period b on a.pk_wa_period=b.pk_wa_period
left join wa_waclass c on a.pk_wa_class=c.pk_wa_class
left join org_orgs org on org.pk_org=a.pk_org 
where a.enableflag = 'Y';
--------------------------------------------------------
--  DDL for View VIEW_BPM_XZXM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_BPM_XZXM (CODE, NAME, PK_WA_ITEM) AS 
  select code, name, pk_wa_item from wa_item;
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEAUTOCOST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYEAUTOCOST (PK_GROUP, PK_ORG, PK_COSTCENTER, CCTYPE, CCNAME, CPERIOD, BCOST, CCOSTOBJECTID, CINVENTORYID, PK_MEASDOC, NNUM) AS 
  (
select  temp.pk_group,temp.pk_org,temp.pk_costcenter, temp.cctype, temp.ccname ,temp.cperiod,temp.bcost,
costobject.ccostobjectid as ccostobjectid, --成本对象默认
costobject.pk_material as cinventoryid,
costobject.pk_measdoc  as pk_measdoc,--计量单位默认
100 as nnum --数量默认
from(
--查询每个会计期间
select  cs.pk_group,cs.pk_org,cs.pk_costcenter,cctype,  cs.ccname ,zuoye.cperiod,
nvl(wgnnum,0),nvl(xhnnum,0), nvl(zynnum,0),
case 
when nvl(wgnnum,0) >0 then 'N'
when  nvl(wgnnum,0) =0 and ccname in ('研发成本中心','销售成本中心','采购成本中心') then '0'
when  nvl(wgnnum,0) =0  and (nvl(xhnnum,0)+nvl(zynnum,0))>0 then '1'
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
inner join view_nc_zuoyeautocost02  costobject on temp.pk_group||temp.pk_org = costobject.pk_group||costobject.pk_org
where bcost in('0','1')
);
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEAUTOCOST01
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYEAUTOCOST01 (PK_GROUP, PK_ORG, CCOSTCENTERID, CPERIOD, WGNNUM, XHNNUM, ZYNNUM) AS 
  (
select pk_group,pk_org,ccostcenterid,cperiod,sum(wgnnum) as wgnnum,sum(xhnum) as xhnnum,sum(zynnum)  as zynnum  from (

select pk_group,pk_org,ccostcenterid,cperiod , sum(1) as  wgnnum, 0 as xhnum, 0 as zynnum  from  cm_product where nvl(dr,0)=0
group by pk_group,pk_org,ccostcenterid,cperiod
union all

select pk_group,pk_org,ccostcenterid,cperiod , 0 as  wgnnum, sum(1) as xhnum, 0 as zynnum from  cm_stuff where nvl(dr,0)=0
group by pk_group,pk_org,ccostcenterid,cperiod
union all

--作业统计单 (cm_actnum) 
select pk_group,pk_org,ccostcenterid,cperiod , 0 as  wgnnum, 0 as xhnum, sum(1) as zynnum from  cm_actnum where nvl(dr,0)=0
group by pk_group,pk_org,ccostcenterid,cperiod
)
group by pk_group,pk_org,ccostcenterid,cperiod
);
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEAUTOCOST02
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYEAUTOCOST02 (PK_GROUP, PK_ORG, CCOSTOBJECTID, PK_MATERIAL, PK_MEASDOC) AS 
  (
select  cm_costobject.pk_group, cm_costobject.pk_org,cm_costobject.ccostobjectid, 
bd_material.pk_material,bd_material.pk_measdoc
from cm_costobject
inner join bd_material on cm_costobject.cmaterialid = bd_material.pk_material
where cm_costobject.vcostobjcode='990200000000004'
);
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYECHONGJIE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYECHONGJIE (PK_GROUP, PK_ORG, PERIODID, CDPTID, PK_LARGEITEM, VACTIVITYNAME, NNUM) AS 
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
--  DDL for View VIEW_NC_ZUOYECHUYUN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYECHUYUN (PK_GROUP, PK_ORG, CDPTID, CTRANTYPEID, DBILLDATE, TAUDITTIME, NNUM) AS 
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
--  DDL for View VIEW_NC_ZUOYEHUANBAO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYEHUANBAO (PK_GROUP, PK_ORG, PERIODID, CDPTID, PK_LARGEITEM, VACTIVITYNAME, NNUM) AS 
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
--  DDL for View VIEW_NC_ZUOYEJIANYAN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYEJIANYAN (PK_GROUP, PK_ORG, CDPTID, CTRANTYPEID, DBILLDATE, TAUDITTIME, NNUM) AS 
  (
---质检报告\
select pk_group,pk_org,cdptid,ctrantypeid,dbilldate,taudittime, pishu*（price+ 0.69） as nnum from 
view_nc_zuoyejianyan_price

);
--------------------------------------------------------
--  DDL for View VIEW_NC_ZUOYEJIANYAN_PRICE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW VIEW_NC_ZUOYEJIANYAN_PRICE (PK_REPORTBILL, PK_GROUP, PK_ORG, CDPTID, CTRANTYPEID, DBILLDATE, TAUDITTIME, PISHU, PRICE) AS 
  (
select rh.pk_reportbill, rh.pk_group, rh.pk_org ,rh.pk_applydept  as  cdptid, rh.ctrantypeid as ctrantypeid, rh.dapplydate as dbilldate, 
rh.taudittime  as  taudittime , DECODE(rbody.vbdef1, '~', 0, rbody.vbdef1) as  pishu, bjd.price
--qc_applybill.pk_applybill ,qc_checkbill.cfirstid,  rh.pk_chkbatch ,qc_checkbill.pk_chkbatch
from qc_reportbill_b rbody
inner join qc_reportbill rh on rbody.pk_reportbill =rh.pk_reportbill 
--报检单 1:1--检验单--检验项目单价和
 inner join (
select pk_applybill,pk_chkbatch, sum( DECODE(qc_checkitem.vbdef1, '~', 0, qc_checkitem.vbdef1)) as  price
 from qc_applybill 
--检验单
inner join qc_checkbill on  qc_applybill.pk_applybill =qc_checkbill. cfirstid 
inner JOIN qc_checkbill_b  ON qc_checkbill.pk_checkbill= qc_checkbill_b.pk_checkbill
--检验项目
inner join  qc_checkitem   on qc_checkbill_b. pk_checkitem =qc_checkitem.pk_checkitem
where 
 nvl(qc_applybill.dr,0)= 0
AND nvl(qc_checkbill.dr,0)=0
AND nvl(qc_checkbill_b.dr,0) = 0
AND nvl(qc_checkbill.dr,0) = 0
AND NVL ( qc_checkbill_b.buseless, 'N' ) = 'N'
AND NVL ( qc_checkbill.blatest, 'N') = 'Y'
group by pk_applybill,pk_chkbatch
) bjd on rh.pk_applybill = bjd.pk_applybill and  rh.pk_chkbatch =bjd.pk_chkbatch
WHERE 
nvl(rbody.dr,0)= 0 
and nvl(rh.dr,0)=0
);
