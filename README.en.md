# r-pan-scaffolding

#### Description
Source scaffolding for a personal file management system,[r-pan](https://pan.rubinchu.com)based on this scaffolding construction, I hope it will be helpful to friends who have a personal file system quickly.

#### Version 
v3.1

#### Version Updates
* Bulk uploads have been added
* Bulk download functionality has been added
* Optimized table structure and query efficiency
* Fixed some bugs

#### Iterate directions for subsequent releases
* Optimize your website UI
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

#### Installation tutorial

 **_prompt:_** After running this project, the partners can import the project into the project, find all TODOs in the project, and modify them according to the prompts, which will greatly reduce the shelf modification time of the partners. All TODOs are the configuration information that I specially marked out for modification.
*  **Environment required to run this project:** JDK、MySQL,please install before commissioning  
*  **Anout Database：** Please install the Mysql database by yourself. After installation, run the sql script under the sql folder under resources to create the user table.
*  **About back-end projects：** Paste the source code into your partner's own project, find all TODO in the IDE and follow the prompts to modify the configuration. Just start the Spring Boot project.
*  **About front-end projects：** Paste the source code into your partner's own project, find all TODO in the IDE and follow the prompts to modify the configuration. Installation dependencies: npm i (or use cnpm i to accelerate), after installation, use npm run serve to start local debugging, and npm build to package the production environment.

#### Local debugging

*   **Commissioning project：** After the buddy has configured the environment according to the installation instructions above, start two projects to see the effect. Before starting, please configure all TODOs in the project.

#### Online deployment instructions

*   **Deploy back-end projects:** The specific deployment details will not be mentioned (the small partners can deploy in their own way, such as jenkins deployment or manual package deployment). Here I have written the project startup script under the project's run folder. The small partners can Modify the jar package path in the script (if it is a war package deployment, please ignore this deployment instruction). After running the script, a pid.txt file and a pan.log log file will be generated. You can check the running process ID and Project output log
*   **Deploy front-end projects：** Friends can use npm run build to build a production environment package. The server can use Apache, Tomcat or Nginx and other web servers. There are many online materials, so I won’t repeat them here.

#### Written at the end

This project will be continuously updated, and I hope to make a little contribution to the small partners who have this need.Have a problem to be able to private letter author or add QQ: 834123196.