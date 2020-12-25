import java.util.*;

public class SuffixNode
{
	SuffixEdge[] child = new SuffixEdge[256];
	//ArrayList<SuffixEdge> child = new ArrayList<SuffixEdge>(TOTAL);
	SuffixNode SuffixLink ;

	public SuffixNode(){
		/*for(int i =0; i<TOTAL ; i++)
		{
			child.set(i,null);
		}*/
	}
}