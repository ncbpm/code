DELETE FROM pub_bcr_candiattr WHERE pk_nbcr = '0001ZZ1000000000DM70';
DELETE FROM pub_bcr_elem WHERE pk_billcodebase in ( select pk_billcodebase from pub_bcr_RuleBase where nbcrcode = '55Z1' );
DELETE FROM pub_bcr_RuleBase WHERE nbcrcode = '55Z1';
DELETE FROM pub_bcr_nbcr WHERE pk_nbcr = '0001ZZ1000000000DM70';
DELETE FROM pub_bcr_OrgRela WHERE pk_billcodebase = '0001ZZ1000000000DM71';
DELETE FROM pub_bcr_RuleBase WHERE pk_billcodebase = '0001ZZ1000000000DM71';
DELETE FROM pub_bcr_elem WHERE pk_billcodeelem = '0001ZZ1000000000DM72';
DELETE FROM pub_bcr_elem WHERE pk_billcodeelem = '0001ZZ1000000000DM73';
DELETE FROM pub_bcr_elem WHERE pk_billcodeelem = '0001ZZ1000000000DM74';
DELETE FROM bd_billtype2 WHERE pk_billtypeid = '0001ZZ1000000000DJPD';
DELETE FROM bd_fwdbilltype WHERE pk_billtypeid = '0001ZZ1000000000DJPD';
DELETE FROM pub_function WHERE pk_billtype = '55Z1';
DELETE FROM pub_billaction WHERE pk_billtypeid = '0001ZZ1000000000DJPD';
DELETE FROM pub_billactiongroup WHERE pk_billtype = '55Z1';
DELETE FROM bd_billtype WHERE pk_billtypeid = '0001ZZ1000000000DJPD';
delete from temppkts;
DELETE FROM sm_rule_type WHERE pk_rule_type = null;
DELETE FROM sm_permission_res WHERE pk_permission_res = null;
DELETE FROM pub_billaction WHERE pk_billaction = '0001ZZ1000000000DM6O';
DELETE FROM pub_billaction WHERE pk_billaction = '0001ZZ1000000000DM6P';
DELETE FROM pub_billaction WHERE pk_billaction = '0001ZZ1000000000DM6Q';
DELETE FROM pub_billaction WHERE pk_billaction = '0001ZZ1000000000DM6R';
DELETE FROM pub_billaction WHERE pk_billaction = '0001ZZ1000000000DM6S';
DELETE FROM pub_billaction WHERE pk_billaction = '0001ZZ1000000000DM6T';
DELETE FROM pub_busiclass WHERE pk_busiclass = '0001ZZ1000000000DM6U';
DELETE FROM pub_busiclass WHERE pk_busiclass = '0001ZZ1000000000DM6V';
DELETE FROM pub_busiclass WHERE pk_busiclass = '0001ZZ1000000000DM6W';
DELETE FROM pub_busiclass WHERE pk_busiclass = '0001ZZ1000000000DM6X';
DELETE FROM pub_busiclass WHERE pk_busiclass = '0001ZZ1000000000DM6Y';
DELETE FROM pub_busiclass WHERE pk_busiclass = '0001ZZ1000000000DM6Z';
DELETE FROM pub_systemplate_base where pk_systemplate = '0001ZZ1000000000DM6M';
delete from pub_print_datasource where ctemplateid = '0001ZZ1000000000DJVL';
delete from pub_print_cell where ctemplateid = '0001ZZ1000000000DJVL';
delete from pub_print_line where ctemplateid = '0001ZZ1000000000DJVL';
delete from pub_print_variable where ctemplateid = '0001ZZ1000000000DJVL';
delete from pub_print_template where ctemplateid = '0001ZZ1000000000DJVL';
DELETE FROM pub_systemplate_base where pk_systemplate = '0001ZZ1000000000DJVK';
delete from pub_query_condition where pk_templet = '0001ZZ1000000000DJTV';
delete from pub_query_templet where id = '0001ZZ1000000000DJTV';
DELETE FROM pub_systemplate_base where pk_systemplate = '0001ZZ1000000000DJTU';
delete from pub_billtemplet_b where pk_billtemplet = '0001ZZ1000000000DJPH';
delete from pub_billtemplet where pk_billtemplet = '0001ZZ1000000000DJPH';
DELETE FROM pub_billtemplet_t WHERE pk_billtemplet = '0001ZZ1000000000DJPH';
DELETE FROM sm_menuitemreg WHERE pk_menuitem = '0001ZZ1000000000DJPG';
DELETE FROM sm_funcregister WHERE cfunid = '0001ZZ1000000000DJPE';
DELETE FROM sm_paramregister WHERE pk_param = '0001ZZ1000000000DJPF';
DELETE FROM pub_systemplate_base where pk_systemplate = '0001ZZ1000000000DJPC';
delete from pub_query_condition where pk_templet = '0001ZZ1000000000DJNN';
delete from pub_query_templet where id = '0001ZZ1000000000DJNN';
DELETE FROM sm_funcregister WHERE cfunid = '0001ZZ1000000000DJNL';
DELETE FROM sm_paramregister WHERE pk_param = '0001ZZ1000000000DJNM';
