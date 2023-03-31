package pro.sky.coursework.courseworksocks.model;

public enum TypeOfTransaction {
    ADD("Приёмка"),
    PICK_UP("Выдача"),
    DELETE("Списание");

    private String name;

    TypeOfTransaction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TypeOfTransaction getByName(String s) {
        return TypeOfTransaction.valueOf(s);
    }
}
