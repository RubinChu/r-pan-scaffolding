# r-pan-scaffolding

#### 介绍

一个个人文件管理系统的源码脚手架，[r-pan](https://pan.rubinchu.com)基于此脚手架搭建，希望对有快速搭建个人文件管理系统的小伙伴有所帮助  

#### 版本 
v4.0

#### 版本更新内容
* 前端支持了大文件分片上传，可通过配置手动切换
* 使用NIO优化文件处理
* 调整了后端架构，采用了模块化开发
* 存储模块支持了本地存储、FastDFS和阿里云OSS，并支持配置文件手动切换
* 缓存模块支持了本地存储和Redis存储，并支持配置文件手动切换
* 添加了定时任务模块
* 修复了一些BUG

#### 后续版本迭代方向
* 优化网站UI
* 优化定时任务模块，集成现有比较流行的定时任务框架
* 优化上传，支持断点续传、文件夹上传等等
* 优化下载，支持断点继续下载和文件夹打包下载等等
* 优化分享功能，加入聊天以及好友系统
* 敬请期待...

#### 网站图片
![输入图片说明](https://images.gitee.com/uploads/images/2020/0705/210618_7ae41201_1506368.png "WX20200705-204847@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0705/210628_247662dd_1506368.png "WX20200705-204906@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0705/210753_589c72c7_1506368.png "WX20200705-204925@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/183751_d5e53ee7_1506368.png "index.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0608/094301_52648a46_1506368.png "upload.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/183913_a617ed19_1506368.png "recycle.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/183939_28ea83e3_1506368.png "create-share.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/183951_b2bf6f2b_1506368.png "create-share-success.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/184010_f2e3a06c_1506368.png "share-list.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/184031_9b1add15_1506368.png "share-code.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/184042_800e098f_1506368.png "share-front.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0415/184055_a0ee67f8_1506368.png "share-expire.png")

#### 软件架构

SpringBoot 2.x 后台基本框架  
Mybaits 传统的ORM框架  
Redis 缓存中间件
lombox getter setter插件  
druid 数据库连接池    
Vue2 + ElementUI 前端基础框架   

#### 细节文档
该项目细节的文档请移步[WIKI](https://github.com/RubinChu/r-pan-scaffolding/wiki)

#### 写在最后

本项目会持续更新，希望对有此方面需求的小伙伴尽一点绵薄之力。有问题可以私信作者。

