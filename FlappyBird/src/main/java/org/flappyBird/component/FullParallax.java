package org.flappyBird.component;

import org.flappyBird.render.IRenderCmd;
import java.util.List;

public class FullParallax implements IParallaxLayer
{
    private final IParallaxLayer[] layers;

    public FullParallax()
    {
        this.layers = new IParallaxLayer[]
                {
                        new CloudParallax(),
                        new BuildingParallax(),
                        new GroundParallax()
                };
    }

    @Override
    public void update(double deltaMillis)
    {
        for (IParallaxLayer layer : layers)
            layer.update(deltaMillis);
    }

    @Override
    public void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        for (IParallaxLayer layer : layers)
            layer.render(buffer, canvasWidth, canvasHeight);
    }
}