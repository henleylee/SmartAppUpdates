# Android应用增量更新 - Smart App Updates

------

## 介绍

你所看到的，是一个用于Android应用程序增量更新的库。

包括客户端、服务端两部分代码。

## 原理

自从 Android 4.1 开始， [Google Play 引入了应用程序的增量更新功能][1]，App使用该升级方式，可节省约2/3的流量。

> Smart app updates is a new feature of Google Play that introduces a
> better way of delivering app updates to devices. When developers
> publish an update, Google Play now delivers only the bits that have
> changed to devices, rather than the entire APK. This makes the updates
> much lighter-weight in most cases, so they are faster to download,
> save the device’s battery, and conserve bandwidth usage on users’
> mobile data plan. On average, a smart app update is about 1/3 the
> sizeof a full APK update.

现在国内主流的应用市场也都支持应用的增量更新了。

增量更新的原理，就是将手机上已安装apk与服务器端最新apk进行二进制对比，得到差分包，用户更新程序时，只需要下载差分包，并在本地使用差分包与已安装apk，合成新版apk。

例如，当前手机中已安装微博V1，大小为12.8MB，现在微博发布了最新版V2，大小为15.4MB，我们对两个版本的apk文件差分比对之后，发现差异只有3M，那么用户就只需要要下载一个3M的差分包，使用旧版apk与这个差分包，合成得到一个新版本apk，提醒用户安装即可，不需要整包下载15.4M的微博V2版apk。

apk文件的差分、合成，可以通过 [开源的二进制比较工具 bsdiff][2] 来实现，又因为bsdiff依赖bzip2，所以我们还需要用到 [bzip2][3]

bsdiff中，`bsdiff.c` 用于生成差分包，`bspatch.c` 用于合成文件。 

弄清楚原理之后，我们想实现增量更新，共需要做3件事：

* 在服务器端，生成两个版本apk的差分包； 

* 在手机客户端，使用已安装的apk与这个差分包进行合成，得到新版的微博apk； 

* 校验新合成的apk文件是否完整，MD5或SHA1是否正确，如正确，则引导用户安装；