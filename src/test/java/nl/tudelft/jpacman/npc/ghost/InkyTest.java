import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InkyTest {

    private PacManSprites sprites;
    private LevelFactory levelFactory;
    private GhostFactory ghostFactory;
    private PlayerFactory playerFactory;
    private BoardFactory boardFacory;
    private BoardFactory boardFactory;
    private GhostMapParser ghostMapParser;
    private PointCalculator pointCalculator;

    /**
     * 创建游戏所需的所有对象
     */
    @BeforeEach
    void Init() {
        sprites = new PacManSprites();
        pointCalculator = new DefaultPointCalculator();
        boardFacory = new BoardFactory(sprites);
        ghostFactory = new GhostFactory(sprites);
        levelFactory = new LevelFactory(sprites,ghostFactory,pointCalculator);
        ghostMapParser = new GhostMapParser(levelFactory,boardFacory,ghostFactory);
    }


    
    @Test
    @DisplayName("游戏地图中没有Blinky")
    void testNoBlinky() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList( "##..I...P####")
        );
        Player player = new PlayerFactory(sprites).createPacMan();  
        player.setDirection(Direction.EAST);             
        level.registerPlayer(player);                   
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }
    @Test
    @DisplayName("游戏地图中没有Player")
    void testNoPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("##..I..B.####")
        );
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("player与inky之间没有有效路径.")
    void testNoPath() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("#BP#....#I##")
        );
        Player player = new PlayerFactory(sprites).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());

    }
    @Test
     @DisplayName("Inky将远离Player")
    void testInkyMovesAway() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
            "#..B.P..I....#"
            )
        );
        Player player =  new PlayerFactory(sprites).createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));

    }

    /**
     * 当player、inky、blinky都在地图中，且存在有效路径
     * Inky在Player与Binky之间，则Inky将靠近Player
     */
    @Test
     @DisplayName("Inky将靠近Player")
    void testInkyMove(){
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
               "#.B..I.P#"
            )
        );
        Player player =  new PlayerFactory(sprites).createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class,level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.ofNullable(Direction.EAST));
    }
}
