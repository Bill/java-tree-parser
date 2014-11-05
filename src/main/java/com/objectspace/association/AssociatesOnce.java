package com.objectspace.association;

import java.util.Enumeration;
import java.util.Dictionary; // FIXME: will be Map in Java 1.2

// Design Decision 44:  This is so frickin' cool!  Circularity checking is just
// a special kind of associates "expander"!!!
// Design Decision 45:  Delegate actual node expansion to subclass in order to
// make this behavior reusable across object types.
// Design Decision 47: Use GOF Decorator pattern in order to be able to compose
// this object with other IAssociates.
public class AssociatesOnce implements IAssociates
{
  public AssociatesOnce( Dictionary seen, IAssociates other)
    {
    // Design Decision 46:
    // Rather than have this class hard code the dictionary type (e.g. Hashtable)
    // I've chosen to allow caller to specify it.  This can lead to extremely 
    // space-efficient solutions if for instance, it's OK to pollute the target
    // objects with a "seen" bit.  In that case the dictionary would be stateless
    // and would just cast the arg to the appropriate type to extract the seen bit.
    this.seen = seen;
    this.other = other;
    }
  
  public Enumeration apply( Object object)
    {
    if( seen.get( object) == null)
      {
      seen.put( object, object);
      return other.apply( object);
      }    
    else
      return com.objectspace.util.EmptyEnumeration.EmptyEnumeration;
    }
  
  private Dictionary seen; // FIXME: will be Map in Java 1.2
  private IAssociates other;
}
