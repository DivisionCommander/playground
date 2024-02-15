module sarakt.tools {
    
    exports bg.sarakt.z.tools;
    
    opens bg.sarakt.z.tools to spring.core;
    
    requires java.instrument;
    
    requires jakarta.annotation;
    requires sarakt.base;
    requires sarakt.logging;
    requires sarakt.storing;
    requires sarakt.characters;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires transitive sarakt.attributes;
    
}