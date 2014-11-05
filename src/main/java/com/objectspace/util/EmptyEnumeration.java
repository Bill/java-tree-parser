package com.objectspace.util;

import java.util.Enumeration;

// Design Decision 34: this is so handy I moved it out where everyone could use it.
public class EmptyEnumeration implements Enumeration
{
  public boolean hasMoreElements() 
    { 
    return false;
    }
  public Object nextElement() 
    { 
    return null;
    }
  
  // Design Decision 28: since the "fake" enumeration has no state, it can be
  // shared (Flyweight GOF)
  // Design Decision 42: move this singleton to this class so it could be shared.
  // I found lots of other classes needed it.
  public static final Enumeration EmptyEnumeration = new EmptyEnumeration();
  
}
