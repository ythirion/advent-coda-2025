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

Vous pouvez essayer ce morceau de code à l'aide du playground disponible [ici](https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMi4yLjIxIiwicGxhdGZvcm0iOiJqYXZhIiwiYXJncyI6IiIsIm5vbmVNYXJrZXJzIjp0cnVlLCJ0aGVtZSI6ImlkZWEiLCJjb2RlIjoiZnVuIG1haW4oKSB7XG4gICAgdmFsIG1lc3NhZ2UgPVxuICAgICAgICAgICAgXCJcIlwicWp4IHV3anVmd2Z5bmt4IHV3anNzanN5IGl6IHdqeWZ3aSBxangganFranggeHRzeSBpamd0d2lqeCBqeSBxZiBxdGxueHludnpqIGl6IHl3Zm5zamZ6IGp4eSBqcyB1ZnNzai4gXG5xaiB1andqIHN0anEgaG1qd2htaiB6cyB0eiB6c2ogaWphIGhmdWZncWogaWogcXpuIHV3anlqdyByZm5zIGt0d3lqLiBcbnFqeCBxenluc3ggeHRzeSBpdHpqeCBmYWpoIHFqeCBvdHpqeXggcnRuc3ggZmFqaCBxaiBodGlqLiBcbmZhamggaGp5eWogd2p4dHF6eW50cyB5eiBmeCB1d3R6YWogeWYgYWZxanp3IGp5IGpyZ2Z3dnpqIGlmc3ggaGp5eWogZmFqc3l6d2ogISEhXCJcIlwiXG4gICAgICAgIHByaW50bG4oZGVjaXBoZXIobWVzc2FnZSwgNSkpXG59XG5cbmZ1biBkZWNpcGhlcihtZXNzYWdlOiBTdHJpbmcsIGNlc2FyOiBJbnQpOiBTdHJpbmcgPVxuICAgIG1lc3NhZ2Uuc3BsaXQoXCIgXCIpXG4gICAgICAgIC5qb2luVG9TdHJpbmcoXCIgXCIpIHtcbiAgICAgICAgICAgIGRlY2lwaGVyV29yZChpdCwgY2VzYXIpXG4gICAgICAgIH1cblxuZnVuIGRlY2lwaGVyV29yZCh3b3JkOiBTdHJpbmcsIGNlc2FyOiBJbnQpOiBTdHJpbmcgPSB3b3JkLm1hcCB7IGRlY2lwaGVyQ2hhcihpdCwgY2VzYXIpIH0uam9pblRvU3RyaW5nKFwiXCIpXG5cbmZ1biBkZWNpcGhlckNoYXIoYzogQ2hhciwgY2VzYXI6IEludCk6IENoYXIge1xuICAgIGlmICghYy5pc0xldHRlcigpKSByZXR1cm4gY1xuXG4gICAgdmFsIGJhc2UgPSBpZiAoYy5pc0xvd2VyQ2FzZSgpKSAnYScuY29kZSBlbHNlICdBJy5jb2RlXG4gICAgdmFsIG9mZnNldCA9IChjLmNvZGUgLSBiYXNlIC0gY2VzYXIgKyAyNikgJSAyNlxuICAgIFxuICAgIHJldHVybiAoYmFzZSArIG9mZnNldCkudG9DaGFyKClcbn0ifQ==).