# [Jour 5 – Le piège de la date infernale](https://coda-school.github.io/advent-2025/?day=05)
Le challenge est bien tordu puisqu'il s'agit de rendre un Date Picker moins ergonomique. 
Étant Dev ça tombe bien, c'est exactement ce que je fais naturellement 😉.

Voici quelques idées qui me viennent pêle-mêle en lisant la mission :
- Afficher les mois dans le désordre (Mars, Janvier, Août, ...)
- Inverser les jours de la semaine
- Rendre les années non consécutives (2020, 2022, 2025, 2030, ...)
- Faire commencer le calendrier de manière systématique en l'an 0
- Afficher aléatoirement une année à l'ouverture du calendrier
- Déplacer le calendrier sur la page à chaque clic
- Faire défiler les années depuis l'an 0 jusqu'à l'an 3000 et sélectionner la date en appuyant sur espace
- Encore mieux, utiliser une machine à sous comme au casino pour sélectionner la date
    - L'intérêt c'est que cela devient addictif et que l'utilisateur va vouloir réessayer plusieurs fois

Je vais implémenter la dernière idée qui me semble la plus fun et la plus tordue.

Et voici le résultat :
![Date Picker Infernal](img/date-picker-from-hell.gif)