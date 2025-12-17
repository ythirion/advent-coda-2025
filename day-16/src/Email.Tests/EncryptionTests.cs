using FluentAssertions;
using Xunit;
using Xunit.Abstractions;
using static Email.Encryption;

namespace Email.Tests;

public class EncryptionTests(ITestOutputHelper output)
{
    private readonly Configuration _configuration = createConfiguration(key: "Coda", iv: "2025");

    [Fact]
    public void Decrypt_Email()
    {
        var decryptedContent = decrypt(_configuration, FileUtils.GetFileContent("email"));
        decryptedContent.Should().StartWith("De : Santa [santa@northpole.local](mailto:santa@northpole.local)");
        output.WriteLine(decryptedContent);
    }
}