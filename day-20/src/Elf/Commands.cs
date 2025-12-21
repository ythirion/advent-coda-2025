using System.Text;
using ConsoleAppFramework;

namespace Elf;

public class Commands
{
    /// <summary>Elf logistique.</summary>
    [Command("")]
    public void Root() => Console.WriteLine();

    /// <summary>
    /// Affichage standard
    /// </summary>
    /// <param name="path"></param>
    public void Normal(string? path = null)
        => PrintInfosFor(
            path,
            items =>
            {
                var builder = new StringBuilder();
                builder.AppendLine("Nom                Type      Taille   Poids  Magie");
                builder.AppendLine("---------------------------------------------------");

                items.Select(item =>
                    {
                        var name = item.Name.PadRight(16)[..16];
                        var type = item.IsDirectory ? "Dossier" : "Fichier";
                        var size = item.IsDirectory ? "-" : item.Size + "";
                        var weight = item.IsDirectory ? "-" : $"{item.Size}g";
                        var magic = item.IsDirectory ? "-" : StarsFor(item.Size);

                        return $"{name}   {type,-9} {size,-8} {weight,-7} {magic}";
                    })
                    .ToList()
                    .ForEach(line => builder.AppendLine(line));

                return builder.ToString();
            });

    /// <summary>
    /// Avoir l'information rapidement
    /// </summary>
    public void Compact(string? path = null)
        => PrintInfosFor(
            path,
            items => string.Join(", ",
                items.Select(item => item.IsDirectory
                    ? $"{item.Name}/"
                    : $"{item.Name} ({item.Size}g, {StarsFor(item.Size)})"
                )
            ));

    /// <summary>
    /// Avoir l'information sous forme d'arbre
    /// </summary>
    public void Tree(string? path = null) => PrintInfosFor(
        path,
        items => TreeFor(items)
    );

    private static string TreeFor(FsItem[] items, string indent = "")
    {
        var builder = new StringBuilder();
        for (var i = 0; i < items.Length; i++)
        {
            var isLast = i == items.Length - 1;
            var prefix = isLast ? "└── " : "├── ";
            builder.Append(indent + prefix);

            if (items[i].IsDirectory)
            {
                builder.AppendLine($"{items[i].Name}/");
                builder.Append(TreeFor(items[i].Children, indent + (isLast ? "    " : "│   ")));
            }
            else
            {
                var weight = $"{items[i].Size}g";
                var stars = StarsFor(items[i].Size);
                builder.AppendLine($"{items[i].Name} ({weight}, {stars})");
            }
        }

        return builder.ToString();
    }

    private static string StarsFor(long size)
        => size switch
        {
            < 20_000 => "✨",
            < 50_000 => "✨✨",
            _ => "✨✨✨"
        };

    private static void PrintInfosFor(string? path, Func<FsItem[], string> formatter)
        => Console.WriteLine(
            formatter(Files.InfosFor(path).Children)
        );
}