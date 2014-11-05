package com.objectspace.graph.node;

import java.util.Enumeration;

import com.objectspace.association.*;

public class AssociatedNodes implements IAssociates
{
  public Enumeration apply( Object object)
    {
    return ((Node)object).associates();
    }
}
