package com.objectspace.graph;

import java.util.Enumeration;
import java.util.Hashtable;

import com.objectspace.association.*;
  
// Design Decision 36: you may hate me for saying this but this is Master-level design:
// the problem is: we don't want to enumerate everything "ahead of time".  Instead we
// want to do it "on demand".  To do so I use an Inner class to implement a "promise".
// (refer to Structure and Interpretation of Computer Programs, Abelson & Sussman
// chapter on Streams).  Note that if you don't do this form a lot then it might take
// a while to "sink in".  ** It takes me OTOO an hour each time I need to re-grok it **
// This "delayed evaluation" is a natural extension to the recursive solution.

// Design Decision 43: it is important to decide whether the empty stream is represented
// by an object who's head is null, or by a null object.  It's about like deciding whether
// to be one-based or zero-based on indexes -- if you vaccilate then things get messy.
// We will denote an empty stream by a stream who's head is null.

public class EnumerateGraph implements Enumeration
{  
  public EnumerateGraph( Object node, IAssociates associate)
    {
    this.associate = associate;
    theStream = enumerateTree( node);
    }
  
  public boolean hasMoreElements()
    {
    return theStream != null && theStream.head() != null;
    }
  
  public Object nextElement()
    {
    if( theStream == null)
      return null;
    Object result = theStream.head();
    theStream = theStream.tail();
    return result;
    }
      
  // Here's the "stream" magic (see DD 36 above).
  // Design Decision 37: it's a static because it keeps all its state on the stack.
  // See page 246 SICP
  private IObjectStream enumerateTree( final Object node)
    {
    final Enumeration associates = associate.apply( node);
    if( associates.hasMoreElements() )
      return new IObjectStream ()
        { 
        public Object head()
          {
          return node;
          }
        public IObjectStream tail()
          {
          // Extension of append-streams on page 246 SICP, from two streams to N
          // Design Decision 40: builds on binary version of appendStreams.
          
          // associates has at least one element:
          IObjectStream result = enumerateTree( associates.nextElement());
          while( associates.hasMoreElements())
            {
            result = appendStreams( result, enumerateTree( associates.nextElement()));
            } 
          return result;
          }
        };
    else
      // "Leaf" node
      return new IObjectStream ()
        { 
        public Object head()
          {
          return node;
          }
        public IObjectStream tail()
          {
          return null;
          }
        };
    }
  
  // append-streams on page 246 SICP
  private static IObjectStream appendStreams( final IObjectStream lhs, final IObjectStream rhs)
    {
    if( lhs == null)
      return rhs;
    return new IObjectStream ()
        { 
        public Object head()
          {
          return lhs.head();
          }
        public IObjectStream tail()
          {
          return appendStreams( lhs.tail(), rhs); // recurse
          }
        };
    }

  // This interface is isomorphic to the LISP list (and stream) interface.  Unlike Java
  // Enumeration, this interface has no mutating operations (like nextElement).  It
  // would be an interesting project to reimplement this whole class in terms of Enumeration
  // only.
  private static interface IObjectStream
    {
    Object head();
    IObjectStream tail();
    }
 
  private IObjectStream theStream;
  
  private IAssociates associate;
  
  private static IObjectStream EmptyStream = new IObjectStream ()
        { 
        public Object head()
          {
            return null;
          }
        public IObjectStream tail()
          {
          return this;
          }
        };  
}
