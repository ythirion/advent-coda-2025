# [Jour 15 — Le code parfait de Nori](https://coda-school.github.io/advent-2025/?day=15)
Aujourd'hui il s'agit de relire le code de **Nori**, l’un des elfes les plus enthousiastes de l’équipe technique du Pôle Nord.
Mais attention ! Selon **comment** on relit du code, le résultat peut être… très différent.

Voici deux manières de faire une *code review* : la première est glaciale et contre-productive, la seconde est constructive et bienveillante.

## La *cold review*

```typescript
// "Sérieusement ? Tu publies une classe avec deux méthodes et tu appelles ça un module ? 
// Tu connais les principes SOLID au moins ? C’est du niveau atelier des oursons !"
export class ElfWorkshop {
	 // Pour toi, une tâche c’est juste une chaîne de caractères ? Jamais entendu parler de 'primitive obsession' ? 😬
    taskList: string[] = [];
    
	// À propos de la méthode addTask :
	// "Oh, super, une méthode pour ajouter une tâche. Quelle innovation ! 
	// Tu veux une médaille en sucre d’orge ?"
    addTask(task: string) {
        if (task !== "") {
            this.taskList.push(task);
        }
    }
    
    // "Et ça ? Tu appelles ça de la logique ? Même un lutin de première année ferait mieux. 
    // Essaie d’ajouter une vraie fonctionnalité la prochaine fois."
    completeTask() {
        if (this.taskList.length > 0) {
            return this.taskList.shift();
        }
        // Null ? Tu veux faire planter tout le traîneau ou quoi ?
        return null;
    }
}

// elfWorkshop.spec.ts
import { ElfWorkshop } from './ElfWorkshop';

describe('ElfWorkshop Tasks', () => {
    // "Qui t’a appris à nommer tes tests ? On dirait que tu veux saboter ton propre code."
    test('removeTask should add a task', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Build toy train");
        expect(workshop.taskList).toContain("Build toy train");
    });

	// "Ah oui, test2… la quintessence du nom descriptif. 
	// Tu as tapé sur ton clavier au hasard ?"
    test('test2 checks for task addition', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Craft dollhouse");
        expect(workshop.taskList.includes("Craft dollhouse")).toBeTruthy();
    });

    // "Copié-collé, vraiment ? C’est pour ça qu’on ne peut pas avoir de beaux pipelines."
    test('test2 checks for task addition', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Paint bicycle");
        expect(workshop.taskList.includes("Paint bicycle")).toBeTruthy();
    });

    // "Nom vague encore ! 'Should handle empty tasks correctly' — merci pour le mystère !"
    test('Should handle empty tasks correctly', () => { 
        const workshop = new ElfWorkshop();
        workshop.addTask("");
        expect(workshop.taskList.length).toBe(0);
    });

    // "‘Task removal functionality’… ça pourrait vouloir dire n’importe quoi. Un peu de précision, voyons !"
    test('Task removal functionality', () => { 
        const workshop = new ElfWorkshop();
        workshop.addTask("Wrap gifts");
        const removedTask = workshop.completeTask();
        expect(removedTask).toBe("Wrap gifts");
        expect(workshop.taskList.length).toBe(0);
    });
});
```

### Le problème
Les individus peuvent adopter différents comportements lorsqu’ils font une revue de code. 

En voici quelques exemples :
![Code vengers](img/code-vengers.webp)

* **Bully** : une personne pleine d’ego, qui cherche à démontrer sa supériorité.
* **Gate Keeper** : veut aller vite et se fiche de l’impact de son attitude sur le reste de l’équipe.
* **Mentor** : cherche à aider les autres à progresser, car c’est bénéfique pour tout le monde (les personnes, l’équipe, l’organisation, le produit, etc.).

Dans cette *code review*, les mots utilisés sont très **jugeant**.

Elle est clairement **guidée par l’ego** (type *Bully*).

![Finger pointing](img/fingers.webp)

> L’attention est mise sur la dévalorisation du travail et de la personne, au lieu de proposer un retour constructif visant à l’amélioration.

Ce type de commentaire a un **impact très négatif** sur la motivation des dévs et sur la **dynamique globale de l’équipe**.

### The Ten Commandments of [Egoless Programming](https://blog.codinghorror.com/the-ten-commandments-of-egoless-programming/)
1. Understand and accept that you will make mistakes
2. You are not your code
3. No matter how much "karate" you know, someone else will always know more
4. Don't rewrite code without consultation
5. Treat people who know less than you with respect, deference, and patience
6. The only constant in the world is change
7. The only true authority stems from knowledge, not from position
8. Fight for what you believe, but gracefully accept defeat
9. Don't be "the guy in the room."
10. `Critique code instead of people – be kind to the coder, not to the code`

> Ces principes du *Software Craftsmanship* sont la clé pour devenir un artisan du code durable, humble et inspirant.

## Une `Egoless review`

```typescript
export class ElfWorkshop {
    taskList: string[] = [];

    // C’est une bonne idée de vérifier que la tâche n’est pas vide
    // On pourrait aller un peu plus loin : ignorer aussi les espaces vides (avec task.trim()) ?  
    // Cela éviterait d’ajouter des entrées inutiles dans la liste.
    addTask(task: string) {
        // Belle validation ici ! Peut-être qu’un petit task.trim() !== "" renforcerait encore la robustesse.
        if (task.trim() !== "") {
            this.taskList.push(task);
        }
    }

    // La méthode est simple et efficace !  
    // Quand il n’y a plus de tâches, que penses-tu de retourner un message explicite 
    // (ex. : "Aucune tâche à compléter 🎄") ?  
    // Cela pourrait aider les autres elfes à comprendre ce qui se passe.
    completeTask() {
        // La simplicité de cette logique est appréciable.  
        // Pour plus de clarté, envisager un message spécifique lorsqu’il n’y a aucune tâche.
        if (this.taskList.length > 0) {
            return this.taskList.shift();
        }
        return null;
    }
}

// elfWorkshop.spec.ts
import { ElfWorkshop } from './ElfWorkshop';

describe('ElfWorkshop Tasks', () => {
    let system: ElfWorkshop;

    beforeEach(() => {
        system = new ElfWorkshop();
    });

    // Il semble y avoir eu une petite confusion dans le nom du test.  
    // Pourquoi ne pas le renommer : 'addTask should include a new task in the taskList' ?  
    // Ce serait plus explicite pour les futurs elfes !
    test('removeTask should add a task', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Build toy train");
        expect(workshop.taskList).toContain("Build toy train");
    });

    // Ce nom est un peu générique. Peut-être :  
    // 'addTask successfully adds a craft dollhouse task to the taskList' ?
    test('test2 checks for task addition', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Craft dollhouse");
        expect(workshop.taskList.includes("Craft dollhouse")).toBeTruthy();
    });

    // On dirait un doublon.  
    // On pourrait soit le supprimer, soit préciser ce qu’il teste différemment.  
    // Des tests clairs et uniques sont un vrai cadeau 🎁 pour les prochains lecteurs.
    test('test2 checks for task addition', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Paint bicycle");
        expect(workshop.taskList.includes("Paint bicycle")).toBeTruthy();
    });

    // Excellent réflexe de tester les cas limites !  
    // Pourquoi ne pas le renommer : 'addTask does not add empty tasks to the taskList' ?  
    test('Should handle empty tasks correctly', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("");
        expect(workshop.taskList.length).toBe(0);
    });

    // Bonne couverture du scénario !  
    // Un nom plus explicite : 'completeTask removes the first task and returns it'  
    // permettrait de comprendre le comportement en un clin d’œil ✨
    test('Task removal functionality', () => {
        const workshop = new ElfWorkshop();
        workshop.addTask("Wrap gifts");
        const removedTask = workshop.completeTask();
        expect(removedTask).toBe("Wrap gifts");
        expect(workshop.taskList.length).toBe(0);
    });
});
```

> L’objectif d’une revue de code n’est pas de démontrer sa supériorité,
> mais **d’améliorer collectivement la qualité du code** et de **partager les connaissances** au sein de l’équipe.

Utiliser la **Communication Non Violente (CNV)** peut vraiment nous aider à formuler des retours avec **respect et bienveillance**, afin de favoriser un environnement de travail **positif et productif**.

![NonViolent Communication](img/nvc.webp)

### Les checklists
Utiliser des *checklists* peut grandement améliorer la qualité des revues de code :
elles permettent à toute l’équipe d’être **alignée sur les points à vérifier**.

Voici un exemple :

![Checklist example](img/checklist.webp)

> Nous te recommandons d’en créer une au sein de ton équipe 🎄

Tu peux t’inspirer du modèle ci-dessous — la **Code Review Pyramid** — pour construire la tienne :

![Code Review Pyramid](img/code-review-pyramid.webp)

J'espère sincèrement que cette revue va nous amener, Nori et moi, à avoir une conversation qui nous permette à tous les 2 de grandir. 