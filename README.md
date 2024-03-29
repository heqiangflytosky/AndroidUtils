
实现一些Java和Android中常用的工具类

# Utils

## AESUtil
 - Java实现AES加密
 - Java实现AES解密

## AndroidUtil

 - 获取应用是否创建桌面快捷方式
 - 获取应用的权限信息：包括权限列表，及其所在的权限分组等
 - 获取AIDL进程间调用的调用方包名
 - 获取ContentProvider调用的调用方包名
 - 验证跨进程调用方的前面和本应用前面是否一致，可以用来做权限限制等

## ActivityUtil
 - 锁定当前屏幕方向禁止旋转
 - 解除屏幕方向锁定
 - 显示或者隐藏状态栏
 - 修改Activity样式为弹框样式
 - 获取Activity调用方的包名
 - 是否是从任务管理器启动

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
 - 颜色转换成灰度值
 - 判断颜色是否偏黑色

## DensityUtil

 - dp 转成为 px
 - px(像素) 转成为 dp
 - sp 转成 px

## DeviceUtil

 - 判断设备是否支持闪光灯
 - 获取屏幕可显示区域宽高
 - 获取设备屏幕宽高
 - 获取应用可绘制区域高度
 - 判断是否是异形屏
 - 获取异形屏区域高度
 - 适配异形屏

## FileUtil

 - 对文件的复制
 - 对文件的删除
 - 计算目录大小
 - 获取文件MD5功能
 - 解压zip文件
 - 获取文件扩展名

## MD5Util

 - 获取字符串的MD5值

## NetWorkUtil

 - 检测设备当前网络是否可用
 - 检测当前网络类型(wifi、2g、3g、4g、off、unknown)

## StringUtil

 - 对字符串异或加密

## CrashHandler

 - 使用CrashHandler来获取应用的crash信息
 - 解决 FinalizerWatchdogDaemon 线程的 TimeoutException 问题

## NetWorkObserver
网络变化监听器

## BackgroundHandler
执行后台任务的线程池和Handler

## AppExecutors
提供后台线程以及主线程的执行接口

## Base64
提供Base64的编解码方法

## AppContextUtils
提供一个全局的获取Application Context的方法

## FileCacheManager
简单LRU算法实现缓存大小的限制策略

## StorageUtil

 - 获取可用存储空间大小
 - 获取总存储空间大小

## UrlUtil

 - 对Url进行参数拼接

## ImageUtil

 - 合成两张图片

## FormatUtils

 - 字节数据格式化，转换为 KB，M，G等
 - 判断是否是手机号码

## ThreadUtils

 - 判断是否在主线程
 - 在主线程运行方法

## AbstractTask

 - 类似 AsyncTask，配合在线程池中使用的 Task

## ToastUtils
避免 Android 7.X 及以下版本的 BadTokenException 问题

## ViewUtil

 - 按比例缩放View
 - 按比例缩放，保持宽高的最大值和原来相同

# Views

## SizeObserverLinearLayout
布局大小变化监听器，可以用作根布局来监听键盘弹起等导致的大小变化

## NoScrollViewPager
可以禁止左右滑动的ViewPager

## BannerView
轮播组件

