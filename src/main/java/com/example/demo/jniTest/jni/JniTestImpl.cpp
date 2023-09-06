#include "com_example_demo_jniTest_JniTest.h"
#include <stdio.h>

JNIEXPORT void JNICALL Java_com_example_demo_jniTest_JniTest_callCppMethod
  (JNIEnv *, jclass)
{
    printf("Print From Cpp: \n");
    printf("I am a cpp method ! \n");
}

