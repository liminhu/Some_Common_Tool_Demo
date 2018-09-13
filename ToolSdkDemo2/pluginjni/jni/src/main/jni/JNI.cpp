#include "MyUtils.h"


jstring getPackname(JNIEnv *env, jobject clazz, jobject obj) {
	jclass native_class = env->GetObjectClass(obj);
	jmethodID mId = env->GetMethodID(native_class, "getPackageName", "()Ljava/lang/String;");
	jstring packName = static_cast<jstring>(env->CallObjectMethod(obj, mId));
	char *c_msg = (char*)env->GetStringUTFChars(packName, 0);
    LogE("包名--- packName: %s", c_msg);
	return packName;
}



jstring getSignature(JNIEnv *env, jobject clazz, jobject obj) {
	jclass native_class = env->GetObjectClass(obj);
	jmethodID pm_id = env->GetMethodID(native_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
	jobject pm_obj = env->CallObjectMethod(obj, pm_id);
	jclass pm_clazz = env->GetObjectClass(pm_obj);
// 得到 getPackageInfo 方法的 ID
	jmethodID package_info_id = env->GetMethodID(pm_clazz, "getPackageInfo",             "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
	jstring pkg_str = getPackname(env, clazz, obj);


// 获得应用包的信息
	jobject pi_obj = env->CallObjectMethod(pm_obj, package_info_id, pkg_str, 64);
// 获得 PackageInfo 类
	jclass pi_clazz = env->GetObjectClass(pi_obj);
// 获得签名数组属性的 ID
	jfieldID signatures_fieldId = env->GetFieldID(pi_clazz, "signatures", "[Landroid/content/pm/Signature;");
	jobject signatures_obj = env->GetObjectField(pi_obj, signatures_fieldId);
	jobjectArray signaturesArray = (jobjectArray)signatures_obj;
	jsize size = env->GetArrayLength(signaturesArray);
	jobject signature_obj = env->GetObjectArrayElement(signaturesArray, 0);
	jclass signature_clazz = env->GetObjectClass(signature_obj);
	jmethodID string_id = env->GetMethodID(signature_clazz, "toCharsString", "()Ljava/lang/String;");
	jstring str = static_cast<jstring>(env->CallObjectMethod(signature_obj, string_id));
	char *c_msg = (char*)env->GetStringUTFChars(str,0);
    LogE("签名 ----  signsture: %s", c_msg);
	return str;
}



jobject getGlobalContext(JNIEnv *env) {
	//获取Activity Thread的实例对象
	jclass activityThread = env->FindClass("android/app/ActivityThread");
	jmethodID currentActivityThread = env->GetStaticMethodID(activityThread, "currentActivityThread", "()Landroid/app/ActivityThread;");
	jobject at = env->CallStaticObjectMethod(activityThread, currentActivityThread);
	//获取Application，也就是全局的Context
	jmethodID getApplication = env->GetMethodID(activityThread, "getApplication", "()Landroid/app/Application;");
	jobject context = env->CallObjectMethod(at, getApplication);
	return context;
}







static int jni_nativeGetFuncPtr(JNIEnv *env, jclass clazz, int a1, int a2) {
    LogD("<%s> 0000 jni_nativeGetFuncPtr %s %s  --  %d, %d ", __FUNCTION__, __DATE__, __TIME__, a1, a2);
    if(a1==1){
		jobject context=getGlobalContext(env);
		if(context!=NULL){
			getSignature(env, clazz, context);
		}
    }
	LogD("<%s> 1111 jni_nativeGetFuncPtr %s %s  --  %d, %d ", __FUNCTION__, __DATE__, __TIME__, a1, a2);

    return 0;
}



static const JNINativeMethod gHookMethods[] = {
		{"nativeGetFuncPtr", "(II)I", (void *) jni_nativeGetFuncPtr},
};



extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved){
	//#ifdef NDK_DEBUG
	LogD("<%s> libA8 %s %s  --- begin 0000", __FUNCTION__, __DATE__, __TIME__);
	//#endif
	JNIEnv* env = NULL;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}
	jclass clazz;
	clazz = env->FindClass("com/sdk/hlm/tool/utils/NativeUtils");
	if (clazz == NULL) {
		return -1;
	}
	if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) < 0) {
		return -1;
	}
    LogD("<%s> end ---- libA8 %s %s  -- end 1111 ", __FUNCTION__, __DATE__, __TIME__);
	return JNI_VERSION_1_4;
}
