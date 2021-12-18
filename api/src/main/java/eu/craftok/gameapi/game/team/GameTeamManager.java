package eu.craftok.gameapi.game.team;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.game.settings.GameSettings;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

/**
 * Project game-api Created by Sithey
 */

public class GameTeamManager {

    private List<GameTeam> gTeams;

    private int colort = 0;
    private int symbolt = 0;

    private final String[] names = { "Rouge", "Jaune", "Orange", "Violet", "Vert", "Noir", "Bleu", "Gris", "Rose"};

    private final String[] colors = { "§c", "§e", "§6", "§d", "§a", "§0", "§b", "§7", "§5"};

    private final DyeColor[] dyeColors = { DyeColor.RED, DyeColor.YELLOW, DyeColor.ORANGE, DyeColor.PURPLE,
            DyeColor.GREEN, DyeColor.BLACK, DyeColor.BLUE, DyeColor.GRAY, DyeColor.PINK};

    private final String[] symbols = {  "", "❤ ",  "♣ ",
            "\u263c ",  "\u2620 ",  "\u2606 "};
    private final Pattern[][] patternTypes = {
            { },
            {new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP)},
            {new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)},
            {new Pattern(DyeColor.WHITE, PatternType.FLOWER)},
            {new Pattern(DyeColor.WHITE, PatternType.SKULL)},
            {new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.FLOWER), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)}
    };

    public GameTeamManager(){
        this.gTeams = new ArrayList<>();
    }

    public List<GameTeam> getgTeams() {
        return gTeams;
    }

    public List<GameTeam> getgTeamsPerGame(Game game){
        List<GameTeam> value = new ArrayList<>();
        for (GameTeam gt : getgTeams()){
            if (!gt.getAliveGPlayers(game).isEmpty()){
                value.add(gt);
            }
        }
        return value;
    }

    public void createNewTeam(){

        GameTeam team = GameAPI.getInstance().getGameManagers().newGTeam();

        if (symbolt == symbols.length){
            System.out.println("Impossible de creer plus de teams");
            return;
        }

        team.setDyeColor(dyeColors[colort]);
        team.setPrefix(colors[colort] + symbols[symbolt]);
        team.setName(team.getPrefix() + names[colort]);
        team.setPatterns(patternTypes[symbolt]);
        team.createTeam();

        colort++;
        if (colort == dyeColors.length){
            colort = 0;
            symbolt++;
        }
    }

    public void createTeams(){
        GameSettings settings = GameAPI.getInstance().getGameManagers().getSettings();
//        10 / 1 = 10 10 / 3 = 3.1
        double teams = ((double) settings.getMaxPlayers()) / ((double) settings.getSizeTeams());
        if (teams % 1 != 0){
            teams = teams + 1;
        }
        for (int i = 0; i < ((int) teams); i++){
            GameAPI.getInstance().getGameTeamManager().createNewTeam();
        }
    }

    public void deleteTeam(){
        new ArrayList<>(getgTeams()).forEach(GameTeam::deleteTeam);
        colort = 0;
        symbolt = 0;
    }

    public void fillAllTeams(Game game){
        for (GamePlayer gp : game.getPlayingGPlayers()){
            if (gp.getTeam() != null)
                continue;
            for (GameTeam team : getgTeams()){
                if (team.getAliveGPlayers(game).size() < GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams()){
                    gp.setTeam(gp.getPlayer(), team);
                    break;
                }
            }
        }
    }
}
