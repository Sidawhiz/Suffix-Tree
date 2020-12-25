import java.util.*;

public class SuffixTree
{
	private SuffixNode root;
	private Active active;
    private int remaining;
    private End end;
    private char input[];
    private static char UNIQUE_CHAR = '$';

    private static class EndOfPathException extends Exception{}

    public SuffixTree(String inpu)
    {
    	int glen = inpu.length() + 1;
        input = new char[glen];
        for(int i=0; i < glen-1; i++){
            input[i] = inpu.charAt(i);
        }
        input[glen-1] = UNIQUE_CHAR;
    }

    private SuffixEdge SelectEdge() //retrieves correct child as per activeEdge //activelength != 0, gives correct edge just by using activedge
    {
    	return active.ActiveNode.child[input[active.ActiveEdge]-' '];
    }

    private SuffixEdge SelectEdge(int i)  //retrieves correct child according to index //activelength = 0. i is phase . goes to correct edge after we enter node by using phase
    {
    	return active.ActiveNode.child[input[i]-' '];
    }

    private int NoOfElem(SuffixEdge edge) // Returns number of elements in a particular edge
    {
    	return edge.end.end - edge.start + 1;
    }

    public void build()
    {
    	root = new SuffixNode();
    	active = new Active(root);
    	end = new End(-1);
    	int size = this.input.length;
    	for (int i = 0 ; i<input.length ; i++)
    	{
    		Phase(i);
    	}
    	//System.out.println(remaining);
    	if(remaining!= 0)
    	{
    		System.out.println("Something Wrong Happened");
    	}
    	for(SuffixEdge edge : root.child)
    	{
    		if(edge!=null)
    		{
    			SetIndexUsingDFS(edge, 0 , size );
    		}
    	}
    	//ArrayList<ArrayList<Character>> lis = new ArrayList<ArrayList<Character>>();
    	//Map<Integer,ArrayList<ArrayList<Character>>> map = new HashMap<Integer,ArrayList<ArrayList<Character>>>();
    	//PrintSuffixes(root , lis, map);
    	//ArrayList<Integer> sortedk = new ArrayList<Integer>(map.keySet());


    }

    private Character NextPoint(int i) throws EndOfPathException
    {
    	SuffixEdge edge = SelectEdge();
    	if(active.ActiveLength == 0)
    	{
    		return null;
    	}
    	if(NoOfElem(edge)>active.ActiveLength)
    	{
    		return input[active.ActiveLength + edge.start];
    	}
    	if(NoOfElem(edge)==active.ActiveLength)
    	{
    		if(edge.son.child[input[i]-' '] != null)
    		{
    			return input[i];
    		}
    	}
    	active.ActiveNode = edge.son ;
    	active.ActiveLength = active.ActiveLength - NoOfElem(edge);
    	active.ActiveEdge = active.ActiveEdge + NoOfElem(edge);
    	return NextPoint(i);
    	
    }

    private void walkdown(int i)
    {	
    	SuffixEdge edge = SelectEdge();
    	if(NoOfElem(edge)<=active.ActiveLength)
    	{
    		SuffixNode child = edge.son;
    		active.ActiveNode = child;
    		active.ActiveLength = active.ActiveLength - NoOfElem(edge) + 1;
    		active.ActiveEdge = child.child[input[i]-' '].start;
    	}
    	else
    	{
    		active.ActiveLength++;
    	}
    }

    private void SetIndexUsingDFS(SuffixEdge edge,int val, int size )   // size is the size of input[].... for edges just after root initialise val with 0
    {
    	if(edge == null)
    	{
    		return;
    	}
    	if(edge.son==null)
    	{	
    		edge.index = size- val - NoOfElem(edge);
    		return;
    	}
    	if(edge.son!=null)
    	{
    		edge.index = -1;
    	}
    	int val2;
    	val2= val + NoOfElem(edge);
    	for (SuffixEdge edge2 : edge.son.child)
    	{
    		SetIndexUsingDFS(edge2 , val2 , size);
    	}
    }

    public void Phase(int i)
    {
    	remaining++;

    	end.end++;

    	SuffixNode lastNodebuilt = new SuffixNode();
    	lastNodebuilt = null ;
    	SuffixNode tempnode = new SuffixNode();
    	int templen;
    	int tempedge;

    	while (remaining>0)
    	{	
    		tempedge = active.ActiveEdge;
    		templen = active.ActiveLength;
    		tempnode = active.ActiveNode;
    		if(active.ActiveLength == 0) // If activelength is 0 activenode is always root
    		{	
    			if(SelectEdge(i)!=null) // already existence  //Rule3
    			{
    				active.ActiveEdge = SelectEdge(i).start;
    				active.ActiveLength++;
    				break;
    			}
    			else // Rule2 // If activelength is 0 and new edge is created at root no need to update active field
    			{	
    				active.ActiveNode.child[input[i]-' '] = new SuffixEdge(i,end);
    				active.ActiveNode.child[input[i]-' '].parent = active.ActiveNode;
    				SuffixNode temp = active.ActiveNode;
    				if(lastNodebuilt != null && (lastNodebuilt.SuffixLink == root || lastNodebuilt.SuffixLink == null) )
    				{
    					lastNodebuilt.SuffixLink = temp;
    				}
    				lastNodebuilt = active.ActiveNode ; 
    				if(lastNodebuilt.SuffixLink == null && lastNodebuilt != root)
    				{
    					lastNodebuilt.SuffixLink = root;
    				}
    				remaining--;
    				if(active.ActiveNode != root)
    				{	
    					if(active.ActiveNode != root)
    					{
    						active.ActiveNode = active.ActiveNode.SuffixLink;
    					}
    				}	

    			}
    		}
    		else // If activelength != 0 we need to check next character after active point
    		{
    			try
    			{
    				Character ch = NextPoint(i);  // what is the charater after current activepoint
    				if(ch==null)
    				{	
    					SuffixEdge edge = new SuffixEdge(i,this.end);
    					active.ActiveNode.child[input[i]-' '] = edge;
    					edge.parent = active.ActiveNode;
    					remaining--;
    					SuffixNode temp = new SuffixNode();
    					//System.out.println(active.ActiveNode);
    					temp = active.ActiveNode;
    					//System.out.println(lastNodebuilt);
    					
    					if(lastNodebuilt != null && (lastNodebuilt.SuffixLink == root || lastNodebuilt.SuffixLink == null) )
    					{
    						lastNodebuilt.SuffixLink = temp;
    					}
    					lastNodebuilt = temp;
    					if(lastNodebuilt.SuffixLink == null && lastNodebuilt!= root)
    					{
    						lastNodebuilt.SuffixLink = root;
    					}		
    					active.ActiveNode = tempnode;
    					active.ActiveEdge = tempedge;
    					active.ActiveLength = templen;
    					if(active.ActiveNode != root)
    					{
    						active.ActiveNode = active.ActiveNode.SuffixLink;
    					}
    					else
    					{
    						active.ActiveEdge = active.ActiveEdge +1;
    						active.ActiveLength = active.ActiveLength - 1;
    					}
    				}
    				else
    				{

    					if(ch == input[i]) // nextPoint is same as current phase // go to that point and update your active class
    					{
    						walkdown(i);
    							
    						if( lastNodebuilt != null && (lastNodebuilt.SuffixLink == root || lastNodebuilt.SuffixLink == null)) // in the same phase is thier another created node 
    						{	
    							lastNodebuilt.SuffixLink = active.ActiveNode;
    						}
    						break;
    					}
    			
    				
    					else // NEXT CHARACTER IS NOT SAME AS PHASE CHARACTER SO RULE 2 EXTENSION AND UPDATE TO ACTIVE POINT
    					{	
    						SuffixEdge edge = SelectEdge();
    						SuffixNode par = edge.parent;
    						int oldstart = edge.start;
    						int oldend = edge.end.end;
    						SuffixNode temp = new SuffixNode(); // temp is newly created node
    						edge.start = edge.start + active.ActiveLength;
    						edge.parent = temp ;
    						temp.child[input[edge.start]-' '] = edge;
    						SuffixEdge edge2 = new SuffixEdge(oldstart,new End(oldstart+active.ActiveLength-1));
    						edge2.parent = par;
    						par.child[input[oldstart]-' '] = edge2;
    						edge2.son = temp;

    						SuffixEdge newleafedge = new SuffixEdge(i, this.end);
    						newleafedge.parent = temp;
    						temp.child[input[i] - ' '] = newleafedge ; 

    						if(lastNodebuilt != null && (lastNodebuilt.SuffixLink == root || lastNodebuilt.SuffixLink == null) )
    						{
    							lastNodebuilt.SuffixLink = temp;
    						}

    						lastNodebuilt = temp;
    						if(lastNodebuilt.SuffixLink == null && lastNodebuilt!=root )
    						{    							
    							lastNodebuilt.SuffixLink = root;
    						}

    						if(active.ActiveNode != root)
    						{
    							active.ActiveNode = active.ActiveNode.SuffixLink;
    						}
    						else
    						{
    							active.ActiveEdge = active.ActiveEdge +1;
    							active.ActiveLength = active.ActiveLength - 1;
    						}
    						remaining = remaining -1;

    					}
    				}	
    			}
    			catch(EndOfPathException e)
    			{	
    				SuffixEdge edge = SelectEdge();
    				SuffixNode temp = edge.parent;
    				if(lastNodebuilt != null)
    				{
    					lastNodebuilt.SuffixLink = temp;
    				}
    				lastNodebuilt = temp;
    				if(active.ActiveNode != root)
    				{
    					active.ActiveNode = active.ActiveNode.SuffixLink;
    				}
    				else
    				{
    					active.ActiveEdge = active.ActiveEdge +1;
    					active.ActiveLength = active.ActiveLength - 1;
    				}
    				remaining--;
    			}		
    		}
    	}
    }

    /*public void  PrintSuffixes(SuffixNode root, ArrayList<ArrayList<Character>> arr , Map<Integer,ArrayList<ArrayList<Character>>> map)
    {	
    	for(SuffixEdge edge : root.child)
    	{	
    		if(edge !=null &&  edge.son != null)
    		{
    			int x = edge.start;
    			int y = edge.end.end;
    			ArrayList<Character> temp = new ArrayList<Character>();
    			for(int i = x ; i<y+1 ; i++)
    			{
    				temp.add(input[i]);
    			}
    			arr.add(temp);
    			PrintSuffixes(edge.son, arr , map);
    			arr.remove(arr.size()-1);
    		}
    		else if(edge != null)
    		{
    			int x = edge.start;
    			int y = edge.end.end;
    			ArrayList<Character> temp = new ArrayList<Character>();
    			for(int i = x; i<y+1; i++)
    			{
    				temp.add(input[i]);
    			}
    			arr.add(temp);
    			
    			System.out.println(arr);

    			arr.remove(arr.size()-1);
    		}
    	}
    }*/

    /*public static void main(String args[])
    {	
    	String a = "Coronaviruses enter host cells by first, binding to a cell surface receptor for viral attachment. Subsequently they enter the endosomes and eventually fuse viral and lysosomal membranes. The surface-anchored spike protein mediate the virus entry. SARS-CoV-2 spike binds to its receptor human ACE2 through its receptor-binding domain and is activated by human proteases. The respiratory track contains lots of ACE2 receptors making it the primary colony for the virus. Its cell entry is preactivated by furin which reduces its dependence on cell proteases. Proteases are capable of hydrolyzing peptide bonds in proteins. The host cell serine protease TMPRSS2 primes the S protein of SARS-CoV-2 for entry. The S protein has two domains (parts) in it the S1 and the S2 and both of them have specified functions that work in the viral entry to the host cell. The S1 domain functions to cling to the typical receptor, while the S2 domain helps in fusion with the membrane of the host cell. Now, to be effective, the S protein needs to be cleaved for the separation of S1 and S2 domains. The TMPRSS2 protein present in the host cell (human cell) does this cleaving. This makes the TMPRSS2 protein an essential ingredient in the viral entry to the host cell. Once inside the cell, viral polyproteins are synthesized that encode for the replicase-transcriptase complex. The virus then synthesizes RNA via its RNA-dependent RNA polymerase. Structural proteins are synthesized leading to completion of assembly and release of viral particles.";
    	SuffixTree dash = new SuffixTree("Coronaviruses ó enter host cells by first, binding to a cell surface receptor for viral attachment. Subsequently they enter the endosomes and eventually fuse viral and lysosomal membranes. The surface-anchored spike protein mediate the virus entry. SARS-CoV-2 spike binds to its receptor human ACE2 through its receptor-binding domain and is activated by human proteases. The respiratory track contains lots of ACE2 receptors making it the primary colony for the virus. Its cell entry is preactivated by furin which reduces its dependence on cell proteases. Proteases are capable of hydrolyzing peptide bonds in proteins. The host cell serine protease TMPRSS2 primes the S protein of SARS-CoV-2 for entry. The S protein has two domains (parts) in it the S1 and the S2 and both of them have specified functions that work in the viral entry to the host cell. The S1 domain functions to cling to the typical receptor, while the S2 domain helps in fusion with the membrane of the host cell. Now, to be effective, the S protein needs to be cleaved for the separation of S1 and S2 domains. The TMPRSS2 protein present in the host cell (human cell) does this cleaving. This makes the TMPRSS2 protein an essential ingredient in the viral entry to the host cell. Once inside the cell, viral polyproteins are synthesized that encode for the replicase-transcriptase complex. The virus then synthesizes RNA via its RNA-dependent RNA polymerase. Structural proteins are synthesized leading to completion of assembly and release of viral particles.");
    	dash.build();
    }*/


}