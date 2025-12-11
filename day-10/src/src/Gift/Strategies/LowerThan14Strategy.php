<?php

declare(strict_types=1);

namespace Gift\Strategies;

use Gift\Child;

class LowerThan14Strategy extends GiftSelectionStrategy
{
    public function selectGiftFor(Child $child): ?string
    {
        return $this->selectFeasibleGiftFor($child);
    }
}
