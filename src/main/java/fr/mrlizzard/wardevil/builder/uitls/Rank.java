package fr.mrlizzard.wardevil.builder.uitls;

import org.bukkit.GameMode;

/**
 * Rank enum
 * @author mrlizzard
 * @version 0.0.1
 */
public enum Rank {

    SUPER_USER      (100, true, "§4(Admin) ", GameMode.CREATIVE),
    OWNER           (90, false, "§c(Resp.) ", GameMode.CREATIVE),
    BUILDER         (80, false, "§9(Builder) ", GameMode.CREATIVE),
    SPECTATOR       (0, false, "§8(Spectateur) ", GameMode.SPECTATOR);

    Integer         id;
    Boolean         op;
    String          prefix;
    GameMode        gamemode;

    /**
     * Configure rank
     * @param id Integer Rank identifier
     * @param op Boolean If rank is operator
     * @param prefix String Rank prifix
     */
    Rank(Integer id, Boolean op, String prefix, GameMode gamemode) {
        this.id = id;
        this.op = op;
        this.prefix = prefix;
        this.gamemode = gamemode;
    }

    /**
     * Get rank identifier
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    /**
     * Is operator rank
     * @return Boolean
     */
    public Boolean isOp() {
        return op;
    }

    /**
     * Get rank prefix
     * @return String
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get rank gamemode
     * @return GameMode
     */
    public GameMode getGamemode() {
        return gamemode;
    }
}
