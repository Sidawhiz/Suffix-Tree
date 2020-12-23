import java.util.*;


public class SuffixEdge
{
	int start;
	int index;
	End end;
	SuffixNode son;
	SuffixNode parent;

	public SuffixEdge(int Star, End en)
	{
		start = Star;
		end = en;
	}
}