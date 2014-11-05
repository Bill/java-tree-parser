package com.objectspace.graph.node;

import java.util.Hashtable;

import com.objectspace.util.*;

public class EqualNodes implements IBinaryPredicate
{
  public boolean apply( Object lhs, Object rhs)
    {        
    // Design Decision 28:
    // Two graphs can have nodes with the same names, but that doesn't mean they
    // are "equal" -- name-based identity is only enforced per graph.  See DD 19.
    
    return ((Node)lhs).name().equals( ((Node)rhs).name());
    }
}
