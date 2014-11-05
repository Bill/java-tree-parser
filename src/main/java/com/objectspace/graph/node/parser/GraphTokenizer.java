package com.objectspace.graph.node.parser;

import java.io.*;

// Design Decision 8: have a tokenizer separate from the parser
public class GraphTokenizer
{
  // Design Decision 16: Make Token Types public inner classes.  Could use a package
  // to do this just as well.
  public static interface IToken
  {
  }
  public static class Close implements IToken
  {
  }
  public static class End implements IToken
  {
  }
  public static class Error implements IToken
  {
  }
  public static class Open implements IToken
  {
  }
  public static class NodeName implements IToken
  {
    public NodeName( char c)
      {
      append(c);
      }
    public void append( char c)
      {
      name.append(c);
      }
    public String name()
      {
      return name.toString();
      }
    private StringBuffer name = new StringBuffer();
  }
  
  // Design Decision 10: client does not need to know that this class requires
  // a PushbackReader -- only a Reader.
  public GraphTokenizer( Reader reader)
    {
    // Create a pushback reader to make tokenizing easier
    this.reader = new PushbackReader( reader);
    }
  
  public IToken nextToken()
    {
    IToken result;
    
    // Design Decision 14: encapsulate the one token lookahead in this public function
    // leaving computeNextToken clean.
    if( tokenStack == null)
      result = computeNextToken();
    else
      {
      result = tokenStack;
      tokenStack = null;
      }
    
    return result;
    }
  
  public void unread( IToken token)
    {
    tokenStack = token;
    }
  
  // Design Decision 2: keep lexer state in auto booleans
  // Rationale: it's a little messy, but this routine is so tiny it's better to be 
  // able to see all the code at once than distribute it over a bunch of "cleaner"
  // classes.
  private IToken computeNextToken()
    {
    int c;
    boolean inName = false;  // lexer "state"
    NodeName nodeName = null;

    try
    {
      // Design Decision 24: it would be easy to allow "whitespace" in the language
      // (as well as comments.  These features would be implemented here.
      
      // Design Decision 11: classic lexer with while() at head acquiring next input
      // character and breaking gracefully on end of input
     while( (c = reader.read()) != -1)
        {
        if( inName)
          switch( c)
            {
            case '(':
            case ')':
              reader.unread( c);
              return nodeName; // Design Decision 5: direct return (expedient)
            default:
              nodeName.append( (char)c);
            }
        else
          switch( c)
            {
            case (int) '(':
              return open;       
            case (int) ')':
              return close;
            default:
              inName = true;
              nodeName = new NodeName( (char)c);
            }
        }
        if( inName)
          return nodeName;
        // else
        return end;    
    }
    catch( IOException ex)
      {
      ex.printStackTrace();
      }    
    return error;
    }

  // Design Decision 13: have one token of lookahead (not to be confused w/ one
  // character of lookahead in PushbackReader
  private IToken tokenStack;
  
  // Design Decision 3: singleton tokens
  private static final IToken open = new Open();
  private static final IToken close = new Close();
  private static final IToken end = new End();
  private static final IToken error = new Error();
  
  // Design Decision 9: tokenizer is associated 1-1 with the input stream.  This is
  // because the tokenizer needs to be able to "push back" characters on that stream.
  private PushbackReader reader;
  
}
