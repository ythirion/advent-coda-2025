package main

import (
	"testing"
)

func TestForInputFile(t *testing.T) {
	printReport(
		countUnhappiestByCountry(
			readFile("input")))
}
