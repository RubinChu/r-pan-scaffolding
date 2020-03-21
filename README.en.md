# r-pan-scaffolding

#### Description
Source scaffolding for a personal file management system,[r-pan](http://pan.rubinchu.com)based on this scaffolding construction, I hope it will be helpful to friends who have a personal blog quickly.

#### Website image


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
