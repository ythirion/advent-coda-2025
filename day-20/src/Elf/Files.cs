using static Elf.FsItem;

namespace Elf;

public static class Files
{
    public static FsItem InfosFor(string? path = null)
    {
        var directory = new DirectoryInfo(
            string.IsNullOrEmpty(path)
                ? Directory.GetCurrentDirectory()
                : path
        );
        return FsItemFor(directory, ChildrenFor(directory));
    }

    private static FsItem[] ChildrenFor(DirectoryInfo directory) =>
        DirectoriesFor(directory)
            .Concat(FilesFor(directory))
            .ToArray();

    private static IEnumerable<FsItem> DirectoriesFor(DirectoryInfo directory)
        => directory
            .GetDirectories()
            .Select(dir => FsItemFor(dir, ChildrenFor(dir)));

    private static IEnumerable<FsItem> FilesFor(DirectoryInfo directory)
        => directory
            .GetFiles()
            .Select(FsItemFor);
}