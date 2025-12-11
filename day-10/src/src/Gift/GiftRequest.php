<?php
declare(strict_types=1);

namespace Gift;

class GiftRequest
{
    public function __construct(
        public readonly string $giftName,
        public readonly bool   $isFeasible
    )
    {
    }
}
