const isValid = (reindeers: number[]) => reindeers != null && reindeers.length > 0;
const sumWeight = (reindeers: number[]) => reindeers.reduce((acc, weight) => acc + weight, 0);

export function averageWeight(reindeers: number[]): number {
    if (!isValid(reindeers)) {
        return 0;
    }
    return parseFloat(
        (sumWeight(reindeers) / reindeers.length).toFixed(2)
    );
}

const weights1: number[] = [2, 5, 7, 10];
const weights2: number[] = [2];

console.log(`Average weight for 4 gifts: ${averageWeight(weights1).toFixed(2)}`);
console.log(`Average weight for 1 gift: ${averageWeight(weights2).toFixed(2)}`);