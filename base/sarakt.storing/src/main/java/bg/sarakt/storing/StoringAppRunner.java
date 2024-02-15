/*
 * AppRunner.java
 *
 * created at 2024-02-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing;

import java.math.BigInteger;

import bg.sarakt.attributes.services.AttributeService;
import bg.sarakt.attributes.utils.Attributes;
import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.utils.HibernateConf;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;
import bg.sarakt.storing.hibernate.interfaces.ILevelNodeDAO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(HibernateConf.class)
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = "bg.sarakt.storing.hibernate.entities")
@ComponentScan(scopedProxy = ScopedProxyMode.INTERFACES, basePackages =
    {
            "bg.sarakt", "bg.sarakt.base", "bg.sarakt.storing", "bg.sarakt.attributes.impl", "bg.sarakt.storing.hibernate"
})
public class StoringAppRunner implements Attributes {
    
    private static final int SWITCH = 1;
    ApplicationContext       ctx    = ApplicationContextProvider.getApplicationContext();
    
    public static void main(String[] args) {
        System.out.println("RUNNNN");
        SpringApplication.run(StoringAppRunner.class, args);
        System.out.println("Running");
        new StoringAppRunner().run();
    }
    
    public void run() {
        switch (SWITCH)
        {
        case 1:
            test1();
            break;
        default:
            System.out.println("Unsupported");
            break;
        
        }
    }
    
    private void test1() {
        System.err.println("Run run run");
        AttributeService ser = ctx.getBean(AttributeService.class);
        // System.out.println(ser);
        
        ser.loadDefaultAttributes();
        
        ILevelNodeDAO nodeDAO = ctx.getBean(ILevelNodeDAO.class);
        ILevelDAO levelDAO = ctx.getBean(ILevelDAO.class);
        
        // var levelNodeForClass = nodeDAO.getLevelNodeForClass(1, 3);
        // System.out.println(levelNodeForClass);
        // List<LevelNodeEntity> nodesForClass = nodeDAO.getNodesForClass(3);
        // nodesForClass.forEach(System.out::println);
        // NavigableMap<Integer, LevelEntity> allAsMap = levelDAO.allAsMap();
        // allAsMap.entrySet().forEach(System.out::println);
        // BigInteger maximalThreshold = levelDAO.maximalThreshold();
        // System.out.println(maximalThreshold);
        // List<LevelNodeEntity> a = nodeDAO.lookUpNodes(144, 3);
        // System.out.println(a.size());
        // a.forEach(System.out::println);
        // LevelNodeEntity lookUpNode = nodeDAO.lookUpNode(143, 3);
        //
        // System.err.println(lookUpNode);
        //
        // List<LevelNodeEntity> nodesForClass = nodeDAO.getClassNodes(3);
        // System.out.println(nodesForClass);
        // Collection<LevelNodeEntity> sortNodes = new
        // LevelMapper().sortNodes(nodesForClass);
        // System.out.println(sortNodes);
        BigInteger val = levelDAO.nextThreshold(BigInteger.valueOf(144));
        System.out.println(val);
        
    }
    
}
