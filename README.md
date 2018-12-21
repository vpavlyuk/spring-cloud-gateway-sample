# Reproducing https://github.com/spring-cloud/spring-cloud-gateway/issues/728

`java.lang.AssertionError: Response header 'Access-Control-Allow-Origin' expected:<[http://wat.com]> but was:<[http://wat.com, http://wat.com]>`

Then downgrade in pom.xml to spring-boot-starter-parent version 2.0.5.RELEASE, and to Finchley.BUILD-SNAPSHOT. The test passes.

