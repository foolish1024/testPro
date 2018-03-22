package cn.foolish.asm;

import java.lang.reflect.Method;

public class ReflectClient {
	public static void main(String[] args) throws Exception {
        testJdkReflect();
//        testReflectAsm();
    }
    
    public static void testJdkReflect() throws Exception {
        TestClass someObject = new TestClass();  
        Method method = TestClass.class.getMethod("foo", String.class);

        for (int i = 0; i < 5; i++) {
            long begin = System.currentTimeMillis();
            for (int j = 0; j < 100000000; j++) {
                method.invoke(someObject, "Unmi");
            }
            System.out.print(System.currentTimeMillis() - begin +" ");
        }
    }
 
    public static void testReflectAsm() throws Exception {
    	TestClass someObject = new TestClass();
        MethodAccess access = MethodAccess.get(TestClass.class);

        for (int i = 0; i < 5; i++) {
            long begin = System.currentTimeMillis();
            for (int j = 0; j < 100000000; j++) {
                access.invoke(someObject, "foo", "Unmi");
            }
            System.out.print(System.currentTimeMillis() - begin + " ");
        }
    }
}
