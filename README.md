# r-pan-scaffolding

#### 版本
v1.0

#### 介绍

一个个人文件管理系统的源码脚手架，[r-pan](http://pan.rubinchu.com)基于此脚手架搭建，希望对有快速搭建个人文件管理系统的小伙伴有所帮助   

#### 网站图片
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/093944_ed89b5a3_1506368.png "WX20200321-093130@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/093953_0681943e_1506368.png "WX20200321-093154@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094001_bbf509d9_1506368.png "WX20200321-093254@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094010_fab361a4_1506368.png "WX20200321-093304@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094022_01d7ff6a_1506368.png "WX20200321-093351@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094029_246d5502_1506368.png "WX20200321-093413@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094039_2a92e402_1506368.png "WX20200321-093437@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094051_79c0b627_1506368.png "WX20200321-093458@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094101_76a6cdce_1506368.png "WX20200321-093538@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094109_f37c3c23_1506368.png "WX20200321-093627@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094118_ed112c2c_1506368.png "WX20200321-093705@2x.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0321/094127_f2604d8e_1506368.png "WX20200321-093724@2x.png")

#### 软件架构

SpringBoot 2.x 后台基本框架  
thymeleaf SpringBoot默认集成的模板引擎
jQuery 传统的前端框架
Mybaits 传统的ORM框架  
lombox getter setter插件  
druid 数据库连接池   

#### 安装教程

 **_提示：_** 运行此项目小伙伴可以将项目导入工程之后，查找工程中的所有TODO，并按照提示修改，会大大的减少小伙伴的架子修改时间，所有的TODO都是我特别标记出来要修改的配置信息
*  **运行此项目需要的环境：** JDK、MySQL、Nginx，请调试前自行安装   
*  **关于Nginx：** Nginx的安装网上资料很多，请自行查找安装，Nginx的静态资源转发的配置文件详见项目中resources下面的nginx文件夹下面的配置文件
*  **关于数据库：** Mysql数据库请自行安装。安装之后运行resources下面的sql文件夹下面的sql脚本，创建用户表。
*  **关于项目：** 将源码粘贴到小伙伴自己的项目中，在IDE中查找所有的TODO并按照提示修改配置。启动SpringBoot项目即可。

#### 本地调试

*   **调试项目：** 小伙伴按照上面的安装说明配置好环境之后，就像启动平常的SpringBoot项目一样启动此项目即可。启动之前请配置好项目中所有的TODO，并配置好Nginx静态资源转发，并启动Nginx。


#### 线上部署说明

*   **部署项目：** 具体的部署细节就不说了（小伙伴可以自己喜欢的方式部署，比如jenkins部署或者手动传包部署），这里我已经在项目的run文件夹下面写好了项目的启动脚本，小伙伴们可以修改一下脚本里面的jar包路径（如果是war包部署请忽略本条部署说明），运行脚本之后，会生成一个pid.txt文件和一个pan.log的日志文件，小伙伴可以查看运行的进程号和项目输出日志

