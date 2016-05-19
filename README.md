# android-workspace
android开发公共库

功能：
1、包含组件：	a、下拉刷新和上拉加载的listview
			b、FButton
			c、时间区间选择组件
			d、录音组件
2、YSingleProgressDialog：圆形进度条
3、YSingleToast：Toast
4、YJsonUtil：json
5、YDateUtils：日期转换
6、BaseActivity
7、CrashLogHandlerUtil：奔溃日志，保存到文件
8、Logger：格式化的log工具，可以将error级别的日志保存到文件
9、VolleyService：网络访问工具
10、扫码功能
11、GreenDao

使用步骤：
1、将两个库引入到项目中
2、将项目的AndroidManifest.xml文件中的application标签下添加：android:name="com.android.ysq.utils.application.BaseApplication"或者BaseApplication的子类
3、添加权限：	<uses-permission android:name="android.permission.INTERNET" />
			<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
4、如果需要扫码功能，则添加扫码库到项目中，并参照扫码库的AndroidManifest－orig.xml文件配置项目的AndroidManifest.xml
5、扫码类型支持配置可以修改扫码库的preferences.xml文件
