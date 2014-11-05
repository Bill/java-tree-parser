package com.objectspace.graph.node.parser.test;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

import com.objectspace.util.*;
import com.objectspace.association.*;
import com.objectspace.graph.*;
import com.objectspace.graph.node.*;
import com.objectspace.graph.node.parser.*;

public class GraphParserTest
{
  // Design Decision 24: decided not to put this test "main" into GraphParser class
  // because doing so would have required importing classes from java.io into that
  // class unnecessarily.

	public static void main (String[] args)
	{
    // Design Decision 27: first, test the test!
    Reader reader = new StringReader( "(a)");
    Node theGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    Node expectGraph = null;
    compare( theGraph, expectGraph, false);

    // Design Decision 45: test the test again.
    reader = new StringReader( "(a(b)(c))");
    theGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    reader = new StringReader( "(a(b(c)))");
    expectGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    compare( theGraph, expectGraph, false);

    // Now test the "unit under test"
    reader = new StringReader( "(a(b)(c))");
    theGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    expectGraph = new Node("a");
    expectGraph.addAssociate( new Node("b"));
    expectGraph.addAssociate( new Node("c"));
    compare( theGraph, expectGraph, true);

    // Design Decision 20: test for empty input
    reader = new StringReader( "");
    expectGraph = null;
    theGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    compare( theGraph, expectGraph, true);

    // Design Decision 22: test for single node tree
    reader = new StringReader( "(x)");
    expectGraph = new Node("x");
    theGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    compare( theGraph, expectGraph, true);

    // Design Decision 23: test for graph (circular)
    reader = new StringReader( "(a(b(a)))");
    expectGraph = new Node("a");
    Node temp = new Node("b");
    expectGraph.addAssociate( temp);
    temp.addAssociate( expectGraph);
    theGraph = new GraphParser( new GraphTokenizer( reader)).parse();
    compare( theGraph, expectGraph, true);

	}

  private static void compare( Node theGraph, Node expectGraph, boolean expectedResult)
    {
    // Design Decision 30: By design, empty graphs (null graphs) are not allowed
    // so check for them here and short-circuit.  This solve lots of otherwise _messy_
    // issues in the graph comparison code (EqualNodes class)!
    // Design Decision 26: empty trees are equal
    boolean result = !( theGraph == null && expectGraph != null ||
        expectGraph == null && theGraph != null ||
        theGraph != null && expectGraph != null &&
        ! GraphComparing.equalGraphs( theGraph, expectGraph,
                                      new EqualNodes(), new AssociatesOnce( new Hashtable(), new AssociatedNodes())));

    if( result != expectedResult )
      {
      System.out.println( "Failure!\nExpected:");
      dumpGraph( expectGraph);
      System.out.println( "Got:");
      dumpGraph( theGraph);
      }
    }

  // This could be broken out into a really cool little framework.
  private static void dumpGraph( Node graph)
    {
    if( graph == null)
      System.out.println( "<<empty graph>>");
    else
      {
      Enumeration e = new EnumerateGraph( graph, new AssociatesOnce( new Hashtable(), new AssociatedNodes()));
      while( e.hasMoreElements() )
        {
        Node node = (Node)e.nextElement();
        System.out.println( node.name());
        }
      }
    }
 }
