# VService Web

Add below into **pom.xml**.

```
<dependency>
  <groupId>com.tsengfhy.vservice</groupId>
  <artifactId>vservice-basic-web</artifactId>
</dependency>
```

## Highly customizable

As a highly adaptive framework, return different response data to different terminals is necessary, 
we can touch it by adding customizable http headers. </br>
It means if you want json in response data, please add `"Accept": "application/json"`. 
In contrast, if you are browsing web pages, browser will add `"Accept": "text/html"` by default. </br>
The same rules apply to i18n as well.

## Exception handle

**VService** will handle all exceptions, and if request contains `"Accept": "application/json"`, response data format show as below.

```
{
    "data": "",
    "message": "",
    "path": "",
    "status": 200,
    "timestamp": "2019-07-09 20:53:37"
}
```


## Validation handle

If request validate failed, **VService** will handle them and add `errors` into response show as below.

```
{
    "data": "",
    "errors": {},
    "message": "",
    "path": "",
    "status": 200,
    "timestamp": "2019-07-09 20:53:37"
}
```

