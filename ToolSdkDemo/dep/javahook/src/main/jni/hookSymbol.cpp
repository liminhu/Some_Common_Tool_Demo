#include <string.h>
#include "hookutils.h"

int sLockGold = 0, sLockGem = 0;
int sLockNOCD = 0, sLockMana = 0, skill = 0;
int speed = 0;
bool sLockPotion = true;
int sLockRepository = 0;

void doProcessCheat(int flag, int arg1, int arg2) {
    switch (flag) {
        case 1:
            sLockGold = arg1;
            break;
        case 2:
            sLockGem = arg1;
            break;
        case 3:
            sLockRepository = arg1;
            break;

        case 4:
            sLockNOCD = arg1;
            break;

        case 5:
            sLockMana = arg1;
            break;//MP


        case 6:
            arg1 ? sLockPotion = true : sLockPotion = false;
            break;//HP

        case 7:
            speed = arg1;
            break;

        case 8:
            skill = arg1;
            break;
    }
}

int (*old_getGem)(void *r0) = NULL;

int getGem(void *r0) {
#ifdef NDK_DEBUG
    LogD("<%s> sLockGem = %d", __FUNCTION__, sLockGem);
#endif
    return sLockGem ? 992599 : old_getGem(r0);
}

int (*old_getGold)(void *r0) = NULL;

int getGold(void *r0) {
#ifdef NDK_DEBUG
    LogD("<%s> sLockGold = %d", __FUNCTION__, sLockGold);
#endif
    return sLockGold ? 992599 : old_getGold(r0);
}

int *(*old_CheckIfUserIsBanned)(int *, int, int, int) = NULL;

int *CheckIfUserIsBanned(int *r0, int r1, int r2, int r3) {
    *r0 = 0;
#ifdef NDK_DEBUG
    LogD("<%s> r0 = %d,r0+4 = %d, r0+8 = %d", __FUNCTION__,*r0,*(r0+1),*(r0+2));
#endif
    old_CheckIfUserIsBanned(r0, r1, r2, r3);
    *r0 = 0;
#ifdef NDK_DEBUG
    LogD("<%s> r0 = %d,r0+4 = %d, r0+8 = %d", __FUNCTION__,*r0,*(r0+1),*(r0+2));
#endif
    return r0;
}

void (*old_setSkillCD)(void *r0, void *r1, int r2) = NULL;

void setSkillCD(void *r0, void *r1, int r2) {
#ifdef NDK_DEBUG
    LogD("<%s> r2 = %d, sLockNOCD=%d", __FUNCTION__, r2,sLockNOCD);
#endif
    old_setSkillCD(r0, r1, sLockNOCD ? 0 : r2);
}

int (*old_SkillEvent)(void *r0, int r1, int r2) = NULL;

int SkillEvent(void *r0, int r1, int r2) {
#ifdef NDK_DEBUG
    LogD("<%s> r1=%d, r2=%d,sLockMana = %d", __FUNCTION__, r1, r2,sLockMana);
#endif
    return sLockMana ? 0 : old_SkillEvent(r0, r1, r2);
}

int (*IsPlayer)(void *r0) = NULL;

int (*addSkillpoint)(void *r0, int r1) = NULL;

int (*fillall)(void *r0, int r1, int r2) = NULL;


int (*old_IsGodMP)(void *r0) = NULL;

int IsGodMP(void *r0) {
    if (sLockMana && IsPlayer && IsPlayer(r0)) {
        return 1;
    }
#ifdef NDK_DEBUG
    LogD("<%s> r1=%d, r2=%d,sLockMana = %d", __FUNCTION__, r1, r2,sLockMana);
#endif
    return old_IsGodMP(r0);
}

int (*old_IsGodHP)(void *r0) = NULL;

int IsGodHP(void *r0) {
    if (sLockPotion) {
        if (IsPlayer && IsPlayer(r0)) {
            return 1;
        }
    } else {
        return old_IsGodHP(r0);
    }
}


int (*old_setSpeed)(void *r0, float r1) = NULL;

int setSpeed(void *r0, float r1) {
    if (speed > 0) {
        return old_setSpeed(r0, r1 * speed);
    } else {
        return old_setSpeed(r0, r1);
    }
}

int (*old_update)(void *a1) = NULL;

int update(void *a1) {
    if (skill > 0) {
        int ret = old_update(a1 + skill);
        return ret;
    } else {
        int ret = old_update(a1);
        return ret;
    }
//	addSkillpoint(a1,1);

}

int (*old_mergeCost)(void *r0) = NULL;

int mergeCost(void *r0) {
    int ret = old_mergeCost(r0);
    fillall(r0, 54, 5);
    LogD("<%s> r1=%d", __FUNCTION__, 222);
    return ret;
}

int (*old_maxBonus)(void *r) = NULL;

int maxBonus(void *r0) {
    int ret = old_maxBonus(r0);
//	LogD("<%s> id=%d", __FUNCTION__, ret);
    return sLockRepository ? 999 : ret;
}




int (*old_nativeGetMainObbName)(void* r0) = NULL;
int my_nativeGetMainObbName(void* r0){
    int  ret =old_nativeGetMainObbName(r0);
    char *data="main.19029.com.gameloft.android.ANMP.GloftD4HM.obb";
    int len=strlen(data);
    char *data1=(char *)ret;
    for(int i=0; i<len ; i++){
        data1[i]=data[i];
    }
    data1[len]='\0';
    return ret;
}


const static HOOK_SYMBOL gHookSymbols[] = {
        {"_Z20nativeGetMainObbNamePc", (void *)&my_nativeGetMainObbName,(void **)&old_nativeGetMainObbName},   //修复闪退


        {"_ZNK12StoreManager15GetCurrencyGemsEv",                  (void *) &getGem,              (void **) &old_getGem},//锁定钻石
        {"_ZNK12StoreManager15GetCurrencyGoldEv",                  (void *) &getGold,             (void **) &old_getGold},//锁定金币
        {"_ZN20OnlineServiceRequest19CheckIfUserIsBannedERKSsS1_", (void *) &CheckIfUserIsBanned, (void **) &old_CheckIfUserIsBanned},//封号
        {"_ZN14SkillComponent19_StartCooldownTimerEP5Skilli",      (void *) &setSkillCD,          (void **) &old_setSkillCD},//技能无cd
//	{"_ZN11HUDControls10SkillEventEib", (void *)&SkillEvent,      (void **)&old_SkillEvent},
        {"_ZNK10GameObject7IsGodMPEv",                             (void *) &IsGodMP,             (void **) &old_IsGodMP},//无限蓝
        {"_ZNK10GameObject7IsGodHPEv",                             (void *) &IsGodHP,             (void **) &old_IsGodHP},//无限血
        {"_ZNK10GameObject6IsDeadEv",                              (void *) &update,              (void **) &old_update},// 增加技能点
        {"_ZN18AnimationComponent12SetAnimSpeedEf",                (void *) &setSpeed,            (void **) &old_setSpeed},//游戏加速
//	{"_ZN12StoreManager6UpdateEv",    (void *)&mergeCost,   (void **)&old_mergeCost},//无限血
        {"_ZNK18InventoryComponent20GetMaxLimitWithBonusEv",       (void *) &maxBonus,            (void **) &old_maxBonus},//仓库
//1.000000
};
const static FIND_SYMBOL gFindSymbols[] = {
        {"_ZNK10GameObject8IsPlayerEv",       (void **) &IsPlayer},
        {"_ZN10GameObject14AddSkillsPointEi", (void **) &addSkillpoint},
        {"_ZN12StoreManager8GiveItemEii",     (void **) &fillall},
};

void hook_symbols(soinfo *handle) {
#ifdef NDK_DEBUG
    LogD("<%s> %s %s handle = %x( funcs = %d)", __FUNCTION__, __DATE__,__TIME__,(int)handle, sizeof(gHookSymbols)/sizeof(gHookSymbols[0]));
#endif
    for (int i = 0; i < sizeof(gFindSymbols) / sizeof(gFindSymbols[0]); i++) {
        FIND_SYMBOL find = gFindSymbols[i];
        *find.func = dlsym(handle, find.symbol);
#ifdef NDK_DEBUG
        LogD("<HookSymbol> symbol = 0x%x : %s", (int)*find.func, find.symbol);
#endif
    }
    for (int i = 0; i < sizeof(gHookSymbols) / sizeof(gHookSymbols[0]); i++) {
        HOOK_SYMBOL hook = gHookSymbols[i];
        inlineHookSymbol(handle, hook.symbol, hook.new_func, hook.old_func);
#ifdef NDK_DEBUG
        LogD("<HookSymbol> symbol = %s(%x), new = 0x%x, old = 0x%x", hook.symbol, (int)getAddress(handle,hook.symbol ),(int)hook.new_func, (int)*hook.old_func);
#endif
    }
}
