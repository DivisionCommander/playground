module sarakt.attributes {
    
    opens bg.sarakt.attributes.impl to spring.core, com.fasterxml.jackson.databind;
    
    exports bg.sarakt.attributes;
    exports bg.sarakt.attributes.impl;
    exports bg.sarakt.attributes.levels;
    exports bg.sarakt.attributes.levels.impl;
    exports bg.sarakt.attributes.services;
    exports bg.sarakt.attributes.utils;
    
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires sarakt.base;
    requires sarakt.logging;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
}