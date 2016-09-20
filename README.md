## Welcome to Pay!

各种支付SDK的集合与重构, 使用之前建议先了解各支付方式的支付流程，以便于更好的理解。支付流程在各API文档的最上方。

本项目不提供某个版本的长期支持，请各位开发者及时配合更新，并查看CHANGELOG

### Import from

使用之前务必配置[Maven私服](http://180.167.77.60:30000/ppd/nexus-maven-repo), `${pay.version}`请查看Tags

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

### Configure

- [pay.yml](docs/pay.yml.md)

### API

- [支付宝](docs/alipay/alipay.md)
- [微信支付](docs/wxpay/wxpay.md)
- [银联支付](docs/unionpay/unionpay.md)
    - [无跳转Token支付](docs/unionpay/token.md)
    - [网关支付](docs/unionpay/b2c.md)

### Sample

- [AlipayResource.java](src/test/java/com/boyuanitsm/pay/rest/AlipayResource.java)
- [UnionPayAcpResource.java](src/test/java/com/boyuanitsm/pay/rest/UnionPayAcpResource.java)
- [WeChatResource.java](src/test/java/com/boyuanitsm/pay/rest/WeChatResource.java)

### Development

```
./gradlew
```
