/*
 * ToolsRunner.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.z.tools;

public class ToolsRunner
{

    public static void main(String[] args)
    {
        DBPopulator.getInstance()
        .populateLevels()
//        .populateAttributes()
        ;
    }
}



