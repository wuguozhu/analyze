# analyze
**Build the project**
```
mvn package -Dmaven.test.skip=true
```
**Run the project**
```

```
## 作业
**使用spark streaming读取kafka数据， 将offset保存到zookeeper中** 

```
# 向kafka中生产数据
[root@cdsw1 ~]# kafka-console-producer  --broker-list cdsw1.anan.com:9092 --topic test
# 从kafka消费数据
kafka-console-consumer --bootstrap-server cdsw1.anan.com:9092 --topic test --from-beginning
```
