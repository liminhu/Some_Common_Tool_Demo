#include "hookutils.h"
/*
	int (*oldlockgini)(int a1, int a2)=NULL;
		int lockgini(int a1, int a2){
			return 100;
		}*/
const static HOOK_Address gHookAddress[] = {
	  //{ 0x1CCF38, (void *)&lockgini, (void **)&oldlockgini,{0,0,0,0,0}}, //现代战争5 无限子弹地址:005E94D8 /* v68 = sub_3AABA0(*(_DWORD *)(_R4 + 60));  sub_383ACC(_R4 + 56, v68 - 1); v68改成25*/
};


void hook_address(int baseAddr){

    #ifdef NDK_DEBUG
	LogD("<%s> %s %s  baseAddr = 0x%x ", __FUNCTION__, __DATE__,__TIME__, baseAddr);
	 #endif

	for(unsigned int i = 0; i < sizeof(gHookAddress)/sizeof(gHookAddress[0]); i++){
		HOOK_Address hook = gHookAddress[i];

		if( *hook.old_func != NULL){
	 #ifdef NDK_DEBUG
			LogD("<%s> address %x already hooked %x", __FUNCTION__, hook.address, (int)*hook.old_func);
		 	#endif
			continue;
		}
		
//		if(memcmp( (void *)(baseAddr + hook.address), (void *)hook.disamble, DISAMBLE_CNT * sizeof(int)) != 0 ){
		//	#ifdef NDK_DEBUG
//			LogD("<%s> address %x content fail", __FUNCTION__, (int)hook.address);
		//	#endif
//			continue;
//		}

		inlineHookAddress( (void *)(baseAddr + hook.address), hook.new_func, hook.old_func);
 #ifdef NDK_DEBUG
		LogD("<%s> address %x hooked to new 0x%x, (old 0x%x)", __FUNCTION__, (int)hook.address,(int) hook.new_func, (int)*hook.old_func);
 	#endif
	}
}
