### 需求背景

当合同（协议）双方，因为距离的原因，不方便面对面签字的情况下。在线上签名成为一个方便的解决方案。对于线上教育培训机构，需要与学员签订“网上课程购买协议”。

以前签署协议的场景是这样的：

[![0SaIqP.png](https://s1.ax1x.com/2020/09/24/0SaIqP.png)](https://imgchr.com/i/0SaIqP)

用了线上签名之后：

[![0SaHIS.png](https://s1.ax1x.com/2020/09/24/0SaHIS.png)](https://imgchr.com/i/0SaHIS)

### 实际效果

##### 电脑登录：

[![0SaLGQ.png](https://s1.ax1x.com/2020/09/24/0SaLGQ.png)](https://imgchr.com/i/0SaLGQ)

##### 手机登录：

[![0Sa7a8.jpg](https://s1.ax1x.com/2020/09/24/0Sa7a8.jpg)](https://imgchr.com/i/0Sa7a8)

##### 上传合同（协议）：

[![0Sa5rt.png](https://s1.ax1x.com/2020/09/24/0Sa5rt.png)](https://imgchr.com/i/0Sa5rt)

##### 签名：

[![0SaTVf.jpg](https://s1.ax1x.com/2020/09/24/0SaTVf.jpg)](https://imgchr.com/i/0SaTVf)

##### 签名后：

[![0SaqPg.jpg](https://s1.ax1x.com/2020/09/24/0SaqPg.jpg)](https://imgchr.com/i/0SaqPg)

### 实现方式

1. 用户在浏览器上，画出名字。通过canvas监听鼠标事件（手机浏览器监听 拖拽事件），从而生成画出图片。

2. 保存时，将图片的base64编码数据，发送到后台，通过itextpdf将图片贴到pdf上。



### 技术选型

#### 前端

vue-cli：作为前端整体框架搭建静态页面

Element-ui：作为系统样式及组件

canvas：用作签名时生成图片

pdf-js：用作展示pdf文件

### 后端

spring-boot：作为后端整体框架

jpa：用作数据库操作

jwt：用户身份校验（token）

itextpdf：将图片贴到pdf文件上


### 启动方式

#### sign-app（前端）

- 开发环境

1. npm install
2. npm run serve

- 生产环境

1. npm run build （生成发布包）
2. 将发布包通过nginx部署

#### sign-server

1. 执行数据库脚本sign.sql（mysql）

2. 运行程序

- 开发环境

1. 直接运行SpringBootApplication主类

- 生产环境

1. maven clean package （生成发布包）
2. java -jar jar包

