<?php

namespace Gift\Strategies;

use Gift\Child;

abstract class GiftSelectionStrategy
{
    abstract public function selectGiftFor(Child $child): ?string;

    protected function selectFeasibleGiftFor(Child $child): ?string
    {
        $feasibleGifts = $child->feasibleGifts();
        return !empty($feasibleGifts)
            ? $this->selectGift($child, $feasibleGifts)
            : null;
    }

    private function selectGift(Child $child, array $feasibleGifts)
    {
        return $child->hasNormalBehavior()
            ? end($feasibleGifts)->giftName
            : reset($feasibleGifts)->giftName;
    }
}
