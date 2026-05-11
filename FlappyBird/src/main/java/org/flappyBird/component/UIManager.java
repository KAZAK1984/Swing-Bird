package org.flappyBird.component;

import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.IRenderCmd;

import java.util.ArrayList;
import java.util.List;

public class UIManager
{
    private static final int MIN_BUTTON_WIDTH = 90;
    private static final int MAX_BUTTON_WIDTH = 260;
    private static final int MIN_BUTTON_HEIGHT = 28;
    private static final int MAX_BUTTON_HEIGHT = 68;

    private final List<UIButton> buttons = new ArrayList<>();
    private final List<GameAction> actionBuffer = new ArrayList<>(64);

    private int selectedIndex = 0;

    public void addButton(UIButton button)
    {
        if (buttons.isEmpty())
            button.setSelected(true); // Первая кнопка по умолчанию в фокусе

        buttons.add(button);
    }

    public void update(InputSnapshot input)
    {
        if (buttons.isEmpty())
            return;

        input.getJustPressedActions(actionBuffer);
        for (GameAction action : actionBuffer)
        {
            switch (action)
            {
                case UI_DOWN -> selectNext();
                case UI_UP -> selectPrevious();
                case UI_CONFIRM, FLAP -> executeSelected();
                default -> {
                }
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

    public void changeButtonsBounds(int x, int y, int width, int height)
    {
        if (buttons.isEmpty()) return;

        int count = buttons.size();
        int slotWidth = Math.max(1, width / count);
        int rawButtonWidth = Math.max(1, (int) (slotWidth * 0.75));
        int rawButtonHeight = Math.max(1, (int) (height * 0.6));
        int buttonWidth = clamp(rawButtonWidth, MIN_BUTTON_WIDTH, Math.min(slotWidth, MAX_BUTTON_WIDTH));
        int buttonHeight = clamp(rawButtonHeight, MIN_BUTTON_HEIGHT, Math.min(height, MAX_BUTTON_HEIGHT));
        int offsetY = y + Math.max(0, (height - buttonHeight) / 2);

        for (int i = 0; i < count; i++)
        {
            UIButton btn = buttons.get(i);
            int slotX = x + i * slotWidth;
            int offsetX = slotX + Math.max(0, (slotWidth - buttonWidth) / 2);
            btn.setBounds(offsetX, offsetY, buttonWidth, buttonHeight);
        }
    }

    public void changeButtonsBoundsVertical(int x, int y, int width, int height)
    {
        if (buttons.isEmpty()) return;

        int count = buttons.size();
        int slotHeight = Math.max(1, height / count);
        int rawButtonWidth = Math.max(1, (int) (width * 0.85));
        int rawButtonHeight = Math.max(1, (int) (slotHeight * 0.75));
        int buttonWidth = clamp(rawButtonWidth, MIN_BUTTON_WIDTH, Math.min(width, MAX_BUTTON_WIDTH));
        int buttonHeight = clamp(rawButtonHeight, MIN_BUTTON_HEIGHT, Math.min(slotHeight, MAX_BUTTON_HEIGHT));
        int offsetX = x + Math.max(0, (width - buttonWidth) / 2);

        for (int i = 0; i < count; i++)
        {
            UIButton btn = buttons.get(i);
            int slotY = y + i * slotHeight;
            int offsetY = slotY + Math.max(0, (slotHeight - buttonHeight) / 2);
            btn.setBounds(offsetX, offsetY, buttonWidth, buttonHeight);
        }
    }

    private int clamp(int value, int min, int max)
    {
        int upperBound = Math.max(min, max);
        return Math.max(min, Math.min(value, upperBound));
    }
}