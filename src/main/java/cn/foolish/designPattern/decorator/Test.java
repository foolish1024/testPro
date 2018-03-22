package cn.foolish.designPattern.decorator;

public class Test {
	public static void main(String[] args) {
		AbstractInterface ai = new BeDecorated();
		Decorate de = new DecorateImpl(ai);
		de.method1();
		de.method2();
	}
}
