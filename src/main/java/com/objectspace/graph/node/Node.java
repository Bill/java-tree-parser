package com.objectspace.graph.node;

import java.util.*;

// A node in a graph.  Associated (uni-directionally) with zero or more other Nodes.
public class Node
{
  public Node( String name)
    {
    this.name = name;
    }
  
  public void addAssociate( Node associate)
    {
    if( associates == null)
      // Design Decision 18: associates should be a _set_ but I have decided to leave it
      // a sequence for simplicity's sake.  As a consequence, the _order_ in which 
      // associates are added is important _and_ nodes with the same associates in 
      // _different_ orders are _not_ equal.  Left as an exercise to the reader ;-)
      associates = new Vector();
    associates.addElement( associate);
    }
  
  public String name()
    {
    return name;
    }
  
  public Enumeration associates()
    {
    // BUG HISTORY 2: used to lazily instantiate associates
    // container.  Got null pointer exception at this line.
    // Design Decision 27: I want to still lazily instantiate the associates
    // container, so I'm going to build a "fake" enumeration when my associates
    // is still not existent.
    if( associates == null)
      return com.objectspace.util.EmptyEnumeration.EmptyEnumeration;
    else
      return associates.elements();
    }
  
  private String name;
  private Vector associates;
  }
