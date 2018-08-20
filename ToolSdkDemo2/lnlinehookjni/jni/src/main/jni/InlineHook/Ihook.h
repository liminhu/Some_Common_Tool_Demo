#ifndef _IHOOK_H
#define _IHOOK_H

#include <stdio.h>
#include <android/log.h>
#include <errno.h>
#include <unistd.h>
#include <sys/mman.h>
#include <string.h>
#include <stdlib.h>
#include <sys/ptrace.h>
#include <stdbool.h>

#ifndef BYTE
#define BYTE unsigned char
#endif

#define OPCODEMAXLEN 12      //inline hook����Ҫ��opcodes��󳤶�,armΪ8��thumbΪ12/10����ΪҪ��һ��nop������������ȡ12����arm��ʱ��ֻmemcpy 8btye������
#define BACKUP_CODE_NUM_MAX 10  //���ܱ���ָ�����Ŀ�����thumb-2�µ�6��thumb16������Ϊ�˱������ѡ����10��

#define LOG_TAG "GToad"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args);

#define PAGE_START(addr)	(~(PAGE_SIZE - 1) & (addr))
#define SET_BIT0(addr)		(addr | 1)
#define CLEAR_BIT0(addr)	(addr & 0xFFFFFFFE)
#define TEST_BIT0(addr)		(addr & 1)

#define ACTION_ENABLE	0
#define ACTION_DISABLE	1

extern unsigned long _shellcode_start_s;
extern unsigned long _shellcode_end_s;
extern unsigned long _hookstub_function_addr_s;
extern unsigned long _old_function_addr_s;
extern unsigned long _shellcode_start_s_thumb;
extern unsigned long _shellcode_end_s_thumb;
extern unsigned long _hookstub_function_addr_s_thumb;
extern unsigned long _old_function_addr_s_thumb;

//hook����Ϣ
typedef struct tagINLINEHOOKINFO{
    void *pHookAddr;                //hook�ĵ�ַ
    void *pStubShellCodeAddr;            //����ȥ��shellcode stub�ĵ�ַ
    void (*onCallBack)(struct pt_regs *);       //�ص���������ת��ȥ�ĺ�����ַ
    void ** ppOldFuncAddr;             //shellcode �д��old function�ĵ�ַ
    BYTE szbyBackupOpcodes[OPCODEMAXLEN];    //ԭ����opcodes
    int backUpLength; //���ݴ���ĳ��ȣ�armģʽ��Ϊ8��thumbģʽ��Ϊ10��12
    int backUpFixLengthList[BACKUP_CODE_NUM_MAX]; //����
    uint32_t *pNewEntryForOldFunction;
} INLINE_HOOK_INFO;

bool ChangePageProperty(void *pAddress, size_t size);

extern void * GetModuleBaseAddr(pid_t pid, char* pszModuleName);

bool InitArmHookInfo(INLINE_HOOK_INFO* pstInlineHook);

bool BuildStub(INLINE_HOOK_INFO* pstInlineHook);

bool BuildArmJumpCode(void *pCurAddress , void *pJumpAddress);

bool BuildOldFunction(INLINE_HOOK_INFO* pstInlineHook);

bool RebuildHookTarget(INLINE_HOOK_INFO* pstInlineHook);

extern bool HookArm(INLINE_HOOK_INFO* pstInlineHook);

extern bool HookThumb(INLINE_HOOK_INFO* pstInlineHook);

#endif

