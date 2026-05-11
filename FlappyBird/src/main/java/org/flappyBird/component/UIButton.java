package org.flappyBird.component;

import org.flappyBird.render.CmdRect;
import org.flappyBird.render.CmdText;
import org.flappyBird.render.IRenderCmd;

import java.util.List;

public class UIButton
{
    private int x, y, width, height;
    private final String text;
    private final Runnable action;

    private boolean isSelected = false;

    public UIButton(int x, int y, int width, int height, String text, Runnable action)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
    }

    public void setSelected(boolean selected)
    {
        this.isSelected = selected;
    }

    public void setBounds(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void execute()
    {
        if (action != null)
            action.run();
    }

    public void render(List<IRenderCmd> buffer)
    {
        int color = isSelected ? 0x00AA00 : 0x005500;
        buffer.add(new CmdRect(x, y, width, height, color));

        // TODO: в будущем заменить на расчеты метрики шрифта
        buffer.add(new CmdText(text, x + width / 4, y + height / 2 + 5, 0xFFFFFF));

        if (isSelected)
            buffer.add(new CmdRect(x - 20, y + height / 2 - 5, 10, 10, 0xFFFFFF));
    }
}