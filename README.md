# superPay Android SDK

## 目录
* [1. 简介](#1)
* [2. 版本要求](#2)
* [3. 快速体验](#3)
* [4. 工程配置及使用](#4)
    * [4.1 导入依赖包](#4.1)
    * [4.2 权限配置](#4.2)
    * [4.3 使用 superPay SDK](#4.3)
    * [4.4 superPay SDK 返回状态码说明](#4.4)
* [6. 注意事项](#6)
* [7. 常见问题](#issues)

## <h2 id='1'>简介</h2>
本项目为支付sdk的示例项目。

[![](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html) 
[![Codacy Badge](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/jeasonlzy/okhttp-OkGo/blob/master/LICENSE)
[![](https://img.shields.io/badge/superPay-v1.0.3-brightgreen.svg)](https://bintray.com/supay/superPay/superPay)
[![](https://img.shields.io/badge/wechatSdk-v5.1.4-brightgreen.svg)](https://bintray.com/wechat-sdk-team/maven/com.tencent.mm.opensdk%3Awechat-sdk-android-with-mta) 
[![](https://img.shields.io/badge/gson-v2.8.4-brightgreen.svg)](https://travis-ci.org/google/gson) 

## <h2 id='2'>版本要求</h2>
Android SDK 要求 Android 4.4 及以上版本
请使用 Java 8 或以上版本

## <h2 id='3'>快速体验</h2>
### Android Studio
导入 superpay-android-sdk 整个项目，修改成自己的微信appid，和包名 即可运行该demo。

<font color="red">需要注意: </font>测试微信支付，需要签名和包名与微信开放平台上的一致，才可支付成功。给出的demo并没给出正确的签名，会返回微信支付失败的结果。
<font color="red">导入 demo 中可能会遇到的开发环境版本问题，修改 build.gradle 中的版本</font>

## <h2 id='4'>工程配置及使用</h2>
### <h3 id='4.1'>一、导入依赖包</h3>

<font color='red'>(注：依赖渠道 SDK 时，可能会和其他第三方SDK有冲突，移除依赖冲突的包就可以。如：[问题二](#issue2)、[问题三](#issue3))</font>

#### Gradle 导入方式

```java
dependencies {
   //superPaysdk
   implementation 'com.superpay.androidsdk:superPay:1.0.3'
}
```

#### Maven 导入方式

```xml
<dependency>
  <groupId>com.superpay.androidsdk</groupId>
  <artifactId>superPay</artifactId>
  <version>1.0.3</version>
  <type>pom</type>
</dependency>
```


### <h3 id='4.2'>二、清单文件配置所需权限</h3>

<font color='red'>(注：有些权限是需要动态注册的,如 `READ_PHONE_STATE` 权限)</font>

``` xml
<!-- 通用权限 -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

### <h3 id='4.3'>三、使用 superPay SDK</h3>
#### 1. 清单文件注册相关类
- 微信支付需要注册
    <font color='red'> 注：
    1. 需要将以下“替换成自己 APK 的包名”换成在微信平台上注册填写的包名
    2. WxPayEntryActivity 这个类在 SDK 内部实现，开发者不需要额外实现该类
    </font>

```xml
<activity-alias                                                                
    android:name="你自己的 app 包名.wxapi.WXPayEntryActivity"           
    android:exported="true"                                                    
    android:targetActivity="com.recheng.superpay.pay.wechatpay.WeChatResult" />
```

- 支付宝支付需要注册

```xml
<activity                                                                   
    android:name="com.alipay.sdk.app.H5PayActivity"                         
    android:configChanges="orientation|keyboardHidden|navigation|screenSize"
    android:exported="false"                                                
    android:screenOrientation="behind"                                      
    android:windowSoftInputMode="adjustResize|stateHidden" >                
</activity>                                                                 
<activity                                                                   
    android:name="com.alipay.sdk.app.H5AuthActivity"                        
    android:configChanges="orientation|keyboardHidden|navigation"           
    android:exported="false"                                                
    android:screenOrientation="behind"                                      
    android:windowSoftInputMode="adjustResize|stateHidden" >                
</activity>                                                                                                                     
```
#### 2. 获取到 微信或支付宝的订单信息后，调起支付
##### 调起支付

因为 superPay 已经封装好了相应的调用方法，所以只需要调用支付方法即可调起支付控件：
(<font color='red'>注：该调用方法需要在主线程(UI 线程)完成</font>)
具体详情参考代码
```java
 PayParams.Builder payBuilder = new PayParams.Builder(MainActivity.this);                                                
 switch (rg.getCheckedRadioButtonId()) {                                                                                 
     case R.id.rbWechat:                                                                                                 
         payBuilder.payWay(PayWay.WechatPay);                                                                            
         //微信支付包名签名必须和官网一致  请注意!!!                                                                                 
         payBuilder.wechatAppID("填入你的微信appid");                                                                          
         break;                                                                                                          
     case R.id.rbAliPay:                                                                                                 
         payBuilder.payWay(PayWay.AliPay);                                                                               
         break;                                                                                                          
 }                                                                                                                       
 PayParams payParams = payBuilder.payInfo(editText.getText().toString()).build();                                        
 ChengPay.newInstance(payParams).doPay(new OnPayResultListener() {                                                       
     @Override                                                                                                           
     public void onPaySuccess(PayWay payWay) {                                                                           
         LogUtil.i("支付成功 " + payWay.toString());                                                                         
         Toast.makeText(MainActivity.this, "支付成功 " + payWay.toString(), Toast.LENGTH_LONG).show();                       
     }                                                                                                                   
                                                                                                                         
     @Override                                                                                                           
     public void onPayCancel(PayWay payWay) {                                                                            
         LogUtil.i("支付取消 " + payWay.toString());                                                                         
         Toast.makeText(MainActivity.this, "支付取消 " + payWay.toString(), Toast.LENGTH_LONG).show();                       
     }                                                                                                                   
                                                                                                                         
     @Override                                                                                                           
     public void onPayFailure(PayWay payWay, int errCode) {                                                              
         LogUtil.i("支付失败 " + payWay.toString() + errCode);                                                               
         Toast.makeText(MainActivity.this, "支付失败 " + payWay.toString() + errCode, Toast.LENGTH_LONG).show();             
     }                                                                                                                   
 });                                                                                                                     
```

### <h3 id='4.4'>四、superPay SDK 返回状态码说明 superPay SDK</h3>
#### 返回码 errCode
```
- 通用部分
  0; //支付成功                                                        
  1; // 网络连接失败                                  
  2; // 交易取消                                                   
  3; // SDK内部错误                                                                                     
- 微信部分                                                                
 -1; //可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
 -5; //不支持                                                 
 -6; //没有安装微信                                          
 -7; // 没设置微信appId                                         
- 支付宝部分                                                            
 8000; //正在处理中                                         
 4000;// 交易失败                                             
 5000;// 重复请求                                             
 6002; //网络链接出错                                                 
 6004; //支付结果未知                                              
 6005; //其他错误                                                 
```                                                                 

## 混淆设置
<font color='red'>(注：将以下对应渠道的混淆代码加到主 module 以及该 SDK 依赖所在的 module 中，不然会出现 jar 包重复或者找不到该类的问题，如：[问题二](#issue2))</font>

```
#suPay混淆过滤
-dontwarn com.recheng.**
-keep class com.recheng.**{*;}

#支付宝混淆过滤
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

#微信支付混淆过滤
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

##gson 混淆过滤
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

```

## <h2 id='6'>注意事项</h2>

- superPay SDK 可能会与友盟、百度地图等其他第三方 jar 包冲突，当同时使用这些 jar 包的时候用户需要根据情况判断保留哪一方的 jar 包。
- 新版建议使用 Android Studio

## <span id = "issues">常见问题</span>
### 问题一： 微信支付失败，返回 wx_err_code:-1

- 报错原因:
    微信验证 apk 的签名包名失败。
- 解决方案:
    1. 项目的 package 名字、AndroidManifest.xml 里面的包名，必须和微信开放平台注册的一致；
    2. 必须打包成为发布版本的 apk，apk 签名必须和在微信开放平台注册的一致，微信开放平台签名要求: MD5，无冒号；
    3. 清理微信缓存；
    4. 如果签名包名均正确，仍旧返回 -1 报错，请检查时间戳格式是否有问题或重置微信开放平台的安卓版本的签名包名。
    
###  <span id = "issue2">问题二：与其他第三方SDK有冲突</span>

- 报错Log:

```java
Error:Execution failed for task ':app:transformClassesWithJarMergingForDebug'.
com.android.build.api.transform.TransformException:
java.util.zip.ZipException: duplicate entry: a/a/a/a.class
```

- 报错原因:
    1. 没有加过滤混淆的代码
    2. 有重复的jar包存在

- 解决方案:
    2. 删除重复的 jar 包(可以是第三方SDK中的,也可以是 superPay SDK 中的jar包)
