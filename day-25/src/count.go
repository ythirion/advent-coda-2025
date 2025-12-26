package main

import (
	"log"
	"os"
	"regexp"
	"sort"
)

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
