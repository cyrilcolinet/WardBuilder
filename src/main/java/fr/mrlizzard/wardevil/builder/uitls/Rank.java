package fr.mrlizzard.wardevil.builder.uitls;

public enum Rank {

    SUPER_USER      (true, "superuser"),
    OWNER           (false, "owner"),
    SPECTATOR       (false, "spec");

    Boolean         op;
    String          prefix;

    Rank(Boolean op, String prefix) {
        this.op = op;
        this.prefix = prefix;
    }

    public Boolean isOp() {
        return op;
    }

    public String getPrefix() {
        return prefix;
    }

}
