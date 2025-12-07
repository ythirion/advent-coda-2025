import {expect, test} from 'vitest';
import {averageWeight} from "../src/weightCalculator";

test.each([
    {reindeers: [2, 5, 7, 10], expected: 6},
    {reindeers: [2], expected: 2},
    {reindeers: [], expected: 0},
    {reindeers: [1, 2], expected: 1.5},
    {reindeers: null, expected: 0},
])('calculateWeight should return $expected for $reindeers', ({reindeers, expected}) => {
    expect(averageWeight(reindeers)).toBe(expected);
});
