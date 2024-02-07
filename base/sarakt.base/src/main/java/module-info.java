module sarakt.base {
    
    exports bg.sarakt.base.utils;
    exports bg.sarakt.base.exceptions;
    exports bg.sarakt.base;
    exports bg.sarakt.base.windows;
    
    requires jakarta.persistence;
    requires java.desktop;
    requires java.naming;
    requires transitive java.sql;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires transitive spring.orm;
    requires transitive spring.tx;
}