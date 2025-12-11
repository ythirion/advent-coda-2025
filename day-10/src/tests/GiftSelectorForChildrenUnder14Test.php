<?php

use Tests\ChildBuilder;
use Tests\TestData;

function evaluateRequestForChildrenUnder14(callable $childConfiguration): ?string
{
    return TestData::evaluateRequestFor(function (ChildBuilder $child) use ($childConfiguration) {
        $childConfiguration($child->under14());
        return $child;
    });
}

it('selects first feasible gift for nice child', function () {
    expect(
        evaluateRequestForChildrenUnder14(fn(ChildBuilder $child) => $child
            ->nice()
            ->requestingFeasibleGift(TestData::TOY)
            ->requestingFeasibleGift(TestData::BOOK)
        ))->toBe(TestData::TOY);
});

it('returns nothing for nice child with only infeasible gifts', function () {
    expect(
        evaluateRequestForChildrenUnder14(fn(ChildBuilder $child) => $child
            ->nice()
            ->requestingInfeasibleGift()
            ->requestingInfeasibleGift()
        ))->toBeNull();
});

it('selects last feasible gift for normal child', function () {
    expect(
        evaluateRequestForChildrenUnder14(fn(ChildBuilder $child) => $child
            ->normal()
            ->requestingFeasibleGift(TestData::TOY)
            ->requestingFeasibleGift(TestData::PS_5)
            ->requestingFeasibleGift(TestData::BOOK)
        ))->toBe(TestData::BOOK);
});

it('returns nothing for normal child with only infeasible gifts', function () {
    expect(
        evaluateRequestForChildrenUnder14(fn(ChildBuilder $child) => $child
            ->normal()
            ->requestingInfeasibleGift(TestData::TOY)
            ->requestingInfeasibleGift(TestData::BOOK)
        ))->toBeNull();
});

it('returns nothing for naughty child regardless of gifts', function () {
    expect(
        evaluateRequestForChildrenUnder14(fn(ChildBuilder $child) => $child
            ->naughty()
            ->requestingFeasibleGift(TestData::TOY)
        ))->toBeNull();
});
