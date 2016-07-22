## Pay

各种支付SDK的集合与重构

### Build Tools

使用之前务必配置[Maven私服](http://172.16.8.21:20000/helper/nexus-maven-repo)

#### Maven
```xml
<dependency>
    <groupId>com.boyuanitsm</groupId>
    <artifactId>pay</artifactId>
    <version>${pay.version}</version>
</dependency>
```
#### Gradle
```
compile "com.boyuanitsm:pay:${pay.version}"
```

### [Configure(pay.yml)](docs/pay.yml.md)

### API

- [Wxpay](docs/wepay/wepay.md)
- [Alipay](docs/alipay/alipay.md)

### Development

```
./gradlew
```
