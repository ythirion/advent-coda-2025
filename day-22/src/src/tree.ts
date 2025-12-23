import {colors} from "./colors";
import {treeLines} from "./treeLines";
import {guirlands} from "./guirlands";

const clearConsole = (): void => console.clear();
const displayStar = (): void => console.log(colors.yellow + "        ★        " + colors.reset);
const guirlandColorAt = (lineIndex: number): string =>
    lineIndex % 2 === 0 ? colors.red : colors.blue; // Cocorico à la 🇫🇷

const mergeLineWithGuirland = (line: string, guirlandLine: string, color: string): string =>
    line.split('')
        .map((char, i) => guirlandLine[i] === '•' ? color + '•' + colors.reset : char)
        .join('');

const mergeLine = (line: string, i: number) =>
    showGuirlands && i < guirlands.length
        ? mergeLineWithGuirland(line, guirlands[i], guirlandColorAt(i + 1))
        : colors.green + line + colors.reset;

const displayCrown = (): void =>
    treeLines.slice(1) // Skip first line (replaced by star)
        .map((line, i) => mergeLine(line, i))
        .forEach(mergedLine => console.log(mergedLine));

const displayTrunk = (): void => {
    console.log(colors.brown + "        |        " + colors.reset);
    console.log(colors.brown + "        |        " + colors.reset);
};

const displayTree = (): void => {
    clearConsole();
    displayStar();
    displayCrown();
    displayTrunk();
};

let showGuirlands = true;
setInterval(() => {
    showGuirlands = !showGuirlands;
    displayTree();
}, 1000);

displayTree();
