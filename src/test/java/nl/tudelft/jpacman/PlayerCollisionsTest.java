package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerCollisions;
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
import static org.mockito.Mockito.*;

public class PlayerCollisionsTest {

    private PlayerCollisions playerCollisions;
    private PointCalculator pointCalculator;
    private Player player;
    private Ghost ghost;
    private Pellet pellet;
    private Sprite[] frames;
    private Map<Direction, Sprite> spriteMap;
    private AnimatedSprite deathAnimation;

    @BeforeEach
    void setUp() {
        pointCalculator = mock(PointCalculator.class);
        pellet = mock(Pellet.class);
        playerCollisions = new PlayerCollisions(pointCalculator);
        spriteMap = new HashMap<>();

        frames = new Sprite[]{new EmptySprite(), new EmptySprite()};
        deathAnimation = new AnimatedSprite(frames, 0, true);
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
        playerCollisions.collide(player, ghost);
        verify(pointCalculator).collidedWithAGhost(player, ghost);
        Assertions.assertFalse(player.isAlive());
        Assertions.assertEquals(ghost, player.getKiller());
    }

    @Test
    void ghostCollidingWithPlayer() {
        playerCollisions.collide(ghost, player);
        verify(pointCalculator).collidedWithAGhost(player, ghost);
        Assertions.assertFalse(player.isAlive());
        Assertions.assertEquals(ghost, player.getKiller());
    }

    @Test
    void playerCollidingWithPellet() {
        playerCollisions.collide(player, pellet);
        verify(pointCalculator).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }

    @Test
    void pelletCollidingWithPlayer() {
        playerCollisions.collide(pellet, player);
        verify(pointCalculator).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }
}
