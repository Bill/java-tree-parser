package com.objectspace.util;

import java.util.Enumeration;

// Design Decision 25: generic comparison over enumerations (with predicate passed in).
// Allows for elegant recursion between Comparing.equal on enumerations, and the
// implementation of IBinaryPredicate, EqualNodes.
public class Comparing
  {
  public static boolean equal( Enumeration lhs, Enumeration rhs, IBinaryPredicate pred)
    {
    while( lhs.hasMoreElements())
      {
      if( rhs.hasMoreElements())
        {
        if( ! pred.apply( lhs.nextElement(), rhs.nextElement()))
          return false;
        }
      else
        return false; // rhs shorter than lhs
      }
    if( rhs.hasMoreElements())
      return false; // lhs shorter than rhs
    return true;
    }
  }
