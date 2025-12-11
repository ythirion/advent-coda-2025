<?php
declare(strict_types=1);

namespace Gift;

use Gift\Strategies\GiftSelectionStrategy;
use Gift\Strategies\GreaterOrEqual14Strategy;
use Gift\Strategies\LowerThan14Strategy;

class GiftSelector
{
    public static function selectGiftFor(Child $child): ?string
    {
        return !$child->isNaughty()
            ? self::strategyFor($child)->selectGiftFor($child)
            : null;
    }

    private static function strategyFor(Child $child): GiftSelectionStrategy
    {
        return $child->age >= 14
            ? new GreaterOrEqual14Strategy()
            : new LowerThan14Strategy();
    }
}
