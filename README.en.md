# r-pan-scaffolding

#### Description
Source scaffolding for a personal file management system,[r-pan](http://pan.rubinchu.com)based on this scaffolding construction, I hope it will be helpful to friends who have a personal file system quickly.

#### Version 
v2.0

#### Website image
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

#### Software Architecture

SpringBoot 2.x Back office basic framework  
Mybaits Traditional ORM framework  
lombox getter setter plugin  
druid Database connection pool   
Vue2 + ElementUI Front-end basic framework  

#### Installation tutorial

 **_prompt:_** After running this project, the partners can import the project into the project, find all TODOs in the project, and modify them according to the prompts, which will greatly reduce the shelf modification time of the partners. All TODOs are the configuration information that I specially marked out for modification.
*  **Environment required to run this project:** JDK、MySQL、Nginx，please install before commissioning  
*  **About Nginx：** There are a lot of online information on Nginx installation. Please find the installation by yourself. 
*  **Anout Database：** Please install the Mysql database by yourself. After installation, run the sql script under the sql folder under resources to create the user table.
*  **About back-end projects：** Paste the source code into your partner's own project, find all TODO in the IDE and follow the prompts to modify the configuration. Just start the Spring Boot project.
*  **About front-end projects：** Paste the source code into your partner's own project, find all TODO in the IDE and follow the prompts to modify the configuration. Installation dependencies: npm i (or use cnpm i to accelerate), after installation, use npm run serve to start local debugging, and npm build to package the production environment.

#### Local debugging

*   **Commissioning project：** After the buddy has configured the environment according to the installation instructions above, start two projects to see the effect. Before starting, please configure all TODOs in the project, configure Nginx static resource forwarding, and start Nginx.

#### Online deployment instructions

*   **Deploy back-end projects:** The specific deployment details will not be mentioned (the small partners can deploy in their own way, such as jenkins deployment or manual package deployment). Here I have written the project startup script under the project's run folder. The small partners can Modify the jar package path in the script (if it is a war package deployment, please ignore this deployment instruction). After running the script, a pid.txt file and a pan.log log file will be generated. You can check the running process ID and Project output log
*   **Deploy front-end projects：** Friends can use npm run build to build a production environment package. The server can use Apache, Tomcat or Nginx and other web servers. There are many online materials, so I won’t repeat them here.

#### Written at the end

This project will be continuously updated, and I hope to make a little contribution to the small partners who have this need.