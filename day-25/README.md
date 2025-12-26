# [Jour 25 - L’Audit des cadeaux mécontents](https://coda-school.github.io/advent-2025/?day=25)

Vu qu'on ne peut pas utiliser de `if`, on va utiliser 1 `Regex`.

Pour comprendre et utiliser des `regexp`, vous pouvez regarder sur [https://regexper.com/](https://regexper.com/).

J'avoue qu'on est le 25 décembre, j'ai de nouveau la flemme d'aller dans le détail de ma résolution que j'ai codé aujourd'hui en `go` :

```go
type unhappyCountry struct {
	country          string
	numberOfChildren int
}

func readFile(path string) string {
	data, err := os.ReadFile(path)
	if err != nil {
		panic(err)
	}
	return string(data)
}

func countUnhappiestByCountry(data string) []unhappyCountry {
	regex := regexp.MustCompile(`([A-Za-z]+)-[A-Za-z]+-unhappy-\d+`)
	counts := make(map[string]int)
	for _, m := range regex.FindAllStringSubmatch(data, -1) {
		counts[m[1]]++
	}

	result := make([]unhappyCountry, 0, len(counts))
	for country, count := range counts {
		result = append(result, unhappyCountry{country, count})
	}

	sort.Slice(result, func(i, j int) bool {
		return result[i].numberOfChildren > result[j].numberOfChildren
	})
	return result
}

func printReport(entries []unhappyCountry) {
	log.Println("=== Rapport des Enfants Mécontents ===")
	for _, e := range entries {
		log.Printf("%s : %d mécontents", e.country, e.numberOfChildren)
	}

	total := 0
	for _, e := range entries {
		total += e.numberOfChildren
	}
	log.Printf("\nTotal global : %d enfants mécontents\n", total)
}
```

Et voici le test utilisé :

```go
func TestForInputFile(t *testing.T) {
	printReport(
		countUnhappiestByCountry(
			readFile("input")))
}
```

Concernant le résultat, j'obtiens :

```text
=== Rapport des Enfants Mécontents ===
Poland : 274 mécontents
Brazil : 268 mécontents
Mexico : 261 mécontents
France : 255 mécontents
UK : 251 mécontents
Austria : 250 mécontents
Japan : 250 mécontents
Finland : 248 mécontents
China : 246 mécontents
Spain : 246 mécontents
Portugal : 245 mécontents
Denmark : 245 mécontents
Ireland : 244 mécontents
Canada : 244 mécontents
Argentina : 242 mécontents
Netherlands : 241 mécontents
Chile : 239 mécontents
USA : 239 mécontents
Switzerland : 239 mécontents
Germany : 236 mécontents
Belgium : 236 mécontents
Hungary : 234 mécontents
Greece : 233 mécontents
Czechia : 229 mécontents
Sweden : 225 mécontents
Turkey : 225 mécontents
Norway : 220 mécontents
India : 214 mécontents
Australia : 199 mécontents
Italy : 199 mécontents

Total global : 7177 enfants mécontents
```

Un grand merci à toi d'avoir participé à cette initiative jusqu'au bout 🎅