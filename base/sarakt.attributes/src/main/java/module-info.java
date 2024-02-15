module sarakt.attributes {
    
    opens bg.sarakt.attributes.conf to spring.core;
    opens bg.sarakt.attributes to spring.core;
    opens bg.sarakt.attributes.impl to spring.core, com.fasterxml.jackson.databind;
    
    exports bg.sarakt.attributes.conf;// to spring.beans, spring.context;
    
    opens bg.sarakt.attributes.levels.simple;
    opens bg.sarakt.attributes.resources;
    opens bg.sarakt.attributes.secondary;
    opens bg.sarakt.attributes.utils;
    
    exports bg.sarakt.attributes;
    exports bg.sarakt.attributes.impl;
    exports bg.sarakt.attributes.levels;
    exports bg.sarakt.attributes.primary;
    exports bg.sarakt.attributes.secondary;
    exports bg.sarakt.attributes.resources;
    exports bg.sarakt.attributes.services;
    exports bg.sarakt.attributes.utils;
    
    requires org.hibernate.orm.core;
    
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires sarakt.base;
    requires sarakt.logging;
    requires spring.beans;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires java.instrument;
    requires spring.context;
}