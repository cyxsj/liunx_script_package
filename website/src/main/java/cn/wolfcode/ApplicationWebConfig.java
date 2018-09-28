package cn.wolfcode;

import cn.wolfcode.p2p.ApplicationCoreConfig;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * website配置类
 */
@SpringBootApplication
@PropertySource("classpath:application-website.properties")
@EnableScheduling//开启定时功能
@Import(ApplicationCoreConfig.class)
public class ApplicationWebConfig {

    public static void main( String[] args ) {
        SpringApplication.run(ApplicationWebConfig.class,args);
    }
}
