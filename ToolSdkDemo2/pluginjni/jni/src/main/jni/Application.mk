
APP_STL := stlport_static 


APP_CPPFLAGS += -fpermissive  #此项有效时表示宽松的编译形式，比如没有用到的代码中有错误也可以通过编译；

APP_CPPFLAGS += -fvisibility=hidden -O3

APP_ABI := armeabi-v7a

APP_PLATFORM=android-9
