# Introduction

ftp-client-pool-root is a lightweight ftpclient connection pool toolkit based on ***commons-net*** and ***commons-pool2***. The purpose is to pool ftpclient for scenarios where ftp is used frequently to improve the performance of the program.

this project refers to the following open source projects

1. [XiaZengming/FtpClientPool](https://github.com/XiaZengming/FtpClientPool)
2. [jayknoxqu/ftp-pool: using commons-pool2 to implement a ftp pool](https://github.com/jayknoxqu/ftp-pool)

it is recommended to enable ***testOnBorrow*** and ***testOnReturn*** so that you can ensure that the connection you get is available every time.
when **FTPClientFactory** **create** FTPClient, it is already logged in. As long as the long connection is not disconnected, there will be no exit in the logged in state.

# Module

| Module                              | Description                                                  |
| ----------------------------------- | ------------------------------------------------------------ |
| ftp-client-pool                     | Lightweight FTPClient Connection Pool                        |
| spring-boot-starter-ftp-client-pool | SpringBoot Starter For Lightweight FTPClient Connection Pool |

# Install

## Maven

```xml
<dependency>
    <groupId>top.meethigher</groupId>
    <artifactId>ftp-client-pool</artifactId>
    <version>1.0</version>
</dependency>
```

If you are a springboot project

```xml
<dependency>
    <groupId>top.meethigher</groupId>
    <artifactId>spring-boot-starter-ftp-client-pool</artifactId>
    <version>1.0</version>
</dependency>
```

## Gradle

```
implementation 'top.meethigher:ftp-client-pool:1.0'
```

If you are a springboot project

```
implementation 'top.meethigher:spring-boot-starter-ftp-client-pool:1.0'
```

# Doc

## ftp-client-pool

developers manually obtain connections and return connections to the connection pool.

```java
public static void example1() throws Exception {
    FTPClientPool pool = new FTPClientPool(new FTPClientFactory(getPoolConfig()));
    FTPClient ftpClient = pool.borrowObject();
    try {
        FTPFile[] ftpFiles = ftpClient.listFiles("/");
        System.out.printf("连接池： %n numActive: %s %n numIdle: %s %n", pool.getNumActive(), pool.getNumIdle());
    } finally {
        pool.returnObject(ftpClient);
    }
    System.out.printf("连接池： %n numActive: %s %n numIdle: %s %n", pool.getNumActive(), pool.getNumIdle());
}
```

developers do not need to worry about the operations of obtaining and returning connections from the pool (the logic of obtaining and returning has been automatically implemented internally), and only need to focus on the actual business implementation.

```java
public static void example2() throws Exception {
    FTPClientPool pool = new FTPClientPool(new FTPClientFactory(getPoolConfig()));
    FTPAutoReleaser autoReleaser = new FTPAutoReleaser(pool);
    Integer total = autoReleaser.execute(ftpClient -> {
        FTPFile[] ftpFiles = ftpClient.listFiles("/");
        System.out.printf("连接池： %n numActive: %s %n numIdle: %s %n", pool.getNumActive(), pool.getNumIdle());
        return ftpFiles.length;
    });
    System.out.printf("连接池： %n numActive: %s %n numIdle: %s %n", pool.getNumActive(), pool.getNumIdle());
}
```

## spring-boot-starter-ftp-client-pool

first you need to configure your connection. 

for specific parameter meanings and default values, please refer to ***top.meethigher.ftp.client.pool.config.FTPPoolConfig***

eg: application.properties

```yml
ftp-client.pool.host=
ftp-client.pool.username=
ftp-client.pool.password=
ftp-client.pool.port=
ftp-client.pool.jmx-enabled=false
```

this time you only need to inject the object and you can use it.

```java
@SpringBootApplication
public class TempDemoApplication {
    @Component
    public static class TestRunner implements CommandLineRunner {
        @Resource
        private FTPClientPool ftpClientPool;
        @Resource
        private FTPAutoReleaser ftpAutoReleaser;
        @Override
        public void run(String... args) throws Exception {
            Integer total = ftpAutoReleaser.execute(FTP::list);
            System.out.println(total);
        }
    }
    public static void main(String[] args) {
        SpringApplication.run(TempDemoApplication.class, args);
    }
}
```

