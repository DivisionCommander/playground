module sarakt.storing {
    
    opens bg.sarakt.glossary.entitites to org.hibernate.orm.core;
    opens bg.sarakt.storing.hibernate.mapping to org.hibernate.orm.core, spring.beans;
    opens bg.sarakt.storing.hibernate.entities to org.hibernate.orm.core, spring.beans;
    
    opens bg.sarakt.storing to spring.core;
    opens bg.sarakt.storing.conf to spring.core;
    opens bg.sarakt.storing.utils to spring.core;
    opens bg.sarakt.storing.hibernate to spring.core, org.hibernate.orm.core;
    
    exports bg.sarakt.glossary;
    exports bg.sarakt.glossary.entitites;
    exports bg.sarakt.storing;
    exports bg.sarakt.storing.conf;
    exports bg.sarakt.storing.utils;
    exports bg.sarakt.storing.hibernate;
    exports bg.sarakt.storing.hibernate.mapping;
    exports bg.sarakt.storing.hibernate.entities;
    exports bg.sarakt.storing.hibernate.interfaces;
    
    requires jakarta.annotation;
    requires jakarta.persistence;
    
    requires sarakt.base;
    requires sarakt.logging;
    requires spring.beans;
    requires spring.context;
    requires spring.tx;
    requires spring.core;
    requires spring.boot.autoconfigure;
    
    requires transitive org.hibernate.orm.core;
    requires transitive sarakt.characters;
    requires transitive sarakt.attributes;
}