package com.objectspace.graph.node.parser;

import java.util.*;

import com.objectspace.graph.node.*;

public class GraphParser
{
  // Design Decision 19: decided to create the Hashtable proactively (rather than lazily)
  // since it is a static (there will be only one instance per root namespace).
  // BUG HISTORY 1: failed to clear this table between parses.  Brought to light common
  // issue of need for concept of "top level parsing context" vs. nested parsing.  Made
  // parse a non-static member function as a result and made this a non-static data member.
  private Hashtable nodes = new Hashtable();
  
  private GraphTokenizer tokenizer;
  
  public GraphParser( GraphTokenizer tokenizer)
    {
    this.tokenizer = tokenizer;
    }
  
  // Design Decision 1: Keep parsing state on the stack.
  // Alternative: keep state in instance variables and create (activate) new parsers
  // as needed.  Doing so alludes to interesting parallels between "continuations"
  // which are reification of LISP activation records; and "objects".
  public Node parse()
    {
      GraphTokenizer.IToken token;
      State state = State.Start;
      Node top = null;
      
      // Design Decision 5: you may think that type checking is uncalled for here.
      // I think it's the best answer.
      // Design Decision 6: classic parser -- while() at head, acquiring next token and
      // breaking gracefully on end of input
      // Design Decision 12: classic parser -- switching first on current state, then
      // (within that state) on token type
      while( ! ( (token = tokenizer.nextToken()) instanceof GraphTokenizer.End))
        {
        //
        // Note: a good test during code walkthroughs of this sort of "state machine" 
        // implementation is as follows:
        // 1. verify that all possible states are represented by a branch (completeness)
        // 2. verify that each branch either changes the state, or returns (progress)
        //
        
        // This would be a "switch" statement if we could switch on object types in Java.
        if( state == State.Start)
          {
          if( token instanceof GraphTokenizer.Open)
            state = State.ExpectNodeName;
          else
            state = State.Error;
          }
        else if( state == State.ExpectNodeName)
          {
          // Design Decision 31: don't allow empty graph to crop up in the middle
          // of a graph specification!!  This is un-LISP-like, but it solves lots
          // of ugly problems.  It's ok to parse an empty input stream (that returns
          // null), but it's not ok to see "()" where a Node is expected.
          /*
          if( token instanceof GraphTokenizer.Close)
            // "empty" Graph
            return null;
          else 
          */
          if( token instanceof GraphTokenizer.NodeName)
            {
            // Design Decision 19: Important enforce identity based on "name".
            if( (top = (Node)nodes.get( ((GraphTokenizer.NodeName)token).name())) == null)
              {
              top = new Node( ((GraphTokenizer.NodeName)token).name());
              nodes.put( ((GraphTokenizer.NodeName)token).name(), top);
              }
                  
            // Design Decision 15: use one token of lookahead here.
            // token has to be in the "first" set of the Node production
            // see Dragon Book for details
            while( (token = tokenizer.nextToken()) instanceof GraphTokenizer.Open)
              {
              tokenizer.unread( token); // push back
              top.addAssociate( parse());
              }
            tokenizer.unread( token); // push back
            state = State.ExpectClose;                
            }                                    
          else
            state = State.Error;
          }     
        else if( state == State.ExpectClose)
          {
          if( token instanceof GraphTokenizer.Close)
            return top;
          else
            state = State.Error;
          }                       
        else
          // Syntax ERROR
          // Design Decision 17: silently ignore errors for now, could try to
          // recover.  Would need additional interface to caller (e.g. exception)
          return null;
        } // while
      return null;
      }
  
  // Design Decision 6: make State an internal class
  private static class State
    {
      public static State Start = new State();
      public static State ExpectNodeName = new State();
      public static State ExpectClose = new State();
      public static State Error = new State();
    }

  // Design Decision 7?: it might be neat to use the State Pattern (GOF) here
  // and give State subclasses responsibility for transitioning themselves
  // based on IToken input
  }