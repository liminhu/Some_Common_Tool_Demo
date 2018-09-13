#include <vector>
#include <android/log.h>
#include <pthread.h>
#include <sys/ptrace.h>

#ifndef PAGE_SIZE
#define PAGE_SIZE 4096
#endif




#define PAGE_START(addr)	(~(PAGE_SIZE - 1) & (addr))
#define SET_BIT0(addr)		(addr | 1)
#define CLEAR_BIT0(addr)	(addr & 0xFFFFFFFE)
#define TEST_BIT0(addr)		(addr & 1)

#define ACTION_ENABLE	0
#define ACTION_DISABLE	1

extern "C"
{
    #include "Ihook.h"
}

void ModifyIBored() __attribute__((constructor));
void before_main() __attribute__((constructor));

typedef std::vector<INLINE_HOOK_INFO*> InlineHookInfoPVec;
static InlineHookInfoPVec gs_vecInlineHookInfo;     //����HOOK��

void before_main() {
    LOGI("Hook is auto loaded!\n");
}

/**
 * ����inline hook�ӿڣ��������inline hook��Ϣ
 * @param  pHookAddr     Ҫhook�ĵ�ַ
 * @param  onCallBack    Ҫ����Ļص�����
 * @return               inlinehook�Ƿ����óɹ����Ѿ����ù����ظ����÷���false��
 */
bool InlineHook(void *pHookAddr, void (*onCallBack)(struct pt_regs *))
{
    bool bRet = false;
    LOGI("InlineHook");

    if(pHookAddr == NULL || onCallBack == NULL)
    {
        return bRet;
    }

    INLINE_HOOK_INFO* pstInlineHook = new INLINE_HOOK_INFO();
    pstInlineHook->pHookAddr = pHookAddr;
    pstInlineHook->onCallBack = onCallBack;

    //DEMOֻ�ܶ�ARMָ�������ʾ����ͨ��������Ҫ�ж�����THUMB��ָ��
    if(TEST_BIT0((uint32_t)pstInlineHook->pHookAddr)){ //thumb mode
        LOGI("HookThumb Start.");
        if(HookThumb(pstInlineHook) == false)
        {
            LOGI("HookThumb fail.");
            delete pstInlineHook;
            return bRet;
        }
    }
    else{   
                                                 //arm mode
        if(HookArm(pstInlineHook) == false)
        {
            LOGI("HookArm fail.");
            delete pstInlineHook;
            return bRet;
        }
    }
    
    gs_vecInlineHookInfo.push_back(pstInlineHook);
    return true;
}

/**
 * ����ӿڣ�����ȡ��inline hook
 * @param  pHookAddr Ҫȡ��inline hook��λ��
 * @return           �Ƿ�ȡ���ɹ��������ڷ���ȡ��ʧ�ܣ�
 */
bool UnInlineHook(void *pHookAddr)
{
    bool bRet = false;

    if(pHookAddr == NULL)
    {
        return bRet;
    }

    InlineHookInfoPVec::iterator itr = gs_vecInlineHookInfo.begin();
    InlineHookInfoPVec::iterator itrend = gs_vecInlineHookInfo.end();

    for (; itr != itrend; ++itr)
    {
        if (pHookAddr == (*itr)->pHookAddr)
        {
            INLINE_HOOK_INFO* pTargetInlineHookInfo = (*itr);

            gs_vecInlineHookInfo.erase(itr);
            if(pTargetInlineHookInfo->pStubShellCodeAddr != NULL)
            {
                delete pTargetInlineHookInfo->pStubShellCodeAddr;
            }
            if(pTargetInlineHookInfo->ppOldFuncAddr)
            {
                delete *(pTargetInlineHookInfo->ppOldFuncAddr);
            }
            delete pTargetInlineHookInfo;
            bRet = true;
        }
    }

    return bRet;
}

/**
 * �û��Զ����stub������Ƕ����hook���У���ֱ�Ӳ����Ĵ����ȸı���Ϸ�߼�����
 * ���ｫR0�Ĵ�������Ϊ0x333��һ��Զ����30��ֵ
 * @param regs �Ĵ����ṹ������Ĵ�����ǰhook��ļĴ�����Ϣ
 */
void EvilHookStubFunctionForIBored(pt_regs *regs) //����regs����ָ��ջ�ϵ�һ�����ݽṹ���ɵڶ����ֵ�mov r0, sp�����ݡ�
{
    LOGI("In Evil Hook Stub.");
    //regs->uregs[2] = 0x333; //regs->uregs[0]=0x333
    regs->uregs[0]=0x333;
}

/**
 * ���IBoredӦ�ã�ͨ��inline hook�ı���Ϸ�߼��Ĳ��Ժ���
 */
void ModifyIBored()
{
    LOGI("In IHook's ModifyIBored.");

    
    //hellojni arm32 hook
    int target_offset = 0xf70; //��Hook��Ŀ����Ŀ��so�е�ƫ��
    bool is_target_thumb = false; //Ŀ���Ƿ���thumbģʽ��
    void* pModuleBaseAddr = GetModuleBaseAddr(-1, "libhellojni.so"); //Ŀ��so������
    

    //inline hook test3 thumb-2 hook
    //int target_offset = 0x43b8; //*��Hook��Ŀ����Ŀ��so�е�ƫ��*
    //bool is_target_thumb = true; //*Ŀ���Ƿ���thumbģʽ��*
    //void* pModuleBaseAddr = GetModuleBaseAddr(-1, "libnative-lib.so"); //Ŀ��so������

    if(pModuleBaseAddr == 0)
    {
        LOGI("get module base error.");
        return;
    }
    
    uint32_t uiHookAddr = (uint32_t)pModuleBaseAddr + target_offset; //��ʵHook���ڴ��ַ

    if(is_target_thumb){ //֮���������ж�������ΪNative Hook֮ǰ�϶���Ҫ�������һ�µģ���ʱ�����֪��������ģʽ�������Զ�ʶ��arm��thumb�Ƚ��鷳��
        uiHookAddr++;
        LOGI("uiHookAddr is %X in thumb mode", uiHookAddr);
    }
    else{
        LOGI("uiHookAddr is %X in arm mode", uiHookAddr);
    }
    
    InlineHook((void*)(uiHookAddr), EvilHookStubFunctionForIBored); //*�ڶ�����������Hook��Ҫ����Ĺ��ܴ�����*
}