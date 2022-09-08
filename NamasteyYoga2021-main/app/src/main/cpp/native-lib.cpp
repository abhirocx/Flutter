#include <jni.h>
#include <string>



void swap(char i, char i1);

extern "C" JNIEXPORT jstring JNICALL
Java_com_np_namasteyoga_utils_ConstUtility_getAPIKey(
        JNIEnv* env,
        jobject /* this */) {

    const char *s = "$#%3O51V@n!h@b$A";
    int n = strlen(s);
    // create dynamic pointer char array
    char *rev = new char[n + 1];
    // copy of string to ptr array
    strcpy(rev, s);
    // Swap character starting from two
    // corners
    char temp;
    for (int i = 0, j = n - 1; i < j; i++, j--) {
        temp = rev[i];
        rev[i] = rev[j];
        rev[j] = temp;

    }
    return env->NewStringUTF(rev);
}

