# Spring Boot Rest API
*Technologies used in project:*
**Spring Boot 3, Spring Security, MySQL, Spring Data JPA, FlyWay, MapStruct**

This is a simple Blog rest API project, that allows you to view all users posts, create personal account and perform CRUD operations with your own posts.
Admins can perform CRUD operations on all users posts.

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/gamlethot/my-blog-springboot-v1.git
```

**2. Create Mysql database**
```bash
create database my_blog
```


**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`
+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

## How to Run

* Build the project by running `mvn clean package`
* Once successfully built, run the service by using the following command:
```
java -jar target/my-blog-springboot-v1-0.0.1-SNAPSHOT.jar
```
* Or using maven command:
```bash
mvn spring-boot:run
```
**The app will start running at <http://localhost:8080>**

## Things to start with
After first successful launch of the application, the FlyWay database migration
will fill the tables with data. You will find 3 accounts and 2 posts.

Account #1 is author of Post #1
```
Account #1
login: user1@mail.com
pass: password
```
Account #2 is author of Post #2
```
Account #2
login: user2@mail.com
pass: password
```
Account #3 is an Admin
```
Account #3
login: admin1@mail.com
pass: password
```
You can **use them to test the application** functionality.

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | /blog/register | Sign up | [JSON](#signup) |

* Once account created, **perform authentication via PostMan** (Authorization -> Basic Auth), 
to access endpoints that require logged-in user

### Posts

| Method | Url                     | Description                                                                | Sample Valid Request Body |
| ------ |-------------------------|----------------------------------------------------------------------------| ------------------------- |
| GET    | /blog/posts             | Get all posts                                                              | |
| GET    | /blog/posts/{id}        | Get post by id                                                             | |
| GET    | /blog/posts/my          | Get user posts (by logged-in user)                                         | |
| POST   | /blog/posts/create      | Create new post (by logged-in user)                                        | [JSON](#postcreate) |
| PUT    | /blog/posts/edit/{id}   | Update post (If post belongs to logged-in user or logged in user is admin) | [JSON](#postupdate) |
| DELETE | /blog/posts/delete/{id} | Delete post (If post belongs to logged-in user or logged in user is admin) | |


## Sample Valid JSON Request Bodys

##### <a id="signup">Sign Up -> /blog/register</a>
```json
{
  "firstName": "Johnny",
  "lastName": "Sins",
  "email": "johnny@mail.com",
  "password": "password"
}
```

##### <a id="postcreate">Create Post -> /blog/posts/create</a>
```json
{
  "title": "Post Title can not be blank and limited to 50 chars",
  "body": "Post body can not be blank and limited to 10 000 chars"
}
```

##### <a id="postupdate">Update Post -> /blog/posts/edit/{id}</a>
```json
{
  "title": "Updated Title",
  "body": "Updated Body "
}
```