package cn.foolish.designPattern.decorator;

public class DecorateImpl extends Decorate {
	private AbstractInterface ai =null;
	
	public DecorateImpl(AbstractInterface ai){
		this.ai = ai;
	}
	
	public void method1(){
		ai.method1();
		System.out.println("具体的装饰者1");
	}
	
	public void method2(){
		ai.method2();
		System.out.println("具体的装饰者2");
	}
}
