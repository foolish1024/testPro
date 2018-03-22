package cn.foolish.designPattern.decorator;

public class BeDecorated implements AbstractInterface{

	@Override
	public void method1() {
		System.out.println("被装饰者---methpd1");
	}

	@Override
	public void method2() {
		System.out.println("被装饰者---methpd2");
	}

}
