module sarakt.storing {
    
    opens bg.sarakt.glossary.entitites to org.hibernate.orm.core;
    opens bg.sarakt.storing.hibernate.entities to org.hibernate.orm.core;
    
    opens bg.sarakt.storing to spring.core;
    opens bg.sarakt.storing.utils to spring.core;
    opens bg.sarakt.storing.hibernate to spring.core;
    
    exports bg.sarakt.glossary;
    exports bg.sarakt.glossary.entitites;
    exports bg.sarakt.storing;
    exports bg.sarakt.storing.utils;
    exports bg.sarakt.storing.hibernate;
    exports bg.sarakt.storing.hibernate.entities;
    exports bg.sarakt.storing.hibernate.interfaces;
    
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires sarakt.base;
    requires sarakt.logging;
    requires spring.beans;
    requires spring.context;
    requires spring.tx;
    
    requires transitive org.hibernate.orm.core;
    requires transitive sarakt.characters;
    requires transitive sarakt.attributes;
}