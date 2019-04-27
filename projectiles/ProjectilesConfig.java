package net.runelite.client.plugins.projectiles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("projectiles")
public interface ProjectilesConfig extends Config {

    @ConfigItem(
            keyName = "overlayColor",
            name = "Overlay Color",
            description = "Color of Projectiles Overlay",
            position = 1
    )
    default Color getOverlayColor()
    {
        return Color.MAGENTA;
    }

    @ConfigItem(
            keyName = "shamanPoison",
            name = "Show Shaman Poison",
            description = "Shows the tile Shaman poison lands on.",
            position = 2
    )
    default boolean showShamanPoison()
    {
        return true;
    }

    @ConfigItem(
            keyName = "tektonFlares",
            name = "Show Tekton Flares",
            description = "Shows the tile Tekton anvil flares lands on.",
            position = 3
    )
    default boolean showTektonFlares()
    {
        return true;
    }

    @ConfigItem(
            keyName = "vasaRocks",
            name = "Show Vasa Rocks",
            description = "Shows the tile Vasa rocks lands on.",
            position = 4
    )
    default boolean showVasaRocks()
    {
        return true;
    }

}
