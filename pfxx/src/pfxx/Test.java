package pfxx;

public class Test {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		for(int i=1;i<=20;i++){
			String fac = "Fator"+i;
//			System.out.println("if (this.get"+fac+"() == null ? vo.get"+fac+"() != null: !this.get"+fac+"().equals(vo.get"+fac+"())) {return false;}");
			
//			System.out.println("hashcode = "+fac+" == null ? 31 * hashcode : 31 * hashcode+ "+fac+".hashCode();");
//			System.out.println("keyVO.set"+fac+"( entry.getKey().get"+fac+"());");
			System.out.println("vdef"+i+" varchar(101) default '~' NULL,");
		}

	}

}
