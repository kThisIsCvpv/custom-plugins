package net.runelite.client.plugins.projectiles;

import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Projectiles",
        description = "Projectile Indicators",
        tags = {"proj"},
        enabledByDefault = false
)
/**
 * Made by Yuri-chan ^_^ @ https://github.com/kThisIsCvpv/
 */
public class ProjectilesPlugin  extends Plugin {

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ProjectilesOverlay overlay;

    @Inject
    private ProjectilesConfig config;

    @Provides
    ProjectilesConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ProjectilesConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
    }
}

