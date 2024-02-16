/*
 * ItemImpl.java
 *
 * created at 2024-01-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.items.basics.impl;

import bg.sarakt.base.AbstractGameObject;
import bg.sarakt.items.basics.Item;
import bg.sarakt.items.basics.ItemType;
import bg.sarakt.items.basics.ItemView;
import bg.sarakt.items.basics.Quality;
import bg.sarakt.logging.Logger;

/**
 * Default implementation of {@link Item}
 *
 * @author IceDragon
 */
public class ItemImpl extends AbstractGameObject implements Item {

    /** field <code>serialVersionUID</code> */
    private static final long     serialVersionUID          = 20240108108L;
    protected static final String representationSignatureID = "2024-jan-08-01:11-DEFAULT-ITEM";

    private String   name;
    private double   basePrice;
    private ItemType type;
    private Quality  quality;
    private boolean  equppable;

    private transient ItemView view;

    public ItemImpl(String name, double basePrice, ItemType type, Quality quality, boolean isEquppable) {
        super(representationSignatureID);
        this.name = name;
        this.basePrice = basePrice;
        this.type = type;
        this.quality = quality;
        this.equppable = isEquppable;
    }

    /**
     * @see bg.sarakt.items.basics.Item#getBasePrice()
     */
    @Override
    public double getBasePrice() { return basePrice; }

    /**
     * @see bg.sarakt.items.basics.Item#getName()
     */
    @Override
    public String getName() { return this.name; }

    /**
     * @see bg.sarakt.items.basics.Item#getType()
     */
    @Override
    public ItemType getType() { return this.type; }

    /**
     * @see bg.sarakt.items.basics.Item#getQuality()
     */
    @Override
    public Quality getQuality() { return this.quality; }

    /**
     * @see bg.sarakt.items.basics.Item#isEquippable()
     */
    @Override
    public boolean isEquippable() { return this.equppable; }

    /**
     * @see bg.sarakt.items.basics.Item#getView()
     */
    @Override
    public ItemView getView() {
        if(view == null)
        {
            Logger.getLogger().debug("NULL");
            // TODO = lazy initialization; create view only on demand. Some kind of cache to
            // drop it after a while non-using
        }
        return view;
    }

}