# r-pan-scaffolding

#### Description
Source scaffolding for a personal file management system,[r-pan](https://pan.rubinchu.com)based on this scaffolding construction, I hope it will be helpful to friends who have a personal file system quickly.

#### Version 
v4.0

#### Version Updates
* The front-end supports uploading large file fragments, which can be manually switched
* Use NIO to optimize file processing
* The backend architecture was adjusted and modular development was adopted
* The storage module supports local storage, FastDFS and Aliyun OSS, and supports manual switching of configuration files  
* The cache module supports local storage and Redis storage, and supports manual switching of configuration files 
* Added the scheduled task module
* Fixed some bugs

#### Iterate directions for subsequent releases
* Optimize your website UI
* Optimize the scheduled task module and integrate the current popular scheduled task framework
* Optimize uploads, support breakpoint continuation, folder uploads, etc
* Optimize download, support breakpoint continue download and folder packaging download and so on
* Optimize sharing, add chat and friend system
* Coming soon...

#### Website image
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

#### Software Architecture

SpringBoot 2.x Back office basic framework  
Mybaits Traditional ORM framework  
lombox getter setter plugin  
Redis cache middleware
druid Database connection pool    
Vue2 + ElementUI Front-end basic framework  

#### Details of the document

 The project details of the document please click [WIKI] (https://github.com/RubinChu/r-pan-scaffolding/wiki)  

#### Local debugging

*   **Commissioning project：** After the buddy has configured the environment according to the installation instructions above, start two projects to see the effect. Before starting, please configure all TODOs in the project.

#### Written at the end

This project will be continuously updated, and I hope to make a little contribution to the small partners who have this need.Have a problem to be able to private letter author.
