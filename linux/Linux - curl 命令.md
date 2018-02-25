
# Linux - curl 命令

> Create Time : 2018年2月25日 Ref : http://ju.outofmemory.cn/entry/84875


## 简介

`curl` 是个很方便的Rest客户端。可以完成许多`Rest API`测试的需求，甚至，如果是需要先登入或认证的Rest API。利用`curl`指令，可以送出`HTTP GET , POST , PUT , DELETE` ，也可以改变HTTP header来满足使用Rest API需要的特定条件。

`curl的参数很多`,这边列出目前测试REST时常用到的：

```shell
-X/--request [GET|POST|PUT|DELETE]  使用指定的http method发出request
-H/--header                         设定requst header
-i/--include                        显示response的header
-d/--data                           设定http parameters
-v/--verbose                        输出比较多的讯息
-u/--user                           使用者账号，密码
-b/--cookie                         cookie
```

Linux command line 中，同一个功能常有两个功能完全相同的参数，一个是比较短的参数，前面使用`-`导引符号，另一个比较长的参数，通常会用`--`两个导引符号。

curl使用说明：
```shell
-X , --request  COMMAND  Specify request command to use 
     --resolve  HOST:PORT:ADDRESS Force resolve of HOST:PORT to ADDRESS
     --retry    NUM  Retry request NUM times if transient problems occur
     --retry-delay SECONDS When retrying, wait this many seconds between each
     --retry-max-time SECONDS  Retry only within this period>
```

参数-X跟--request两个功能是一样的，所以使用时`ex:curl -X POST http://www.example.com/` 跟`curl --request POST http://www.example.com`是相等的功能。

## GET/POST/PUT/DELETE使用方式

-X 后面加http  method ， 

```shell
curl -X GET "http://www.rest.com/api/users"
curl -X POST "http://www.rest.com/api/users"
curl -X PUT "http://www.rest.com/api/users"
curl -X DELETE "http://www.rest.com/api/users"
```

url也可以不加引号，如果有非纯英文字或数字外的字符，不加引号可能会出问题，如果编码过的url，也要加上引号。

## HEADER

在http header添加头部信息
```shell
curl -v -i -H "Content-Type: application/json" "http://www.rest.com/api/users"
```

## HTTP Parameter

http参数可以直接加载url的query string，也可以用`-d`带入，参数间用`&`串接，或使用多个`-d`。

```shell
# 使用&串接多个参数
curl -X POST -d "params1=value1&params2=value2"

# 也可使用多个-d，效果同上
curl -X POST -d "params1=value1" -d "params2=value2"
curl -X POST -d "params1=a space"
# "a space" url encode 后空白字符会编码成 "a%20space" ， 编码后的参数可以直接使用
curl -X POST -d "params1=a%20space"
```

## post json 格式

如同时需要传送request parameter跟json，request parameter可以加在url后面，json信息则放入`-d`的参数，然后利用单引号将json信息包含起来（如果json信息使用单引号，`-d`的参数则改用双引号包含），header要家如"Content-Type: application/json"跟"Accept: application/json"

```shell
curl http://www.example.com?modifier=kent -X PUT -i -H "Content-Type:application/json" -H "Accept:application/json" -d '{"boolean" : false, "foo" : "bar"}'
# 不加"Accept:application/json"也可以
curl http://www.example.com?modifier=kent -X PUT -i -H "Content-Type:application/json" -d '{"boolean" : false, "foo" : "bar"}'
```

## 需要先认证或登入才能使用的service

许多服务，需先进行登录或认证才能存取API服务，以服务要求的条件，curl可以通过cookie ， session或加入header加入session key，api key或认证的token来达到认证的效果。

### session例子

后端如果是用session记录使用的登录信息，后端会传一个session id给前端，前段需要在每次跟后端的request的header中置入此session id，后端便会以此session id识别前段是属于哪个session，以达到session的效果：

```shell
curl --request GET 'http://www.rest.com/api/users' --header 'sessionid:123456890987654321'
```

### cookie例子

如果是使用cookie，在认证后，后端会回一个cookie回来，把该cookie存成文件，当需要存取需要任务的url时，再用`-b cookie_file`的方式在request中植入cookie即可正常使用:

```shell
# 将cookie存档
curl -i -X POST -d username=kent -d password=kent123 -c ~/cookie.txt http://www.rest.com/auth

# 载入cookie到request中
curl -i --header "Accept:application/json" -X GET -b ~/cookie.txt http://www.rest.com/users/1
```

## 文件上传

```shell
curl -i -X POST -F 'file=@/Users/kent/my_file.txt' -F 'name=a_file_name'
```

这个是透过HTTP multipart POST 上传资料 ， `-F` 是使用http query parameter的方式，制定文件位置的参数要加上`@`。


## HTTP Basic Authentication (HTTP基本认证)

如果网站是采用HTTP基本认证，可以使用 --user username:password登入

```shell
curl -i --user kent:secret http://www.rest.com/api/foo
```

认证失败时，回事`401 Unauthorized`

```msg
HTTP/1.1 401 Unauthorized
Server: Apache-Coyote/1.1
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
WWW-Authenticate: Basic realm="Realm"
Content-Type: text/html;charset=utf-8
Content-Language: en
Content-Length: 1022
Date: Thu, 15 May 2014 06:32:49 GMT
```

认证通过时，会回应`200 OK`

```msg
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Set-Cookie: JSESSIONID=A75066DCC816CE31D8F69255DEB6C30B; Path=/mdserver/; HttpOnly
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 15 May 2014 06:14:11 GMT
```

可以把认证后的cookie存起来，重复使用

```shell
curl -i --user kent:secret http://www.rest.com/api/foo -c ~/cookies.txt
```

登入之前暂存的cookies，可以不用每次都认证

```shell
curl -i http://www.rest.com/api/foo -b ~/cookies.txt
```

