package org.flappyBird.component;

import org.flappyBird.render.IRenderCmd;
import java.util.List;

public interface IParallaxLayer
{
    void update(double deltaMillis);

    void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight);
}