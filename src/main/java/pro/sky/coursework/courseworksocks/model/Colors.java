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

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
