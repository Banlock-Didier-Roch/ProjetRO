package Utils;

public class AlphabetUtils {

    private static int letterToAlphabetPos(char letter) {
        return Character.toUpperCase(letter) - 64;
    }
}
