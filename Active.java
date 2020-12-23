import java.util.*;

public class Active 
{
	int ActiveLength;
	int ActiveEdge;
	SuffixNode ActiveNode;

	public Active (SuffixNode node)
	{
		ActiveLength = 0;
		ActiveEdge = -1;
		ActiveNode = node;
	}
}