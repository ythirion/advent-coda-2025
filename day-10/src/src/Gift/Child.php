<?php
declare(strict_types=1);

namespace Gift;

class Child
{
    private const KINDNESS_THRESHOLD = 0.5;

    public function __construct(
        public readonly string   $firstName,
        public readonly string   $lastName,
        public readonly int      $age,
        public readonly Behavior $behavior,
        public readonly float    $kindness,
        public readonly array    $giftRequests = []
    )
    {
    }

    public function feasibleGifts(): array
    {
        return array_filter(
            $this->giftRequests,
            fn(GiftRequest $gift) => $gift->isFeasible
        );
    }

    public function isKind(): bool
    {
        return $this->kindness > self::KINDNESS_THRESHOLD;
    }

    public function hasNormalBehavior(): bool
    {
        return $this->behavior === Behavior::NORMAL;
    }

    public function isNaughty(): bool
    {
        return $this->behavior === Behavior::NAUGHTY;
    }
}
