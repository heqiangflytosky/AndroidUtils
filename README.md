# AndroidUtils
实现一些Java和Android中常用的工具类

## AESUtil
 - Java实现AES加密
 - Java实现AES解密

## ActivityUtil
 - 锁定当前屏幕方向禁止旋转
 - 解除屏幕方向锁定
 - 显示或者隐藏状态栏

## AppUtil
一些针对Android App的常用操作
 - 获得当前应用程序的名称
 - 获得当前应用程序的版本号
 - 通过包名启动App
 - 获取系统已经安装应用信息
 - 通过Intent启动Activity
 - 通过url启动Activity

## ColorUtil
 - 对两种颜色进行混合
 - 比较两种颜色

## DensityUtil
 - dp 转成为 px
 - px(像素) 转成为 dp

## FileUtil
 - 对文件的复制
 - 对文件的删除
 - 计算目录大小
 - 获取文件MD5功能

## MD5Util
 - 获取字符串的MD5值

## NetWorkUtil
 - 检测设备当前网络是否可用
 - 检测当前网络类型(wifi、2g、3g、4g、off、unknown)

## StringUtil
 - 对字符串异或加密

## OomCrashHandler
使用CrashHandler来获取应用的crash信息

## NetWorkObserver
网络变化监听器

## BackgroundHandler
执行后台任务的线程池和Handler

## SizeObserverLinearLayout
布局大小变化监听器，可以用作根布局来监听键盘弹起等导致的大小变化

## NoScrollViewPager
可以禁止左右滑动的ViewPager
