<?php
declare(strict_types=1);

namespace Tests;

use Faker\Factory;
use Faker\Generator;
use Gift\Behavior;
use Gift\Child;
use Gift\GiftRequest;

class ChildBuilder
{
    private Generator $faker;
    private int $age;
    private Behavior $behavior = Behavior::NICE;
    private array $giftRequests = [];
    private float $kindness;

    public function __construct()
    {
        $this->kindness = 0;
        $this->faker = Factory::create();
    }

    public static function aChild(): self
    {
        return new self();
    }

    public function under14(): self
    {
        $this->age = $this->faker->numberBetween(0, 13);
        return $this;
    }

    public function upperOrEqual14(): self
    {
        $this->age = $this->faker->numberBetween(14, 21);
        return $this;
    }

    public function kind(): self
    {
        $this->kindness = $this->faker->randomFloat(2, 0.51, 1.0);
        return $this;
    }

    public function notKind(): self
    {
        $this->kindness = $this->faker->randomFloat(2, 0.01, 0.5);
        return $this;
    }

    public function nice(): self
    {
        $this->behavior = Behavior::NICE;
        return $this;
    }

    public function normal(): self
    {
        $this->behavior = Behavior::NORMAL;
        return $this;
    }

    public function naughty(): self
    {
        $this->behavior = Behavior::NAUGHTY;
        return $this;
    }

    public function requestingFeasibleGift(string $giftName = 'An feasible gift'): self
    {
        $this->giftRequests[] = new GiftRequest($giftName, true);
        return $this;
    }

    public function requestingInfeasibleGift(string $giftName = 'An infeasible gift'): self
    {
        $this->giftRequests[] = new GiftRequest($giftName, false);
        return $this;
    }

    public function build(): Child
    {
        return new Child(
            'Jane',
            'Doe',
            $this->age,
            $this->behavior,
            $this->kindness,
            $this->giftRequests
        );
    }
}
