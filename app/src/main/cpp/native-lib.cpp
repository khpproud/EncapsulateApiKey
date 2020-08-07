/**
 * Created by Peter_Proud
 */

#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "GIPHY_JNI"

#define  LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,    TAG, __VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,       TAG, __VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,      TAG, __VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,       TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,      TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_dev_patrick_gifsample_GiphyUtils_getAPIKey(
        JNIEnv *env,
        jobject /* this */) {

    // First, We need to obtain Base64 encoded Api key string.
    jclass cls_BuildConfig = env->FindClass("dev/patrick/gifsample/BuildConfig");
    jfieldID fid_ApiKey = env->GetStaticFieldID(cls_BuildConfig, "GIPHY_API_KEY",
                                                "Ljava/lang/String;");
    auto apiKeyEncoded = (jstring) env->GetStaticObjectField(cls_BuildConfig,fid_ApiKey);

    const char *nativeString = env->GetStringUTFChars(apiKeyEncoded, nullptr);
    jstring apiKeyEncodedString = env->NewStringUTF(nativeString);
    LOGI("encoded str: %s", nativeString);

    env->ReleaseStringUTFChars(apiKeyEncoded, nativeString);

    // And then, decodes it to byte array.
    jclass cls_Base64 = env->FindClass("android/util/Base64");
    jmethodID mid_decode = env->GetStaticMethodID(cls_Base64, "decode", "(Ljava/lang/String;I)[B");
    jfieldID fid_Default = env->GetStaticFieldID(cls_Base64, "DEFAULT", "I");
    jint Base64Default = env->GetStaticIntField(cls_Base64, fid_Default);

    auto apiKeyByteArray = (jbyteArray) env->CallStaticObjectMethod(cls_Base64, mid_decode,
                                                                    apiKeyEncodedString, Base64Default);

    // Lastly, generates and returns to decoded api key string.
    size_t length = (size_t) env->GetArrayLength(apiKeyByteArray);
    jbyte* pBytes = env->GetByteArrayElements(apiKeyByteArray, nullptr);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(apiKeyByteArray, pBytes, JNI_ABORT);

    return env->NewStringUTF(ret.c_str());
}