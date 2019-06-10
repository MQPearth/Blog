## Blog

1. vue+springboot+mybatis
2. RESTFUL API  
3. jwt-Spring Security权限管理
4. swagger
5. 前后端分离
6. mysql

### 原型设计

#### 首页

   > 未登录
   > ![首页](..\img\index_nologin.png)

#### 登录

   >![登录](..\img\login.png)

#### 注册

   >![注册](..\img\register.png)

#### 个人中心

   > ![个人中心](..\img\myblog_login.png)

#### 账号设置

   > ![账号设置](..\img\account.png)

#### 写新博文

   > ![写新博文](..\img\writeNewBlog.png)

#### 博文详情

   > ![博文详情](..\img\blog.png)

#### 后台管理

   > ![后台管理](..\img\admin.png)

#### 留言板

   > ![留言板](..\img\messageBoard.png)

#### 公告

   > ![公告](..\img\announcement.png)

### 数据库设计

##### 用户_user

| 字段名        | 类型    | 长度 |                  备注 |
| ------------- | ------- | ---- | --------------------: |
| user_id       | int     |      |      用户唯一id--主键 |
| user_name     | varchar | 30   |      用户名--不能重复 |
| user_password | varchar | 255  |              用户密码 |
| user_mail     | varchar | 50   |              用户邮箱 |
| user_state    | int     |      | 用户状态 0 封禁 1正常 |

##### 登录_login

| 字段名     | 类型     | 长度 |         备注 |
| :--------- | :------- | ---- | -----------: |
| login_time | datetime |      | 最后登录时间 |
| login_ip   | varchar  | 16   |   最后登录ip |
| user_id    | int      |      | 用户id--主键 |

##### 邮箱激活码_mailkey

| 字段名           | 类型     | 长度 |       备注 |
| ---------------- | -------- | ---- | ---------: |
| mailkey_id       | int      |      | 唯一主键id |
| mailkey_mail     | varchar  | 50   |   发送邮箱 |
| mailkey_code     | varchar  | 6    | 邮箱验证码 |
| mailkey_sendTime | datetime |      |   发送时间 |



##### 角色_role

| 字段名   | 类型 | 长度 |           备注 |
| :------- | :--- | :--- | -------------: |
| role_id | int  |      | 角色id--主键 |
|role_name   | varchar|30|角色名|

##### 用户_角色__user_role

| 字段名       | 类型 | 长度 |   备注 |
| ------------ | ---- | ---- | -----: |
| user_role_id | int  |      |        |
| user_id      | int  |      | 用户id |
| role_id      | int  |      | 角色id |



##### 个人标签_tag

| 字段名   | 类型    | 长度 |             备注 |
| :------- | :------ | :--- | ---------------: |
| tag_id   | int     |      | 唯一标签id--主键 |
| tag_name | varchar | 20   |           标签名 |
| user_id  | int     |      |           用户id |

##### 博文_blog

| 字段名            | 类型     | 长度 |                         备注 |
| ----------------- | -------- | ---- | ---------------------------: |
| blog_id           | int      |      |             唯一博文id--主键 |
| blog_title        | varchar  | 255  |                     博文标题 |
| blog_body         | text     |      |                     博文内容 |
| blog_discussCount | int      |      |                   博文评论数 |
| blog_blogViews    | int      |      |                   博文浏览数 |
| blog_time         | datetime |      |                 博文发布时间 |
| blog_state        | int      |      | 博文状态--0 删除(保留) 1正常 |
| user_id           | int      |      |                       用户id |

##### 博文标签_blog_tag

| 字段名  | 类型 | 长度 |             备注 |
| ------- | ---- | ---- | ---------------: |
| blog_id | int  |      | 博文id--复合主键 |
| tag_id  | int  |      | 标签id--复合主键 |

##### 留言_message

| 字段名       | 类型    | 长度 |                             备注 |
| ------------ | ------- | ---- | -------------------------------: |
| message_id   | int     |      |                       留言唯一id |
| message_name | varchar | 30   | 游客保存为ip地址，用户保存用户名 |
| message_body | varchar | 255  |                         留言主体 |

##### 公告_announcement

| 字段名             | 类型    | 长度 |                   备注 |
| ------------------ | ------- | ---- | ---------------------: |
| announcement_id    | int     |      |             公告唯一id |
| announcement_title | varchar | 255  |               公告标题 |
| announcement_body  | text    |      |               公告内容 |
| announcement_top   | int     |      | 是否置顶0 置顶 1未置顶 |

##### 评论_discuss

| 字段名       | 类型     | 长度 |       备注 |
| ------------ | -------- | ---- | ---------: |
| discuss_id   | int      |      | 评论唯一id |
| discuss_body | varchar  | 255  |   评论内容 |
| discuss_time | datetime |      |   评论时间 |
| user_id      | int      |      |     用户id |
| blog_id      | int      |      |     博文id |

##### 回复_reply

| 字段名       | 类型     | 长度 |         备注 |
| ------------ | -------- | ---- | -----------: |
| reply_id     | int      |      |   回复唯一id |
| reply_body   | varchar  | 255  |     回复内容 |
| reply_time   | datetime |      |     回复时间 |
| user_id      | int      |      |       用户id |
| discuss_id   | int      |      |       评论id |
| reply_rootid | int      |      | 父回复节点id |

##### 邀请码_code

| 字段名     | 类型    | 长度 |                       备注 |
| ---------- | ------- | ---- | -------------------------: |
| code_id    | varchar | 32   |                 邀请码主键 |
| code_state | int     |      | 激活码状态0 未使用 1已使用 |
| user_id    | int     |      |                     用户id |

