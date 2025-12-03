#include <stdbool.h>
#include <stdio.h>

typedef struct {
    const char *name;
    bool is_present;
} Reindeer;

Reindeer reindeers[8] = {
    {"Dasher", true},
    {"Dancer", false},
    {"Prancer", false},
    {"Vixen", false},
    {"Comet", true},
    {"Cupid", false},
    {"Donner", true},
    {"Blitzen", true},
};

int countPresentReindeers(const Reindeer reindeers[], size_t number_of_reindeers) {
    int count = 0;
    for (int i = 0; i < number_of_reindeers; i++) {
        if (reindeers[i].is_present) count++;
    }
    return count;
}

int main(void) {
    size_t number_of_reindeers = sizeof(reindeers) / sizeof(reindeers[0]);
    printf("🎅 Santa: %d out of %lu reindeers are present in the stable tonight.\n",
           countPresentReindeers(reindeers, number_of_reindeers),
           number_of_reindeers);
    return 0;
}
