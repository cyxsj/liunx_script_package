package cn.wolfcode.p2p;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 核心配置类
 * @EnableScheduling : 开启定时功能
 */
@SpringBootApplication
@MapperScan(basePackages="cn.wolfcode.p2p.*.mapper")
@EnableTransactionManagement
@EnableScheduling//开启定时功能
public class ApplicationCoreConfig {
}
