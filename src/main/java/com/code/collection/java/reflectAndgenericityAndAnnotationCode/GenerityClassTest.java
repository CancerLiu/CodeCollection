package com.code.collection.java.reflectAndgenericityAndAnnotationCode;

/**
 * 泛型类与泛型方法
 * <p>
 * (1)因此静态方法、静态初始化块或者静态变量的声明和初始化中不允许使用类或接口的类型形参（静态的东西也是一个类独有的一块内存区，估计会和泛型参数矛盾）。
 * (2)由于系统中不会真正生成泛型类，所以instanceof运算符后不能使用泛型类或泛型参数。
 * <p>
 * (3)static方法要使用泛型能力，就必须使其成为泛型方法.
 *
 * @param <T>
 */
public class GenerityClassTest<T> {

    private T t;

    public GenerityClassTest(T t) {
        this.t = t;
    }

    /**
     * 泛型方法
     */
    public <U> int generityMethod(U u, int i) {
        if (u instanceof Integer) {
            return i + (Integer) u;
        }
        return i;
    }


    public static void main(String[] args) {
        GenerityClassTest<Integer> test = new GenerityClassTest<>(2);
        System.out.println(test.generityMethod(3, 4));
    }
}
