<?php

declare(strict_types=1);

namespace Gift\Strategies;

use Gift\Child;

class GreaterOrEqual14Strategy extends GiftSelectionStrategy
{
    public function selectGiftFor(Child $child): ?string
    {
        return $child->isKind()
            ? $this->selectFeasibleGiftFor($child)
            : null;
    }
}
