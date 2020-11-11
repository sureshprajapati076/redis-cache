# redis-cache

USE 

start redis server using **brew services start redis**

redis-cli monitor in cli

clear cache using **redis-cli FLUSHDB**

for remote redis:

```sh
redis-cli -u redis://<ip>:<port>
```

For AWS FARGATE deploy redis:latest as container definition [See](https://hub.docker.com/_/redis?tab=tags)
