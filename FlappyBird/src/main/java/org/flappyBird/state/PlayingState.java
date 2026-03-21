package org.flappyBird.state;

import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.entity.Bird;
import org.flappyBird.entity.PipeColumn;
import org.flappyBird.render.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class PlayingState implements IState
{
    private final StateController controller;

    private final Bird bird;
    // Используем PipeColumn вместо Pipe
    private final List<PipeColumn> pipes = new ArrayList<>();

    private int score = 0;
    private float pipeSpawnTimer = 0;

    public PlayingState(StateController controller)
    {
        this.controller = controller;
        this.bird = new Bird(120, 200);
        
        // Стартовая труба (просвет на высоте 200)
        pipes.add(new PipeColumn(400, 200));
    }

    @Override public void onEnter()
    {
        // TODO: инициализация
    }
    @Override public void onExit()
    {
        // TODO: очистка
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        if (input.isJustPressed(GameAction.FLAP))
            bird.flap();
            
        bird.update(deltaMillis);
        
        // 1. Движение всех труб и удаление старых
        pipes.forEach(p -> p.update(deltaMillis));
        pipes.removeIf(PipeColumn::isExpired);
        
        Rectangle birdBounds = bird.getBounds();
        
        for (PipeColumn column : pipes)
        {
            // Если труба уже прошла (ее правый край левее птицы)
            if (column.getX() + column.getWidth() < bird.getX())
            {
                 // Перед тем как пропустить трубу, проверим, не нужно ли начислить очки
                 // Теперь проверка простая: если не засчитано - засчитываем.
                 if (!column.isScored()) {
                     score++;
                     column.setScored(true);
                     // Тут можно вызвать звук score.wav
                 }
                 continue;
            }

            // Если труба еще далеко справа (ее левый край правее птицы) - выходим из цикла
            if (column.getX() > bird.getX() + bird.getWidth())
                break;
            
            // Если мы тут, значит труба где-то в районе птицы - проверяем точное столкновение
            if (column.checkCollision(birdBounds))
            {
                controller.setState(new MenuState(controller));  // TODO: Убрать костыль в onEnter()/onExit()
                System.out.println("GAME OVER");
            }

            // Проверка начисления очков (отзывчивый вариант - по центру)
            if (!column.isScored())
            {
                if (bird.getX() + (float) bird.getWidth() / 2 > column.getX() + (float) column.getWidth() / 2)
                {
                    score++;
                    column.setScored(true);
                }
            }

            // TODO: Конец игры при касании земли
        }

        pipeSpawnTimer += (float) deltaMillis;
        if (pipeSpawnTimer > 2000)
        {
            pipeSpawnTimer = 0;
            // Спавним новую колонну с рандомной высотой просвета
            int gapY = 150 + (int)(Math.random() * 200); // Пример рандома
            pipes.add(new PipeColumn(800, gapY));
        }
    
        // TODO: логика игры (столкновения, счет)
        //  при проигрыше: "заморозить" рендеринг, изменить subState, а далее работать с саб-окном GameOverSubState через это состояние
        //  при паузе: "заморозить" рендеринг, изменить subState, а далее работать с саб-окном PauseSubState через это состояние
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));

        for (PipeColumn p : pipes)
            p.render(buffer);
            
        bird.render(buffer);

        buffer.add(new CmdText("Score: " + score, 20, 30, 0xFFFFFF));

        // TODO: Отрисовка задника, мб с передачей
    }
}
