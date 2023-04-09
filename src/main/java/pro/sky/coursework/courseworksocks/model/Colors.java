package pro.sky.coursework.courseworksocks.model;

public enum Colors {
    WHITE("Белый"),
    BLACK("Черный"),
    RED("Красный"),
    BLUE("Синий"),
    PINK("Розовый"),
    GREEN("Зеленый"),
    ORANGE("Оранжевый"),
    YELLOW("Желтый"),
    VIOLET("Фиолетовый");

    private final String text;

    Colors(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
