Ementas

Apenas testado no Android 5.1.1 caso seja necessário tratar das novas permissões do 6.0+ pode não funcionar  

Notas:

compileSdkVersion 24
minSdkVersion 15
targetSdkVersion 24
Gradle: com.android.tools.build:gradle:2.2.0

res/layout/main_activity.xml         # For handsets (smaller than 600dp available width)
res/layout-w600dp/main_activity.xml  # Multi-pane (any screen with 600dp available width or more)

Caching ementas

Permissões necessárias:
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />