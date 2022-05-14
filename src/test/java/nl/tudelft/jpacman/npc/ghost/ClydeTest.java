package nl.tudelft.jpacman.npc.ghost;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import nl.tudelft.jpacman.level.LevelFactory;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

/**
 *（1）clyde与player路径在8个格子之内
 *（2）clyde与player路径在8个格子之外
 *（3）player不存在
 *（4）clyde与player之间无可达路径
 */
public class ClydeTest {
    private GhostMapParser MapParser;
    private PacManSprites sprites;
    private BoardFactory boardFacory;
    private GhostFactory ghostFactory;
    private LevelFactory levelFactory;
    private PointCalculator pointCalculator;

    /**
     * Set up the map parser.
     */
    @BeforeEach
    void setUp(){
        sprites = new PacManSprites(); //角色显示
        pointCalculator = new DefaultPointCalculator(); //积分计算
        boardFacory = new BoardFactory(sprites); //地图生成
        ghostFactory = new GhostFactory(sprites); //鬼生成
        levelFactory = new LevelFactory(sprites,ghostFactory,pointCalculator); //关卡生成
        MapParser = new GhostMapParser(levelFactory,boardFacory,ghostFactory); //地图解析
    }

    @Test
    @DisplayName("player不存在")
    @Order(1)
    void noPlayerTest() {
        //Arrange
        List<String> text = Lists.newArrayList(
            "##############",
            "####..C...####",
            "###########.##");
        Level level = MapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    @DisplayName("clyde与player之间无可达路径")
    @Order(2)
    void noDepartTest() {
        //Arrange
        List<String> text = Lists.newArrayList(
            "##############",
            "#.C........#P#",
            "##############");
        Level level = MapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());


        // 创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.NORTH);
        level.registerPlayer(player);
        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.isPresent()).isFalse();
    }


    @Test
    @DisplayName("clyde与player路径在8个格子之内")
    @Order(3)
    void departOverSHYNESSTest() {
        //Arrange
        List<String> text = Lists.newArrayList(
            "####.#########",
            "##.......C...P",
            "#####.########");
        //创建clyde
        Level level = MapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());


        // 创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);


        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.get()).isEqualTo(Direction.WEST);
    }

    @Test
    @DisplayName("clyde与player路径在8个格子之外")
    @Order(4)
    void departLessSHYNESSTest() {

        List<String> text = Lists.newArrayList(
            "##############",
            "#.C.........P#",
            "##############");
        Level level = MapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        // 创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);

        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.get()).isEqualTo(Direction.EAST);
    }
}
