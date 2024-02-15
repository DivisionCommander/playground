module sarakt.core {
    
    opens bg.sarakt.core to spring.core;
    
    exports bg.sarakt.maps.impls.arrays;
    exports bg.sarakt.core;
    exports bg.sarakt.core.window;
    exports bg.sarakt.maps;
    exports bg.sarakt.maps.impls;
    
    requires java.desktop;
    requires java.instrument;
    
    requires org.hibernate.orm.core;
    
    requires transitive sarakt.attributes;
    requires transitive sarakt.logging;
    requires sarakt.base;
    requires sarakt.characters;
    requires sarakt.storing;
    
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
}