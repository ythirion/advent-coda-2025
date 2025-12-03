# [Jour 2 — Compter les Rennes](https://coda-school.github.io/advent-2025/?day=02)
La mission est d'écrire un petit programme en `C` pour aider le Père Noël à **compter automatiquement les rennes présents** !

Les instructions sont plutôt précises et va permettre d'avancer sereinement :

- Crée une structure `Reindeer` représentant un renne (nom + présence).
- Initialise un tableau avec les **8 rennes officiels** du traîneau.
- Certains sont présents, d’autres non : ton algorithme doit compter uniquement les présents.
- Affiche le résultat dans une phrase lisible pour Santa.

1 fichier `main.c` a été créé pour l'occasion.
```c
int main(void) {
    printf("🎅 Santa: %d out of %d reindeers are present in the stable tonight.\n", 0, 0);
    return 0;
}
```

## Étape 1 : créer la structure Reindeer
```c
typedef struct {
    const char* name;
    bool is_present;
} Reindeer;
```

## Étape 2 : initialiser le tableau des rennes
```c
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
```

## Étape 3 : compter les rennes présents
```c
// On ajoute le paramètre pour anticiper le fait que le tableau peut changer de taille
int countPresentReindeers(const Reindeer reindeers[], size_t number_of_reindeers) {
    int count = 0;
    for (int i = 0; i < number_of_reindeers; i++) {
        if (reindeers[i].is_present) count++;
    }
    return count;
}
```

## Étape 4 : afficher le résultat pour `Santa`
```c
int main(void) {
    size_t number_of_reindeers = sizeof(reindeers) / sizeof(reindeers[0]);
    printf("🎅 Santa: %d out of %lu reindeers are present in the stable tonight.\n",
           countPresentReindeers(reindeers, number_of_reindeers),
           number_of_reindeers);
    return 0;
}
```

Ce qui donne en exécution :
```
🎅 Santa: 4 out of 8 reindeers are present in the stable tonight.
```