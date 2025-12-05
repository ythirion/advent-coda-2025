package gourmandise;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThat;

class ElfTests {
    public static final String DATA = "data";

    private static String emptyLine() {
        return "\\n\\s*\\n";
    }

    private static String[] elfStrings() {
        return FileUtils.getInputAsString(DATA)
                .split(emptyLine());
    }

    @Test
    void should_find_top_3_elves_by_calories() {
        var elves = top3Elves();

        assertThat(elves.getFirst()).isEqualTo(new Elf("Susanoo", 57177));
        displayWinner(elves);
    }

    private List<Elf> top3Elves() {
        return stream(elfStrings())
                .map(this::mapElf)
                .sorted(comparingInt(Elf::calories)
                        .reversed()
                ).limit(3)
                .toList();
    }

    private void displayWinner(List<Elf> elves) {
        out.println("🍪 Elf of the Day: " + elves.getFirst().name() + " with " + elves.getFirst().calories() + " calories!");
        out.println("🥈 Then comes " + elves.get(1).name() + " (" + elves.get(1).calories() + ") and " + elves.get(2).name() + " (" + elves.get(2).calories() + ")");
        out.println("🎁 Combined snack power of Top 3: " + elves.stream().mapToInt(Elf::calories).sum() + " calories!");
    }

    private Elf mapElf(String elfString) {
        var parsed = elfString.split("\n");

        return new Elf(
                parsed[0],
                stream(parsed)
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .sum()
        );
    }
}