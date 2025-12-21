namespace Elf;

public class FsItem
{
    private FsItem(
        string name,
        string fullPath,
        bool isDirectory,
        long size,
        DateTime lastModified,
        FsItem[] children)
    {
        Name = name;
        FullPath = fullPath;
        IsDirectory = isDirectory;
        Size = size;
        LastModified = lastModified;
        Children = children;
    }

    public string Name { get; }
    public string FullPath { get; }
    public bool IsDirectory { get; }
    public long Size { get; }
    public DateTime LastModified { get; }
    public FsItem[] Children { get; }

    public static FsItem FsItemFor(DirectoryInfo info, FsItem[] children) =>
        new(info.Name, info.FullName, true, 0, info.LastWriteTime, children);

    public static FsItem FsItemFor(FileInfo info) =>
        new(info.Name, info.FullName, false, info.Length, info.LastWriteTime, []);
}