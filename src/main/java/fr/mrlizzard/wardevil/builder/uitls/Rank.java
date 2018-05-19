package fr.mrlizzard.wardevil.builder.uitls;

public enum Rank {

    SUPER_USER      (100, true, "superuser"),
    OWNER           (90, false, "owner"),
    SPECTATOR       (0, false, "spec");

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
