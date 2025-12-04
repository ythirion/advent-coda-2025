# [Jour 3 — Le script fantôme](https://coda-school.github.io/advent-2025/?day=03)
Aujourd'hui la mission est d'identifier pourquoi le script magique (`backup.sh`) du Père Noël refuse de s'exécuter, et de corriger le problème.

Le problème est apparemment lié aux permissions du fichier.
```bash
bash: ./backup.sh: Permission denied
```

## Vérifier les permissions
```bash
ls -l backup.sh
```

Voici la sortie :
```bash
-rw-r--r--@ 1 yot  staff  192 Dec 03 08:00 backup.sh
```

Ici, personne ne peut exécuter (`x` absent).

## **Corriger les droits**
Le Père Noël doit être le seul à lancer ce script.
Nous allons donc lui donner les droits d'exécution, et retirer tous les autres droits.

Pour ce faire on utilise la commande `chmod` (change mode).

Les permissions sous Linux sont représentées par des nombres :
- `4` = lecture (read)
- `2` = écriture (write)
- `1` = exécution (execute)

Nous allons donner les droits uniquement au propriétaire du fichier (le Père Noël ici).
Pour ce faire nous allons utiliser `700` :
- `7` (4+2+1) pour le propriétaire : lecture + écriture + exécution
- `0` pour le groupe : aucune permission
- `0` pour les autres utilisateurs : aucune permission

```bash
chmod 700 backup.sh
```

Résultat :
```bash
ls -l backup.sh
-rwx------@ 1 Santa Claus  staff  192 Dec 03 08:02 backup.sh
```

## Exécuter le script (en tant que Père Noël)
```bash
./backup.sh
```

Et voilà, le script s'exécute correctement !
```
🔒 Sauvegarde en cours...
🎁 La liste des enfants sages a bien été sauvegardée !
```