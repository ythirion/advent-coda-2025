using FluentAssertions;
using Navigation;
using Xunit;
using static System.IO.File;

namespace Navigation.Tests
{
    public class VerticalShiftShould
    {
        [Theory]
        [InlineData("1", 0)]
        [InlineData("2", 3)]
        [InlineData("3", -1)]
        [InlineData("4", 53)]
        [InlineData("5", -3)]
        [InlineData("6", 2920)]
        public void Return_Floor_Number_Based_On_Instructions(string fileName, int expectedFloor)
            => Building.WhichFloor(ReadAllText($"{fileName}.txt"))
                .Should()
                .Be(expectedFloor);
    }
}