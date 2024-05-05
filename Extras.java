public enum Extras {
    CHOCOLATESYRUP,
    CINNAMON,
    CHOCOLATEPOWDER,
    VANILLASYRUP,
    WHIPPEDCREAM,
    CARAMELSYRUP,
    VANILLAICECREAM,
    HONEY,
    
    SKIP;


    public String toString() {
        return switch (this) {
            case CHOCOLATESYRUP -> "Chocolate syrup";
            case CINNAMON -> "Cinnamon";
            case CHOCOLATEPOWDER -> "Chocolate powder";
            case VANILLASYRUP -> "Vanilla syrup";
            case WHIPPEDCREAM -> "Whipped cream";
            case CARAMELSYRUP -> "Caramel syrup";
            case VANILLAICECREAM -> "Vanilla ice cream";
            case HONEY -> "Honey";

            case SKIP -> "skip";
        };
    }
}
