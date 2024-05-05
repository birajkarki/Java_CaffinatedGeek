

public enum Milk {
    WHOLE, SKIM, SOY, ALMOND, OAT, FULL_CREAM, COCONUT, SKIP;

    public String toString() {
         return switch (this) {
            case WHOLE ->  "Whole";
            case SKIM -> "Skim";
            case SOY -> "Soy";
            case ALMOND -> "Almond";
            case OAT -> "Oat";
            case FULL_CREAM -> "Full-cream";
            case COCONUT  -> "Coconut";
            case SKIP -> "SKIP";

        };
    }

}

