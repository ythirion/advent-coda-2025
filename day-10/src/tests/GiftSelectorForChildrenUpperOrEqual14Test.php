<?php

use Tests\ChildBuilder;
use Tests\TestData;

function evaluateRequestForChildrenUpperOrEqual14(callable $childConfiguration): ?string
{
    return TestData::evaluateRequestFor(function (ChildBuilder $child) use ($childConfiguration) {
        $childConfiguration($child->upperOrEqual14());
        return $child;
    });
}

it('Normal, age = 16, bienveillance = 0, liste de cadeaux [PS5 (faisable), Toy (faisable)] -> Rien', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->normal()
                ->notKind()
                ->requestingFeasibleGift(TestData::PS_5)
                ->requestingFeasibleGift(TestData::TOY)
        ))->toBeNull();
});

it('Normal, age = 14, bienveillance = 0.8, liste de cadeaux [PS5 (faisable), Toy (faisable)] -> Toy', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->normal()
                ->kind()
                ->requestingFeasibleGift(TestData::PS_5)
                ->requestingFeasibleGift(TestData::TOY)
        ))->toBe(TestData::TOY);
});

it('Normal, age = 18, bienveillance = 0.8, liste de cadeaux [PS5 (faisable), Car (infaisable)] -> PS5', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->normal()
                ->kind()
                ->requestingFeasibleGift(TestData::PS_5)
                ->requestingInfeasibleGift("Car")
        ))->toBe(TestData::PS_5);
});

it('Sage ("nice"), age = 14, bienveillance = 0.51, liste de cadeaux [PS5 (faisable), Toy (faisable)] -> PS5', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->nice()
                ->kind()
                ->requestingFeasibleGift(TestData::PS_5)
                ->requestingFeasibleGift(TestData::TOY)
        ))->toBe(TestData::PS_5);
});

it('Sage ("nice"), age = 15, bienveillance = 1, liste de cadeaux [A (infaisable), B (infaisable)] -> Rien', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->nice()
                ->kind()
                ->requestingInfeasibleGift("Not feasible 1")
                ->requestingInfeasibleGift("Not feasible 2")
        ))->toBeNull();
});

it('Sage ("nice"), age = 14, bienveillance = 0.49, liste de cadeaux [PS5 (faisable), Toy (faisable)] -> Rien', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->nice()
                ->notKind()
                ->requestingFeasibleGift(TestData::PS_5)
                ->requestingFeasibleGift(TestData::TOY)
        ))->toBeNull();
});

it('Sage ("nice"), age = 20, bienveillance = 1, liste de cadeaux [] -> Rien', function () {
    expect(
        evaluateRequestForChildrenUpperOrEqual14(
            fn(ChildBuilder $child) => $child
                ->nice()
                ->kind()
        ))->toBeNull();
});
