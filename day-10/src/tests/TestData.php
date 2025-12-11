<?php

namespace Tests;

use Gift\GiftSelector;

class TestData
{
    const TOY = 'Toy';
    const BOOK = 'Book';
    const PS_5 = 'PS5';

    public static function evaluateRequestFor(callable $childConfiguration): ?string
    {
        return GiftSelector::selectGiftFor(
            $childConfiguration(ChildBuilder::aChild())->build()
        );
    }
}
