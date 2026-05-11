package org.swingBird.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class AwtInputAdapter extends KeyAdapter
{
    private final InputPoller poller;
    private final Map<Integer, GameAction> keyMap;

    public AwtInputAdapter(InputPoller poller)
    {
        this.poller = poller;
        this.keyMap = new HashMap<>();

        keyMap.put(KeyEvent.VK_SPACE, GameAction.FLAP);
        keyMap.put(KeyEvent.VK_ESCAPE, GameAction.PAUSE);
        keyMap.put(KeyEvent.VK_ENTER, GameAction.UI_CONFIRM);

        keyMap.put(KeyEvent.VK_W, GameAction.UI_UP);
        keyMap.put(KeyEvent.VK_A, GameAction.UI_UP);
        keyMap.put(KeyEvent.VK_S, GameAction.UI_DOWN);
        keyMap.put(KeyEvent.VK_D, GameAction.UI_DOWN);

        keyMap.put(KeyEvent.VK_UP, GameAction.UI_UP);
        keyMap.put(KeyEvent.VK_LEFT, GameAction.UI_UP);
        keyMap.put(KeyEvent.VK_DOWN, GameAction.UI_DOWN);
        keyMap.put(KeyEvent.VK_RIGHT, GameAction.UI_DOWN);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        GameAction action = keyMap.get(e.getKeyCode());
        if (action != null)
            poller.setActionState(action, true);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        GameAction action = keyMap.get(e.getKeyCode());
        if (action != null)
            poller.setActionState(action, false);
    }
}