# r-pan-scaffolding

#### Version
v1.0

#### Description
Source scaffolding for a personal file management system,[r-pan](http://pan.rubinchu.com)based on this scaffolding construction, I hope it will be helpful to friends who have a personal file system quickly.

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
thymeleaf SpringBoot's default template engine
jQuery Traditional front-end framework
Mybaits Traditional ORM framework  
lombox getter setter plugin  
druid Database connection pool   

#### Installation tutorial

 **_prompt:_** After running this project, the partners can import the project into the project, find all TODOs in the project, and modify them according to the prompts, which will greatly reduce the shelf modification time of the partners. All TODOs are the configuration information that I specially marked out for modification.
*  **Environment required to run this project:** JDK、MySQL、Nginx，please install before commissioning  
*  **About Nginx：** There are a lot of online information on Nginx installation. Please find the installation by yourself. For the configuration file of Nginx static resource forwarding, please refer to the configuration file under the nginx folder under resources in the project.
*  **Anout Database：** Please install the Mysql database by yourself. After installation, run the sql script under the sql folder under resources to create the user table.
*  **About Project：** Paste the source code into the partner's own project, find all TODOs in the IDE and modify the configuration according to the prompts. Just start the SpringBoot project.

#### Local debugging

*   **Commissioning Project：** After the partners have configured the environment according to the installation instructions above, they can start this project just like the usual SpringBoot project. Before starting, please configure all TODOs in the project, configure Nginx static resource forwarding, and start Nginx.


#### Online deployment instructions

*   **Deployment project:** The specific deployment details will not be mentioned (the small partners can deploy in their own way, such as jenkins deployment or manual package deployment). Here I have written the project startup script under the project's run folder. The small partners can Modify the jar package path in the script (if it is a war package deployment, please ignore this deployment instruction). After running the script, a pid.txt file and a pan.log log file will be generated. You can check the running process ID and Project output log
