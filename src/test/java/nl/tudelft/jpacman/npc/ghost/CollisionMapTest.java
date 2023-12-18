package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.CollisionMap;
import nl.tudelft.jpacman.level.DefaultPlayerInteractionMap;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.EmptySprite;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CollisionMapTest {

    private CollisionMap collisionMap;
    private PointCalculator pointCalculator;
    private Player player;
    private Ghost ghost;
    private Pellet pellet;
    private Map<Direction, Sprite> spriteMap;

    @BeforeEach
    void setUp() {
        pointCalculator = mock(PointCalculator.class);
        pellet = mock(Pellet.class);
        collisionMap = new DefaultPlayerInteractionMap(pointCalculator);
        spriteMap = new HashMap<>();

        Sprite[] frames = new Sprite[]{new EmptySprite(), new EmptySprite()};
        AnimatedSprite deathAnimation = new AnimatedSprite(frames, 0, true);
        player = new Player(spriteMap, deathAnimation);
        ghost = new Ghost(spriteMap, 0, 0) {
            @Override
            public Optional<Direction> nextAiMove() {
                return Optional.empty();
            }
        };
    }

    @Test
    void playerCollidingWithGhost() {
        collisionMap.collide(player, ghost);
        verify(pointCalculator).collidedWithAGhost(player, ghost);
        Assertions.assertFalse(player.isAlive());
        Assertions.assertEquals(ghost, player.getKiller());
    }

    @Test
    void ghostCollidingWithPlayer() {
        collisionMap.collide(ghost, player);
        verify(pointCalculator).collidedWithAGhost(player, ghost);
        Assertions.assertFalse(player.isAlive());
        Assertions.assertEquals(ghost, player.getKiller());
    }

    @Test
    void playerCollidingWithPellet() {
        collisionMap.collide(player, pellet);
        verify(pointCalculator).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }

    @Test
    void pelletCollidingWithPlayer() {
        collisionMap.collide(pellet, player);
        verify(pointCalculator).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }
}
