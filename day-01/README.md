# [Jour 1 — Un curieux message](https://coda-school.github.io/advent-2025/?day=01)
Ici notre mission est de déchiffrer le message envoyé par le Père Noël :
```
qjx uwjufwfynkx uwjssjsy iz wjyfwi qjx jqkjx xtsy ijgtwijx jy qf qtlnxynvzj iz ywfnsjfz jxy js ufssj. 
qj ujwj stjq hmjwhmj zs tz zsj ija hfufgqj ij qzn uwjyjw rfns ktwyj. 
qjx qzynsx xtsy itzjx fajh qjx otzjyx rtnsx fajh qj htij.
fajh hjyyj wjxtqzynts yz fx uwtzaj yf afqjzw jy jrgfwvzj ifsx hjyyj fajsyzwj !!!
```

L'indice donné est le suivant :
> Pour trouver la clé, recule chaque lettre du même nombre de pas. Ce nombre est égal au nombre de lettres du mot **RENNE**.

Cela laisse à penser qu'il faut utiliser un [chiffre de César](https://fr.wikipedia.org/wiki/Chiffrement_par_d%C3%A9calage) ou "chiffrement par décalage" avec une clé de 5 (le mot "RENNE" contient 5 lettres).

Par exemple :
```
q -> l
j -> e
x -> s
```

Si on applique un décalage de 5 vers l'arrière sur chaque lettre du message chiffré, on obtient le message en clair suivant :

```text
les preparatifs prennent du retard les elfes sont debordes et la logistique du traineau est en panne.
le pere noel cherche un ou une dev capable de lui preter main forte.
les lutins sont doues avec les jouets moins avec le code. 
avec cette resolution tu as prouve ta valeur et embarque dans cette aventure.
```

## Développer le déchiffrage
On peut aller plus loin en s'amusant à déchiffrer le message avec le langage de son choix.

Ici je l'ai résolu à l'aide de `kotlin` :

```kotlin 
fun main() {
    val message = """qjx uwjufwfynkx uwjssjsy iz wjyfwi qjx jqkjx xtsy ijgtwijx jy qf qtlnxynvzj iz ywfnsjfz jxy js ufssj. 
qj ujwj stjq hmjwhmj zs tz zsj ija hfufgqj ij qzn uwjyjw rfns ktwyj. 
qjx qzynsx xtsy itzjx fajh qjx otzjyx rtnsx fajh qj htij. 
fajh hjyyj wjxtqzynts yz fx uwtzaj yf afqjzw jy jrgfwvzj ifsx hjyyj fajsyzwj !!!"""
    
    println(decipher(message, 5))
}

fun decipher(message: String, cesar: Int): String =
    message.map { c -> if (c.isLetter()) 'a' + (c - 'a' - cesar + 26) % 26 else c }
           .joinToString("")
```

Vous pouvez essayer ce morceau de code à l'aide du playground disponible [ici](https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMi4yLjIxIiwicGxhdGZvcm0iOiJqYXZhIiwiYXJncyI6IiIsIm5vbmVNYXJrZXJzIjp0cnVlLCJ0aGVtZSI6ImlkZWEiLCJjb2RlIjoiZnVuIG1haW4oKSB7XG4gICAgdmFsIG1lc3NhZ2UgPSBcIlwiXCJxanggdXdqdWZ3Znlua3ggdXdqc3Nqc3kgaXogd2p5ZndpIHFqeCBqcWtqeCB4dHN5IGlqZ3R3aWp4IGp5IHFmIHF0bG54eW52emogaXogeXdmbnNqZnoganh5IGpzIHVmc3NqLiBcbnFqIHVqd2ogc3RqcSBobWp3aG1qIHpzIHR6IHpzaiBpamEgaGZ1ZmdxaiBpaiBxem4gdXdqeWp3IHJmbnMga3R3eWouIFxucWp4IHF6eW5zeCB4dHN5IGl0emp4IGZhamggcWp4IG90emp5eCBydG5zeCBmYWpoIHFqIGh0aWouIFxuZmFqaCBoanl5aiB3anh0cXp5bnRzIHl6IGZ4IHV3dHphaiB5ZiBhZnFqencgankganJnZnd2emogaWZzeCBoanl5aiBmYWpzeXp3aiAhISFcIlwiXCJcbiAgICBcbiAgICBwcmludGxuKGRlY2lwaGVyKG1lc3NhZ2UsIDUpKVxufVxuXG5mdW4gZGVjaXBoZXIobWVzc2FnZTogU3RyaW5nLCBjZXNhcjogSW50KTogU3RyaW5nID1cbiAgICBtZXNzYWdlLm1hcCB7IGMgLT4gaWYgKGMuaXNMZXR0ZXIoKSkgJ2EnICsgKGMgLSAnYScgLSBjZXNhciArIDI2KSAlIDI2IGVsc2UgYyB9XG4gICAgICAgICAgIC5qb2luVG9TdHJpbmcoXCJcIikifQ==).