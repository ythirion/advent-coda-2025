namespace Registry;

public class GiftRegistry
{
    private readonly List<Gift> _gifts = [];
    private const bool Debug = true;

    public GiftRegistry(List<Gift>? initial = null)
    {
        if (initial != null) _gifts = initial;
    }

    public void AddGift(string child, string gift, bool packed = false)
    {
        if (child == string.Empty) Log("child missing");

        if (!DoesGiftExistFor(child, gift))
        {
            _gifts.Add(new Gift(child, gift, packed, "ok"));
        }
    }

    private bool DoesGiftExistFor(string child, string gift)
        => _gifts.Exists(g => g.ChildName == child && g.GiftName == gift);

    public bool MarkPacked(string child)
    {
        var giftsForChild = _gifts.Where(g => g.ChildName == child).ToList();
        giftsForChild.ForEach(g => g.IsPacked = true);

        return giftsForChild.Count != 0;
    }

    public Gift? FindGiftFor(string child)
        => _gifts.FirstOrDefault(g => g.ChildName == child);

    public int ComputeElfScore()
    {
        var score = SumGifts();
        if (Debug) Log($"score: {score}");

        return score;
    }

    private int SumGifts() => _gifts.Sum(g => (g.IsPacked ? 7 : 3) + (!string.IsNullOrEmpty(g.Notes) ? 1 : 0) + 42);
    private static void Log(string message) => Console.WriteLine(message);
}

public record Gift(string ChildName, string GiftName, bool IsPacked, string Notes)
{
    public string ChildName { get; } = ChildName;
    public string GiftName { get; } = GiftName;
    public bool IsPacked { get; set; } = IsPacked;
    public string Notes { get; } = Notes;
}