using Elf;
using static ConsoleAppFramework.ConsoleApp;

App().Run(args);
return;

static ConsoleAppBuilder App()
{
    var consoleAppBuilder = Create();
    consoleAppBuilder.Add<Commands>();

    return consoleAppBuilder;
}