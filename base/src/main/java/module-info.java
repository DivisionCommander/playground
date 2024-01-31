module sarakt {

    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires spring.jdbc;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.orm;
    requires spring.tx;
    requires java.naming;
    requires mysql.connector.j;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.data.commons;
    requires java.instrument;

    opens bg.sarakt.z.tools;
    opens bg.sarakt.base;
    opens bg.sarakt.characters.attributes1.impls to com.fasterxml.jackson.databind;
    opens bg.sarakt.storing.hibernate;
    opens bg.sarakt.storing.hibernate.entities;
    opens bg.sarakt.glossary.entitites;

}
