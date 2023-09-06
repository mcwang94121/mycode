package com.example.demo.jniTest;

/**
 * JniTest
 *
 * @author mc
 * @date 2023年03月28日
 */
public class JniTest {

    static {
        System.out.println("DLL path:" + System.getProperty("java.library.path"));
        System.loadLibrary("MyNativeDll1");
    }

    public static native void callCppMethod();

    public static void main(String[] args) {
        callCppMethod();
    }
}
