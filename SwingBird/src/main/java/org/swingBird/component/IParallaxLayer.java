package org.swingBird.component;

import org.swingBird.render.IRenderCmd;
import java.util.List;

public interface IParallaxLayer
{
    void update(double deltaMillis);

    void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight);
}