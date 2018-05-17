package fr.mrlizzard.wardevil.builder.uitls;

public enum Rank {

    SUPER_USER      (true, "prefix"),
    OWNER           (false, "prefix");

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
