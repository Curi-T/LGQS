# 理工轻食

文档：https://curi-t.github.io/LGQS/

## 一、后台系统

1. Idea拉取项目

2. 刷新Maven依赖，等待下载完成

3. 修改LGQS-db的application-db.yml中的数据库的地址和密码

3. 新建数据库lgqs，导入执行LGQS-db的resources目录下的lgqs.sql脚本

4. 启动LGQS-all的Application启动类

5. 启动成功后，访问http://localhost:8080/wx/index/index，出现下列数据则完全启动成功

   ```json
   {
       "errno":0,
       "data":"hello world, this is wx service",
       "errmsg":"成功"
   }
   ```

   访问http://localhost:8080/admin/index/index

   ```json
   {
       "errno":0,
       "data":"hello world, this is admin service",
       "errmsg":"成功"
   }
   ```

## 二、管理后台

1. 安装node.js环境，配置环境变量，自行查找资料配置

2. 终端进入Page\LGQS-admin目录下，依次执行指令

   ```sh
   cd Page/LGQS-admin
   npm install
   npm run dev
   ```

   等待管理后台启动，首次启动会比较慢

## 三、小程序前台

1. 下载并安装微信开发者工具

2. 导入项目，AppID不要选默认的，选择测试号，或者自己提前注册一个AppID，不使用云开发

   <img src="https://raw.githubusercontent.com/Curi-T/picture/main/typora/image-20220707001714178.png" alt="image-20220707001714178" style="zoom: 67%;" />

3. 导入完成后，打开config目录下的api.js文件，将第一行改为

   ```js
   var WxApiRoot = 'http://localhost:8080/wx/';
   ```

4. 点击上方编译，编译完成后即可显示大致框架

## 四、新增数据

1. 通过第二步打开的管理后台，首先新增餐品一级类目
2. 然后在此一级类目下新增二级类目
3. 之后新增餐品，输入合理数据即可新增成
4. 去往微信小程序点击刷新后即可查看新增餐品数据

