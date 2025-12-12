# [Jour 11 — Un bug bloque la tournée...](https://coda-school.github.io/advent-2025/?day=11)
Le Père Noël est dépité...

> “La release d’hier a cassé la **navigation des Bâtiments**… et un test rouge bloque tout. Peux-tu jeter un œil ?”

Problème : la fonction qui calcule l’étage final **retourne un mauvais résultat**… et du code “elfique” s’est glissé dans le flux.
On va devoir **corriger le bug** dans ce code inconnu afin qu’il calcule l’étage final correctement.

Je vais partir sur du `C#` aujourd'hui.

## Étape 1 — identifier le bug
Pour identifier le bug, on commence par lancer les tests et observer la sortie :
- Il y a une différence de **–4 étages**.
- Nous regardons aussi le fichier contenant les instructions : `6.txt`.

![Failing test](img/failing-test.webp)

Ensuite, on essaie de comprendre le code de production.
- La méthode est définie ainsi : `string` → `int`.
- Elle parcourt chaque caractère du flux et lui associe une valeur numérique.

```csharp
public static int WhichFloor(string instructions)
{
    List<Tuple<char, int>> val = [];

    for (int i = 0; i < instructions.Length; i++)
    {
        var c = instructions[i];

        // Il semble que le test qui échoue concerne cette branche.
        // Les instructions contiennent le symbole elfe 🧝
        if (instructions.Contains("🧝"))
        {
            int j;
            if (c == ')') j = 3;
            // Parce qu'il y a un 🧝 dans le flux en échec
            // La valeur associée ici est -2
            // MAIS ce n’est pas le comportement attendu selon le test…
            // Il manque probablement un cas pour traiter le symbole elfe.
            else j = -2;

            val.Add(new Tuple<char, int>(c, j));
        }
        else if (!instructions.Contains("🧝"))
        {
            val.Add(new Tuple<char, int>(c, c == '(' ? 1 : -1));
        }
        else val.Add(new Tuple<char, int>(c, c == '(' ? 42 : -2));
    }

    // Le Père Noël démarre au rez-de-chaussée (étage 0)
    // puis suit les instructions une à une
    int result = 0;
    foreach (var kp in val)
    {
        result += kp.Item2;
    }
    return result;
}
```

L’emoji 🧝 occupe 2 `char` (`\uD83E` et `\uDD9D`) ce qui explique le décalage de 4 (`2x-2`).
Si on en croit le test ce symbole ne devrait pas être comptabilisé dans le calcul de l’étage.

## Étape 2 — corriger le bug

Nous tentons de corriger le code :

```csharp
public static int WhichFloor(string instructions)
{
    ...
    if (instructions.Contains("🧝"))
    {
        int j;
        if (c == ')') j = 3;
        // On ajoute cette branche
        else if (c == '(') j = -2;
        // Et on ajoute une valeur neutre pour 🧝
        else j = 0;

        val.Add(new Tuple<char, int>(c, j));
    }
    else if (!instructions.Contains("🧝"))
    {
        val.Add(new Tuple<char, int>(c, c == '(' ? 1 : -1));
    }
    else val.Add(new Tuple<char, int>(c, c == '(' ? 42 : -2));

    ...
    return result;
}
```

Le bug est rapidement corrigé 💪
![Fixed test](img/fixed-test.webp)

> Est-ce que tout est parfait maintenant ? Pas si sûr…

## Étape 3 - appliquer la règle du Scout

Si l’on veut éviter la dette technique, on se doit d'appliquer la [Scout Rule](https://www.oreilly.com/library/view/97-things-every/9780596809515/ch08.html), qui nous invite à améliorer continuellement la qualité du code.

![Boy scout rule](img/boy-scout-rule.webp)

> “Always leave the code cleaner than you found it.”

Bien sûr, il faut vérifier que la couverture de tests nous rend suffisamment confiants pour apporter des changements.
Ici, bonne nouvelle : la fonction est **pure** (sans effet de bord) et bien couverte.

![Code coverage](img/coverage.webp)

On repère immédiatement plusieurs opportunités d’amélioration (lisibilité, duplication, branches inutiles…).

```csharp
public static int WhichFloor(string instructions)
{
    // Pas besoin de stocker des tuples, on peut directement sommer des entiers
    // Mauvais nommage : val
    List<Tuple<char, int>> val = [];

    for (int i = 0; i < instructions.Length; i++)
    {
        var c = instructions[i];

        // Trop de conditions et d’assignations
        if (instructions.Contains("🧝"))
        {
            int j;
            if (c == ')') j = 3;
            else if (c == '(') j = -2;
            else j = 0;

            val.Add(new Tuple<char, int>(c, j));
        }
        else if (!instructions.Contains("🧝"))
        {
            val.Add(new Tuple<char, int>(c, c == '(' ? 1 : -1));
        }
        else val.Add(new Tuple<char, int>(c, c == '(' ? 42 : -2));
    }

    int result = 0;
    foreach (var kp in val)
    {
        result += kp.Item2;
    }

    return result;
}
```

J'active le mode `Continuous Testing` afin de refactorer pas à pas en toute sécurité.

![Continuous Testing](img/continuous-testing.webp)

On commence par **extraire** la logique de mapping `elfique` :

![Extract method](img/extract-method.webp)

```csharp
public static int WhichFloor(string instructions)
{
    List<Tuple<char, int>> val = [];

    for (int i = 0; i < instructions.Length; i++)
    {
        var c = instructions[i];

        if (instructions.Contains("🧝"))
        {
            val.Add(new Tuple<char, int>(c, ElfMapping(c)));
        }
        
    ...

private static int ElfMapping(char c) 
    => c switch
    {
        ')' => 3,
        '(' => -2,
        _ => 0
    };
```

---

On simplifie ensuite les branches :

```csharp
for (int i = 0; i < instructions.Length; i++)
{
    var c = instructions[i];

    val.Add(instructions.Contains("🧝")
        ? new Tuple<char, int>(c, ElfMapping(c))
        : new Tuple<char, int>(c, c == '(' ? 1 : -1));
}
```

Et on "modernise" avec `LINQ` pour réduire le bruit visuel :

![Use LinQ](img/linq.webp)

```csharp
public static int WhichFloor(string instructions)
{
    List<Tuple<char, int>> val = [];

    for (int i = 0; i < instructions.Length; i++)
    {
        var c = instructions[i];

        val.Add(instructions.Contains("🧝")
            ? new Tuple<char, int>(c, ElfMapping(c))
            : new Tuple<char, int>(c, c == '(' ? 1 : -1));
    }
    return val.Sum(kp => kp.Item2);
}
```

On introduit des **constantes métier** pour remplacer les `magic strings` et clarifier l’intention :

```csharp
public static class Building
{
    private const char Up = '(';
    private const char Down = ')';
    private const string Elf = "🧝";

    public static int WhichFloor(string instructions)
    {
        List<Tuple<char, int>> val = [];

        for (int i = 0; i < instructions.Length; i++)
        {
            var c = instructions[i];

            val.Add(instructions.Contains(Elf)
                ? new Tuple<char, int>(c, ElfMapping(c))
                : new Tuple<char, int>(c, NormalMapping(c)));
        }

        return val.Sum(kp => kp.Item2);
    }

    private static int ElfMapping(char c)
        => c switch
        {
            Down => 3,
            Up => -2,
            _ => 0
        };

    private static int NormalMapping(char c) => c == Up ? 1 : -1;
}
```

Petit à petit, le code devient **plus lisible, plus simple et plus robuste**.
J'utilise le "concept" de stratégie vu hier pour isoler les différentes logiques de calcul :

```csharp
using Instruction = char;
using FloorStrategy = Func<char, int>;

public static class Building
{
    private const Instruction Up = '(';
    private const Instruction Down = ')';
    private const string ElfSymbol = "🧝";

    private static readonly FloorStrategy Standard = c => c == Up ? 1 : -1;
    private static readonly FloorStrategy Elf = c => c switch
    {
        Down => 3,
        Up => -2,
        _ => 0
    };

    public static int WhichFloor(string instructions)
        => WhichFloor(
            instructions,
            instructions.Contains(ElfSymbol) ? Elf : Standard
        );

    private static int WhichFloor(string instructions, FloorStrategy strategy)
        => instructions
            .Select(strategy)
            .Sum();
}
```

Même dans l’urgence, on peut **réparer sans salir** 😉. #CleanAsYouCode