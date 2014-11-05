package com.objectspace.graph;

import com.objectspace.association.*;
import com.objectspace.util.*;

public class GraphComparing
{
  public static boolean equalGraphs( Object lhs, Object rhs, final IBinaryPredicate equalNodes, final IAssociates associates)
    {
    if( ! equalNodes.apply( lhs, rhs) )
      return false;
        
    return Comparing.equal( associates.apply( lhs), associates.apply( rhs),
      new IBinaryPredicate () 
      {
	  // can't reuse formal parameter names
        public boolean apply( Object lhsInner, Object rhsInner)
          {
          return equalGraphs( lhsInner, rhsInner, equalNodes, associates);
          }
      }
    );
    }
}
