--外系统取数设置 节点改造
alter table cm_fetchset_b  add(
pk_workitem char(20),
pk_costobject char(20),
pk_qcdept char(20),
pk_serverdept char(20),
pk_largeritem char(20),
pk_factor  char(20),
pk_factorgroup char(20)
);

--外系统取数 节点改造

alter table cm_fetchinfo add(
pk_workitem char(20),
pk_costobject char(20),
pk_qcdept char(20),
pk_serverdept char(20),
pk_largeritem char(20),
pk_factor  char(20),
pk_factorgroup char(20)
);

---作业统计单取数--储运
create or replace view  view_nc_zuoyechuyun as (
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

---作业统计单取数--检验
create or replace view view_nc_zuoyejianyan_price as (
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


--。每张质检报告上（所有质检项目单价之和）*批数 + 0.69*批数
create or replace view  view_nc_zuoyejianyan as (
---质检报告\
select pk_group,pk_org,cdptid,ctrantypeid,dbilldate,taudittime, pishu*（price+ 0.69） as nnum from 
view_nc_zuoyejianyan_price

);


---检验项目 ，增加自定义项目20
alter table  qc_checkitem  add (
vbdef1 varchar(101) default '~' NULL,
vbdef2 varchar(101) default '~' NULL,
vbdef3 varchar(101) default '~' NULL,
vbdef4 varchar(101) default '~' NULL,
vbdef5 varchar(101) default '~' NULL,
vbdef6 varchar(101) default '~' NULL,
vbdef7 varchar(101) default '~' NULL,
vbdef8 varchar(101) default '~' NULL,
vbdef9 varchar(101) default '~' NULL,
vbdef10 varchar(101) default '~' NULL,
vbdef11 varchar(101) default '~' NULL,
vbdef12 varchar(101) default '~' NULL,
vbdef13 varchar(101) default '~' NULL,
vbdef14 varchar(101) default '~' NULL,
vbdef15 varchar(101) default '~' NULL,
vbdef16 varchar(101) default '~' NULL,
vbdef17 varchar(101) default '~' NULL,
vbdef18 varchar(101) default '~' NULL,
vbdef19 varchar(101) default '~' NULL,
vbdef20 varchar(101) default '~' NULL);


--作业统计单取数--环保

 CREATE TABLE "MMPAC_HUANBAO_H" 
   (	"PK_HEAD" CHAR(20 BYTE) NOT NULL ENABLE, 
	"PK_GROUP" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"PK_ORG" VARCHAR2(20 BYTE) DEFAULT '~', 
	"PK_ORG_V" VARCHAR2(20 BYTE) DEFAULT '~', 
	"CPLANNERID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"PERIODID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"CDEPTID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"CDEPTVID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"CODE" VARCHAR2(50 BYTE), 
	"NAME" VARCHAR2(50 BYTE), 
	"VNOTE" VARCHAR2(1000 BYTE), 
	"CREATOR" VARCHAR2(20 BYTE) DEFAULT '~', 
	"CREATIONTIME" CHAR(19 BYTE), 
	"MODIFIER" VARCHAR2(20 BYTE) DEFAULT '~', 
	"MODIFIEDTIME" CHAR(19 BYTE), 
	"BILLNO" VARCHAR2(50 BYTE), 
	"DBILLDATE" CHAR(19 BYTE), 
	"BILLMAKER" VARCHAR2(20 BYTE) DEFAULT '~', 
	"MAKETIME" CHAR(19 BYTE), 
	"APPROVER" VARCHAR2(20 BYTE) DEFAULT '~', 
	"APPROVEDATE" CHAR(19 BYTE), 
	"APPROVESTATUS" NUMBER(*,0), 
	"APPROVENOTE" VARCHAR2(50 BYTE), 
	"BILLTYPE" VARCHAR2(20 BYTE) DEFAULT '~', 
	"LASTMAKETIME" CHAR(19 BYTE), 
	"VDEF1" VARCHAR2(101 BYTE), 
	"VDEF2" VARCHAR2(101 BYTE), 
	"VDEF3" VARCHAR2(101 BYTE), 
	"VDEF4" VARCHAR2(101 BYTE), 
	"VDEF5" VARCHAR2(101 BYTE), 
	"VDEF6" VARCHAR2(101 BYTE), 
	"VDEF7" VARCHAR2(101 BYTE), 
	"VDEF8" VARCHAR2(101 BYTE), 
	"VDEF9" VARCHAR2(101 BYTE), 
	"VDEF10" VARCHAR2(101 BYTE), 
	"VDEF11" VARCHAR2(101 BYTE), 
	"VDEF12" VARCHAR2(101 BYTE), 
	"VDEF13" VARCHAR2(101 BYTE), 
	"VDEF14" VARCHAR2(101 BYTE), 
	"VDEF15" VARCHAR2(101 BYTE), 
	"VDEF16" VARCHAR2(101 BYTE), 
	"VDEF17" VARCHAR2(101 BYTE), 
	"VDEF18" VARCHAR2(101 BYTE), 
	"VDEF19" VARCHAR2(101 BYTE), 
	"VDEF20" VARCHAR2(101 BYTE), 
	"RESERVE1" VARCHAR2(50 BYTE), 
	"RESERVE2" VARCHAR2(50 BYTE), 
	"RESERVE3" VARCHAR2(50 BYTE), 
	"RESERVE4" VARCHAR2(50 BYTE), 
	"RESERVE5" VARCHAR2(50 BYTE), 
	"RESERVE6" CHAR(19 BYTE), 
	"RESERVE7" CHAR(19 BYTE), 
	"RESERVE8" NUMBER(28,8), 
	"RESERVE9" NUMBER(28,8), 
	"RESERVE10" NUMBER(28,8), 
	"TRANSTYPE" VARCHAR2(50 BYTE), 
	"TRANSTYPEPK" VARCHAR2(20 BYTE) DEFAULT '~', 
	"SRCBILLTYPE" VARCHAR2(50 BYTE), 
	"SRCBILLID" VARCHAR2(50 BYTE), 
	"TS" CHAR(19 BYTE), 
	"DR" NUMBER(*,0) DEFAULT 0, 
	 CONSTRAINT "PK_MMPAC_HUANBAO_H" PRIMARY KEY ("PK_HEAD"));
   
 CREATE TABLE "MMPAC_HUANBAO_B" 
   (	"PK_BODY" CHAR(50 BYTE) NOT NULL ENABLE, 
	"CPLANNERID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"CDEPTID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"PK_LARGEITEM" VARCHAR2(20 BYTE) DEFAULT '~', 
	"PK_MEASDOC" VARCHAR2(50 BYTE), 
	"CDEPTVID" VARCHAR2(20 BYTE) DEFAULT '~', 
	"NTOTALNUM" NUMBER(28,8), 
	"NTOTALDAYNUM" NUMBER(28,8), 
	"NTOTALNIGHTNUM" NUMBER(28,8), 
	"NNUM1" NUMBER(28,8), 
	"NNUM2" NUMBER(28,8), 
	"VNOTE" VARCHAR2(181 BYTE), 
	"NTOTAL" NUMBER(28,8), 
	"DAY31" NUMBER(28,8), 
	"NIGHT31" NUMBER(28,8), 
	"DAY30" NUMBER(28,8), 
	"NIGHT30" NUMBER(28,8), 
	"DAY29" NUMBER(28,8), 
	"NIGHT29" NUMBER(28,8), 
	"DAY28" NUMBER(28,8), 
	"NIGHT28" NUMBER(28,8), 
	"DAY27" NUMBER(28,8), 
	"NIGHT27" NUMBER(28,8), 
	"DAY26" NUMBER(28,8), 
	"NIGHT26" NUMBER(28,8), 
	"DAY25" NUMBER(28,8), 
	"NIGHT25" NUMBER(28,8), 
	"DAY24" NUMBER(28,8), 
	"NIGHT24" NUMBER(28,8), 
	"DAY23" NUMBER(28,8), 
	"NIGHT23" NUMBER(28,8), 
	"DAY22" NUMBER(28,8), 
	"NIGHT22" NUMBER(28,8), 
	"DAY21" NUMBER(28,8), 
	"NIGHT21" NUMBER(28,8), 
	"DAY20" NUMBER(28,8), 
	"NIGHT20" NUMBER(28,8), 
	"DAY19" NUMBER(28,8), 
	"NIGHT19" NUMBER(28,8), 
	"DAY18" NUMBER(28,8), 
	"NIGHT18" NUMBER(28,8), 
	"DAY17" NUMBER(28,8), 
	"NIGHT17" NUMBER(28,8), 
	"DAY16" NUMBER(28,8), 
	"NIGHT16" NUMBER(28,8), 
	"DAY15" NUMBER(28,8), 
	"NIGHT15" NUMBER(28,8), 
	"DAY14" NUMBER(28,8), 
	"NIGHT14" NUMBER(28,8), 
	"DAY13" NUMBER(28,8), 
	"NIGHT13" NUMBER(28,8), 
	"DAY12" NUMBER(28,8), 
	"NIGHT12" NUMBER(28,8), 
	"DAY11" NUMBER(28,8), 
	"NIGHT11" NUMBER(28,8), 
	"DAY10" NUMBER(28,8), 
	"NIGHT10" NUMBER(28,8), 
	"DAY9" NUMBER(28,8), 
	"NIGHT9" NUMBER(28,8), 
	"DAY8" NUMBER(28,8), 
	"NIGHT8" NUMBER(28,8), 
	"DAY7" NUMBER(28,8), 
	"NIGHT7" NUMBER(28,8), 
	"DAY6" NUMBER(28,8), 
	"NIGHT6" NUMBER(28,8), 
	"DAY5" NUMBER(28,8), 
	"NIGHT5" NUMBER(28,8), 
	"DAY4" NUMBER(28,8), 
	"NIGHT4" NUMBER(28,8), 
	"DAY3" NUMBER(28,8), 
	"NIGHT3" NUMBER(28,8), 
	"DAY2" NUMBER(28,8), 
	"NIGHT2" NUMBER(28,8), 
	"DAY1" NUMBER(28,8), 
	"NIGHT1" NUMBER(28,8), 
	"ROWNO" VARCHAR2(50 BYTE), 
	"VBDEF1" VARCHAR2(101 BYTE), 
	"VBDEF2" VARCHAR2(101 BYTE), 
	"VBDEF3" VARCHAR2(101 BYTE), 
	"VBDEF4" VARCHAR2(101 BYTE), 
	"VBDEF5" VARCHAR2(101 BYTE), 
	"VBDEF6" VARCHAR2(101 BYTE), 
	"VBDEF7" VARCHAR2(101 BYTE), 
	"VBDEF8" VARCHAR2(101 BYTE), 
	"VBDEF9" VARCHAR2(101 BYTE), 
	"VBDEF10" VARCHAR2(101 BYTE), 
	"VBDEF11" VARCHAR2(101 BYTE), 
	"VBDEF12" VARCHAR2(101 BYTE), 
	"VBDEF13" VARCHAR2(101 BYTE), 
	"VBDEF14" VARCHAR2(101 BYTE), 
	"VBDEF15" VARCHAR2(101 BYTE), 
	"VBDEF16" VARCHAR2(101 BYTE), 
	"VBDEF17" VARCHAR2(101 BYTE), 
	"VBDEF18" VARCHAR2(101 BYTE), 
	"VBDEF19" VARCHAR2(101 BYTE), 
	"VBDEF20" VARCHAR2(101 BYTE), 
	"PK_HEAD" CHAR(20 BYTE) NOT NULL ENABLE, 
	"TS" CHAR(19 BYTE), 
	"DR" NUMBER(*,0) DEFAULT 0, 
	 CONSTRAINT "PK_MMPAC_HUANBAO_B" PRIMARY KEY ("PK_BODY"));
create or replace view  view_nc_zuoyehuanbao as (
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

--在进行崇杰费用取数时的计算规则为： 每个受益部门的废水量、折算废水量、新鲜水量*0.794三个数量中的最大值，生成的作业统计单作业为 崇杰水量。
create or replace view  view_nc_zuoyechongjie as (
select  pk_group, pk_org,periodid,cdptid,max(nnum) as nnum from (
select 
mmpac_huanbao_h. pk_group,
mmpac_huanbao_h. pk_org ,
--会计期间
mmpac_huanbao_h.periodid as periodid,
 ---受益部门
mmpac_huanbao_b.cdeptid as  cdptid,
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
 )
 group by pk_group, pk_org,periodid,cdptid
 
);
--BOM匹配规则
alter table bd_bom  add(
	"VFREE1" VARCHAR2(101 BYTE), 
	"VFREE10" VARCHAR2(101 BYTE), 
	"VFREE2" VARCHAR2(101 BYTE), 
	"VFREE3" VARCHAR2(101 BYTE), 
	"VFREE4" VARCHAR2(101 BYTE), 
	"VFREE5" VARCHAR2(101 BYTE), 
	"VFREE6" VARCHAR2(101 BYTE), 
	"VFREE7" VARCHAR2(101 BYTE), 
	"VFREE8" VARCHAR2(101 BYTE), 
	"VFREE9" VARCHAR2(101 BYTE)
);
--功能节点 作业环保取数

--功能节点 五金预算



