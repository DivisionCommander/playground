module sarakt.characters {
    
    exports bg.sarakt.items.basics;
    exports bg.sarakt.items.inventory;
    exports bg.sarakt.items.basics.impl;
    exports bg.sarakt.characters.attributes;
    exports bg.sarakt.characters;
    exports bg.sarakt.characters.attributes1.impls;
    exports bg.sarakt.characters.impls;
    exports bg.sarakt.items.inventory.impl;
    exports bg.sarakt.characters.attributes1;
    exports bg.sarakt.items.inventory.equipment;
    exports bg.sarakt.combats;
    exports bg.sarakt.characters.attributes.impl;
    exports bg.sarakt.items.inventory.equipment.impl;
    
    requires sarakt.attributes;
    requires sarakt.base;
    requires sarakt.logging;
    requires java.desktop;
}