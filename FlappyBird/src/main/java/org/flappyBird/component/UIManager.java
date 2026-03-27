package org.flappyBird.component;

import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.IRenderCmd;

import java.util.ArrayList;
import java.util.List;

public class UIManager
{
    private final List<UIButton> buttons = new ArrayList<>();
    private final List<GameAction> actionBuffer = new ArrayList<>(64); // Zero-GC буфер

    private int selectedIndex = 0;

    public void addButton(UIButton button)
    {
        if (buttons.isEmpty())
            button.setSelected(true); // Первая кнопка по умолчанию в фокусе

        buttons.add(button);
    }

    public void update(InputSnapshot input)
    {
        if (buttons.isEmpty()) return;

        input.getJustPressedActions(actionBuffer);
        for (GameAction action : actionBuffer)
        {
            switch (action)
            {
                case UI_DOWN -> selectNext();
                case UI_UP -> selectPrevious();
                case UI_CONFIRM -> executeSelected();
                default -> {}
            }
        }
    }

    private void selectNext()
    {
        buttons.get(selectedIndex).setSelected(false);
        selectedIndex = (selectedIndex + 1) % buttons.size();
        buttons.get(selectedIndex).setSelected(true);
    }

    private void selectPrevious()
    {
        buttons.get(selectedIndex).setSelected(false);
        selectedIndex = (selectedIndex - 1 + buttons.size()) % buttons.size();
        buttons.get(selectedIndex).setSelected(true);
    }

    private void executeSelected()
    {
        buttons.get(selectedIndex).execute();
    }

    public void render(List<IRenderCmd> buffer)
    {
        for (UIButton btn : buttons)
        {
            btn.render(buffer);
        }
    }

    // TODO: Сделать чистой функцией, что не будет менять состояние изнутри, а только читать и генерировать команды.
    public void changeButtonsBounds(int canvasWidth, int canvasHeight)
    {
        int intervalX = canvasWidth / (buttons.size() * 2 + 1);
        int intervalY = GroundParallax.GROUND_HEIGHT / 3;
        int posY = canvasHeight - GroundParallax.GROUND_HEIGHT + intervalY;

        for (int i = 0; i < buttons.size(); i++)
        {
            UIButton btn = buttons.get(i);
            btn.setBounds(intervalX * (1 + 2 * i), posY, intervalX, intervalY);
        }
    }
}