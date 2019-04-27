package net.runelite.client.plugins.projectiles;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectilesOverlay extends Overlay {

    private Map<Projectile, WorldPoint> projectiles = new HashMap<Projectile, WorldPoint>();

    private final Client client;
    private final ProjectilesPlugin plugin;
    private final ProjectilesConfig config;

    private final int TEKTON_SPARKS = 660;
    private final int SHAMAN_BLOBS = 1293;
    private final int VASA_ROCKS = 1329;

    @Inject
    private ProjectilesOverlay(Client client, ProjectilesPlugin plugin, ProjectilesConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        // Clear all dead projectiles.
        for (Projectile projectile : new ArrayList<Projectile>(projectiles.keySet())) {
            if (projectile.getRemainingCycles() <= 0)
                projectiles.remove(projectile);
        }

        // Add all projectiles in the client to the render list.
        ArrayList<Projectile> toRender = new ArrayList<Projectile>();
        for (Projectile p : this.client.getProjectiles()) {
            if (this.config.showShamanPoison() && p.getId() == SHAMAN_BLOBS)
                toRender.add(p);
            else if (this.config.showTektonFlares() && p.getId() == TEKTON_SPARKS)
                toRender.add(p);
            else if (this.config.showVasaRocks() && p.getId() == VASA_ROCKS)
                toRender.add(p);
        }

        // Track the projectiles on the render list if they don't already exist and are not dead.
        for (Projectile p : toRender) {
            if (p.getRemainingCycles() <= 0 || projectiles.containsKey(p))
                continue;

            int totalTicks = p.getEndCycle() - p.getStartMovementCycle();

            double deltaX = p.getVelocityX() * totalTicks;
            double deltaY = p.getVelocityY() * totalTicks;

            deltaX = Math.round(deltaX / 128d) * 128d;
            deltaY = Math.round(deltaY / 128d) * 128d;

            int newX = p.getX1() + (int) deltaX;
            int newY = p.getY1() + (int) deltaY;

            // If the projectile launch resulted from an entity doing an animation, use X() as base instead of X1().
            if (p.getId() == SHAMAN_BLOBS) {
                newX = (int) (p.getX() + deltaX);
                newY = (int) (p.getY() + deltaY);
            }

            LocalPoint raw = new LocalPoint(newX, newY);
            WorldPoint world = WorldPoint.fromLocal(client, raw);
            projectiles.put(p, world);
        }

        // Render the projectiles.
        for (Projectile p : projectiles.keySet()) {
            WorldPoint world = projectiles.get(p);
            LocalPoint local = LocalPoint.fromWorld(client, world);

            Polygon poly = Perspective.getCanvasTilePoly(client, local);

            if (poly == null)
                continue;

            Color color = config.getOverlayColor();
            int strokeWidth = 2;
            int outlineAlpha = 255;
            int fillAlpha = 10;

            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
            graphics.setStroke(new BasicStroke(strokeWidth));
            graphics.draw(poly);

            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
            graphics.fill(poly);
        }

        return null;
    }
}
