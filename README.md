# Reproducing https://github.com/spring-cloud/spring-cloud-gateway/issues/728

# Via DemogatewayApplicationTests:

`java.lang.AssertionError: Response header 'Access-Control-Allow-Origin' expected:<[http://wat.com]> but was:<[http://wat.com, http://wat.com]>`

Then downgrade in pom.xml to spring-boot-starter-parent version 2.0.5.RELEASE, and to Finchley.BUILD-SNAPSHOT. The test passes.

# Manually, with httpbin.org backend

```
curl -X GET \
  http://localhost:8080/get \
  -H 'access-control-request-method: GET' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'origin: http://wat.com' \
  -H 'postman-token: 2644ddee-e993-3db9-0a61-939fb69a5115'
```

Finchley train:
Response headers:
```
Access-Control-Allow-Credentials →true
Access-Control-Allow-Origin →http://wat.com
Cache-Control →no-cache, no-store, max-age=0, must-revalidate
Content-Length →836
Content-Type →application/json
Date →Fri, 21 Dec 2018 18:30:25 GMT
Expires →0
Pragma →no-cache
Server →gunicorn/19.9.0
Vary →Origin
Vary →Access-Control-Request-Method
Vary →Access-Control-Request-Headers
Via →1.1 vegur
X-Content-Type-Options →nosniff
X-Frame-Options →DENY
X-XSS-Protection →1 ; mode=block
```

Greenwitch:
Response headers:
```
Access-Control-Allow-Credentials →true
Access-Control-Allow-Credentials →true
Access-Control-Allow-Origin →http://wat.com
Access-Control-Allow-Origin →http://wat.com
Cache-Control →no-cache, no-store, max-age=0, must-revalidate
Content-Length →836
Content-Type →application/json
Date →Fri, 21 Dec 2018 18:36:35 GMT
Expires →0
Pragma →no-cache
Referrer-Policy →no-referrer
Server →gunicorn/19.9.0
Vary →Origin
Vary →Access-Control-Request-Method
Vary →Access-Control-Request-Headers
Via →1.1 vegur
X-Content-Type-Options →nosniff
X-Frame-Options →DENY
X-XSS-Protection →1 ; mode=block
```
