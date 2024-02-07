/*
 * TileFactory.java
 *
 * created at 2023-11-20 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.maps;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bg.sarakt.maps.impls.BasicTileTypes;
import bg.sarakt.maps.impls.FantasyTileTypes;


public class TileFactory
{
    private static final String BASICS = "basics".intern();
    private static final String FANTASY = "fantasy".intern();

    private static final Object LOCK = new Object();
    private static TileFactory INSTANCE;

    private final Random random = new Random();
    private final Map<String, List<TileType>> allTypes;
    private String currentType;

    public static final TileFactory getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized (LOCK)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new TileFactory();
                }
            }
        }
        return INSTANCE;
    }


    private TileFactory()
    {
        this.allTypes = new HashMap<>();
        List<TileType> types = generateBasicTypes();
        allTypes.put(BASICS, types);
        types = generateFantasyTypes();
        allTypes.put(FANTASY, types);
        currentType = FANTASY;
    }


    private List<TileType> generateBasicTypes()
    {
        BasicTileTypes[] values = BasicTileTypes.values();
        return new ArrayList<>(Arrays.asList(values));

    }


    private List<TileType> generateFantasyTypes()
    {
        FantasyTileTypes[] values = FantasyTileTypes.values();
        return new ArrayList<>(Arrays.asList(values));

    }


    public Tile generateTile()
    {
        List<TileType> types = allTypes.get(currentType);
        int typeIndex = random.nextInt(types.size());
        TileType type = types.get(typeIndex);
        return new Tile()
        {

            @Override
            public boolean isPassable()
            {
                return true;
            }


            @Override
            public TileType getType()
            {
                return type;
            }
        };
    }


    public void switchToFantasy()
    {
        this.currentType = FANTASY;
    }


    public void switchToBasics()
    {
        this.currentType = BASICS;
    }
}
