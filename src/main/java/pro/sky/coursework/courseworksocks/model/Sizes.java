package pro.sky.coursework.courseworksocks.model;

import lombok.Data;

public enum Sizes {

    TWENTY_SIX(26),
    TWENTY_SIX_5(26.5),
    TWENTY_SEVEN(27),
    TWENTY_SEVEN_5(27.5),
    TWENTY_EIGHT(28),
    TWENTY_EIGHT_5(28.5),
    TWENTY_NINE(29),
    TWENTY_NINE_5(29.5),
    THIRTY(30),
    THIRTY_5(30.5),
    THIRTY_ONE(31),
    THIRTY_ONE_5(31.5),
    THIRTY_TWO(32),
    THIRTY_TWO_5(32.5),
    THIRTY_THREE(33),
    THIRTY_THREE_5(33.5),
    THIRTY_FOUR(34),
    THIRTY_FOUR_5(34.5),
    THIRTY_FIVE(35),
    THIRTY_FIVE_5(35.5),
    THIRTY_SIX(36),
    THIRTY_SIX_5(36.5),
    THIRTY_SEVEN(37),
    THIRTY_SEVEN_5(37.5),
    THIRTY_EIGHT(38),
    THIRTY_EIGHT_5(38.5),
    THIRTY_NINE(39),
    THIRTY_NINE_5(39.5),
    FORTY(40),
    FORTY_5(40.5),
    FORTY_ONE(41),
    FORTY_ONE_5(41.5),
    FORTY_TWO(42),
    FORTY_TWO_5(42.5),
    FORTY_THREE(43),
    FORTY_THREE_5(43.5),
    FORTY_FOUR(44),
    FORTY_FOUR_5(44.5),
    FORTY_FIVE(45),
    FORTY_FIVE_5(45.5),
    FORTY_SIX(46),
    FORTY_SIX_5(46.5);


    private final double value;

    Sizes(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Sizes getValueByDigits(float size) {
        for (Sizes sizeObj :
                Sizes.values()) {
            if (sizeObj.value == size){
                return sizeObj;
            }
        }
        return null;
    }
}
