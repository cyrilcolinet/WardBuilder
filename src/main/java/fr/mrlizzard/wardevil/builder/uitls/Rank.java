package fr.mrlizzard.wardevil.builder.uitls;

public enum Rank {

    SUPER_USER      (100, true, "§4(Admin) "),
    OWNER           (90, false, "§c(Resp.) "),
    BUILDER         (80, false, "§9(Builder) "),
    SPECTATOR       (0, false, "§8(Spectateur) ");

    Integer         id;
    Boolean         op;
    String          prefix;

    Rank(Integer id, Boolean op, String prefix) {
        this.id = id;
        this.op = op;
        this.prefix = prefix;
    }

    public Integer getId() {
        return id;
    }

    public Boolean isOp() {
        return op;
    }

    public String getPrefix() {
        return prefix;
    }

}
