## Welcome to Pay!

各种支付SDK的集合与重构, 使用之前建议先了解各支付方式的支付流程，以便于更好的理解。支付流程在各API文档的最上方

### Import from

使用之前务必配置[Maven私服](http://172.16.8.21:20000/helper/nexus-maven-repo)

[Version List](http://172.16.8.21:20000/pay/pay/tags)
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

- [Alipay](docs/alipay/alipay.md)
- [Wxpay](docs/wxpay/wxpay.md)
- UnionPay
    - [Acp](docs/unionpay/acp.md)

### Development

```
./gradlew
```

### TODO

- 银联支付接入
